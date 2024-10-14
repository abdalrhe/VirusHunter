package com.cisco.virusHunter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VirusDatabase {
    private Set<String> virusHashes;
    private int virusCount;  // عداد الفيروسات

    public VirusDatabase() {
        this.virusHashes = new HashSet<>();
        this.virusCount = 0;  // تهيئة العداد إلى 0
        loadSignatures();
    }

    private void loadSignatures() {
        File file = new File("signatures.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                virusHashes.add(line.toLowerCase());  // إضافة التواقيع
            }
        } catch (IOException e) {
            System.err.println("Error loading signatures: " + e.getMessage());
        }
    }

    public boolean isVirusHash(String hash) {
        boolean isVirus = virusHashes.contains(hash.toLowerCase());
        if (isVirus) {
            incrementVirusCount();  // زيادة العداد إذا تم اكتشاف فيروس
        }
        return isVirus;
    }

    public void incrementVirusCount() {
        virusCount++;  // زيادة العداد
    }

    public int getVirusCount() {
        return virusCount;  // استرجاع العداد الحالي
    }

    public void resetVirusCount() {
        virusCount = 0;  // إعادة العداد إلى 0
    }
}
