package com.example.crypto;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MillerRabinApp extends Application {

	@Override
	public void start(Stage stage) {

		Label inputLabel = new Label("Enter number to test:");
		TextField inputField = new TextField();
		Button testButton = new Button("Test Primality");
		TextArea outputArea = new TextArea();
		outputArea.setEditable(false);

		testButton.setOnAction(e -> {
			try {
				BigInteger n = new BigInteger(inputField.getText());
				boolean isPrime = isProbablyPrime(n, 10); // 10 rounds
				outputArea.setText(isPrime ? "Probably Prime" : "Composite");
			} catch (Exception ex) {
				outputArea.setText("Invalid number.");
			}
		});

		VBox layout = new VBox(10, inputLabel, inputField, testButton, outputArea);
		Scene scene = new Scene(layout, 400, 250);
		stage.setScene(scene);
		stage.setTitle("Miller-Rabin Primality Test");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Miller-Rabin implementation
	private boolean isProbablyPrime(BigInteger n, int k) {
		if (n.compareTo(BigInteger.TWO) < 0) return false;
		if (n.equals(BigInteger.TWO)) return true;
		if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) return false;

		BigInteger d = n.subtract(BigInteger.ONE);
		int r = 0;
		while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
			d = d.divide(BigInteger.TWO);
			r++;
		}

		SecureRandom rand = new SecureRandom();
		for (int i = 0; i < k; i++) {
			BigInteger a;
			do {
				a = new BigInteger(n.bitLength(), rand);
			} while (a.compareTo(BigInteger.TWO) < 0 || a.compareTo(n.subtract(BigInteger.TWO)) > 0);

			BigInteger x = a.modPow(d, n);
			if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) continue;

			boolean passed = false;
			for (int j = 0; j < r - 1; j++) {
				x = x.modPow(BigInteger.TWO, n);
				if (x.equals(n.subtract(BigInteger.ONE))) {
					passed = true;
					break;
				}
			}

			if (!passed) return false;
		}

		return true; // Passed all rounds, probably prime
	}
}
