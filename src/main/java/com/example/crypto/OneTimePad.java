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

    @Override
    public void start(Stage stage) throws Exception {

        Label inputLabel = new Label("Enter Text");
        Label outputLabel = new Label("Output Text");
        TextArea inputTextField = new TextArea();
        TextArea outputTextField = new TextArea();
        Button encryptButton = new Button("Encrypt");
        Button decryptButton = new Button("Decrypt");
        Button changeKeyButton = new Button("Change Key");

        inputTextField.setPromptText("Enter Text");
        outputTextField.setPromptText("Output Text");

        GridPane inputGrid = new GridPane( );
        GridPane outputGrid = new GridPane();
        GridPane buttonGrid = new GridPane();

        inputGrid.add(inputLabel, 0, 1);
        inputGrid.add(inputTextField, 1, 2);
        inputTextField.setMaxSize(350, 100);
        inputGrid.setHgap(10);




        outputTextField.setMaxSize(350, 150);
        outputGrid.setHgap(10);
        outputGrid.add(outputLabel, 0, 0);
        outputGrid.add(outputTextField, 1, 2);


        buttonGrid.add(encryptButton, 0, 0);
        buttonGrid.add(decryptButton, 1, 0);
        buttonGrid.add(changeKeyButton, 2, 0);
        buttonGrid.setHgap(50);
        buttonGrid.setAlignment(javafx.geometry.Pos.CENTER);


        encryptButton.setOnAction(e -> {
            String text = inputTextField.getText();
            if (key == null || key.length() != text.length()) {
                prepareKey(text.length());
            }
            String output = encrypt(text, key);
            outputTextField.setText("Key: " + key + "\n" + "Cipher Text: " + output);

        });

        decryptButton.setOnAction(e -> {
            String text = inputTextField.getText();
            if (key == null || key.length() != text.length()) {
                prepareKey(text.length());
            }
            String output = decrypt(text, key);
            outputTextField.setText("Key: " + key + "\n" + "Plain Text: " + output);
        });


        changeKeyButton.setOnAction(e -> {
            if (inputTextField.getText() != null) {
                prepareKey(inputTextField.getText().length());
            }

        });










        stage.setTitle("One Time Pad Cipher");
        VBox layout = new VBox(10, inputGrid, buttonGrid, outputGrid);
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

    private void prepareKey(int keyLength) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < keyLength; i++) {
            char c = (char) (Math.random() * 26 + 'a');
            sb.append(c);
        }
        this.key = sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }

}




