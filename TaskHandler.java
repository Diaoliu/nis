package de.unidue.iem.tdr.nis.client;

import java.util.Arrays;

public class TaskHandler {
    // Aufgabe 2 XOR
    public static String xor(String hex1, String hex2){
        int i = Integer.parseInt(hex1,16);
        int j = Integer.parseInt(hex2,16);
        return Integer.toBinaryString(i^j);
    }
    // Aufgabe 4 Faktorisierung
    public static String factor(int n){
        for(int i=2;i<=n;i++){
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

    //Aufgabe 10. AES: Multiplikation im Raum GF8
    public static String AESmultiplication(TaskObject task){
        String str1 = task.getStringArray(0);
        String str2 = task.getStringArray(1);
        return AES.toHexString(
                AES.parseHexInt(str1) * AES.parseHexInt(str2) % 256
        );
    }

    //Aufgabe 11. AES: Schl¨¹ssel-Generierung
    public static String AESgeneralKey (TaskObject task) {
        String key = task.getStringArray(0);
        String[] keys = AES.generalkeySchedule(key, 2);
        return String.format("%S_%s_%S", key, keys[1], keys[2]);
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
}
