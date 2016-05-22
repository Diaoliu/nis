package de.unidue.iem.tdr.nis.client;

public class RC4 {

    public static int[] generateLoop (int[] state, String text){
        int len = state.length;
        int round  = text.length();
        int[] loop = new int[round];
        int i = 0, j = 0;
        for (int a = 0; a < round; a++) {
            i = ( i + 1 ) % len;
            j = ( j + state[i] ) % len;
            state = swap(state, i, j);
            loop[a] = state[(state[i] + state[j]) % len];
        }
        return loop;
    }

    public static int[] generatekeySchedule(int[] key) {
        int len = key.length;
        int[] state = new int[len];
        for (int i = 0; i < len; i++) {
            state[i] = i;
        }
        int j = 0;
        for (int i = 0; i < len; i++) {
            j = (j + key[i % len] + state[i]) % len;
            state = swap(state, i, j);
        }
        return state;
    }
    public static String encryption (int[] key, String text) {
        int[] state = generatekeySchedule(key);
        int[] bits  = generateLoop(state, text);
        String cipher = "";
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            cipher += toBinaryString((int)chars[i] ^ bits[i]);
        }
        return cipher;
    }


    private static int[] swap (int[] arr,int i ,int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return arr;
    }

    public static String toBinaryString (int num) {
        /* Input as ascii code, e.g f = 102
        * convert to 1 byte bit String, e.g. 01100110 */
        String bits = "";
        while (num != 0) {
            int bit = num % 2;
            bits = bit + bits;
            num /= 2;
        }
        int padding = 8 - bits.length();
        bits = "00000000".substring(0, padding) + bits;
        return bits;
    }
}
