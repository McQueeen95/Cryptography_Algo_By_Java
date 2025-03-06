package com.example.crypto;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CaesarCipherApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Caesar Cipher");

		Label inputLabel = new Label("Enter text:");
		TextArea inputArea = new TextArea();
		inputArea.setWrapText(true);

		Button openFileButton = new Button("Open File");
		openFileButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Text File");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				try {
					String fileContent = new String(Files.readAllBytes(selectedFile.toPath()));
					inputArea.setText(fileContent);
				} catch (IOException ex) {
					ex.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to read file: " + ex.getMessage());
					alert.showAndWait();
				}
			}
		});

		Label key = new Label("Enter The Key:");
		TextField inputKey = new TextField();

		Button encryptButton = new Button("Encrypt It Bro");
		Button decryptButton = new Button("Decrypt It Bro");

		Button bruteForceButton = new Button("Brute Force Attack on UR input");

		Label knownPlaintextLabel = new Label("Enter YR Encrypted message here:");
		TextField knownPlaintextField = new TextField();
		Button knownPlaintextButton = new Button("Click to know ur key");

		// Output area.
		Label resultLabel = new Label("Result Down here:");
		TextArea resultArea = new TextArea();
		resultArea.setWrapText(true);
		resultArea.setEditable(false);

		Label time = new Label("The Time to resolve the cypher in Nano Is:");
		TextField timeTextField = new TextField();

		encryptButton.setOnAction(e -> {
			long startTime = System.nanoTime();
			String text = inputArea.getText();
			int shift = Integer.parseInt(inputKey.getText());
			resultArea.setText(caesarCipher(text, shift, true));
			long endTime = System.nanoTime();
			timeTextField.setText(""+(endTime-startTime));
		});

		decryptButton.setOnAction(e -> {
			long startTime = System.nanoTime();
			String text = inputArea.getText();
			int shift = Integer.parseInt(inputKey.getText());
			resultArea.setText(caesarCipher(text, shift, false));
			long endTime = System.nanoTime();
			timeTextField.setText(""+(endTime-startTime));
		});

		bruteForceButton.setOnAction(e -> {
			long startTime = System.nanoTime();
			String text = inputArea.getText();
			resultArea.setText(bruteForceAttack(text));
			long endTime = System.nanoTime();
			timeTextField.setText(""+(endTime-startTime));
		});

		knownPlaintextButton.setOnAction(e -> {
			long startTime = System.nanoTime();
			String text = inputArea.getText();
			String knownSnippet = knownPlaintextField.getText();
			resultArea.setText(knownPlaintextAttack(text, knownSnippet));
			long endTime = System.nanoTime();
			timeTextField.setText(""+(endTime-startTime));
		});

		VBox layout = new VBox(5);
		layout.setPadding(new Insets(10));
//		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(
						inputLabel, inputArea, openFileButton,
						key, inputKey,
						new HBox(200, encryptButton, decryptButton),
						bruteForceButton,
						knownPlaintextLabel, knownPlaintextField, knownPlaintextButton,
						resultLabel, resultArea
						,time,timeTextField
		);

		Scene scene = new Scene(layout, 500, 650);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	private String caesarCipher(String text, int shift, boolean encrypt) {
		if (!encrypt) {
			shift = 26 - shift;
		}
		StringBuilder result = new StringBuilder();
		for (char c : text.toCharArray()) {
			if (Character.isLetter(c)) {
				char base = Character.isUpperCase(c) ? 'A' : 'a';
				result.append((char) ((c - base + shift+26) % 26 + base));
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}


	private String bruteForceAttack(String cipherText) {
		StringBuilder result = new StringBuilder();
		for (int shift = 1; shift < 26; shift++) {
			String decrypted = caesarCipher(cipherText, shift, true);
			result.append("Shift ").append(shift).append(": ").append(decrypted).append("\n");
		}
		return result.toString();
	}


	private String knownPlaintextAttack(String cipherText, String knownPlaintext) {
		StringBuilder result = new StringBuilder();
		boolean found = false;
		for (int shift = 1; shift < 26; shift++) {
			String decrypted = caesarCipher(cipherText, shift, true);
			if (decrypted.contains(knownPlaintext)) {
				result.append("after Calculating it I found That the Decrypted key is ").append(shift).append(".\n");
				found = true;
			}
		}
		if (!found) {
			result.append("No decryption found containing the known plaintext snippet.");
		}
		return result.toString();
	}
}
