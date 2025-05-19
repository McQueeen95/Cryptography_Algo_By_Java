package com.example.crypto;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class RSAApp extends Application {

	private RSA rsa;

	@Override
	public void start(Stage stage) {

		rsa = new RSA(); // Key pair generated once

		Label inputLabel = new Label("Enter your message:");
		TextArea inputText = new TextArea();

		Button encryptButton = new Button("Encrypt");
		Button decryptButton = new Button("Decrypt");

		Label outputLabel = new Label("Output:");
		TextArea outputText = new TextArea();
		outputText.setEditable(false);

		encryptButton.setOnAction(e -> {
			try {
				String message = inputText.getText();
				String encrypted = rsa.encryptString(message);
				outputText.setText(encrypted);
			} catch (Exception ex) {
				outputText.setText("Encryption Error: " + ex.getMessage());
			}
		});

		decryptButton.setOnAction(e -> {
			try {
				String encrypted = inputText.getText();
				String decrypted = rsa.decryptString(encrypted);
				outputText.setText(decrypted);
			} catch (Exception ex) {
				outputText.setText("Decryption Error: " + ex.getMessage());
			}
		});

		VBox layout = new VBox(10, inputLabel, inputText, encryptButton, decryptButton, outputLabel, outputText);
		Scene scene = new Scene(layout, 600, 500);
		stage.setScene(scene);
		stage.setTitle("RSA String Cipher");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	static class RSA {
		private final BigInteger n, e, d;
		private final int blockSize;

		public RSA() {
			SecureRandom random = new SecureRandom();
			int bitLength = 2048;

			BigInteger p = BigInteger.probablePrime(bitLength / 2, random);
			BigInteger q = BigInteger.probablePrime(bitLength / 2, random);
			n = p.multiply(q);
			BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
			e = BigInteger.valueOf(65537); // my public key to enc(e,n)
			d = e.modInverse(phi); //my private key to dec(d,n)
			blockSize = (n.bitLength() - 1) / 8; // to get how many bytes
		}

		public String encryptString(String message) {
			byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
			List<String> encryptedBlocks = new ArrayList<>();
			for (int i = 0; i < messageBytes.length; i += blockSize) {
				int end = Math.min(i + blockSize, messageBytes.length);
				byte[] block = new byte[end - i];
				System.arraycopy(messageBytes, i, block, 0, block.length);
				BigInteger m = new BigInteger(1, block); // positive num
				BigInteger c = m.modPow(e, n);
				encryptedBlocks.add(Base64.getEncoder().encodeToString(c.toByteArray()));
			}

			return String.join(":", encryptedBlocks);
		}

		public String decryptString(String encrypted) {
			String[] blocks = encrypted.split(":");
			List<Byte> decryptedBytes = new ArrayList<>();

			for (String block : blocks) {
				byte[] cipherBytes = Base64.getDecoder().decode(block);
				BigInteger c = new BigInteger(1, cipherBytes);
				BigInteger m = c.modPow(d, n);
				byte[] plainBlock = m.toByteArray();

				// If first byte is zero (due to BigInteger sign), skip it
				if (plainBlock.length > 1 && plainBlock[0] == 0) {
					byte[] tmp = new byte[plainBlock.length - 1];
					System.arraycopy(plainBlock, 1, tmp, 0, tmp.length);
					plainBlock = tmp;
				}

				for (byte b : plainBlock) decryptedBytes.add(b);
			}

			byte[] result = new byte[decryptedBytes.size()];
			for (int i = 0; i < decryptedBytes.size(); i++) {
				result[i] = decryptedBytes.get(i);
			}

			return new String(result, StandardCharsets.UTF_8);
		}
	}
}
