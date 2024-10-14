package com.cisco.virusHunter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class VirusScannerApp extends JFrame {
    private static final Color DARK_BACKGROUND = new Color(18, 18, 18);
    private static final Color ACCENT_BLUE = new Color(0, 122, 204);
    private static final Color TEXT_COLOR = new Color(229, 229, 229);
    private static final Color BUTTON_HOVER = new Color(0, 153, 255);

    private JTextArea textArea;
    private JProgressBar progressBar;
    private JLabel hashCountLabel;
    private VirusScanner scanner;
    private MalwareBazaarAPI malwareBazaarAPI;

    private static final String API_KEY = "d5f0879314e2029e21b7574ea89aa29d";  // أدخل مفتاح API هنا

    public VirusScannerApp() {
        malwareBazaarAPI = new MalwareBazaarAPI(API_KEY);
        setTitle("Dark Mode Virus Scanner");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(DARK_BACKGROUND);
        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        header.setBackground(DARK_BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logo = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(ACCENT_BLUE);
                g2d.fill(new Ellipse2D.Double(0, 0, 40, 40));
                g2d.setColor(DARK_BACKGROUND);
                g2d.fillOval(10, 10, 20, 20);
                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40);
            }
        };

        JLabel title = new JLabel("Virus Hunter");
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        header.add(logo);
        header.add(title);
        return header;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(DARK_BACKGROUND);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setBackground(DARK_BACKGROUND);
        textArea.setForeground(TEXT_COLOR);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainContent.add(scrollPane);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton scanButton = createStyledButton("Scan File");
        scanButton.addActionListener(e -> chooseFileToScan());
        scanButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(scanButton);

        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(ACCENT_BLUE);
        progressBar.setBackground(DARK_BACKGROUND);
        progressBar.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE, 1));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, progressBar.getPreferredSize().height));

        mainContent.add(progressBar);

        hashCountLabel = new JLabel("Virus: 0");
        hashCountLabel.setForeground(TEXT_COLOR);
        hashCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(hashCountLabel);

        return mainContent;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        footer.setBackground(DARK_BACKGROUND);

        JButton fixButton = createStyledButton("Fix");
        fixButton.addActionListener(e -> deleteViruses());

        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> cancelScan());

        JButton downloadButton = createStyledButton("Download Virus DB");
        downloadButton.addActionListener(e -> downloadVirusHashes());

        footer.add(fixButton);
        footer.add(cancelButton);
        footer.add(downloadButton);

        return footer;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isPressed() ? BUTTON_HOVER : ACCENT_BLUE);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void chooseFileToScan() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            VirusDatabase virusDatabase = new VirusDatabase();
            scanner = new VirusScanner(textArea, progressBar, hashCountLabel, virusDatabase, hashCountLabel);

            // Reset the progress bar for a new scan
            progressBar.setValue(0);
            textArea.append("Starting scan...\n");

            // Start scanning the selected file/directory
            scanner.startScan(selectedFile);
        }
    }

    private void downloadVirusHashes() {
        textArea.append("Downloading virus hashes from MalwareBazaar...\n");
        malwareBazaarAPI.downloadVirusHashes();
        textArea.append("Download complete. Total hashes: " + malwareBazaarAPI.getHashCount() + "\n");
        hashCountLabel.setText("Virus Hashes: " + malwareBazaarAPI.getHashCount());
    }

    private void deleteViruses() {
        if (scanner != null) {
            scanner.fixViruses();
        }
    }

    private void cancelScan() {
        if (scanner != null) {
            scanner.cancelScan();
            textArea.append("Scan operation cancelled.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VirusScannerApp().setVisible(true);
        });
    }
}
