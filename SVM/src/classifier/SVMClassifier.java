/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_problem;

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
}
