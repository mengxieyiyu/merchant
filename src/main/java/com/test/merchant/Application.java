package com.test.merchant;

import com.test.merchant.core.Rules;
import com.test.merchant.entity.OtherSymbol;
import com.test.merchant.enums.SymbolsEnum;
import com.test.merchant.utils.TextParsingUtil;
import com.test.merchant.utils.TxtFileReaderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * project main
 */
public class Application {
    /**
     * main method
     *
     * @param args
     */
    public static void main(String[] args) {
        // define string as symbol by yourself
        List<OtherSymbol> otherList = new ArrayList<>();
        for (SymbolsEnum c : SymbolsEnum.values()) {
            otherList.add(new OtherSymbol(c.getSymbol(), c.getSymbol()));
        }

        boolean flag = true;
        while (flag) {
            System.out.println("please choose input type: ");
            System.out.println("1 -> txt file");
            System.out.println("2 -> console");
            Scanner input = new Scanner(System.in);
            int type = input.nextInt();
            if (type == 1) {
                flag = false;
                try {
                    List<String> inputLines = TxtFileReaderUtil.readLines("D://var/input.txt");
                    TextParsingUtil parser = new TextParsingUtil();
                    for (String line : inputLines) {
                        try {
                            parser.beginParse(line, otherList);
                        } catch (Exception ex) {
//                            ex.printStackTrace();
                            System.out.println(ex.getMessage());
                        }

                        if (parser.getParseString() != null && parser.getParseString().length() > 0) {
                            int result = Rules.complete(parser.getParseString());
                            String tip = line.substring(line.indexOf("is") + 2).replace("?", "");
                            System.out.println(tip.trim() + " is " + result);
                            parser.clear();
                        }

                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

            } else if (type == 2) {
                flag = false;
                try {
                    String inputString = "";
                    TextParsingUtil parser = new TextParsingUtil();
                    while (true) {
                        inputString = input.nextLine();
                        try {
                            parser.beginParse(inputString, otherList);
                        } catch (Exception ex) {
//                            ex.printStackTrace();
                            System.out.println(ex.getMessage());
                        }

                        if (parser.getParseString() != null && parser.getParseString().length() > 0) {
                            int result = Rules.complete(parser.getParseString());
                            String tip = inputString.substring(inputString.indexOf("is") + 2).replace("?", "");
                            System.out.println(tip + " is " + result);
                            parser.clear();
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

            } else {
                flag = true;
                System.out.println("Not recognized");
            }
        }
    }
}
