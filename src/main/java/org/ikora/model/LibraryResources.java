package org.ikora.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryResources {
    private Map<String, Item<LibraryKeyword>> keywordsNames;
    private List<LibraryVariable> variables;

    public LibraryResources() {
        this.keywordsNames = new HashMap<>();
        this.variables = new ArrayList<>();
    }

    public <T> void loadClass(Class<T> libraryClass){
        if(LibraryKeyword.class.isAssignableFrom(libraryClass)){
            String keyword = LibraryKeyword.toKeyword((Class<? extends LibraryKeyword>) libraryClass);
            keywordsNames.put(keyword, new Item<>((Class<? extends LibraryKeyword>) libraryClass));
        }
        else if(LibraryVariable.class.isAssignableFrom(libraryClass)){
            try {
                variables.add((LibraryVariable) libraryClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Keyword findKeyword(String name) throws InstantiationException, IllegalAccessException {
        Item<LibraryKeyword> keyword = keywordsNames.get(name.toLowerCase());

        if(keyword == null){
            return null;
        }

        return keyword.getObject();
    }

    public Keyword findKeyword(String library, String name) throws IllegalAccessException, InstantiationException {
        if(library.isEmpty()){
            return findKeyword(name);
        }

        //TODO: define a way to a set of library names;
        return findKeyword(name);
    }

    public Variable findVariable(String name) {
        for(LibraryVariable variable: variables){
            if(variable.matches(name)){
                return variable;
            }
        }

        return null;
    }

    protected class Item<T> {
        private final Class<? extends T> itemClass;
        T itemObject;

        Item(Class<? extends T> libraryClass) {
            this.itemClass = libraryClass;
            this.itemObject = null;
        }

        public T getObject() throws IllegalAccessException, InstantiationException {
            if(itemObject == null) {
                itemObject = itemClass.newInstance();
            }

            return itemObject;
        }
    }
}