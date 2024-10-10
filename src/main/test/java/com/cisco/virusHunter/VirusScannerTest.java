package com.cisco.virusHunter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VirusScannerTest {
    @Test
    public void testIsVirus() {
        VirusDatabase database = new VirusDatabase();
        assertTrue(database.isVirus("malware.exe")); // تأكد من وجود توقيع الفيروس
    }
}
