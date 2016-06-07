package de.unidue.iem.tdr.nis.client;

public class AES {

    private static final int[][] C = {
            { 0x2, 0x3, 0x1, 0x1 },
            { 0x1, 0x2, 0x3, 0x1 },
            { 0x1, 0x1, 0x2, 0x3 },
            { 0x3, 0x1, 0x1, 0x2 }
    };

    private static final int[][] S_BOX = {
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };

    private static final Object[][] RCON = {
            { 0x01, 0x00, 0x00, 0x00 },
            { 0x02, 0x00, 0x00, 0x00 },
            { 0x04, 0x00, 0x00, 0x00 },
            { 0x08, 0x00, 0x00, 0x00 },
            { 0x10, 0x00, 0x00, 0x00 },
            { 0x20, 0x00, 0x00, 0x00 },
            { 0x40, 0x00, 0x00, 0x00 },
            { 0x80, 0x00, 0x00, 0x00 },
            { 0x1b, 0x00, 0x00, 0x00 },
            { 0x36, 0x00, 0x00, 0x00 }
    };

    private static final int[] SR = {
             1,  2,  3,  4,
             6,  7,  8,  5,
            11, 12,  9, 10,
            16, 13, 14, 15,
    };

    public static int parseHexInt(String str){
        // str must be between "00" and "FF"
        str = str.toLowerCase();
        int sum = 0;
        sum += 16 * hexChar2int(str.charAt(0))
                + hexChar2int(str.charAt(1));
        return sum;
    }

    public static String toHexString (int dec) {
        // input must between 0 and 255
        char tens  = int2hexChar(dec / 16);
        char units = int2hexChar(dec % 16);
        return String.format("%c%c", tens, units);
    }

    private static int hexChar2int(char c){
        int charPos = c - '0';
        if (charPos > 9)
            /* Position according ACSCII
              '0' => 48, 'a' => 97 */
            charPos -= 39;
        return charPos;
    }

    private static char int2hexChar (int i) {
        char c;
        if (i <= 9)
            c = (char) (48 + i);
        else
            c = (char) (97 + i  - 10);
        return c;
    }

    public static String[] generalkeySchedule (String key, int round) {
        int len = round + 1;
        String[] roundKey = new String[len];
        roundKey[0] = key;
        for (int i = 1; i < len; i++) {
            roundKey[i] = generalRoundKey(roundKey[i - 1], i);
        }
        return roundKey;
    }

    private static String generalRoundKey (String key, int nth) {
        /*
        input as 4 Words, 32 chars, 128 bits round key from last round
        Seperate key into 4 parts, eachone is word (4 bytes)
        e.g. 2b7e1516 28aed2a6 abf71588 09cf4f3c
        */
        String[][] words = splitText(key);
        /* Generate First Column, W0 XOR W4 XOR Rcon */
        String[] next1 = generalFirstCol(words[0], words[3], nth);
        String[] next2 = xorWord(words[1], next1);
        String[] next3 = xorWord(words[2], next2);
        String[] next4 = xorWord(words[3], next3);
        return  joinByte(next1) +
                joinByte(next2) +
                joinByte(next3) +
                joinByte(next4);
    }

    private static String[] generalFirstCol (String[] w0, String[] w3, int nth){
        String[] rotWord = rotWord(w3);
        String[] tmp = xorWord(w0, rotWord);
        return xorWord(tmp, RCON[nth - 1]);
    }

    public static String[][] splitText (String text) {
        /* Split 128 bit Text or Key into
        * a Matrix, each item is a byte HEX String */
        String[][] matrix = new String[4][4];
        int len = text.length();
        for (int i = 0; i < len; i += 2) {
            matrix[i / 8][( i % 8 ) / 2] = text.substring(i, i + 2);
        }
        return matrix;
    }

    private static String[] xorWord (String[] word1, Object[] word2) {
        String[] word = new String[4];
        for (int i = 0; i < 4; i++) {
            word[i] = xorByte(word1[i], word2[i]);
        }
        return word;
    }

    public static String xorByte (Object byte1, Object byte2) {
        int i, j;
        i = ( byte1 instanceof String) ?  parseHexInt((String) byte1) : (int) byte1;
        j = ( byte2 instanceof String) ?  parseHexInt((String) byte2) : (int) byte2;
        return toHexString(i ^ j);
    }

    private static String[] rotWord (String[] word) {
        // input such as "2a6c7605"
        word = shiftWord(word, 1);
        String[] rotWord = new String[word.length];
        for (int i = 0; i < word.length; i++) {
            rotWord[i] = subByte(word[i]);
        }
        return rotWord;
    }

    private static String joinByte (String[] bytes) {
        String word = "";
        for (String abyte : bytes){
            word += abyte;
        }
        return word;
    }

    public static String mixColumns (String text) {
        /* Input text as 128 bits */
        String[][] words = splitText(text);
        String mixed = "";
        for (String[] word : words) {
            mixed += GMultipl(word);
        }
        return mixed;
    }

    public static String GMultipl (String[] col) {
        String mixed = "";
        for (int i = 0; i < 4; i++) {
            int res = 0;
            for (int j = 0; j < col.length ; j++) {
                int hex = parseHexInt(col[j]);
                int c = C[i][j];
                if (c == 3)
                    hex = hex * 2 ^ hex;
                else
                    hex *= c;
                if (hex >= 0xff)
                    hex ^= 0x11b;
                res ^= hex;
            }
            mixed += toHexString(res);
        }

        return mixed;
    }

    public static String subWords (String text) {
        int len = text.length();
        String subWord = "";
        for (int i = 0; i < len; i += 2) {
            String byteStr = text.substring(i, i + 2);
            subWord += AES.subByte(byteStr);
        }
        return subWord;
    }

    public static String shiftRows (String text) {
        String[][] words = splitText(text);
        String shifted = "";
        for (int i = 0; i < 16; i++) {
            int row = i % 4;
            int col = i / 4;
            int pos = SR[row * 4 + col] - 1;
            shifted += words[pos % 4][pos / 4];
        }
        return shifted;
    }

    public static String addRoundkey (String text, String key) {
        String[][] words = splitText(text);
        String[][] keys  = splitText(key);
        String added     = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                added += xorByte(words[i][j], keys[i][j]);
            }
        }
        return added;
    }


    public static String subByte (String key) {
        // String from "00" to "ff"
        int row = hexChar2int(key.charAt(0));
        int col = hexChar2int(key.charAt(1));
        int hex = S_BOX[row][col];
        return toHexString(hex);
    }

    private static String[] shiftWord (String[] word, int offset) {
        String[] shifted = new String[word.length];
        for (int i = 0; i < word.length; i++) {
            shifted[i] = word[( i + offset ) % word.length];
        }
        return shifted;
    }
}
