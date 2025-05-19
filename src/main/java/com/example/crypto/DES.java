package com.example.crypto;
import java.util.Arrays;
import java.util.Base64;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class DES {
	private static final int[] IP = {
					58,50,42,34,26,18,10,2,
					60,52,44,36,28,20,12,4,
					62,54,46,38,30,22,14,6,
					64,56,48,40,32,24,16,8,
					57,49,41,33,25,17,9,1,
					59,51,43,35,27,19,11,3,
					61,53,45,37,29,21,13,5,
					63,55,47,39,31,23,15,7
	};
	private static final int[] FP = {
					40,8,48,16,56,24,64,32,
					39,7,47,15,55,23,63,31,
					38,6,46,14,54,22,62,30,
					37,5,45,13,53,21,61,29,
					36,4,44,12,52,20,60,28,
					35,3,43,11,51,19,59,27,
					34,2,42,10,50,18,58,26,
					33,1,41,9,49,17,57,25
	};

	private static final int[] E = {
					32,1,2,3,4,5,
					4,5,6,7,8,9,
					8,9,10,11,12,13,
					12,13,14,15,16,17,
					16,17,18,19,20,21,
					20,21,22,23,24,25,
					24,25,26,27,28,29,
					28,29,30,31,32,1
	};

	private static final int[][][] S_BOX = {
					{
									{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
									{0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
									{4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
									{15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}
					},
					{
									{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
									{3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
									{0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
									{13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
					},
					{
									{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
									{13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
									{13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
									{1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}
					},
					{
									{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
									{13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
									{10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
									{3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
					},
					{
									{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
									{14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
									{4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
									{11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}
					},
					{
									{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
									{10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
									{9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
									{4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}
					},
					{
									{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
									{13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
									{1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
									{6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}
					},
					{
									{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
									{1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
									{7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
									{2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
					}
	};

	private static final int[] P = {
					16,7,20,21,
					29,12,28,17,
					1,15,23,26,
					5,18,31,10,
					2,8,24,14,
					32,27,3,9,
					19,13,30,6,
					22,11,4,25
	};

	private static final int[] PC1 = {
					57,49,41,33,25,17,9,
					1,58,50,42,34,26,18,
					10,2,59,51,43,35,27,
					19,11,3,60,52,44,36,
					63,55,47,39,31,23,15,
					7,62,54,46,38,30,22,
					14,6,61,53,45,37,29,
					21,13,5,28,20,12,4
	};private static final int[] PC2 = {
					14,17,11,24,1,5,
					3,28,15,6,21,10,
					23,19,12,4,26,8,
					16,7,27,20,13,2,
					41,52,31,37,47,55,
					30,40,51,45,33,48,
					44,49,39,56,34,53,
					46,42,50,36,29,32
	};

	private static final int[] SHIFTS = {
					1, 1, 2, 2, 2, 2, 2, 2,
					1, 2, 2, 2, 2, 2, 2, 1
	};

	private static int[] bytesToBits(byte[] bytes) {
		int[] bits = new int[bytes.length * 8];
		for (int i = 0; i < bytes.length; i++) {
			for (int j = 7; j >= 0; j--) {
				bits[i * 8 + (7 - j)] = (bytes[i] >> j) & 1;
			}
		}
		return bits;
	}

	private static byte[] bitsToBytes(int[] bits) {
		byte[] bytes = new byte[bits.length / 8];
		for (int i = 0; i < bytes.length; i++) {
			byte b = 0;
			for (int j = 0; j < 8; j++) {
				b = (byte)((b << 1) | bits[i * 8 + j]);
			}
			bytes[i] = b;
		}
		return bytes;
	}

	private static int[] permute(int[] bits, int[] table) {
		int[] permuted = new int[table.length];
		for (int i = 0; i < table.length; i++) {
			permuted[i] = bits[table[i] - 1];
		}
		return permuted;
	}

	private static int[] leftShift(int[] bits, int n) {
		int[] shifted = new int[bits.length];
		System.arraycopy(bits, n, shifted, 0, bits.length - n);
		System.arraycopy(bits, 0, shifted, bits.length - n, n);
		return shifted;
	}

	private static int[] rightShift(int[] bits, int n) {
		int[] shifted = new int[bits.length];
		System.arraycopy(bits, bits.length - n, shifted, 0, n);
		System.arraycopy(bits, 0, shifted, n, bits.length - n);
		return shifted;
	}

	private static int[] xor(int[] a, int[] b) {
		int[] result = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i] ^ b[i];
		}
		return result;
	}

	private static int[][] generateSubkeys(byte[] keyBytes) {
		int[] keyBits = bytesToBits(keyBytes);
		int[] permutedKey = permute(keyBits, PC1);
		int[] C = Arrays.copyOfRange(permutedKey, 0, 28);
		int[] D = Arrays.copyOfRange(permutedKey, 28, 56);

		int[][] subkeys = new int[16][];
		for (int round = 0; round < 16; round++) {
			C = leftShift(C, SHIFTS[round]);
			D = leftShift(D, SHIFTS[round]);
			int[] CD = new int[56];
			System.arraycopy(C, 0, CD, 0, 28);
			System.arraycopy(D, 0, CD, 28, 28);
			subkeys[round] = permute(CD, PC2);
		}
		return subkeys;
	}

	private static int[] feistel(int[] R, int[] subkey) {
		int[] expandedR = permute(R, E);
		int[] xored = xor(expandedR, subkey);
		int[] output = new int[32];

		for (int i = 0; i < 8; i++) {
			int[] sixBits = Arrays.copyOfRange(xored, i * 6, (i + 1) * 6);
			int row = (sixBits[0] << 1) | sixBits[5];
			int col = (sixBits[1] << 3) | (sixBits[2] << 2) | (sixBits[3] << 1) | sixBits[4];
			int sBoxVal = S_BOX[i][row][col];
			for (int j = 0; j < 4; j++) {
				output[i * 4 + (3 - j)] = (sBoxVal >> j) & 1;
			}
		}
		output = permute(output, P);
		return output;
	}

	public static byte[] encryptBlock(byte[] block, byte[] key) {
		int[] blockBits = bytesToBits(block);
		blockBits = permute(blockBits, IP);

		int[] L = Arrays.copyOfRange(blockBits, 0, 32);
		int[] R = Arrays.copyOfRange(blockBits, 32, 64);

		int[][] subkeys = generateSubkeys(key);
		byte[] round16SubkeyBytes = bitsToBytes(subkeys[15]);
		String round16KeyText = Base64.getEncoder().encodeToString(round16SubkeyBytes);
		System.out.println("Encryption Round 16 subkey (Base64, 8 characters): " + round16KeyText);

		for (int round = 0; round < 16; round++) {
			int[] temp = R;
			int[] fOutput = feistel(R, subkeys[round]);
			R = xor(L, fOutput);
			L = temp;
		}

		int[] preOutput = new int[64];
		System.arraycopy(R, 0, preOutput, 0, 32);
		System.arraycopy(L, 0, preOutput, 32, 32);
		int[] cipherBits = permute(preOutput, FP);

		return bitsToBytes(cipherBits);
	}

	public static byte[] decryptBlock(byte[] block, byte[] key) {
		int[] blockBits = bytesToBits(block);
		blockBits = permute(blockBits, IP);

		int[] L = Arrays.copyOfRange(blockBits, 0, 32);
		int[] R = Arrays.copyOfRange(blockBits, 32, 64);

		int[][] subkeys = generateSubkeys(key);

		for (int round = 15; round >= 0; round--) {
			byte[] subkeyBytes = bitsToBytes(subkeys[round]);
			String subkeyBase64 = Base64.getEncoder().encodeToString(subkeyBytes);
			System.out.println("Decryption Round " + (16 - round) + " subkey (Base64): " + subkeyBase64);
		}

		for (int round = 15; round >= 0; round--) {
			int[] temp = R;
			int[] fOutput = feistel(R, subkeys[round]);
			R = xor(L, fOutput);
			L = temp;
		}

		int[] preOutput = new int[64];
		System.arraycopy(R, 0, preOutput, 0, 32);
		System.arraycopy(L, 0, preOutput, 32, 32);
		int[] plainBits = permute(preOutput, FP);

		return bitsToBytes(plainBits);
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public static byte[] hexToBytes(String hex) {
		int len = hex.length();
		if (len % 2 != 0) {
			throw new IllegalArgumentException("Invalid hex string.");
		}
		byte[] bytes = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			bytes[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
		}
		return bytes;
	}
}

