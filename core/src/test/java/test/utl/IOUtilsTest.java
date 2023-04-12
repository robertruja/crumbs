package test.utl;

import org.crumbs.core.util.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class IOUtilsTest {

    @Test
    public void shouldCorrectlyTransformIsToByteArray() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("testIOUtilStream.txt");
        byte[] out = IOUtils.readInputStream(is);
        Assertions.assertEquals("SomeInputLine", new String(out).trim());
    }
}
