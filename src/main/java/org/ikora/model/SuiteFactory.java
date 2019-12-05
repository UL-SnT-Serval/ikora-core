package org.ikora.model;

import org.apache.commons.io.FilenameUtils;
import org.ikora.utils.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

class SuiteFactory {
    static Suite create(SourceFile sourceFile){
        return create(null, sourceFile);
    }

    static Suite create(Suite parent, SourceFile sourceFile){
        String name = computeName(parent, sourceFile, true);
        File source = computeSource(parent, sourceFile);

        Suite suite = new Suite(name, source);
        suite.addSourceFile(sourceFile);

        return suite;
    }

    public static String computeName(SourceFile sourceFile, boolean beautiful){
        return computeName(null, sourceFile, beautiful);
    }

    static String computeName(Suite parent, SourceFile sourceFile, boolean beautiful){
        String name = sourceFile.getName();

        if(parent != null){
            Path base = Paths.get(parent.getSource().getAbsolutePath().trim());
            Path path = Paths.get(sourceFile.getFile().getAbsolutePath().trim()).normalize();

            name = base.relativize(path).toString();
        }

        name =  Paths.get(name).getName(0).toString();

        if(beautiful){
            name = FilenameUtils.removeExtension(name);
            name = StringUtils.toBeautifulName(name);
        }

        return name;
    }

    private static File computeSource(Suite parent, SourceFile sourceFile){
        File base;

        if(parent != null){
            base = parent.getSource();
        }
        else {
            base = sourceFile.getProject().getRootFolder();

            if(base.isFile()){
                base = base.getParentFile();
            }
        }

        return new File(base, computeName(parent, sourceFile, false));
    }
}