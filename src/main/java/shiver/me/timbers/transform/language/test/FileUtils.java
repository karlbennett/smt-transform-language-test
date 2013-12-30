package shiver.me.timbers.transform.language.test;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static shiver.me.timbers.checks.Checks.isNull;

/**
 * Utility methods for loading tests data files that will be in pre and post transformation states.
 */
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

            final InputStream stream = readTestFile(anchor, fileName);

            if (isNull(stream)) {

                throw new IOException("no input stream was able to be created for a file with the name \"" +
                        fileName + "\" in the package \"" + anchor.getPackage().getName() + "\"");
            }

            return IOUtils.toString(stream);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}
