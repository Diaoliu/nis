package de.unidue.iem.tdr.nis.client;

import java.util.Arrays;

public class TaskHandler {
    // Aufgabe 2 XOR
    // Using handmade Code for XOR only here, later using standart ^ opertion  to speed up and for clear code
    // Impelementation finden Sie in ToolKit Class
    public static String xor(String hex1, String hex2){
        int i = ToolKit.hex2dec(hex1);
        int j = ToolKit.hex2dec(hex2);
        return ToolKit.xor(i, j);
    }
    // Aufgabe 4 Faktorisierung
    // Using handmade Code for MOD only here, later using standart % opertion to speed up and for clear code
    // Impelementation finden Sie in ToolKit Class
    public static String factor(int n){
        for(int i = 2; i <= n; i++){
            if(ToolKit.mod(n, i) == 0)
                return i + "*" + factor(n / i);
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
        int[] key = ToolKit.string2bit(task.getStringArray(0));
        DES des = new DES(new int[64], key);
        des.generateKey();
        return ToolKit.bit2string(des.keySchedule[round - 1]);
    }

    // Aufgabe 7. DES: R-Block-Berechnung
    public static String DESRBlock(TaskObject task){
        int[] bits = ToolKit.string2bit(task.getStringArray(0));
        int round  = task.getIntArray(0);
        DES des = new DES(bits, new int[64]);
        des.generateKey();
        des.cipherBits();
        return ToolKit.bit2string(des.bitRightBlock[round]);
    }

    // Aufgabe 8. DES: Feistel-Funktion
    public static String DESfeistel(TaskObject task){
        int[] bits  = ToolKit.string2bit(task.getStringArray(0));
        int[] key   = ToolKit.string2bit(task.getStringArray(1));
        int[] left  = Arrays.copyOfRange(bits, 0, 32);
        int[] right = Arrays.copyOfRange(bits, 32, 64);
        DES des = new DES(new int[64], new int[64]);
        return ToolKit.bit2string(des.feistel(left, right, key));
    }

    // Aufgabe 9. DES: Berechnung einer Runde
    public static String DEScomplete(TaskObject task){
        int[] left   = ToolKit.string2bit(task.getStringArray(0));
        int[] right  = ToolKit.string2bit(task.getStringArray(1));
        int[] key    = ToolKit.string2bit(task.getStringArray(2));
        int round    = task.getIntArray(0);
        DES des      = new DES(new int[64], key);
        des.generateKey();
        return  ToolKit.bit2string(des.rotateOneRound(left, right, round));
    }

    // Aufgabe 10. AES: Multiplikation im Raum GF8
    public static String AESmultiplication(TaskObject task){
        String str1 = task.getStringArray(0);
        String str2 = task.getStringArray(1);
        // int hex = AES.parseHexInt(str1) * AES.parseHexInt(str2);
        int hex = ToolKit.hex2dec(str1) * ToolKit.hex2dec(str2);
        if (hex >= 0xff)
            hex ^= 0x11b;
        return ToolKit.dec2hex(hex);
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
        return ToolKit.bit2string(loop);
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
        int a = ToolKit.getRandomInt(1, p - 2);
        int A = ToolKit.power(g, a, p);
        con.sendMoreParams(task, new String[] { String.valueOf(A) });
        int key = ToolKit.power((int) B, a, p);
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
        String clearText = task.getStringArray(0);
        int[] cipher = RSA.encryption(n, e, clearText);
        String cipherString = "";
        for (int c : cipher) {
            cipherString += c + "_";
        }
        return cipherString.substring(0, cipherString.length() - 1);
    }

    // Aufgabe 20. RSA: Entschluesselung
    public static String RASdecryption (TaskObject task, int[] keyPair)  {
        String[] cipher = task.getStringArray(0).split("_");
        int n = keyPair[0];
        int d = keyPair[2];
        return RSA.decryption(n, d, cipher);
    }

    // 21. ElGamal: Verschluesselung
    public static String ELGamalEncryption(TaskObject task)  {
        int p     = task.getIntArray(0);
        int alpha = task.getIntArray(1);
        int beta  = task.getIntArray(2);
        String clearText = task.getStringArray(0);
        return ELGamal.encryption(p, alpha, beta, clearText);
    }

    //  22 ELGamal : Entschluesselung
    public static String ELGamalDecryption (TaskObject task, int[] keyPair) {
        String cipher = task.getStringArray()[0];
        return ELGamal.decryption(cipher, keyPair);
    }
}
