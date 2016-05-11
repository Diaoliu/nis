package de.unidue.iem.tdr.nis.client;

import java.util.Arrays;

public class DES {
    // Permuted Choice 1 table
    private static final byte[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1,  58, 50, 42, 34, 26, 18,
            10, 2,  59, 51, 43, 35, 27,
            19, 11, 3,  60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7,  62, 54, 46, 38, 30, 22,
            14, 6,  61, 53, 45, 37, 29,
            21, 13, 5,  28, 20, 12, 4
    };

    // Permuted Choice 2 table
    private static final byte[] PC2 = {
            14, 17, 11, 24, 1,  5,
            3,  28, 15, 6,  21, 10,
            23, 19, 12, 4,  26, 8,
            16, 7,  27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private static final byte[] LEFT_SHIFT = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    // Initial Permutation table
    private static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9,  1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    // Expansion (aka P-box) table
    private static final byte[] E = {
            32, 1,  2,  3,  4,  5,
            4,  5,  6,  7,  8,  9,
            8,  9,  10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    // S-boxes (i.e. Substitution boxes)
    private static final byte[][] S = { {
            14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
            0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
            4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
            15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
    }, {
            15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
            3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
            0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
            13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
    }, {
            10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
            13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
            13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
            1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
    }, {
            7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
            13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
            10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
            3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
    }, {
            2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
            14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
            4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
            11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
    }, {
            12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
            10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
            9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
            4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
    }, {
            4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
            13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
            1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
            6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
    }, {
            13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
            1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
            7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
            2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
    } };

    // Permutation table
    private static final byte[] P = {
            16, 7,  20, 21,
            29, 12, 28, 17,
            1,  15, 23, 26,
            5,  18, 31, 10,
            2,  8,  24, 14,
            32, 27, 3,  9,
            19, 13, 30, 6,
            22, 11, 4,  25
    };

    // Final permutation (aka Inverse permutation) table
    private static final byte[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    public int[] clearBits; // 64 bit
    public int[] key; // 64 bit
    public int[][] keySchedule; // 16 Group, 48 bit
    public int[][] bitLeftBlock; // 32 bit
    public int[][] bitRightBlock; // 32 bit
    public int[] cipherBits; // 64 bit

    public DES(int[] clearBits, int[] key){
        this.clearBits = clearBits;
        this.key       = key;
        bitLeftBlock   = new int[17][32];
        bitRightBlock  = new int[17][32];
        cipherBits     = new int[64];
    }

    public void generateKey(){
        int[] keys56bit  = keyInitialPermutation();
        int[][] subKeys  = keyRotation(keys56bit);
        this.keySchedule = keyFinalPermutation(subKeys);
    }

    public void cipherBits(){
        int[] permutedBits = bitInitialPermutation();
        rotateComplete(permutedBits);
        bitFinalPermutation();
    }

    private int[] keyInitialPermutation(){
        // Input as 64bit initial Keys, using table PC-1
        return permutation(this.key, PC1);
    }

    private int[][] keyRotation(int[] keys56bit){
        int[][] subkeys = new int[16][56];
        int[] cBlock = Arrays.copyOfRange(keys56bit, 0, 28);
        int[] dBlock = Arrays.copyOfRange(keys56bit, 28, 56);
        for (int i = 0; i < 16; i++) {
            cBlock = shiftBits(cBlock, LEFT_SHIFT[i]);
            dBlock = shiftBits(dBlock, LEFT_SHIFT[i]);
            subkeys[i] = mergeArray(cBlock, dBlock);
        }
        return  subkeys;
    }

    private int[][] keyFinalPermutation(int[][] subkeys){
        // Input as 56bit Keys, var permuteTable is PC-2
        int[][] permuted = new int[16][48];
        for (int i = 0; i < 16; i++) {
            permuted[i] = permutation(subkeys[i], PC2);
        }
        // return 48 bit keys
        return permuted;
    }

    public int[] bitInitialPermutation(){
        // Input as 64bit initial clear text, using table IP
        return permutation(this.clearBits, IP);
    }

    public void rotateComplete(int[] permutedBits){
        int[] left  = Arrays.copyOfRange(permutedBits, 0, 32);
        int[] right = Arrays.copyOfRange(permutedBits, 32, 64);
        for (int i = 1; i <= 16; i++) {
            int[] next = rotateOneRound(left, right, i);
            left  = Arrays.copyOfRange(next, 0, 32);
            right = Arrays.copyOfRange(next, 32, 64);
            this.bitLeftBlock[i] = left.clone();
            this.bitRightBlock[i] = right.clone();
        }
    }

    public int[] rotateOneRound(int[] left, int[] right, int round){
        int[] nextLeft = right.clone();
        int[] nextRight = feistel(left, right, keySchedule[round - 1]);
        return mergeArray(nextLeft, nextRight);
    }

    public int[] feistel(int[] leftblock, int[] rightBlock, int[] key){
        int[] bits = expansion(rightBlock);
        bits = xor(bits, key);
        bits = sBox(bits);
        return xor(leftblock, permutation(bits, P));
    }

    private int[] sBox(int[] bits){ // input 48 bit
        int[] rightBlock = new int[32];
        for (int i = 0; i < 48; i+=6) {
            int row = bits[i] * 2
                    + bits[i + 5];
            int col = bits[i + 1] * 8
                    + bits[i + 2] * 4
                    + bits[i + 3] * 2
                    + bits[i + 4];
            String bitString = Integer.toBinaryString(S[i / 6][row * 16 + col]);
            int padding = 4 - bitString.length();
            for (int j = padding; j < 4; j++) {
                rightBlock[(i / 6) * 4 + j] = bitString.charAt(j - padding) - '0';
            }
        }
        return rightBlock;
    }

    private void bitFinalPermutation(){
        System.arraycopy(bitRightBlock[16], 0, cipherBits, 0, 32);
        System.arraycopy(bitLeftBlock[16], 0, cipherBits, 32, 32);
        cipherBits = permutation(cipherBits, FP);
    }

    // Utils
    private int[] permutation(int[] arr, byte[] table){
        int len = table.length;
        int[] permuted = new int[len];
        for (int i = 0; i < len; i++) {
            permuted[i] = arr[table[i] - 1];
        }
        return permuted;
    }

    private int[] shiftBits(int[] array, int offset){
        int[] shifted = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            int index = ( i + offset ) % array.length;
            shifted[i] = array[index];
        }
        return  shifted;
    }

    private int[] expansion(int[] bits){
        // expand 32 bit to 48 bit using table E
        int[] permuted = new int[48];
        for (int i = 0; i < 48; i++) {
            permuted[i] = bits[E[i] - 1];
        }
        return permuted;
    }

    private int[] xor(int[] first, int[] second){
        for (int i = 0; i < first.length; i++) {
            first[i] = first[i] ^ second[i];
        }
        return first;
    }

    private int[] mergeArray(int[] fir, int[] sec){
        int len = fir.length;
        int[] res = new int[len * 2];
        System.arraycopy(fir, 0, res, 0, len);
        System.arraycopy(sec, 0, res, len, len);
        return res;
    }

}
