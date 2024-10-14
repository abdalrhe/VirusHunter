package com.cisco.virusHunter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.io.File;

import javax.swing.BorderFactory;
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
    private JLabel hashCountLabel;  // لعرض عدد الفيروسات
    private VirusScanner scanner;
    private MalwareBazaarAPI malwareBazaarAPI;  // لربط الـ API

    private static final String API_KEY = "d5f0879314e2029e21b7574ea89aa29d";  // أدخل مفتاح API هنا

    public VirusScannerApp() {
        malwareBazaarAPI = new MalwareBazaarAPI(API_KEY);  // تهيئة الـ API مع مفتاح API
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
        title.setFont(new Font("Arial", Font.BOLD, 16));

        header.add(logo);
        header.add(title);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(DARK_BACKGROUND);
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textArea = new JTextArea(15, 30);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setForeground(TEXT_COLOR);
        textArea.setBackground(DARK_BACKGROUND);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(DARK_BACKGROUND);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(ACCENT_BLUE);
        progressBar.setBackground(DARK_BACKGROUND);

        hashCountLabel = new JLabel("Hash Count: 0");
        hashCountLabel.setForeground(TEXT_COLOR);
        hashCountLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(DARK_BACKGROUND);
        topPanel.add(hashCountLabel);

        mainContent.add(scrollPane, BorderLayout.CENTER);
        mainContent.add(topPanel, BorderLayout.NORTH);
        mainContent.add(progressBar, BorderLayout.SOUTH);

        return mainContent;
    }

    private JPanel createFooter() {
        // إنشاء JPanel بدون تمرير FlowLayout مباشرةً
        JPanel footer = new JPanel();
        footer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));  // ضبط تخطيط JPanel بعد الإنشاء
        footer.setBackground(DARK_BACKGROUND);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton scanButton = createStyledButton("Scan Directory");
        scanButton.addActionListener(e -> chooseFileToScan());

        JButton downloadButton = createStyledButton("Download Virus Hashes");
        downloadButton.addActionListener(e -> downloadVirusHashes());  // زر لتنزيل التوقيعات من MalwareBazaar

        JButton quitButton = createStyledButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        footer.add(scanButton);
        footer.add(downloadButton);  // إضافة زر تنزيل التوقيعات
        footer.add(quitButton);

        return footer;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(TEXT_COLOR);
        button.setBackground(ACCENT_BLUE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_BLUE);
            }
        });

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
            scanner.startScan(selectedFile);
        }
    }

    private void downloadVirusHashes() {
        // استدعاء دالة تحميل التواقيع من MalwareBazaar
        textArea.append("Downloading virus hashes from MalwareBazaar...\n");
        malwareBazaarAPI.downloadVirusHashes();
        textArea.append("Download complete. Total hashes: " + malwareBazaarAPI.getHashCount() + "\n");
        hashCountLabel.setText("Hash Count: " + malwareBazaarAPI.getHashCount());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VirusScannerApp().setVisible(true);
        });
    }
}
