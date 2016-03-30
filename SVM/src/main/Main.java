/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import reader.CSVReader;

import libsvm.*;

/**
 *
 * @author Simone Raimondi
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        svm_problem problem;
        problem = CSVReader.setupSVMProblem("../../data/test_small.csv");
    }
    
}
