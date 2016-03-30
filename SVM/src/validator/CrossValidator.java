/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

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
}
