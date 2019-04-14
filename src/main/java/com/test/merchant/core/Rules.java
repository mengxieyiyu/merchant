package com.test.merchant.core;

import com.test.merchant.entity.OtherSymbol;
import com.test.merchant.enums.SymbolsEnum;

import java.lang.reflect.ReflectPermission;
import java.util.ArrayList;
import java.util.List;

/**
 * complete rules
 */
public class Rules {
    private static List<String> subtractedRule;
    private static List<String> errorRule;

    static {
        subtractedRule = new ArrayList<String>();
        // "I" can be subtracted from "V" and "X" only.
        subtractedRule.add("IV");
        subtractedRule.add("IX");
        //  "X" can be subtracted from "L" and "C" only.
        subtractedRule.add("XL");
        subtractedRule.add("XC");
        //  "C" can be subtracted from "D" and "M" only.
        subtractedRule.add("CD");
        subtractedRule.add("CM");

        errorRule = new ArrayList<String>();
        // The symbols "I", "X", "C", and "M" can be repeated three times in succession, but no more
        errorRule.add("IIII");
        errorRule.add("XXXX");
        errorRule.add("CCCC");
        errorRule.add("MMMM");
        // "D", "L", and "V" can never be repeated.
        errorRule.add("DD");
        errorRule.add("LL");
        errorRule.add("VV");
        // only. "V", "L", and "D" can never be subtracted.
//        errorRule.add("VX");
//        errorRule.add("VL");
//        errorRule.add("VC");
//        errorRule.add("VD");
//        errorRule.add("VM");
//        errorRule.add("LC");
//        errorRule.add("LD");
//        errorRule.add("LM");
//        errorRule.add("DM");
    }

    /**
     * 将数字转为字母串
     *
     * @param num
     * @return
     */
    public static String broken(Integer num) {
        // M:1000 D:500 C:100 L:50 X:10 V:5 I:1
        // CM:900 CD:400 XC:90 XL:40 IX:9 IV:4
        // no repeat: IIII XXXX  CCCC MMMM
        // never repeat: DD LL VV
        StringBuffer result = new StringBuffer();
        int i = 0;
        int live = num;

        boolean flag = true;
        int repeatTimes = 3;
        while (flag) {
            flag = false;
            int xm = live / SymbolsEnum.getValue("M");
            if (xm > repeatTimes) {
                xm = repeatTimes;
                live = live - xm * SymbolsEnum.getValue("M");
            } else {
                live = live % SymbolsEnum.getValue("M");
            }

            for (i = 0; i < xm; i++) {
                result.append("M");
            }

            int xcm = live / complete("CM");
            live = live % complete("CM");

            for (i = 0; i < xcm; i++) {
                result.append("CM");
            }

            if (live>=SymbolsEnum.getValue("M")){
                flag = true;
                if (live >= 3 * SymbolsEnum.getValue("M")) {
                    repeatTimes = 2;
                }
            }
        }

        int xd = live / SymbolsEnum.getValue("D");
        if (xd>=1){
            xd = 1;
            live = live - SymbolsEnum.getValue("D");
            result.append("D");
        }

        int xcd = live / complete("CD");
        live = live % complete("CD");

        for (i = 0; i < xcd; i++) {
            result.append("CD");
        }

        int xc = live / SymbolsEnum.getValue("C");
        if (xc > 3) {
            xc = 3;
            live = live - xc * SymbolsEnum.getValue("C");
        } else {
            live = live % SymbolsEnum.getValue("C");
        }

        for (i = 0; i < xc; i++) {
            result.append("C");
        }

        int xxc = live / complete("XC");
        live = live % complete("XC");

        for (i = 0; i < xxc; i++) {
            result.append("XC");
        }

        int xl = live / SymbolsEnum.getValue("L");
        live = live % SymbolsEnum.getValue("L");
        for (i = 0; i < xl; i++) {
            result.append("L");
        }

        int xxl = live / complete("XL");
        live = live % complete("XL");
        for (i = 0; i < xxl; i++) {
            result.append("XL");
        }

        int xx = live / SymbolsEnum.getValue("X");
        if (xx > 3) {
            xx = 3;
            live = live - xx * SymbolsEnum.getValue("X");
        } else {
            live = live % SymbolsEnum.getValue("X");
        }

        for (i = 0; i < xx; i++) {
            result.append("X");
        }


        int xix = live / complete("IX");
        live = live % complete("IX");
        for (i = 0; i < xix; i++) {
            result.append("IX");
        }

        int xv = live / SymbolsEnum.getValue("V");
        live = live % SymbolsEnum.getValue("V");

        for (i = 0; i < xv; i++) {
            result.append("V");
        }

        int xiv = live / complete("IV");
        live = live % complete("IV");
        for (i = 0; i < xiv; i++) {
            result.append("IV");
        }

        int xi = live / SymbolsEnum.getValue("I");
        for (i = 0; i < xi; i++) {
            result.append("I");
        }

        return result.toString();
    }

    /**
     * get count value
     *
     * @param str input string
     * @return
     */
    public static int complete(String str) {
        // if fit error rule ,return exception
        if (isError(str)) {
            throw new RuntimeException("I have no idea what you are talking about");
        }

        int sum = 0;
        sum += countStr(str);

        return sum;
    }

    /**
     * count substring
     *
     * @param str
     * @return
     */
    private static int countStr(String str) {
        StringBuffer temp = new StringBuffer(str);
        if (temp.length() == 0)
            return 0;

        if (temp.length() == 1) {
            return SymbolsEnum.getValue(temp.toString());
        } else if (temp.length() >= 2) {
            // subtracted rule
            int flag = subtracted(temp.substring(0, 2));
            if (flag == -1) {
                return SymbolsEnum.getValue(temp.substring(0, 1)) + countStr(temp.substring(1));
            } else {
                return flag + countStr(temp.substring(2));
            }
        }

        return 0;
    }

    /**
     * is error or not
     *
     * @param expression
     * @return
     */
    private static boolean isError(String expression) {
        for (String error : errorRule) {
            if (expression.contains(error)) {
                return true;
            }
        }

        return false;
    }

    /**
     * if fit subtracted rule, return the value.else return -1.
     *
     * @param expression
     * @return
     */
    private static int subtracted(String expression) {
        for (String right : subtractedRule) {
            if (expression.equals(right)) {
                return SymbolsEnum.getValue(expression.substring(1)) - SymbolsEnum.getValue(expression.substring(0, 1));
            }
        }

        return -1;
    }
}
