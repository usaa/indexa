package com.usaa.mapper.cli;

import com.usaa.mapper.processors.Indexer;

import java.io.File;


public class Main {
    public static void main(String[] args) {
        try {
            String directory = args[0];
            if(directory.charAt(0) == '~'){
                directory = args[0].substring(1);
            }
            File inputDirectory = new File(directory);
            if (inputDirectory != null && inputDirectory.exists()) {
                Indexer.runIndexer(inputDirectory);
            } else {
                System.out.println("The directory given was not valid");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
