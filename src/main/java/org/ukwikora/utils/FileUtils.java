package org.ukwikora.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    private FileUtils() {}

    public static String[] getSubFolders(File folder) {
        if(!folder.exists() || !folder.isDirectory()){
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        File[] files = folder.listFiles( (File current, String name) -> new File(current, name).isDirectory() );

        if(files == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        String[] paths = new String[files.length];

        for(int i = 0; i < files.length; ++i) {
            paths[i] = FilenameUtils.normalize(files[i].getAbsolutePath());
        }

        return paths;
    }

    public static String[] getSubFolders(String location) {
        return getSubFolders(new File(location));
    }

    public static boolean isSubDirectory(File base, File child) throws IOException {
        base = base.getCanonicalFile();
        child = child.getCanonicalFile();

        File parentFile = child;
        while (parentFile != null) {
            if (base.equals(parentFile)) {
                return true;
            }
            parentFile = parentFile.getParentFile();
        }
        return false;
    }

    public static void copyResources(Class<?> caller, final String resources, final File destination) throws IOException, URISyntaxException {
        final File jarFile = new File(caller.getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {
            logger.info("Extracting resources from Jar File.");

            try(ZipFile jar = new ZipFile(jarFile)){
                List<? extends ZipEntry> entries = jar.stream()
                        .filter(e -> e.getName().startsWith(resources + "/"))
                        .sorted(Comparator.comparing(ZipEntry::getName))
                        .collect(Collectors.toList());

                for (ZipEntry entry : entries) {
                    String name = StringUtils.removeStart(entry.getName(), resources + "/");
                    Path entryDest = destination.toPath().resolve(name);

                    if (entry.isDirectory()) {
                        Files.createDirectory(entryDest);
                        continue;
                    }

                    Files.copy(jar.getInputStream(entry), entryDest);
                }
            }
        } else {
            logger.info("Extracting resources from folder.");

            File file = FileUtils.getResourceFile(resources);
            if(file.exists()){
                if(file.isDirectory()){
                    org.apache.commons.io.FileUtils.copyDirectory(file, destination);
                }
                else{
                    org.apache.commons.io.FileUtils.copyFile(file, destination);
                }
            }
        }
    }

    public static File getResourceFile(String name) throws IOException, URISyntaxException {
        URL resource = FileUtils.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IOException("failed to locate resource template for project analytics");
        } else {
            logger.info(resource.getPath());
            return Paths.get(resource.toURI()).toFile();
        }
    }

    public static Charset detectCharset(File f, Charset defaultCharset) {

        Charset charset = null;

        for (Charset current : Charset.availableCharsets().values()) {
            charset = validateCharset(f, current);
            if (charset != null) {
                break;
            }
        }

        if(charset == null){
            return defaultCharset;
        }

        return charset;
    }

    private static Charset validateCharset(File f, Charset defaultValue) {
        try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(f))) {
            CharsetDetector detector = new CharsetDetector();
            detector.setText(input);

            String match = detector.detect().getName();
            return Charset.forName(match);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
