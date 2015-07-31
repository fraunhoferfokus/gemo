/**
 * 
 */
package de.fraunhofer.fokus.mdc.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Scheel, thomas.scheel@fokus.fraunhofer.de
 * 
 */
public class FileUtil {

    /** The Constant log. */
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Load.
     * 
     * @param filePath
     *            the file path
     * @return the string
     * @throws FileNotFoundException
     *             the file not found exception
     */
    public static String load(String path, String fileName)
            throws FileNotFoundException {
        // TODO muß geändert werden, dass Repository genutzt wird!
        Path path2 = null;
        if (path == null) {
            path2 = Paths.get(fileName);
        } else {
            path2 = Paths.get(path, fileName);
        }
        log.info(path2.toString());
        String content = null;
        try {

            // log.info("read file " + path2 + " with length " +
            // Files.size(path2)
            // + " Byte(s).");

            content = new String(Files.readAllBytes(path2),
                    StandardCharsets.UTF_8);

        } catch (IOException e) {
            log.error("Can't read file: " + path2);
            throw new FileNotFoundException("Can't read file: " + path2);
        }

        return content;
    }

}
