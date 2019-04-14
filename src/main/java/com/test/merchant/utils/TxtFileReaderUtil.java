package com.test.merchant.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TxtFileReaderUtil {
    /**
     * read txt
     *
     * @param path
     * @return lines
     */
    public static List<String> readLines(String path) {
        try {
            List<String> lines = new ArrayList<String>();
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileReader reader = new FileReader(path);
                 BufferedReader br = new BufferedReader(reader)) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    lines.add(line);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }

            return lines;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
