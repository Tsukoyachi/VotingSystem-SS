package helper;

import java.util.Random;

public class OTPGenerator {
    public static String generate(int n) {
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        if(n<= 0) {
            return res.toString();
        }

        String alphaNumericElement = "0123456789" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";

        while(res.length()<n) {
            res.append(alphaNumericElement.charAt(rand.nextInt(alphaNumericElement.length())));
        }

        return res.toString();
    }
}
