package com.cisco.virusHunter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class VirusDatabase {
    private HashSet<String> virusSignatures;

    public VirusDatabase() {
        virusSignatures = new HashSet<>();
        loadSignatures();
    }

    private void loadSignatures() {
        String filePath = "src/main/resources/signatures.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String signature;
            while ((signature = br.readLine()) != null) {
                virusSignatures.add(signature.trim().toLowerCase());
            }
            System.out.println("تم تحميل توقيعات الفيروسات.");
        } catch (IOException e) {
            System.out.println("حدث خطأ أثناء تحميل توقيعات الفيروسات: " + e.getMessage());
        }
    }

    public boolean isVirus(String fileName) {
        return virusSignatures.contains(fileName.toLowerCase());
    }
}
