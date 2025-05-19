package com.example.crypto;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DESGui extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("DES");

		Label encryptPlainLabel = new Label("Encryption Plaintext:");
		TextField encryptPlainField = new TextField();
		Label encryptKeyLabel = new Label("Encryption Key :");
		TextField encryptKeyField = new TextField();
		Button encryptButton = new Button("Encrypt");
		Label cipherLabel = new Label("Ciphertext (hex):");
		TextField cipherField = new TextField();
		cipherField.setEditable(false);
		Label usedKeyLabel = new Label();

		encryptButton.setOnAction(e -> {
			String plaintext = encryptPlainField.getText();
			String key = encryptKeyField.getText();
			if (plaintext.length() != 8 || key.length() != 8) {
				usedKeyLabel.setText("");
				return;
			}
			System.out.println("Encryption/Decryption key: " + key);
			usedKeyLabel.setText("Used Key: " + key);
			byte[] plaintextBytes = plaintext.getBytes();
			byte[] keyBytes = key.getBytes();
			byte[] cipherBytes = DES.encryptBlock(plaintextBytes, keyBytes);
			cipherField.setText(DES.bytesToHex(cipherBytes));
		});

		Label decryptCipherLabel = new Label("Decryption Ciphertext hex digits:");
		TextField decryptCipherField = new TextField();
		Label decryptKeyLabel = new Label("Decryption Key :");
		TextField decryptKeyField = new TextField();
		Button decryptButton = new Button("Decrypt");
		Label decryptPlainLabel = new Label("Decrypted Plaintext:");
		TextField decryptPlainField = new TextField();
		decryptPlainField.setEditable(false);

		decryptButton.setOnAction(e -> {
			String cipherHex = decryptCipherField.getText();
			String key = decryptKeyField.getText();
//			if (cipherHex.length() != 16 || key.length() != 8) {
//				decryptPlainField.setText("Ciphertext must be 16 hex digits and key 8 characters!");
//				return;
//			}
			System.out.println("Decryption key: " + key);
			try {
				byte[] cipherBytes = DES.hexToBytes(cipherHex);
				byte[] keyBytes = key.getBytes();
				byte[] plainBytes = DES.decryptBlock(cipherBytes, keyBytes);
				decryptPlainField.setText(new String(plainBytes));
			} catch (IllegalArgumentException ex) {
				decryptPlainField.setText("Invalid ciphertext hex!");
			}
		});

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(5));
		vbox.getChildren().addAll(
						encryptPlainLabel, encryptPlainField,
						encryptKeyLabel, encryptKeyField,
						encryptButton, cipherLabel, cipherField,
						usedKeyLabel
		);
		vbox.getChildren().add(new Separator());
		vbox.getChildren().addAll(
						decryptCipherLabel, decryptCipherField,
						decryptKeyLabel, decryptKeyField,
						decryptButton, decryptPlainLabel, decryptPlainField
		);

		Scene scene = new Scene(vbox, 400, 550);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
