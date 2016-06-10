package de.unidue.iem.tdr.nis.client;

import java.util.Random;

/**
Toolkit for parse integer, string to binary and so on
 */
public class ToolKit {

    public static final String DIGITS = "0123456789ABCDEF";

    public static int mod (int a, int p) {
        int i = a / p;
        return a - p * i;
    }

    public static String xor (int a, int b) {
        int small = (a <= b) ? a :b ;
        int big   = (a <= b) ? b :a ;
        String p = toBinaryString(small);
        String q = toBinaryString(big);
        int len  = q.length() - 1;
        int index = p.length() - 1;
        String result = "";
        for (int i = len; i >= 0; i--) {
            if (index >= 0) {
                char c = p.charAt(index);
                if ( c == q.charAt(i) )
                    result = "0" + result;
                else
                    result =  "1" + result;
                index--;
            } else
                result = q.charAt(i) + result;
        }
        return result;
        //return (~a & b) | (a & ~b);
    }

    /**
     *  conver to binary string to binary Integer array
     *  @param hex  string in hex format, e.g. "ff"
     *  @return Ingteger in decimal format, e.g. 255
     */
    public static int hex2dec (String hex) {
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int d = DIGITS.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    /**
     *  conver to binary string to binary Integer array
     *  @param dec  Integer in dcimal format, e.g. 255
     *  @return string in hex format, e.g. FF
     */
    public static String dec2hex (int dec) {
        if (dec == 0)
            return "00";
        else if (dec <= 15)
            // return value must be at least 1 Byte lang
            return "0" + DIGITS.charAt(dec);
        String hex = "";
        while (dec > 0) {
            int digit = dec % 16;
            hex = DIGITS.charAt(digit) + hex;
            dec = dec / 16;
        }
        return hex;
    }
   /**
    *  conver to binary string to binary Integer array
    *  @param str  string in binary format, such as "101110"
    *  @return  Ingteger array, e.g. [1, 0, 1, 1, 1, 0]
    */
    public static int[] string2bit(String str){
        int len   = str.length();
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = str.charAt(i) - '0';
        }
        return arr;
    }

    public static String bit2string(int[] arr){
        String str = "";
        for (int anArr : arr) {
            str += Integer.toString(anArr);
        }
        return str;
    }

    public static String toBinaryString (int num) {
        String bits = "";
        while (num != 0) {
            int bit = num % 2;
            bits = bit + bits;
            num /= 2;
        }
        return bits;
    }

    /**
     * @return n ^ e % mod, e.g 7 ^ 23 mod 143 = 2 */
    public static int power(int n, int exp, int mod){
        if (exp > 1)
            return power(n, exp - 1, mod) * n % mod;
        else if(exp < 1)
            return power(n, -exp, mod) * (mod - 1) % mod;
        else
            return n % mod;
    }

    /**
     * @return start <= value <= start + interval - 1*/
    public static int getRandomInt(int start, int interval) {
        Random r = new Random();
        return r.nextInt(interval) + start;
    }

    /**
     * reference http://www.sanfoundry.com/java-program-extended-euclid-algorithm/
     * @param a for RSA, here a = n
     * @param b for RSA, here b = e
     * @return a * x + b * y = gcd(a,b), for n * x + e * d = 1
     */
    public static int extendeEuclid(int a, int b) {
        int x = 0, y = 1, lastx = 1, lasty = 0, temp;
        while (b > 1)
        {
            int q = a / b;
            int r = a % b;

            a = b;
            b = r;

            temp = x;
            x = lastx - q * x;
            lastx = temp;

            temp = y;
            y = lasty - q * y;
            lasty = temp;
        }
        return y;
    }
}
