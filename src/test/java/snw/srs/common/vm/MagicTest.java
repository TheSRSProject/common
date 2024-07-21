package snw.srs.common.vm;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MagicTest {
    static final int TEST = 1;

    @Test
    void testForceSetField() throws NoSuchFieldException, IllegalAccessException {
        final int expected = 2;
        Field testField = MagicTest.class.getDeclaredField("TEST");
        Magic.forceSetField(testField, null, expected);
        // We cannot use direct reference to TEST there
        // because compiler will inline the static final member value
        Object modified = testField.get(null);
        assertEquals(expected, modified);
    }
}