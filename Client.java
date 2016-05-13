package de.unidue.iem.tdr.nis.client;

import java.lang.Integer;
import java.lang.String;

/**
 * Diese Klasse ermoeglicht das Abrufen von Aufgaben vom Server und die
 * Implementierung der dazugehoerigen Loesungen.
 * <p>
 * Naehere Informationen zu den anderen Klassen und den einzelnen Aufgabentypen
 * entnehmen Sie bitte der entsprechenden Dokumentation im TMT und den Javadocs
 * zu den anderen Klassen.
 *
 * @see Connection
 * @see TaskObject
 *
 */
public class Client implements TaskDefs {
	private Connection con;
	private TaskObject currentTask;

	/* hier bitte die Matrikelnummer eintragen */
	private final int matrikelnr = 3021987;

	/* hier bitte das TMT-Passwort eintragen */
	private final String password = "rJJCqTTa";

	/* Aufgaben, die bearbeitet werden sollen */
	private final int[] tasks = { TASK_CLEARTEXT, TASK_XOR, TASK_MODULO,
			TASK_FACTORIZATION, TASK_VIGENERE, TASK_DES_KEYSCHEDULE,
			TASK_DES_RBLOCK, TASK_DES_FEISTEL, TASK_DES_ROUND, TASK_AES_GF8,
			TASK_AES_KEYEXPANSION, TASK_AES_MIXCOLUMNS,
			TASK_AES_TRANSFORMATION, TASK_AES_3ROUNDS, TASK_RC4_LOOP,
			TASK_RC4_KEYSCHEDULE, TASK_RC4_ENCRYPTION, TASK_DIFFIEHELLMAN,
			TASK_RSA_ENCRYPTION, TASK_RSA_DECRYPTION, TASK_ELGAMAL_ENCRYPTION,
			TASK_ELGAMAL_DECRYPTION };

	/**
	 * Klassenkonstruktor. Baut die Verbindung zum Server auf.
	 */
	public Client() {
		con = new Connection();
		if (con.auth(matrikelnr, password))
			System.out.println("Anmeldung erfolgreich.");
		else
			System.out.println("Anmeldung nicht erfolgreich.");
	}

	/**
	 * Besteht die Verbindung zum Server?
	 *
	 * @return true, falls Verbindung bereit, andernfalls false
	 */
	public boolean isReady() {
		return con.isReady();
	}

	/**
	 * Beendet die Verbindungs zum Server.
	 */
	public void close() {
		con.close();
	}

	/**
	 * Durchlaeuft eine Liste von Aufgaben und fordert diese vom Server an.
	 */
	public void taskLoop() {
		String solution;
		for (int task : tasks) {
			currentTask = con.getTask(task);
			switch (task) {
				case TASK_CLEARTEXT:
					solution = currentTask.getStringArray(0);
					break;
				case TASK_XOR:
					String arg1 = currentTask.getStringArray(0);
					String arg2 = currentTask.getStringArray(1);
					solution = TaskHandler.xor(arg1, arg2);
					break;
				case TASK_MODULO:
					int integer1 = currentTask.getIntArray(0);
					int integer2 = currentTask.getIntArray(1);
					solution = Integer.toString(integer1 % integer2);
					break;
				case TASK_FACTORIZATION:
					int n = currentTask.getIntArray(0);
					String str = TaskHandler.factor(n);
					solution = str.substring(0, str.length() - 1);
					break;
				case TASK_VIGENERE:
					String cipher = currentTask.getStringArray(0);
					String key = currentTask.getStringArray(1);
					solution = TaskHandler.vigenere(cipher, key);
					break;
				case TASK_DES_KEYSCHEDULE:
					solution = TaskHandler.DESkeyschedule(currentTask);
					break;
				case TASK_DES_RBLOCK:
					solution = TaskHandler.DESRBlock(currentTask);
					break;
				case TASK_DES_FEISTEL:
					solution = TaskHandler.DESfeistel(currentTask);
					break;
				case TASK_DES_ROUND:
					solution = TaskHandler.DEScomplete(currentTask);
					break;
				case TASK_AES_GF8:
					solution = TaskHandler.AESmultiplication(currentTask);
					break;
				case TASK_AES_KEYEXPANSION:
					solution = TaskHandler.AESgeneralKey(currentTask);
					break;
				default:
					currentTask = con.getTask(task);
					solution = "Nicht implementiert!";
					break;
			}

			if (con.sendSolution(solution))
				System.out.println("Aufgabe " + task + ": Loesung korrekt");
			else
				System.out.println("Aufgabe " + task + ": Loesung falsch");
		}
	}

	public static void main(String[] args) {
		Client c = new Client();
		if (c.isReady()) {
			c.taskLoop();
		}
		c.close();
	}
}
