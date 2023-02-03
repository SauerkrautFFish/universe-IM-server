package edu.yjzxc.universeimserver.utils;

import java.util.Random;

public class VerifyCode {

    public static String buildDigitalVerificationCode(int number) {
        StringBuilder verifyCode = new StringBuilder();
        Random random = new Random();
        int count = 0;
        while(count < number) {
            verifyCode.append(random.nextInt(10));
            count ++;
        }

        return verifyCode.toString();
    }
}
