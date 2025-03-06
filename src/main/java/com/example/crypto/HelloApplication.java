package com.example.crypto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
//		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
		VBox vbox = new VBox();
		TextField inputText = new TextField();
		TextField outputText = new TextField();
		TextField inputKey = new TextField();
		Button b1 = new Button("Encrypt");
		vbox.getChildren().add(inputText);
		vbox.getChildren().add(inputKey);
		vbox.getChildren().add(b1);
		vbox.getChildren().add(outputText);
		b1.setOnAction((e) ->{
			String input = inputText.getText();
			String key = inputKey.getText();

			String encrpt = encrypt(input,key);
			outputText.setText(encrpt);
		});

		Scene scene = new Scene(vbox, 320, 240);
		stage.setTitle("Ceaser Cipher");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
	private String encrypt(String in,String key){
		int k = Integer.parseInt(key);
		StringBuilder mes = new StringBuilder();
		for (char c : in.toCharArray()) {
			if (Character.isLetter(c)) {
				char base = Character.isUpperCase(c) ? 'A' : 'a';
				mes.append((char) ((c - base + k) % 26 + base));
			}else{
				mes.append(c);
			}
		}
		return mes.toString();
	}
}