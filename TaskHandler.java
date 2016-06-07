package de.unidue.iem.tdr.nis.client;

import java.util.Arrays;
import java.util.Random;

public class TaskHandler {
    // Aufgabe 2 XOR
    public static String xor(String hex1, String hex2){
        int i = Kit.hex2dec(hex1);
        int j = Kit.hex2dec(hex2);
        return Integer.toBinaryString(i^j);
    }
    // Aufgabe 4 Faktorisierung
    public static String factor(int n){
        for(int i = 2; i <= n; i++){
            if(n % i == 0){
                return i + "*" + factor(n / i);
            }
        }
        return "";
    }
    //Aufgabe 5 Vigenere
    public static String vigenere(String cipher, String key){
        String result = "";
        int  a = (int)'a';
        cipher = cipher.toLowerCase();
        key    = key.toLowerCase();
        int cipherLen = cipher.length();
        int keyLen = key.length();
        for (int i = 0; i < cipherLen; i++) {
            int offset = (int)cipher.charAt(i) - (int)key.charAt(i % keyLen);
            char clear_text = (char) ((offset >= 0)? a + offset : a + offset + 26);
            result += clear_text;
        }
        return result;
    }
    // Aufgabe 6 DES: Rundenschluessel-Berechnung
    public static String DESkeyschedule(TaskObject task){
        int round  = task.getIntArray(0);
        int[] key = string2bit(task.getStringArray(0));
        DES des = new DES(new int[64], key);
        des.generateKey();
        return bit2string(des.keySchedule[round - 1]);
    }

    // Aufgabe 7. DES: R-Block-Berechnung
    public static String DESRBlock(TaskObject task){
        int[] bits = string2bit(task.getStringArray(0));
        int round  = task.getIntArray(0);
        DES des = new DES(bits, new int[64]);
        des.generateKey();
        des.cipherBits();
        return bit2string(des.bitRightBlock[round]);
    }

    // Aufgabe 8. DES: Feistel-Funktion
    public static String DESfeistel(TaskObject task){
        int[] bits  = string2bit(task.getStringArray(0));
        int[] key   = string2bit(task.getStringArray(1));
        int[] left  = Arrays.copyOfRange(bits, 0, 32);
        int[] right = Arrays.copyOfRange(bits, 32, 64);
        DES des = new DES(new int[64], new int[64]);
        return bit2string(des.feistel(left, right, key));

    }

    // Aufgabe 9. DES: Berechnung einer Runde
    public static String DEScomplete(TaskObject task){
        int[] left  = string2bit(task.getStringArray(0));
        int[] right  = string2bit(task.getStringArray(1));
        int[] key   = string2bit(task.getStringArray(2));
        int round = task.getIntArray(0);
        DES des = new DES(new int[64], key);
        des.generateKey();
        return  bit2string(des.rotateOneRound(left, right, round));
    }

    // Aufgabe 10. AES: Multiplikation im Raum GF8
    public static String AESmultiplication(TaskObject task){
        String str1 = task.getStringArray(0);
        String str2 = task.getStringArray(1);
        int hex = AES.parseHexInt(str1) * AES.parseHexInt(str2);
        if (hex >= 0xff)
            hex ^= 0x11b;
        return Kit.dec2hex(hex);
    }

    // Aufgabe 11. AES: Schl¨¹ssel-Generierung
    public static String AESgeneralKey (TaskObject task) {
        String key = task.getStringArray(0);
        String[] keys = AES.generalkeySchedule(key, 2);
        return String.format("%S_%s_%S", key, keys[1], keys[2]);
    }

    // Aufgabe 12. AES: MixColumns()
    public static String AESmixColumns (TaskObject task) {
        String text = task.getStringArray(0);
        return AES.mixColumns(text);
    }

    // Aufgabe 13. AES: SubBytes(), ShiftRows() und MixColumns()
    public static String AEStransform (TaskObject task) {
        String text = task.getStringArray(0);
        return AES.mixColumns(
                AES.shiftRows(
                        AES.subWords(text)
                ));
    }

    // Aufgabe 14. AES: Initiale & zwei weitere Runden
    public static String AES3rounds (TaskObject task) {
        String text = task.getStringArray(0);
        String key  = task.getStringArray(1);
        String[] roundKeys = AES.generalkeySchedule(key, 2);
        String initalRound = AES.addRoundkey(text, key);
        String firstRound = AES.addRoundkey(
                AES.mixColumns(AES.shiftRows(AES.subWords(initalRound))),
                roundKeys[1]
        );
        String secondRound = AES.addRoundkey(
                AES.mixColumns(AES.shiftRows(AES.subWords(firstRound))),
                roundKeys[2]
        );
        return String.format("%S_%s_%S", initalRound, firstRound, secondRound);
    }

    // Aufgabe 15. RC4: Generation Loop
    public static String RC4Loop (TaskObject task) {
        String[] state   = task.getStringArray(0).split("_");
        String text      = task.getStringArray(1);
        int[] stateTable = new int[state.length];
        for (int i = 0; i < state.length; i++) {
            stateTable[i] = Integer.parseInt(state[i]);
        }
        int[] loop   = RC4.generateLoop(stateTable, text);
        return bit2string(loop);
    }

    // Aufgabe 16. RC4: Keyscheduling
    public static String RC4keySchedule (TaskObject task) {
        String[] key = task.getStringArray(0).split("_");
        int[] initKey = new int[key.length];
        for (int i = 0; i < key.length; i++) {
            initKey[i] = Integer.parseInt(key[i]);
        }
        int[] state = RC4.generatekeySchedule(initKey);
        String stateTable = "";
        for (int index : state) {
            stateTable += index + "_";
        }
        return stateTable.substring(0, stateTable.length() - 1);
    }

    // Aufgabe 17. RC4: Verschluesselung
    public static String RC4encryption (TaskObject task) {
        String[] key = task.getStringArray(0).split("_");
        int[] initKey = new int[key.length];
        for (int i = 0; i < key.length; i++) {
            initKey[i] = Integer.parseInt(key[i]);
        }
        String text = task.getStringArray(1);
        return RC4.encryption(initKey, text);
    }

    // Aufgabe 18. Diffie-Hellman
    public static String diffieHellman (TaskObject task, Connection con) {
        int p    = task.getIntArray(0);
        int g    = task.getIntArray(1);
        double B = task.getDoubleArray(0);
        int a = getRandomInt(1, p - 2);
        int A = power(g, a, p);
        con.sendMoreParams(task, new String[] { String.valueOf(A) });
        int key = power((int)B, a, p);
        String cipher = "";
        String text = task.getStringArray(0);
        for (String str : text.split("_")) {
            cipher += (char)(Integer.valueOf(str) ^ key);
        }
        return cipher;
    }

    // Aufgabe 19. RSA: Verschluesselung
    public static String RSAencryption (TaskObject task) {
        int n = task.getIntArray(0);
        int e = task.getIntArray(1);
        char[] clearText = task.getStringArray(0).toCharArray();
        String cipher = "";
        for (char word : clearText) {
            long result = power((int)word, e, n) % n;
            cipher += result + "_";
        }
        return cipher.substring(0, cipher.length() - 1);
    }

    // Aufgabe 20. RSA: Entschluesselung
    public static String RASdecryption (TaskObject task)  {
        /* public key (143, 23)
        *  private key (143, 47)*/
        String[] cipher = task.getStringArray(0).split("_");
        String clearText = "";
        for (String num : cipher) {
            int i = Integer.valueOf(num);
            clearText += (char)power(i, 47, 143);
        }
        return clearText;
    }

    // 21. ElGamal: Verschluesselung
    public static String ElGamalencryption (TaskObject task)  {
        int p     = task.getIntArray(0);
        int alpha = task.getIntArray(1);
        int beta  = task.getIntArray(2);
        // random integer 1 <= k <= p - 2
        int k = getRandomInt(1, p - 2);
        int key = power(beta, k, p);
        int y1  = power(alpha, k, p);
        String cipher = Kit.dec2hex(y1) + "_";
        char[] clearText = task.getStringArray(0).toCharArray();
        for (char m : clearText) {
            cipher += Kit.dec2hex((int)m * key % p) + "_";
        }
        return  cipher.substring(0, cipher.length() - 1);
    }

    // Utils
    private static int[] string2bit(String str){
        int len   = str.length();
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = str.charAt(i) - '0';
        }
        return arr;
    }

    private static String bit2string(int[] arr){
        String str = "";
        for (int anArr : arr) {
            str += Integer.toString(anArr);
        }
        return str;
    }

    /* @return n ^ e % mod, e.g 7 ^ 23 mod 143 = 2 */
    public static int power(int n, int exp, int mod){
        if (exp != 1)
            return power(n, exp -1, mod) * n % mod;
        else
            return n % mod;
    }
    public static int getRandomInt(int start, int interval) {
        // return start <= value <= start + interval - 1
        Random r = new Random();
        return r.nextInt(interval) + start;
    }
}
