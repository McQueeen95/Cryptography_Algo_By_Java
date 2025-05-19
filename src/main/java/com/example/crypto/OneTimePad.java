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

public class OneTimePad extends Application {
    String key ;
    int a = 10;
    int b = 15;
    int s0 = 5;

    @Override
    public void start(Stage stage) throws Exception {

        Label inputLabel = new Label("Enter Your message:");
        TextArea inputTextField = new TextArea();
        Button encryptButton = new Button("Click here to Encrypt");
        Button decryptButton = new Button("Click here to Decrypt");
        Button changeKeyButton = new Button("Change Key");
        Label outputLabel = new Label("Output:");
        TextArea outputTextField = new TextArea();

//        GridPane inputGrid = new GridPane( );
//        GridPane outputGrid = new GridPane();
//        GridPane buttonGrid = new GridPane();
//        inputGrid.add(inputLabel, 0, 1);
//        inputGrid.add(inputTextField, 1, 2);
//        inputTextField.setMaxSize(350, 100);
//        inputGrid.setHgap(10);
//        outputTextField.setMaxSize(350, 150);
//        outputGrid.setHgap(10);
//        outputGrid.add(outputLabel, 0, 0);
//        outputGrid.add(outputTextField, 1, 2);
//        buttonGrid.add(encryptButton, 0, 0);
//        buttonGrid.add(decryptButton, 1, 0);
//        buttonGrid.add(changeKeyButton, 2, 0);
//        buttonGrid.setHgap(50);
//        buttonGrid.setAlignment(javafx.geometry.Pos.CENTER);
        encryptButton.setOnAction(e -> {
            String text = inputTextField.getText();
            if (key == null || key.length() != text.length()) {
                prepareKey(text.length(),a,b,s0);
            }
            System.out.println(key);
            String output = encrypt(text, key);
            outputTextField.setText("Key: " + key + "\n" + "Encrypted Message is: " + output);

        });
        decryptButton.setOnAction(e -> {
            String text = inputTextField.getText();
            if (key == null || key.length() != text.length()) {
                prepareKey(text.length(),a,b,s0);

            }
            System.out.println(key);
            String output = decrypt(text, key);
            outputTextField.setText("Key: " + key + "\n" + "Plain Text: " + output);
        });
        changeKeyButton.setOnAction(e -> {
            if (inputTextField.getText() != null) {
                prepareKey(inputTextField.getText().length(),a,b,s0);
            }

        });
        stage.setTitle("One Time Pad Cipher");
//        VBox layout = new VBox(10, inputGrid, buttonGrid, outputGrid);
        VBox layout = new VBox(10, inputLabel, inputTextField, encryptButton, decryptButton, changeKeyButton, outputLabel, outputTextField);
        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    private String encrypt(String text , String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isAlphabetic(text.charAt(i))) {
                if (Character.isUpperCase(text.charAt(i))) {
                    int index = ((text.charAt(i) - 'A') + (key.charAt(i) - 'A') + 26) % 26;
                    char c = (char) (index + 'A');
                    sb.append(c);
                } else {
                    int index = ((text.charAt(i) - 'a') + (key.charAt(i) - 'a') + 26) % 26;
                    char c = (char) (index + 'a');
                    sb.append(c);
                }
            } else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }
    private String decrypt(String text , String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isAlphabetic(text.charAt(i))) {
                if (Character.isUpperCase(text.charAt(i))) {
                    int index = ((text.charAt(i) - 'A') - (key.charAt(i) - 'A') + 26) % 26;
                    char c = (char) (index + 'A');
                    sb.append(c);
                } else {
                    int index = ((text.charAt(i) - 'a') - (key.charAt(i) - 'a') + 26) % 26;
                    char c = (char) (index + 'a');
                    sb.append(c);
                }
            } else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }
    private void prepareKey(int keyLength,int a,int b,int s0) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyLength; i++) {
//            char c = (char) (Math.random() * 26 + 'a');
            int index = (s0 * a + b) % 26;
            char c = (char)(index+'a');
            sb.append(c);
            s0 = index;
        }
        this.key = sb.toString();
    }
    public static void main(String[] args) {
        launch(args);
    }

}




