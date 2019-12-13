package org.ikora.error;

import org.ikora.model.Position;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ErrorFile {
    private final Set<SyntaxError> syntaxErrors;
    private final Set<SymbolError> symbolErrors;
    private final Set<InternalError> internalErrors;
    private final Set<IOError> ioErrors;
    private final Set<UnhandledError> unhandledErrors;

    ErrorFile(){
        syntaxErrors = new HashSet<>();
        symbolErrors = new HashSet<>();
        ioErrors = new HashSet<>();
        internalErrors = new HashSet<>();
        unhandledErrors = new HashSet<>();
    }

    public void registerSyntaxError(String message, Position position){
        SyntaxError error = new SyntaxError(message, position);
        syntaxErrors.add(error);
    }

    public void registerSymbolError(String message, Position position){
        SymbolError error = new SymbolError(message, position);
        symbolErrors.add(error);
    }

    public void registerIOError(String message, File file){
        IOError error = new IOError(message, file);
        ioErrors.add(error);
    }

    public void registerInternalError(String message, Position position){
        InternalError error = new InternalError(message, position);
        internalErrors.add(error);
    }

    public void  registerUnhandledError(String message, Exception exception){
        UnhandledError error = new UnhandledError(message, exception);
        unhandledErrors.add(error);
    }

    public int getSize(){
        return syntaxErrors.size()
                + symbolErrors.size()
                + ioErrors.size()
                + internalErrors.size()
                + unhandledErrors.size();
    }

    public Set<Error> getAll(){
        Set<Error> all = new HashSet<>(getSize());

        all.addAll(syntaxErrors);
        all.addAll(symbolErrors);
        all.addAll(ioErrors);
        all.addAll(internalErrors);
        all.addAll(unhandledErrors);

        return all;
    }

    public boolean isEmpty() {
        return syntaxErrors.isEmpty()
                && symbolErrors.isEmpty()
                && ioErrors.isEmpty()
                && internalErrors.isEmpty()
                && unhandledErrors.isEmpty();
    }
}
