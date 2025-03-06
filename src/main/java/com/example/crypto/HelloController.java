package com.example.crypto;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable{
//	@FXML
//	private Label welcomeText;
//	@FXML
//	protected void onHelloButtonClick() {
//		welcomeText.setText("Welcome to JavaFX Application!");
//	}
	@FXML
	private TextField inputField;
	@FXML
	private Button encryptoin;
	@FXML
	private TextField outputField;
	@Override
	public void initialize(URL url, ResourceBundle r){
		encryptoin.setOnAction( (e) -> {
			String input = inputField.getText();
			String encrpt = encrypt(input);
			outputField.setText(encrpt);
		});
	}
	private String encrypt(String in){
		return in.toUpperCase();
	}
}