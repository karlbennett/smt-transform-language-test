package shiver.me.timbers.transform.language.test;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    private FileUtils() {
    }

    /**
     * @return an input stream for the test file in the anchor classes package that has the supplied name.
     */
    public static InputStream readTestFile(Class anchor, String fileName) {

        return anchor.getResourceAsStream(fileName);
    }

    /**
     * @return a {@code String} containing the contents of the test file in the anchor classes package that has the
     *         supplied name.
     */
    public static String readTestFileToString(Class anchor, String fileName) {

        try {

            return IOUtils.toString(readTestFile(anchor, fileName));

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}
