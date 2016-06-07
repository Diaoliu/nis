package de.unidue.iem.tdr.nis.client;
/**
Toolkit for parse integer, string to binary and so on
 */
public class Kit {

    public static final String digits = "0123456789ABCDEF";

    // convert hex String to decimal Integer, such as FF => 255
    public static int hex2dec (String hex) {
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    // convert decimal Integer to hex String, such as 255 => FF
    public static String dec2hex (int dec) {
        if (dec == 0) return "0";
        String hex = "";
        while (dec > 0) {
            int digit = dec % 16;
            hex = digits.charAt(digit) + hex;
            dec = dec / 16;
        }
        return hex;
    }
}
