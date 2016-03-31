/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.util.ArrayList;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import validator.AccuracyConfiguration;
import validator.CrossValidator;
import validator.PropertiesSteps;

/**
 *
 * @author Simone Raimondi
 */
public class SVMClassifier {
    /**
     * This method return the accuracy of the classification for a given problem 
     * @param model Model used for classification
     * @param classify_problem Problem to classify
     * @param classification Array with all the classification
     * @return Accuracy of the classification
     */
    public static double classifyElements(svm_model model, svm_problem classify_problem, double[] classification) {
        int index = 0, c_c_i = 0;
        /* Loop over all element on the problem to classify */
        for (svm_node[] test : classify_problem.x) {
            /* Predict classification */
            classification[index] = svm.svm_predict(model, test);
            
            /* Check if classification is correct */
            if (classify_problem.y[index] == classification[index]) {
                c_c_i++;
            }
            index++;
        }
        
        /* Return accuracy */
        return (c_c_i / (double)classify_problem.l);
    }
    
    /**
     * This method finds the best parameters C and gamma for a given problem using cross validation
     * @param problem Problem used for parameter optimization
     * @param startig_params Starting parameters
     * @param cross_validation_n_folds Number of folds for cross validation
     * @param params_step Steps used for parameters optimization
     * @return The best parameters for the given problem, starting parameters and step
     */
    public static svm_parameter optimizeParameters(  svm_problem problem, svm_parameter startig_params,
                                                    int cross_validation_n_folds, PropertiesSteps params_step) {
        /* Declare list to store accuracy and paramters */
        ArrayList<AccuracyConfiguration> acc_conf = new ArrayList();
        
        /* Cross validation */
        CrossValidator.produceCrossValidationData(  problem, startig_params, cross_validation_n_folds,
                                                    params_step, acc_conf);
        
        /* Copy starting parameters */
        svm_parameter params_best = CrossValidator.copyParameters(startig_params);
        
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
        
        return params_best;
    }
}
