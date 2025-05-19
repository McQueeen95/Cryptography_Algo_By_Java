package com.example.crypto;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;

public class MonoalphabeticCipherApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Monoalphabetic Cipher");

		Label inputLabel = new Label("Enter text below:");
		TextArea inputArea = new TextArea();
		inputArea.setWrapText(true);

		Button loadFileButton = new Button("Load File From ur PC");
		loadFileButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Text File");
			fileChooser.getExtensionFilters().addAll(
							new FileChooser.ExtensionFilter("Text Files", "*.txt"),
							new FileChooser.ExtensionFilter("All Files", "*.*")
			);
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				try {
					String content = new String(Files.readAllBytes(selectedFile.toPath()), StandardCharsets.UTF_8);
					inputArea.setText(content);
				} catch (IOException ex) {
					System.out.println("Failed to load file: " + ex.getMessage());
				}
			}
		});

//		Label keyLabel = new Label("Enter key (26 unique letters):");
//		TextField keyField = new TextField();
//		keyField.setPromptText("e.g., QWERTYUIOPASDFGHJKLZXCVBNM");

		Button encryptButton = new Button("Encrypt It Bro");
		Button decryptButton = new Button("Decrypt It Bro");
		TextField timeTextField = new TextField();

		Label resultLabel = new Label("Result Down Here:");
		Label time = new Label("The Time to resolve the cypher in Nano Is:");
		TextArea resultArea = new TextArea();
		resultArea.setWrapText(true);
		resultArea.setEditable(false);

		String key =randomKey();
		encryptButton.setOnAction(e -> {
			long startTime = System.nanoTime();
			String text = inputArea.getText();
//			String key = randomKey();
			System.out.println(key);
//			if (!isValidKey(key)) {
//				showError("Invalid key. The key must contain 26 unique alphabetic characters.");
//				return;
//			}
			String encrypted = encrypt(text, key);
			resultArea.setText(encrypted);
			long endTime = System.nanoTime();
			timeTextField.setText(""+(endTime-startTime));
		});

		decryptButton.setOnAction(e -> {
			long startTime = System.nanoTime();
			String text = inputArea.getText();
//			String key = randomKey();
			System.out.println(key);
//			if (!isValidKey(key)) {
//				showError("Invalid key. The key must contain 26 unique alphabetic characters.");
//				return;
//			}
			String decrypted = decrypt(text, key);
			resultArea.setText(decrypted);
			long endTime = System.nanoTime();
			timeTextField.setText(""+(endTime-startTime));
		});
		// Layout setup.
		VBox layout = new VBox(10, inputLabel, inputArea, loadFileButton, encryptButton , decryptButton, resultLabel, resultArea,time,timeTextField);
		layout.setPadding(new Insets(10));
//		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout, 400, 550);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


//	private boolean isValidKey(String key) {
//		if (key.length() != 26) return false;
//		key = key.toUpperCase();
//		boolean[] seen = new boolean[26];
//		for (char c : key.toCharArray()) {
//			if (c < 'A' || c > 'Z') return false;
//			int index = c - 'A';
//			if (seen[index]) return false;
//			seen[index] = true;
//		}
//		return true;
//	}

	public String randomKey(){
		String chars = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder S = new StringBuilder();
		Random R = new Random();
		boolean[] usedBefore = new boolean[26];
		for (int i = 0; i < chars.length(); i++) {
			int index;
			do {
				index = R.nextInt(26);
			} while (usedBefore[index]);
			usedBefore[index] = true;
			S.append(chars.charAt(index));
		}
		return S.toString();
	}

//	private void showError(String message) {
//		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
//		alert.showAndWait();
//	}


	private String encrypt(String plaintext, String key) {
		StringBuilder sb = new StringBuilder();
		key = key.toUpperCase();
		for (char c : plaintext.toCharArray()) {
			if (Character.isLetter(c)) {
				if (Character.isUpperCase(c)) {
					int index = c - 'A';
					sb.append(key.charAt(index));
				} else {
					int index = c - 'a';
					sb.append(Character.toLowerCase(key.charAt(index)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}


	private String decrypt(String ciphertext, String key) {
		StringBuilder sb = new StringBuilder();
		key = key.toUpperCase();
		for (char c : ciphertext.toCharArray()) {
			if (Character.isLetter(c)) {
				char upperC = Character.toUpperCase(c);
				int index = key.indexOf(upperC);
				if (index != -1) {
					char plainChar = (char) ('A' + index);
					if (Character.isUpperCase(c)) {
						sb.append(plainChar);
					} else {
						sb.append(Character.toLowerCase(plainChar));
					}
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
