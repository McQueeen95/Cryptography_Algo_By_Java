package com.example.crypto;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Arrays;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("AES");

		TextArea inputArea = new TextArea();
		inputArea.setPromptText("Enter plaintext or Base64 ciphertext here:");
//		inputArea.setWrapText(true);

		TextField keyField = new TextField();
		keyField.setPromptText("Enter 16‑character key Here:");

		Button encryptBtn = new Button("Encrypt");
		Button decryptBtn = new Button("Decrypt");

		TextArea outputArea = new TextArea();
//		outputArea.setEditable(false);
		outputArea.setPromptText("Result (Base64 for ciphertext)");
//		outputArea.setWrapText(true);

		encryptBtn.setOnAction(e -> {
			try {
				String input = inputArea.getText();
				String key = keyField.getText();
				if (key.length() != 16) {
					outputArea.setText("Key must be exactly 16 characters.");
					return;
				}
				byte[] enc = encrypt(input.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8));
				outputArea.setText(Base64.getEncoder().encodeToString(enc));
			} catch (Exception ex) {
				outputArea.setText("Encryption error: " + ex.getMessage());
				ex.printStackTrace();
			}
		});

		decryptBtn.setOnAction(e -> {
			try {
				String input = inputArea.getText();
				String key = keyField.getText();
				if (key.length() != 16) {
					outputArea.setText("Key must be exactly 16 characters.");
					return;
				}
				byte[] ct = Base64.getDecoder().decode(input);
				byte[] pt = decrypt(ct, key.getBytes(StandardCharsets.UTF_8));
				outputArea.setText(new String(pt, StandardCharsets.UTF_8));
			} catch (Exception ex) {
				outputArea.setText("Decryption error: " + ex.getMessage());
				ex.printStackTrace();
			}
		});

		VBox root = new VBox(10,
						new Label("Input:"), inputArea,
						new Label("Key:"), keyField,
						new HBox(10, encryptBtn, decryptBtn),
						new Label("Output:"), outputArea
		);
		root.setPadding(new Insets(10));

		primaryStage.setScene(new Scene(root, 500, 550));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	// --- AES-128 Logic ---
	private static final int[] S_BOX = {
					0x63,0x7c,0x77,0x7b,0xf2,0x6b,0x6f,0xc5,0x30,0x01,0x67,0x2b,0xfe,0xd7,0xab,0x76,
					0xca,0x82,0xc9,0x7d,0xfa,0x59,0x47,0xf0,0xad,0xd4,0xa2,0xaf,0x9c,0xa4,0x72,0xc0,
					0xb7,0xfd,0x93,0x26,0x36,0x3f,0xf7,0xcc,0x34,0xa5,0xe5,0xf1,0x71,0xd8,0x31,0x15,
					0x04,0xc7,0x23,0xc3,0x18,0x96,0x05,0x9a,0x07,0x12,0x80,0xe2,0xeb,0x27,0xb2,0x75,
					0x09,0x83,0x2c,0x1a,0x1b,0x6e,0x5a,0xa0,0x52,0x3b,0xd6,0xb3,0x29,0xe3,0x2f,0x84,
					0x53,0xd1,0x00,0xed,0x20,0xfc,0xb1,0x5b,0x6a,0xcb,0xbe,0x39,0x4a,0x4c,0x58,0xcf,
					0xd0,0xef,0xaa,0xfb,0x43,0x4d,0x33,0x85,0x45,0xf9,0x02,0x7f,0x50,0x3c,0x9f,0xa8,
					0x51,0xa3,0x40,0x8f,0x92,0x9d,0x38,0xf5,0xbc,0xb6,0xda,0x21,0x10,0xff,0xf3,0xd2,
					0xcd,0x0c,0x13,0xec,0x5f,0x97,0x44,0x17,0xc4,0xa7,0x7e,0x3d,0x64,0x5d,0x19,0x73,
					0x60,0x81,0x4f,0xdc,0x22,0x2a,0x90,0x88,0x46,0xee,0xb8,0x14,0xde,0x5e,0x0b,0xdb,
					0xe0,0x32,0x3a,0x0a,0x49,0x06,0x24,0x5c,0xc2,0xd3,0xac,0x62,0x91,0x95,0xe4,0x79,
					0xe7,0xc8,0x37,0x6d,0x8d,0xd5,0x4e,0xa9,0x6c,0x56,0xf4,0xea,0x65,0x7a,0xae,0x08,
					0xba,0x78,0x25,0x2e,0x1c,0xa6,0xb4,0xc6,0xe8,0xdd,0x74,0x1f,0x4b,0xbd,0x8b,0x8a,
					0x70,0x3e,0xb5,0x66,0x48,0x03,0xf6,0x0e,0x61,0x35,0x57,0xb9,0x86,0xc1,0x1d,0x9e,
					0xe1,0xf8,0x98,0x11,0x69,0xd9,0x8e,0x94,0x9b,0x1e,0x87,0xe9,0xce,0x55,0x28,0xdf,
					0x8c,0xa1,0x89,0x0d,0xbf,0xe6,0x42,0x68,0x41,0x99,0x2d,0x0f,0xb0,0x54,0xbb,0x16
	};

	private static final int[] INV_S_BOX = {
					0x52,0x09,0x6a,0xd5,0x30,0x36,0xa5,0x38,0xbf,0x40,0xa3,0x9e,0x81,0xf3,0xd7,0xfb,
					0x7c,0xe3,0x39,0x82,0x9b,0x2f,0xff,0x87,0x34,0x8e,0x43,0x44,0xc4,0xde,0xe9,0xcb,
					0x54,0x7b,0x94,0x32,0xa6,0xc2,0x23,0x3d,0xee,0x4c,0x95,0x0b,0x42,0xfa,0xc3,0x4e,
					0x08,0x2e,0xa1,0x66,0x28,0xd9,0x24,0xb2,0x76,0x5b,0xa2,0x49,0x6d,0x8b,0xd1,0x25,
					0x72,0xf8,0xf6,0x64,0x86,0x68,0x98,0x16,0xd4,0xa4,0x5c,0xcc,0x5d,0x65,0xb6,0x92,
					0x6c,0x70,0x48,0x50,0xfd,0xed,0xb9,0xda,0x5e,0x15,0x46,0x57,0xa7,0x8d,0x9d,0x84,
					0x90,0xd8,0xab,0x00,0x8c,0xbc,0xd3,0x0a,0xf7,0xe4,0x58,0x05,0xb8,0xb3,0x45,0x06,
					0xd0,0x2c,0x1e,0x8f,0xca,0x3f,0x0f,0x02,0xc1,0xaf,0xbd,0x03,0x01,0x13,0x8a,0x6b,
					0x3a,0x91,0x11,0x41,0x4f,0x67,0xdc,0xea,0x97,0xf2,0xcf,0xce,0xf0,0xb4,0xe6,0x73,
					0x96,0xac,0x74,0x22,0xe7,0xad,0x35,0x85,0xe2,0xf9,0x37,0xe8,0x1c,0x75,0xdf,0x6e,
					0x47,0xf1,0x1a,0x71,0x1d,0x29,0xc5,0x89,0x6f,0xb7,0x62,0x0e,0xaa,0x18,0xbe,0x1b,
					0xfc,0x56,0x3e,0x4b,0xc6,0xd2,0x79,0x20,0x9a,0xdb,0xc0,0xfe,0x78,0xcd,0x5a,0xf4,
					0x1f,0xdd,0xa8,0x33,0x88,0x07,0xc7,0x31,0xb1,0x12,0x10,0x59,0x27,0x80,0xec,0x5f,
					0x60,0x51,0x7f,0xa9,0x19,0xb5,0x4a,0x0d,0x2d,0xe5,0x7a,0x9f,0x93,0xc9,0x9c,0xef,
					0xa0,0xe0,0x3b,0x4d,0xae,0x2a,0xf5,0xb0,0xc8,0xeb,0xbb,0x3c,0x83,0x53,0x99,0x61,
					0x17,0x2b,0x04,0x7e,0xba,0x77,0xd6,0x26,0xe1,0x69,0x14,0x63,0x55,0x21,0x0c,0x7d
	};

	private static final int[] RCON = {
					0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80,0x1B,0x36
	};
	public static byte[] encrypt(byte[] plaintext, byte[] key) {
		plaintext = pad(plaintext, 16);

		int blockSize = 16;
		int numBlocks = (int) Math.ceil((double) plaintext.length / blockSize);
		byte[] ciphertext = new byte[numBlocks * blockSize];

		// Encrypt each block
		for (int i = 0; i < numBlocks; i++) {
			byte[] block = Arrays.copyOfRange(plaintext, i * blockSize, (i + 1) * blockSize);
			byte[] encryptedBlock = encryptBlock(block, key);
			System.arraycopy(encryptedBlock, 0, ciphertext, i * blockSize, blockSize);
		}

		return ciphertext;
	}

	public static byte[] decrypt(byte[] ciphertext, byte[] key) {
		int blockSize = 16;
		int numBlocks = ciphertext.length / blockSize;
		byte[] plaintext = new byte[numBlocks * blockSize];

		// Decrypt each block
		for (int i = 0; i < numBlocks; i++) {
			byte[] block = Arrays.copyOfRange(ciphertext, i * blockSize, (i + 1) * blockSize);
			byte[] decryptedBlock = decryptBlock(block, key);
			System.arraycopy(decryptedBlock, 0, plaintext, i * blockSize, blockSize);
		}

		// Remove the padding
		return plaintext;
	}

	public static byte[] encryptBlock(byte[] in, byte[] key) {
		if (in.length != 16 || key.length != 16)
			throw new IllegalArgumentException("Both must be 16 bytes.");
		int[] w = expandKey(key);
		byte[][] st = toState(in);
		addRoundKey(st, w, 0);
		for (int rnd = 1; rnd < 10; rnd++) {
			subBytes(st);
			shiftRows(st);
			mixColumns(st);
			addRoundKey(st, w, rnd);
		}
		subBytes(st);
		shiftRows(st);
		addRoundKey(st, w, 10);
		return fromState(st);
	}

	public static byte[] decryptBlock(byte[] ct, byte[] key) {
		if (ct.length != 16 || key.length != 16)
			throw new IllegalArgumentException("Both must be 16 bytes.");
		int[] w = expandKey(key);
		byte[][] st = toState(ct);
		addRoundKey(st, w, 10);
		for (int rnd = 9; rnd >= 1; rnd--) {
			invShiftRows(st);
			invSubBytes(st);
			addRoundKey(st, w, rnd);
			invMixColumns(st);
		}
		invShiftRows(st);
		invSubBytes(st);
		addRoundKey(st, w, 0);
		return fromState(st);
	}
	private static byte[] pad(byte[] data, int blockSize) {
		int paddingLength = blockSize - (data.length % blockSize);
		byte[] paddedData = new byte[data.length + paddingLength];
		System.arraycopy(data, 0, paddedData, 0, data.length);
		for (int i = data.length; i < paddedData.length; i++) {
			paddedData[i] = (byte) paddingLength;
		}
		return paddedData;
	}

	// Remove PKCS#7 padding
	private static byte[] unpad(byte[] data) {
		int paddingLength = data[data.length - 1];
		byte[] unpaddedData = new byte[data.length - paddingLength];
		System.arraycopy(data, 0, unpaddedData, 0, unpaddedData.length);
		return unpaddedData;
	}

	private static int[] expandKey(byte[] key) {
		int[] w = new int[44];
		for (int i = 0; i < 4; i++)
			w[i] = ((key[4*i]&0xFF)<<24)|((key[4*i+1]&0xFF)<<16)|
							((key[4*i+2]&0xFF)<<8)|(key[4*i+3]&0xFF);
		for (int i = 4; i < 44; i++) {
			int t = w[i-1];
			if (i%4==0)
				t = subWord(rotWord(t)) ^ (RCON[i/4-1]<<24);
			w[i] = w[i-4] ^ t;
		}
		return w;
	}

	private static int subWord(int w) {
		return (S_BOX[(w>>>24)&0xFF]<<24)|(S_BOX[(w>>>16)&0xFF]<<16)|
						(S_BOX[(w>>>8)&0xFF]<<8)|S_BOX[w&0xFF];
	}
	private static int rotWord(int w) {
		return (w<<8)|((w>>>24)&0xFF);
	}

	private static void subBytes(byte[][] st) {
		for (int r=0;r<4;r++)
			for (int c=0;c<4;c++)
				st[r][c] = (byte)(S_BOX[st[r][c]&0xFF]);
	}

	private static void invSubBytes(byte[][] st) {
		for (int r=0;r<4;r++)
			for (int c=0;c<4;c++)
				st[r][c] = (byte)(INV_S_BOX[st[r][c]&0xFF]);
	}

	private static void shiftRows(byte[][] st) {
		for (int i=1;i<4;i++) {
			byte[] tmp = new byte[4];
			for (int j=0;j<4;j++)
				tmp[j] = st[i][(j+i)%4];
			System.arraycopy(tmp,0,st[i],0,4);
		}
	}

	private static void invShiftRows(byte[][] st) {
		for (int i=1;i<4;i++) {
			byte[] tmp = new byte[4];
			for (int j=0;j<4;j++)
				tmp[(j+i)%4] = st[i][j];
			System.arraycopy(tmp,0,st[i],0,4);
		}
	}

	private static void mixColumns(byte[][] st) {
		for (int c=0;c<4;c++) {
			byte a0=st[0][c], a1=st[1][c], a2=st[2][c], a3=st[3][c];
			st[0][c] = (byte)(gmul(a0,2)^gmul(a1,3)^a2^a3);
			st[1][c] = (byte)(a0^gmul(a1,2)^gmul(a2,3)^a3);
			st[2][c] = (byte)(a0^a1^gmul(a2,2)^gmul(a3,3));
			st[3][c] = (byte)(gmul(a0,3)^a1^a2^gmul(a3,2));
		}
	}

	private static void invMixColumns(byte[][] st) {
		for (int c=0;c<4;c++) {
			byte a0=st[0][c], a1=st[1][c], a2=st[2][c], a3=st[3][c];
			st[0][c] = (byte)(gmul(a0,14)^gmul(a1,11)^gmul(a2,13)^gmul(a3,9));
			st[1][c] = (byte)(gmul(a0,9)^gmul(a1,14)^gmul(a2,11)^gmul(a3,13));
			st[2][c] = (byte)(gmul(a0,13)^gmul(a1,9)^gmul(a2,14)^gmul(a3,11));
			st[3][c] = (byte)(gmul(a0,11)^gmul(a1,13)^gmul(a2,9)^gmul(a3,14));
		}
	}

	private static byte gmul(byte a, int b) {
		byte p=0, hi;
		for(int i=0;i<8;i++) {
			if((b&1)!=0) p^=a;
			hi = (byte)(a&0x80);
			a <<= 1;
			if(hi!=0) a ^= 0x1b;
			b >>= 1;
		}
		return p;
	}

	private static void addRoundKey(byte[][] st, int[] w, int rnd) {
		for (int c=0;c<4;c++) {
			int word = w[rnd*4 + c];
			for (int r=0;r<4;r++)
				st[r][c] ^= (byte)((word >>> (24 - 8*r)) & 0xFF);
		}
	}

	private static byte[][] toState(byte[] in) {
		byte[][] st = new byte[4][4];
		for (int i=0;i<16;i++)
			st[i%4][i/4] = in[i];
		return st;
	}

	private static byte[] fromState(byte[][] st) {
		byte[] out = new byte[16];
		for (int i=0;i<16;i++)
			out[i] = st[i%4][i/4];
		return out;
	}
}