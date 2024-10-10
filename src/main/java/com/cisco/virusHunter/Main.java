package com.cisco.virusHunter;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // تأكد من تشغيل واجهة المستخدم على خيط واجهة المستخدم
        SwingUtilities.invokeLater(() -> {
            VirusScannerApp app = new VirusScannerApp();
            app.setVisible(true); // جعل الواجهة مرئية
        });
    }
}
