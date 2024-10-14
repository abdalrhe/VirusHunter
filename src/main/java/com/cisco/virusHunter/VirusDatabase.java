package com.cisco.virusHunter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VirusDatabase {
    private Set<String> virusHashes;

    public VirusDatabase() {
        this.virusHashes = new HashSet<>();
        loadSignatures();
    }

    private void loadSignatures() {
        File file = new File("signatures.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                virusHashes.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error loading signatures: " + e.getMessage());
        }
    }

    public boolean isVirusHash(String hash) {
        return virusHashes.contains(hash.toLowerCase());
    }

    public int getHashCount() {
        return virusHashes.size();
    }

    public void incrementVirusCount() {
        // implement virus count increment logic
    }

    public int getVirusCount() {
        // implement virus count getter logic
        return 0;
    }

    public void resetVirusCount() {
        // implement virus count reset logic
    }
}