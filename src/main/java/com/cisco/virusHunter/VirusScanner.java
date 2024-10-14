package com.cisco.virusHunter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security .NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class VirusScanner {
    private final JTextArea textArea;
    private final JProgressBar progressBar;
    private final JLabel statusLabel;
    private final VirusDatabase virusDatabase;
    private volatile boolean isCancelled = false;
    private List<File> infectedFiles;
    private JLabel hashCountLabel;

    public VirusScanner(JTextArea textArea, JProgressBar progressBar, JLabel statusLabel, VirusDatabase virusDatabase, JLabel hashCountLabel) {
        this.textArea = textArea;
        this.progressBar = progressBar;
        this.statusLabel = statusLabel;
        this.virusDatabase = virusDatabase;
        this.infectedFiles = new ArrayList<>();
        this.hashCountLabel = hashCountLabel;
    }

    public void startScan(File startDir) {
        new Thread(() -> {
            try {
                virusDatabase.resetVirusCount();
                infectedFiles.clear();
                scanDirectory(startDir);
                finishScan();
            } catch (IOException e) {
                updateUI("Error during scan: " + e.getMessage());
            }
            SwingUtilities.invokeLater(() -> progressBar.setValue(100));
        }).start();
    }

    private void scanDirectory(File dir) throws IOException {
        if (isCancelled) return;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (isCancelled) return;
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else {
                    scanFile(file);
                }
            }
        }
    }

    private void scanFile(File file) {
        try {
            String fileHash = calculateSHA256(file);
            boolean isVirus = virusDatabase.isVirusHash(fileHash);
            if (isVirus) {
                virusDatabase.incrementVirusCount();
                infectedFiles.add(file);
                updateUI("VIRUS DETECTED: " + file.getPath());
            } else {
                updateUI("Clean: " + file.getPath());
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            updateUI("Error scanning file " + file.getPath() + ": " + e.getMessage());
        }
    }

    private String calculateSHA256(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private void updateUI(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
            statusLabel.setText("Scanned files: " + (progressBar.getValue() + 1) + " | Viruses: " + virusDatabase.getVirusCount());
            progressBar.setValue(progressBar.getValue() + 1);
            hashCountLabel.setText("Virus : " + virusDatabase.getVirusCount());
        });
    }

    public void finishScan() {
        int virusCount = virusDatabase.getVirusCount();
        if (virusCount > 0) {
            updateUI("Scan completed. Total viruses found: " + virusCount);
        } else {
            updateUI("Scan completed. No viruses found. All files are clean.");
        }
    }

    public void cancelScan() {
        isCancelled = true;
        updateUI("Scan cancelled by user.");
    }

    public void fixViruses() {
        for (File file : infectedFiles) {
            if (file.delete()) {
                updateUI("Removed infected file: " + file.getPath());
            } else {
                updateUI("Failed to remove infected file: " + file.getPath());
            }
        }
        updateUI("All viruses and infected files have been removed cleanly.");
        infectedFiles.clear();
        virusDatabase.resetVirusCount();
    }
}