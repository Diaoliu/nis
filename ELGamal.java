package de.unidue.iem.tdr.nis.client;

import java.util.Random;

public class ELGamal {

    private static final int[] PRIME = {
            // start with prime nummber > ASCII Endcodin of "z" = 123
            127, 131, 137, 139, 149, 151, 157, 163,
            167, 173, 179, 181, 191, 193, 197, 199
    };

    /**
     *
     * @return the key pair (p, alpha, beta, a) sended to Bob
     */
    public static int[] generateKey () {
        int p = getPrimeInt();
        int alpha = -1;
        while (alpha == -1) {
            alpha = findPrimeRoot(p);
        }
        int a = ToolKit.getRandomInt(0, p -2);
        int beta = ToolKit.power(alpha, a, p);
        return new int[]{p, alpha, beta, a};
    }

    public static String encryption (int p, int alpha, int beta, String text) {
        int k = ToolKit.getRandomInt(1, p - 2);
        int key = ToolKit.power(beta, k, p);
        int y1  = ToolKit.power(alpha, k, p);
        String cipher = ToolKit.dec2hex(y1) + "_";
        char[] clearText = text.toCharArray();
        for (char m : clearText) {
            cipher += ToolKit.dec2hex((int) m * key % p) + "_";
        }
        return  cipher.substring(0, cipher.length() - 1);
    }

    /**
     *
     * @param cipher HEX String sperated with "_"
     * @param keyPair Private Key (n, d)
     * @return clear text
     */
    public static String decryption (String cipher, int[] keyPair) {
        String[] text = cipher.split("_");
        int beta = ToolKit.hex2dec(text[0]);
        int n = keyPair[0];
        int d = keyPair[1];
        int key = ToolKit.power(beta, d, n);
        String clearText = "";
        for (int i = 1; i < text.length; i++) {
            int y = ToolKit.hex2dec(text[i]);
            int inverse = ToolKit.extendeEuclid(n, key);
            int c;
            if ( inverse >= 0)
                c = y * inverse % n;
            else
                c = y * -inverse * ( n - 1) % n;
            clearText += (char)c;
        }
        return clearText;
    }

    /**
     *
     * @param prime Prime  Nummer betwwen 100 and 200
     * @return the smallest primitiva root of input
     */
    private static int findPrimeRoot (int prime) {
        // try every nummer < prime
        for (int root = 2; root < prime; root++) {
            int counter = 1;
            for (int i = 1; i < prime - 1; i++) {
                int k = ToolKit.power(root, i, prime);
                counter ++;
                if (k == 1) break;
            }
            if (counter == prime - 1){
                return root;
            }
        }
        // prime root not find
        return -1;
    }

    private static int getPrimeInt () {
        int range = PRIME.length;
        Random random = new Random();
        return PRIME[random.nextInt(range)];
    }
}
