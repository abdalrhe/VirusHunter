package com.cisco.virusHunter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class VirusDatabase {
    private HashSet<String> virusSignatures;

    // Constructor - Loads signatures at initialization
    public VirusDatabase() {
        virusSignatures = new HashSet<>();
        loadSignatures(); // Load virus signatures when object is created
    }

    // Load signatures from file
    private void loadSignatures() {
        String filePath = "src/main/resources/signatures.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String signature;
            while ((signature = br.readLine()) != null) {
                virusSignatures.add(signature.trim().toLowerCase()); // Add the signature to the set
            }
            System.out.println("Virus signatures loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading virus signatures: " + e.getMessage());
        }
    }

    // Reload virus signatures (if needed)
    public void reloadSignatures() {
        virusSignatures.clear();
        loadSignatures();
        System.out.println("Virus signatures reloaded.");
    }

    // Check if a file name matches a known virus signature
    public boolean isVirus(String fileName) {
        return virusSignatures.contains(fileName.toLowerCase());
    }

    // Get the total number of virus signatures
    public int getSignatureCount() {
        return virusSignatures.size();
    }
}
