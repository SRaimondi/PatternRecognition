/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import reader.CSVReader;

import libsvm.*;
import validator.CrossValidator;
import validator.PropertiesSteps;

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
        problem = CSVReader.setupSVMProblem("../../data/train_small.csv");
        
        svm_parameter params = new svm_parameter();
        
        // default values
        params.svm_type = svm_parameter.C_SVC;
        params.kernel_type = svm_parameter.RBF;
        params.degree = 3;
        params.gamma = 0.1;
        params.coef0 = 0;
        params.nu = 0.5;
        params.cache_size = 100;
        params.C = 1;
        params.eps = 1e-3;
        params.p = 0.1;
        params.shrinking = 1;
        params.probability = 0;
        params.nr_weight = 0;
        params.weight_label = new int[0];
        params.weight = new double[0];
        
        /* Create step structure */
        PropertiesSteps steps = new PropertiesSteps();
        
        steps.kernel_types = new int[2];
        steps.kernel_types[0] = svm_parameter.LINEAR;
        steps.kernel_types[1] = svm_parameter.RBF;
        
        steps.svm_types = new int[1];
        steps.svm_types[0] = svm_parameter.C_SVC;
        
        steps.C_end = 2;
        steps.C_step = 1;
        
        steps.gamma_end = 0.2;
        steps.gamma_step = 0.1;
        
        CrossValidator.produceCrossValidationReport(problem, params, 0, steps);
        
        /* Test cross classification */
        // double[] accuracy = CrossValidator.computeCrossValidationAccuracy(problem, params, 4); 
        // System.out.println("Cross validation accuracy: " + accuracy[0] * 100 + "%");
        
//        svm_model model = svm.svm_train(problem, params);
//        
//        svm_node[][] test = CSVReader.setupSVMNodesArray("../../data/train_small.csv");
//        
//        /* Test classification */
//        for (svm_node[] test1 : test) {
//            double classification = svm.svm_predict(model, test1);
//            System.out.println("Classification: " + classification);
//        }
    }
    
}
