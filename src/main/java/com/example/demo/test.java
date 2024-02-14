package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        CursorStack<String> stack = new CursorStack<>();
        try {
            File myObj = new File("src/file.242");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine().trim();

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
