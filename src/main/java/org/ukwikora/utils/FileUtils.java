package org.ukwikora.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    private FileUtils() {}

    public static Set<File> getSubFolders(File folder) {
        if(!folder.exists() || !folder.isDirectory()){
            return Collections.emptySet();
        }

        File[] files = folder.listFiles( (File current, String name) -> new File(current, name).isDirectory() );

        if(files == null) {
            return Collections.emptySet();
        }

        return new HashSet<>(Arrays.asList(files));
    }

    public static Set<File> getSubFolders(String location) {
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
        final File file = new File(caller.getProtectionDomain().getCodeSource().getLocation().getPath());

        if(isJarFile(file)) {
            logger.info("Extracting resources from Jar File.");

            try(ZipFile jar = new ZipFile(file)){
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
        } else if(file.isDirectory()) {
            logger.info("Extracting resources from folder.");

            File resourceFile = FileUtils.getResourceFile(resources);
            if(resourceFile.exists()){
                if(resourceFile.isDirectory()){
                    org.apache.commons.io.FileUtils.copyDirectory(resourceFile, destination);
                }
                else{
                    org.apache.commons.io.FileUtils.copyFile(resourceFile, destination);
                }
            }
        }
        else{
            throw new IOException(String.format("Try to extract resources from unsupported file type: %s", file.getAbsolutePath()));
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

    public static boolean isJarFile(File file) throws IOException {
        if (!isZipFile(file)) {
            return false;
        }

        ZipFile zip = new ZipFile(file);
        boolean manifest = zip.getEntry("META-INF/MANIFEST.MF") != null;
        zip.close();

        return manifest;
    }

    public static boolean isZipFile(File file) throws IOException {
        if(file.isDirectory()) {
            return false;
        }

        if(!file.canRead()) {
            throw new IOException("Cannot read file "+file.getAbsolutePath());
        }

        if(file.length() < 4) {
            return false;
        }

        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        int test = in.readInt();
        in.close();

        return test == 0x504b0304;
    }

    private static FileSystem initFileSystem(URI uri) throws IOException
    {
        try
        {
            return FileSystems.getFileSystem(uri);
        }
        catch( FileSystemNotFoundException e )
        {
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            return FileSystems.newFileSystem(uri, env);
        }
    }
}
