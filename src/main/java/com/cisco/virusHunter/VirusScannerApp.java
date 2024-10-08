package com.cisco.virusHunter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class VirusScannerApp extends Application {

    private TextArea resultArea;
    private VirusScanner scanner;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        scanner = new VirusScanner();

        Button chooseDirectoryButton = new Button("اختر المجلد للفحص");
        chooseDirectoryButton.setOnAction(e -> chooseDirectory());

        Button scanButton = new Button("ابدأ الفحص");
        scanButton.setOnAction(e -> startScan());

        resultArea = new TextArea();
        resultArea.setEditable(false);

        VBox layout = new VBox(10, chooseDirectoryButton, scanButton, resultArea);
        Scene scene = new Scene(layout, 400, 300);

        primaryStage.setTitle("Virus Hunter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chooseDirectory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("اختر مجلد");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = fileChooser.showDialog(null);
        if (selectedDirectory != null) {
            // هنا يمكنك تخزين مسار المجلد المحدد لاستخدامه في الفحص
            resultArea.appendText("تم اختيار المجلد: " + selectedDirectory.getAbsolutePath() + "\n");
            // تأكد من تعديل الكود في VirusScanner لاستقبال المسار من هنا
            scanner.setDirectory(selectedDirectory);
        }
    }

    private void startScan() {
        resultArea.clear();
        resultArea.appendText("بدء عملية الفحص...\n");
        // قم بتعديل الكود في VirusScanner ليرجع النتائج ويعرضها في المنطقة النصية
        scanner.startScan(resultArea);
    }
}
