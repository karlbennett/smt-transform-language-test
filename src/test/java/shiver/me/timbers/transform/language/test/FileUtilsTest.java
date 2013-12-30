package shiver.me.timbers.transform.language.test;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static shiver.me.timbers.transform.language.test.FileUtils.readTestFile;
import static shiver.me.timbers.transform.language.test.FileUtils.readTestFileToString;

public class FileUtilsTest {

    private static final String TEST_FILE_NAME = "Test.txt";
    private static final String TEST_TEXT = "Some test text.";

    @Test
    public void testReadTestFileWithValidFile() throws IOException {

        final InputStream stream = readTestFile(getClass(), TEST_FILE_NAME);

        assertEquals("the test text should be read correctly.", TEST_TEXT, IOUtils.toString(stream));
    }

    @Test
    public void testReadTestFileWithInvalidFile() throws IOException {

        assertNull("no stream should still get created for an invalid file",
                readTestFile(getClass(), "this is not a valid file name"));
    }

    @Test(expected = NullPointerException.class)
    public void testReadTestFileWithNulClass() throws IOException {

        readTestFile(null, TEST_FILE_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void testReadTestFileWithNulFileName() throws IOException {

        readTestFile(getClass(), null);
    }

    @Test
    public void testReadTestFileToStringWithValidFile() {

        final String text = readTestFileToString(getClass(), TEST_FILE_NAME);

        assertEquals("the test text should be read correctly.", TEST_TEXT, text);
    }

    @Test(expected = RuntimeException.class)
    public void testReadTestFileToStringWithInvalidFile() {

        readTestFileToString(getClass(), "this is also not a valid file name");
    }

    @Test(expected = NullPointerException.class)
    public void testReadTestFileToStringWithNullClass() {

        readTestFileToString(null, TEST_FILE_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void testReadTestFileToStringWithNullFileName() {

        readTestFileToString(getClass(), null);
    }
}
