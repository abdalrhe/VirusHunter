package com.cisco.virusHunter;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VirusScannerApp app = new VirusScannerApp();
            app.setVisible(true);
        });
    }
}