/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package svm;

import java.util.ArrayList;
import java.util.List;
import reader.CSVReader;

/**
 *
 * @author Simone Raimondi
 */
public class SVM {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        List<ArrayList<Integer>> file = new ArrayList<>();
        CSVReader.readCSVFile("../../data/test_small.csv", file);
        
        file.stream().forEach((line) -> {
            line.stream().forEach((e) -> {
                System.out.print(e + " ");
            });
            System.out.println("");
        });
    }
    
}
