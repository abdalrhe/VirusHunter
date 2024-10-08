package com.cisco.virusHunter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VirusScannerTest {

    @Test
    void testIsVirus() {
        VirusDatabase db = new VirusDatabase();
        assertTrue(db.isVirus("malware.exe"));
        assertFalse(db.isVirus("safeFile.txt"));
    }

    @Test
    void testScanFile() {
        VirusScanner scanner = new VirusScanner();
        // يمكنك هنا كتابة اختبارات إضافية لفحص الملفات
    }
}
