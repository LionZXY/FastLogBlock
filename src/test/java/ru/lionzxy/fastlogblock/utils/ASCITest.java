package ru.lionzxy.fastlogblock.utils;

import junit.framework.TestCase;
import ru.lionzxy.fastlogblock.models.ASCIString;

public class ASCITest extends TestCase {
    public void testEquals() {
        final ASCIString str1 = new ASCIString(new String("Test"));
        final ASCIString str2 = new ASCIString("Test");
        assertEquals(str1, str2);
        assertEquals(str1.hashCode(), str2.hashCode());
        assertEquals(str1.toString(), "Test");
    }
}
