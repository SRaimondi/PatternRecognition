/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import libsvm.svm;
import libsvm.svm_parameter;
import libsvm.svm_problem;

/**
 *
 * @author Simone Raimondi
 */
public class CrossValidator {
    /**
     * This methods compute the accuracy using cross validation on a given svm_problem and a given svm_parameter
     * @param problem Problem used for cross validation
     * @param params Parameters for cross validation
     * @param n_folds Number of folds to divide the problem data into
     * @return Accuracy of the cross validation, for SVM types EPSILON_SVR and NU_SVR, two values are returned:
     * one for the mean squared error and one for the squared error correlation. 
     * For the other SVM type, just the first element contains the accuracy
     */
    public static double[] computeCrossValidationAccuracy(svm_problem problem, svm_parameter params, int n_folds) {
        int total_correct = 0;
        double total_error = 0;
        double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
        double[] target = new double[problem.l];
        double[] accuracy = new double[2];
        
        /* Compute cross validation */
        svm.svm_cross_validation(problem, params, n_folds, target);
        
        /* Compute accuracy, from the example, we need to compute the error differentily for two Kernel types */
        if (params.svm_type == svm_parameter.EPSILON_SVR || params.svm_type == svm_parameter.NU_SVR) {
            for(int i = 0;i < problem.l; i++) {
                double y = problem.y[i];
                double v = target[i];
                total_error += (v-y)*(v-y);
                sumv += v;
                sumy += y;
                sumvv += v*v;
                sumyy += y*y;
                sumvy += v*y;
            }
            accuracy[0] =   total_error / (double)problem.l;
            accuracy[1] =   ((problem.l * sumvy - sumv * sumy) * (problem.l * sumvy - sumv * sumy)) /
                            (double)((problem.l * sumvv - sumv * sumv) * (problem.l * sumyy - sumy * sumy));
        } else {
            for (int i = 0; i < problem.l; i++) {
                if (target[i] == problem.y[i]) {
                    total_correct++;
                }
            }
            
            accuracy[0] = (total_correct / (double)problem.l);
        }
        
        /* Return accuracy */
        return accuracy;
    }
    
    /**
     * This method produces a report about the accuracy during cross validation 
     * for a given problem, starting from a given configuration and testing all possible
     * combinations using the steps given
     * @param problem Problem to compute cross validation on
     * @param params Starting parameters
     * @param n_folds Number of folds
     * @param steps Steps for the parameter variables
     */
    public static void produceCrossValidationReport(svm_problem problem, svm_parameter params, int n_folds,
                                                    PropertiesSteps steps) {
        /* Copy parameters so we don't touch them */
        svm_parameter params_copy = copyParameters(params);
        PrintWriter out;
        
        try {
            out = new PrintWriter("../../CrossValidationLog.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CrossValidator.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        /* Run cross validation for every possible set of parameters and log it to the file */
        ArrayList<AccuracyConfiguration> accuracies = new ArrayList<>();
        
        produceCrossValidationData(problem, params, n_folds, steps, accuracies);
        
        /* Print data to the file */
        for (AccuracyConfiguration acc : accuracies) {
            /* Write everyting to file */
            out.println("SVM type: " + acc.svm_type);
            out.println("Kernel type: " + acc.kernel_type);
            out.println("C value: " + acc.C);
            out.println("Gamma value: " + acc.gamma);
            if (params_copy.svm_type == svm_parameter.EPSILON_SVR ||
                params_copy.svm_type == svm_parameter.NU_SVR) {
                out.println("Cross Validation Mean squared error: " + acc.accuracy[0] * 100 + "%");
                out.println("Cross Validation Squared correlation coefficient: " + acc.accuracy[1] * 100 + "%");
            } else {
                out.println("Accuracy: " + acc.accuracy[0] * 100 + "%");
            }
            
            out.print("\n");
        }
        
        /* Close file */
        out.close();
    }
    
    /**
     * This method returns a list of all the accuracy for a given configuration
     * @param problem Problem to compute cross validation on
     * @param params Starting parameters
     * @param n_folds Number of folds
     * @param steps Steps for the parameter variables
     * @param result ArrayList containing all the configurations and the resulting accuracy
     */
    public static void produceCrossValidationData(  svm_problem problem, svm_parameter params, int n_folds,
                                                    PropertiesSteps steps, ArrayList<AccuracyConfiguration> result) {
        /* Copy parameters so we don't touch them */
        svm_parameter params_copy = copyParameters(params);

        /* Run cross validation for every possible set of parameters and log it to the file */
        /* Loop over all svm types */
        for (int s = 0; s < steps.svm_types.length; s++) {
            /* Set svm type */
            params_copy.svm_type = steps.svm_types[s];

            /* Loop over all kernel types */
            for (int k = 0; k < steps.kernel_types.length; k++) {
                /* Set kernel type */
                params_copy.kernel_type = steps.kernel_types[k];

                /* Loop over all the C values */
                do {
                    /* Loop over all the gamma value */
                    do {
                        /* Store accuracy for given data */
                        AccuracyConfiguration conf = new AccuracyConfiguration();

                        /* Copy fields */
                        conf.svm_type = params_copy.svm_type;
                        conf.kernel_type = params_copy.kernel_type;
                        conf.gamma = params_copy.gamma;
                        conf.C = params_copy.C;
                        /* Compute cross validation for current configuration */
                        conf.accuracy = computeCrossValidationAccuracy(problem, params_copy, n_folds);

                        /* Add configuration to result */
                        result.add(conf);

                        /* Increment gamma */
                        params_copy.gamma *= steps.gamma_step;
                    } while (params_copy.gamma <= steps.gamma_end);
                    /* Reset gamma value */
                    params_copy.gamma = params.gamma;
                    
                /* Increment C */
                params_copy.C *= steps.C_step;  
                } while (params_copy.C <= steps.C_end);
                /* Reset C value */
                params_copy.C = params.C;
            }
        }
    }

    /**
     * Copies all the fields of a given original svm_parameter class
     *
     * @param orig_params Original svm_parameter
     * @return A copy of orig_params
     */
    public static svm_parameter copyParameters(svm_parameter orig_params) {
        svm_parameter copy = new svm_parameter();

        /* Copy parameter */
        copy.svm_type = orig_params.svm_type;
        copy.kernel_type = orig_params.kernel_type;
        copy.degree = orig_params.degree;
        copy.gamma = orig_params.gamma;
        copy.coef0 = orig_params.coef0;
        copy.cache_size = orig_params.cache_size;
        copy.eps = orig_params.eps;
        copy.C = orig_params.C;
        copy.nr_weight = orig_params.nr_weight;

        copy.weight_label = new int[orig_params.weight_label.length];
        if (orig_params.weight_label.length != 0) {
            System.arraycopy(orig_params.weight_label, 0, copy.weight_label, 0, orig_params.weight_label.length);
        }

        copy.weight = new double[orig_params.weight.length];
        if (orig_params.weight.length != 0) {
            System.arraycopy(orig_params.weight, 0, copy.weight, 0, orig_params.weight.length);
        }

        copy.nu = orig_params.nu;
        copy.p = orig_params.p;
        copy.shrinking = orig_params.shrinking;
        copy.probability = orig_params.probability;

        return copy;
    }
}
