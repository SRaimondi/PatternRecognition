/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import reader.CSVReader;

import libsvm.*;
import validator.AccuracyConfiguration;
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
        params.gamma = Math.pow(2, -10);
        params.coef0 = 0;
        params.nu = 0.5;
        params.cache_size = 100;
        params.C = 2;
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

        steps.C_end = Math.pow(2, 15);
        steps.C_step = 4;

        steps.gamma_end = Math.pow(2, 3);
        steps.gamma_step = 4;

        // CrossValidator.produceCrossValidationReport(problem, params, 2, steps);

        ArrayList<AccuracyConfiguration> acc_conf = new ArrayList();

        /* 2 folds cross validation */
        CrossValidator.produceCrossValidationData(problem, params, 2, steps, acc_conf);
        
        /* Select best parameters */
        svm_parameter params_best = CrossValidator.copyParameters(params);
        
        AccuracyConfiguration acc_best = acc_conf.get(0);
        
        //look up the best accuracy found during CV and change optimal parameters accordingly
        for (AccuracyConfiguration conf : acc_conf) {
            if (conf.accuracy[0] > acc_best.accuracy[0]) {
                acc_best = conf;
            }
        }
        
        /* Copy best params */
        params_best.svm_type = acc_best.svm_type;
        params_best.kernel_type = acc_best.kernel_type;
        params_best.C = acc_best.C;
        params_best.gamma = acc_best.gamma;
        
        /* Create model */
        svm_model model = svm.svm_train(problem, params_best);
        
        /* Load test problem */
        svm_problem test_problem = CSVReader.setupSVMProblem("../../data/test_small.csv");
        
        /* c_c_i = correctly classified instances */
        int count = 0, c_c_i = 0;

        /* Test classification */
        for (svm_node[] test1 : test_problem.x) {
            double classification = svm.svm_predict(model, test1);
            System.out.println("Classification: " + classification + " / Actual class: " + test_problem.y[count]);

            if (test_problem.y[count] == classification) {
                c_c_i++;
            }
            count++;
        }
        
        double test_acc = c_c_i / (double)test_problem.l;

        System.out.println("Accuracy on test set: " + test_acc * 100 + "%");
    }

}
