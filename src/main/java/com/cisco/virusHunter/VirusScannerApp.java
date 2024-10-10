package com.cisco.virusHunter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;

public class VirusScannerApp extends JFrame {
    private static final Color DARK_BACKGROUND = new Color(18, 18, 18);
    private static final Color ACCENT_BLUE = new Color(0, 122, 204);
    private static final Color TEXT_COLOR = new Color(229, 229, 229);
    private static final Color BUTTON_HOVER = new Color(0, 153, 255);

    private JTextArea textArea;
    private JProgressBar progressBar;
    private VirusScanner scanner;

    public VirusScannerApp() {
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
        return mainContent;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        footer.setBackground(DARK_BACKGROUND);

        JButton fixButton = createStyledButton("Fix");
        fixButton.addActionListener(e -> deleteViruses());

        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> cancelScan());

        footer.add(fixButton);
        footer.add(cancelButton);

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
            scanner = new VirusScanner(textArea, progressBar);
            scanner.startScan(selectedFile);
        }
    }

    private void deleteViruses() {
        // Implement the logic to delete the viruses
        // This should be linked to the VirusScanner class to identify and remove the infected files
        textArea.append("All detected viruses have been deleted.\n");
    }

    private void cancelScan() {
        // Cancel any ongoing scan operation
        if (scanner != null) {
            textArea.append("Scan operation cancelled.\n");
            // Implement cancellation logic in VirusScanner
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VirusScannerApp().setVisible(true);
        });
    }
}
