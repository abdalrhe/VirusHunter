package com.cisco.virusHunter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class VirusScanner {
    private JTextArea textArea;
    private JProgressBar progressBar;
    private VirusDatabase virusDatabase;
    private List<File> filesToScan;

    public VirusScanner(JTextArea textArea, JProgressBar progressBar) {
        this.textArea = textArea;
        this.progressBar = progressBar;
        this.virusDatabase = new VirusDatabase();
        this.filesToScan = new ArrayList<>();
    }

    public void startScan(File directory) {
        textArea.setText(""); // Clear previous results
        filesToScan.clear();
        collectFiles(directory);
        int totalFiles = filesToScan.size();
        progressBar.setMaximum(totalFiles);
        progressBar.setValue(0);

        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < totalFiles; i++) {
                    scanFile(filesToScan.get(i));
                    publish(i + 1);
                }
                return null;
            }

            @Override
            //عرضScan complete بنسبة الماوية
            protected void process(List<Integer> chunks) {
                int progress = chunks.get(chunks.size() - 1);
                int percentage = (progress * 100) / totalFiles;
                progressBar.setValue(progress);
                progressBar.setString(percentage + "%");
            }

            @Override
            protected void done() {
                textArea.append("Scan complete!\n");
                showSignatureCount();
            }
        }.execute();
    }

    private void collectFiles(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    collectFiles(file);
                } else {
                    filesToScan.add(file);
                }
            }
        }
    }

    private void scanFile(File file) {
        String fileName = file.getName().toLowerCase();
        String result;

        if (virusDatabase.isVirus(fileName)) {
            result = "Warning: The file '" + fileName + "' is infected with a virus!";
        } else {
            result = "The file '" + fileName + "' is safe.";
        }

        textArea.append(result + " (Path: " + file.getAbsolutePath() + ")\n");
    }

    public void showSignatureCount() {
        int count = virusDatabase.getSignatureCount();
        textArea.append("Virus signature count: " + count + "\n");
    }
}
