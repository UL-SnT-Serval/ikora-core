package org.ikora.model;

import java.util.*;

import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.InvalidImportTypeException;

public class Link<K extends Node,T extends Node> {
    public enum Import{
        STATIC,
        DYNAMIC,
        BOTH
    }

    private final K source;
    private Set<T> staticCallee;
    private Set<T> dynamicCallee;

    public Link(K source) {
        this.source = source;
        this.staticCallee = new HashSet<>();
        this.dynamicCallee = new HashSet<>();
    }

    public K getSource() {
        return source;
    }

    public Optional<T> getNode(){
        if(dynamicCallee.size() == 1){
            return Optional.of(dynamicCallee.iterator().next());
        }

        if(staticCallee.size() == 1){
            return Optional.of(staticCallee.iterator().next());
        }

        return Optional.empty();
    }

    public Set<T> getAllLinks(Import importType){
        Set<T> allLinks = new HashSet<>();

        switch (importType) {
            case STATIC:
                allLinks.addAll(staticCallee);
                break;
            case DYNAMIC:
                allLinks.addAll(dynamicCallee);
                break;
            case BOTH:
                allLinks.addAll(staticCallee);
                allLinks.addAll(dynamicCallee);
                break;
        }

        return allLinks;
    }

    public void addNode(T destination, Import importType)
            throws InvalidImportTypeException, InvalidDependencyException {
        switch (importType) {
            case STATIC:
                addNode(destination, staticCallee);
                break;
            case DYNAMIC:
                addNode(destination, dynamicCallee);
                break;
            default:
                throw new InvalidImportTypeException(
                        String.format("Was expecting a STATIC or DYNAMIC import type, got %s instead", importType.name()));
        }
    }

    private void addNode(T destination, Set<T> destinations) throws InvalidDependencyException {
        if(destination == null){
            return;
        }

        destinations.add(destination);
        destination.addDependency(source);
    }
}