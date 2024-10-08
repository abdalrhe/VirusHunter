package com.cisco.virusHunter;

import java.io.File;

public class VirusScanner {
    private VirusDatabase database;

    public VirusScanner() {
        database = new VirusDatabase();
    }

    public void startScan() {
        System.out.println("بدء عملية الفحص...");
        
        // تحديد المجلد الذي سيتم فحصه (يمكنك تعديله حسب الحاجة)
        File directory = new File("/home/cisco/Documents/"); // على سبيل المثال، جذر محرك C
        
        // بدء الفحص
        scanDirectory(directory);
        
        System.out.println("انتهى الفحص.");
    }

    private void scanDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // فحص المجلدات الفرعية
                        scanDirectory(file);
                    } else {
                        // فحص الملف
                        scanFile(file);
                    }
                }
            }
        }
    }

    private void scanFile(File file) {
        String fileName = file.getName();
        if (database.isVirus(fileName)) {
            System.out.println("تم العثور على فيروس في الملف: " + file.getAbsolutePath());
            // يمكنك إضافة إجراءات إضافية هنا مثل عزل الملف أو حذفه
        }
    }
}
