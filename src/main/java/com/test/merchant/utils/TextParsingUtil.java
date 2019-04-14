package com.test.merchant.utils;

import com.test.merchant.core.Rules;
import com.test.merchant.entity.OtherSymbol;
import com.test.merchant.enums.SymbolsEnum;

import java.util.ArrayList;
import java.util.List;

public class TextParsingUtil {
    private String parseString;
    private List<OtherSymbol> others;

    public String getParseString() {
        return parseString;
    }

    public void setParseString(String parseString) {
        this.parseString = parseString;
    }

    public List<OtherSymbol> getOthers() {
        return others;
    }

    public void setOthers(List<OtherSymbol> others) {
        this.others = others;
    }

    /**
     * Parse input content
     *
     * @param inputStr
     */
    public void beginParse(String inputStr, List<OtherSymbol> old) {
        if (!inputStr.contains(" is ")) {
            throw new RuntimeException("I have no idea what you are talking about");
        }

        // replace two or more space to one space.
        inputStr = inputStr.replaceAll("  ", " ");
        // Evaluation
        if (inputStr.contains("how much") || inputStr.contains("how many")) {
            others = new ArrayList<>();
            StringBuffer expression = new StringBuffer(inputStr);
            int index = expression.indexOf(" is ");
            expression.delete(0, index + 4);

            String strEx = expression.toString().replace("?", "");
            String[] arr = strEx.trim().split(" ");
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < arr.length; i++) {
                for (OtherSymbol item : old) {
                    if (arr[i].equals(item.getSymbolStr())) {
                        temp.append(item.getValue());
                    }
                }
            }

            parseString = temp.toString();

        } else {
            // set new symbol.
            StringBuffer expression = new StringBuffer(inputStr);
            String[] strs = inputStr.split(" is ");
            if (strs.length != 2)
                throw new RuntimeException("I have no idea what you are talking about");

            // 1. right expression
            String value = strs[1].trim();
            if (value.contains(" "))
                value = value.substring(0, value.indexOf(" "));

            String rValue = "";
            int valueInt = 0;
            if (value.matches("\\d+\\.?\\d*")) {
                valueInt = Integer.parseInt(value);
                rValue = Rules.broken(valueInt);
            } else {
                boolean isExist = false;
                for (OtherSymbol item : old) {
                    if (value.equals(item.getSymbolStr())) {
                        valueInt = Rules.complete(item.getValue());
                        rValue = item.getValue();

                        isExist = true;
                        break;
                    }
                }

                // Not recognized.
                if (!isExist)
                    throw new RuntimeException("I have no idea what you are talking about");
            }

            // 2. left expression
            strs[0] = strs[0].trim();
            if (strs[0].contains(" ")) {
                String[] systr = strs[0].split(" ");
                int leftNums = valueInt;
                int nlableNums = 0; // not define label num
                int times = 0;
                String labelA = "";

                for (int k = 0; k < systr.length; k++) {
                    String label = systr[k];
                    boolean flag = false;
                    for (OtherSymbol item : old) {
                        if (item.getSymbolStr().equals(label)) {
                            if (item.getValue().matches("\\d+\\.?\\d*")) {
                                leftNums = leftNums - Integer.parseInt(item.getValue());
                            } else {
                                for (SymbolsEnum c : SymbolsEnum.values()) {
                                    if (c.getSymbol().equals(item.getValue())) {
                                        leftNums = leftNums - c.getValue();
                                    }
                                }
                            }

                            flag = true;
                            break;
                        }
                    }

                    // a new string which not define.
                    if (!flag) {
                        if (labelA.equals("")) {
                            labelA = label;
                            nlableNums++;
                            times++;
                        } else if (!labelA.equals(label)) {
                            nlableNums++;
                            if (nlableNums >= 2)
                                throw new RuntimeException("I have no idea what you are talking about");
                        } else {
                            times++;
                        }
                    }
                }

                if (times > 0) {
                    old.add(new OtherSymbol(Rules.broken(leftNums / times), labelA));
                }

            } else {
                for (OtherSymbol item : old) {
                    // exist
                    if (strs[0].equals(item.getSymbolStr())) {
                        return;
                    }
                }

                old.add(new OtherSymbol(rValue, strs[0]));
            }
        }
    }

    public void clear() {
        this.setOthers(null);
        this.setParseString(null);
    }
}
