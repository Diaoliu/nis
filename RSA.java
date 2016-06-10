package de.unidue.iem.tdr.nis.client;

import java.util.Random;

public class RSA {

    private static final int[] PRIME = {
            // can not provide PRIME > 100
            // otherwise will case Stack overflow
            // for RSA public key, n must be bigger than ASCII Encoding Range
            // That means N = p * q > 122 = 'z'
            11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47,
            53,	59,	61,	67, 71,	73,	79,	83,	89,	97
    };

    /**
     * @return Int-Arr with key pair (n, e, d)*/
    public static int[] generateKey() {
        int n ,e, d;
        int p = getPrimeInt();
        int q =  getPrimeInt();
        n = p * q;
        int phi = (p - 1) * (q - 1);
        do {
            do { //  1 < e < phi
                // Random public key
                e = getPrimeInt();
            } while (e >= phi);
            // Multiplicative Inverse of e als private key
            d = ToolKit.extendeEuclid(phi, e);
        } while (d <= 1 || d >= phi); // 1 < d < phi
        return new int[]{ n,e,d };
    }
    /**
     * @return cipher text als integer array without encoding */
    public static int[] encryption(int n, int e, String clearText)  {
        char[] text = clearText.toCharArray();
        int[] cipher = new int[text.length];
        for (int i = 0; i < text.length; i++) {
            int c = ToolKit.power((int) text[i], e, n) % n;
            cipher[i] = c;
        }
        return cipher;
    }

    public static String decryption (int n, int d, String[] cipher) {
        String clearText = "";
        for (String num : cipher) {
            int i = Integer.valueOf(num);
            int c = ToolKit.power(i, d, n);
            clearText += (char)c;
        }
        return clearText;
    }

    private static int getPrimeInt () {
        int range = PRIME.length;
        Random random = new Random();
        return PRIME[random.nextInt(range)];
    }

}
