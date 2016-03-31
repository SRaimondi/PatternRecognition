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
        params.gamma = Math.pow(2, -15);
        params.coef0 = 0;
        params.nu = 0.5;
        params.cache_size = 100;
        params.C = Math.pow(2, -5);
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

        CrossValidator.produceCrossValidationReport(problem, params, 0, steps);

        ArrayList<AccuracyConfiguration> acc_conf = new ArrayList();

        CrossValidator.produceCrossValidationData(problem, params, 0, steps, acc_conf);
//a list of the optimal parameters
        svm_parameter params_best = CrossValidator.copyParameters(params);
        double acc_best = 0;
        //look up the best accuracy found during CV and change optimal parameters accordingly
        for (AccuracyConfiguration conf : acc_conf) {
            if (conf.accuracy[0] > acc_best) {
                acc_best = conf.accuracy[0];
                params_best.svm_type = conf.svm_type;
                params_best.kernel_type = conf.kernel_type;
                params_best.C = conf.C;
                params_best.gamma = conf.gamma;
            }
        }
        /*Test cross classification */
//        double[] accuracy = CrossValidator.computeCrossValidationAccuracy(problem, params, 4); 
        // System.out.println("Cross validation accuracy: " + accuracy[0] * 100 + "%");
        svm_model model = svm.svm_train(problem, params_best);
        //get the actual classes of the test-set instances
        ArrayList<Integer> actual_values = CSVReader.getActualValues("../../data/test_small.csv");
        //c_c_i = correctly classified instances
        int count = 0, c_c_i = 0;
        svm_node[][] test = CSVReader.setupSVMNodesArray("../../data/test_small.csv");

        /* Test classification */
        for (svm_node[] test1 : test) {
            double classification = svm.svm_predict(model, test1);
            System.out.println("Classification: " + classification + " / Actual class: " + actual_values.get(count));

            if (actual_values.get(count) == classification) {
                c_c_i++;
            }
            count++;
        }
        double test_acc = c_c_i / (actual_values.size() + 0.0);

        System.out.println("Accuracy on test set: " + test_acc * 100 + "%");

    }

}
