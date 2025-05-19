package com.example.crypto;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Vigenere extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Label inputLabel = new Label("Enter Your Message");
        Label keyLabel = new Label("Enter Your Key");
        Label outputLabel = new Label("The Output");
        TextArea inputTextField = new TextArea();
        TextField keyTextField = new TextField();
        TextArea outputTextField = new TextArea();
        Button encryptButton = new Button("Click here to Encrypt");
        Button decryptButton = new Button("Click here to Decrypt");

//        inputTextField.setPromptText("Enter Text");
//        keyTextField.setPromptText("Enter Key");
//        outputTextField.setPromptText("Output Text");
//        GridPane inputGrid = new GridPane( );
//        GridPane keyGrid = new GridPane();
//        GridPane outputGrid = new GridPane();
//        GridPane buttonGrid = new GridPane();
//        inputGrid.add(inputLabel, 0, 1);
//        inputGrid.add(inputTextField, 1, 2);
//        inputTextField.setMaxSize(350, 100);
//        inputGrid.setHgap(10);
//        keyGrid.setHgap(10);
//        keyTextField.setMinSize(350, 20);
//        keyGrid.add(keyLabel, 0, 0);
//        keyGrid.add(keyTextField, 1, 0);
//        outputTextField.setMaxSize(350, 100);
//        outputGrid.setHgap(10);
//        outputGrid.add(outputLabel, 0, 0);
//        outputGrid.add(outputTextField, 1, 2);
//        buttonGrid.add(encryptButton, 0, 0);
//        buttonGrid.add(decryptButton, 1, 0);
//        buttonGrid.setHgap(50);
//        buttonGrid.setAlignment(javafx.geometry.Pos.CENTER);

        encryptButton.setOnAction(e -> {
            String text = inputTextField.getText();
            String key = makeTheKey(text.length(), keyTextField.getText());
            System.out.println(key);
            String output = encrypt(text, key);
            outputTextField.setText(output);
        });
        decryptButton.setOnAction(e -> {
            String text = inputTextField.getText();
            String key = makeTheKey(text.length(), keyTextField.getText());
            System.out.println(key);
            String output = decrypt(text, key);
            outputTextField.setText(output);
        });
        stage.setTitle("Vigenere Cipher");
//        VBox layout = new VBox(10, inputGrid, keyGrid, buttonGrid, outputGrid);
        VBox layout = new VBox(10,inputLabel,inputTextField,keyLabel,keyTextField, encryptButton,decryptButton, outputLabel, outputTextField);
        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.show();
    }
    private String encrypt(String text, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                if (Character.isUpperCase(text.charAt(i))) {
                    int index = ((text.charAt(i) - 'A') + (key.charAt(i) - 'A')) % 26;
                    char c = (char) (index + 'A');
                    sb.append(c);
                } else {
                    int index = ((text.charAt(i) - 'a') + (key.charAt(i) - 'a')) % 26;
                    char c = (char) (index + 'a');
                    sb.append(c);
                }
            }
            else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }
    private String decrypt(String text, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                if (Character.isUpperCase(text.charAt(i))) {
                    int index = ((text.charAt(i) - 'A') - (key.charAt(i) - 'A') + 26) % 26;
                    char c = (char) (index + 'A');
                    sb.append(c);
                } else {
                    int index = ((text.charAt(i) - 'a') - (key.charAt(i) - 'a') + 26) % 26;
                    char c = (char) (index + 'a');
                    sb.append(c);
                }
            }
            else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }
    private String makeTheKey(int keyLength , String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyLength; i++) {
            sb.append(key.charAt(i % key.length()));
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
