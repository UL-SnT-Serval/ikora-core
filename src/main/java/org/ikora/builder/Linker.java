package org.ikora.builder;

import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.InvalidImportTypeException;
import org.ikora.model.*;
import org.ikora.runner.Runtime;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Linker {
    private final Runtime runtime;

    private Linker(Runtime runtime){
        this.runtime = runtime;
    }

    private static final Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    public static void link(Runtime runtime) {
        Linker linker = new Linker(runtime);

        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (SourceFile sourceFile : runtime.getSourceFiles()) {
            for(TestCase testCase: sourceFile.getTestCases()) {
                unresolvedArguments.addAll(linker.linkSteps(testCase));
            }

            for(UserKeyword userKeyword: sourceFile.getUserKeywords()) {
                unresolvedArguments.addAll(linker.linkSteps(userKeyword));
            }
        }

        linker.processUnresolvedArguments(unresolvedArguments);
    }

    public static void link(KeywordCall call, Runtime runtime) {
        Linker linker = new Linker(runtime);

        Matcher matcher = gherkinPattern.matcher(call.getName());
        String name = matcher.replaceAll("").trim();

        linker.resolveCall(call, name);
    }

    private List<ScopeValue> linkSteps(TestCase testCase) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        KeywordCall setup = testCase.getSetup();
        if(setup != null){
            unresolvedArguments.addAll(resolveCall(setup, setup.getName()));
        }

        for(Step step: testCase) {
            if(!(step instanceof KeywordCall)) {
                runtime.getErrors().registerSyntaxError(
                        step.getFile(),
                        "Expecting a step of type keyword call",
                        step.getPosition()
                );
            }
            else {
                KeywordCall call = (KeywordCall)step;

                Matcher matcher = gherkinPattern.matcher(step.getName());
                String name = matcher.replaceAll("").trim();

                unresolvedArguments.addAll(resolveCall(call, name));
            }
        }

        KeywordCall teardown = testCase.getTearDown();
        if(teardown != null){
            unresolvedArguments.addAll(resolveCall(teardown, teardown.getName()));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> linkSteps(UserKeyword userKeyword) throws RuntimeException {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (Step step: userKeyword) {
            step.getKeywordCall().ifPresent(call -> unresolvedArguments.addAll(resolveCall(call, call.getName())));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> resolveCall(KeywordCall call, String name) {
        getKeywords(name, call.getSourceFile()).forEach(keyword -> {
            try {
                call.linkKeyword((Keyword) keyword, Link.Import.STATIC);
            } catch (InvalidImportTypeException e) {
                runtime.getErrors().registerInternalError(
                        call.getFile(),
                        "Should handle Static import at this point",
                        call.getPosition()
                );
            } catch (InvalidDependencyException e) {
                runtime.getErrors().registerSymbolError(
                        ((Keyword) keyword).getFile(),
                        e.getMessage(),
                        ((Keyword) keyword).getPosition()
                );
            }
        });

        return linkCallArguments(call);
    }

    private List<ScopeValue> linkCallArguments(KeywordCall call) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        Iterator<Argument> iterator = call.getArgumentList().iterator();

        List<Argument> newArgumentList = new ArrayList<>(call.getArgumentList().size());

        while(iterator.hasNext()){
            Argument argument = iterator.next();
            Set<? super Keyword> keywords = getKeywords(argument.getName(), argument.getSourceFile());

            if(keywords.isEmpty()){
                newArgumentList.add(argument);
            }
            else if(keywords.size() == 1){
                try {
                    Keyword keyword = (Keyword)keywords.iterator().next();
                    KeywordCall keywordCall = createKeywordCall(keyword, argument, iterator);
                    Argument keywordArgument = new Argument(call, keywordCall.toString());
                    keywordArgument.setCall(keywordCall);

                    newArgumentList.add(keywordArgument);
                } catch (InvalidDependencyException e) {
                    runtime.getErrors().registerSymbolError(
                            call.getFile(),
                            e.getMessage(),
                            argument.getPosition()
                    );

                    break;
                }
            }
            else{
                newArgumentList.add(argument);

                runtime.getErrors().registerSymbolError(
                        call.getFile(),
                        "Found more than one keyword to match argument",
                        argument.getPosition()
                );

                iterator.forEachRemaining(newArgumentList::add);
            }
        }

        call.setArgumentList(newArgumentList);

        updateScope(call);

        return unresolvedArguments;
    }

    private KeywordCall createKeywordCall(Keyword keyword, Argument first, Iterator<Argument> iterator) throws InvalidDependencyException {
        KeywordCall call = new KeywordCall();

        Argument last = first;

        int i = keyword.getMaxNumberArguments();

        while (iterator.hasNext() && i > 0){
            last = iterator.next();

            Argument current = new Argument(call, last.getName());
            current.setSourceFile(last.getSourceFile());
            current.setPosition(last.getPosition());

            call.addArgument(current);
            --i;
        }

        call.setName(first.getName());
        call.setSourceFile(keyword.getSourceFile());
        call.setPosition(first.getPosition().merge(last.getPosition()));
        call.addDependency(keyword);

        resolveCall(call, call.getName());

        return call;
    }

    private void updateScope(KeywordCall call) {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call);
            }
        }
    }

    private Set<? super Keyword> getKeywords(String fullName, SourceFile sourceFile) {
        Set<? super Keyword> keywordsFound = getKeywords(fullName, sourceFile, false);

        if(keywordsFound.isEmpty()){
            keywordsFound = getKeywords(fullName, sourceFile, true);
        }

        return keywordsFound;
    }

    private Set<? super Keyword> getKeywords(String fullName, SourceFile sourceFile, boolean allowSplit) {
        String library;
        String name;

        if(allowSplit){
            List<String> particles = Arrays.asList(fullName.split("\\."));
            library = particles.size() > 1 ? String.join(".", particles.subList(0, particles.size() - 1)) : "";
            name = particles.get(particles.size() - 1);
        }
        else {
            library = "";
            name = fullName;
        }

        final Set<? super Keyword> keywordsFound = new HashSet<>(sourceFile.findUserKeyword(library, name));

        if(keywordsFound.isEmpty()){
            try {
                Keyword runtimeKeyword = runtime.findKeyword(library, name);

                if(runtimeKeyword != null){
                    keywordsFound.add(runtimeKeyword);
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                runtime.getErrors().registerUnhandledError(
                        sourceFile.getFile(),
                        String.format("Failed to load library keyword: %s::%s", library, name),
                        exception
                );
            }
        }

        return keywordsFound;
    }

    private void processUnresolvedArguments(List<ScopeValue> unresolvedArguments) {
        for(ScopeValue valueScope: unresolvedArguments){
            Set<Variable> variables = runtime.findInScope(valueScope.getTestCases(), valueScope.getSuites(), valueScope.variableName);

            if(!variables.isEmpty()){
                for(Variable variable: variables){
                    try {
                        valueScope.value.setVariable(valueScope.variableName, variable);
                    } catch (InvalidDependencyException e) {
                        runtime.getErrors().registerInternalError(
                                valueScope.keyword.getFile(),
                                e.getMessage(),
                                valueScope.keyword.getPosition()
                        );
                    }
                }
            }
            else {
                runtime.getErrors().registerSymbolError(
                        valueScope.keyword.getFile(),
                        String.format("Found no definition for local variable: %s", valueScope.variableName),
                        valueScope.keyword.getPosition()
                );
            }
        }
    }
}