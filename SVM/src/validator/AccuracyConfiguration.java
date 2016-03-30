/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

/**
 *
 * @author Simone Raimondi
 */
public class AccuracyConfiguration {
    /* Type of the SVM used */
    public int svm_type;
    
    /* Type of kernel used */
    public int kernel_type;
    
    /* Gamma value */
    public double gamma;
    
    /* C value */
    public double C;
    
    /* Accuracy values */
    public double[] accuracy;
    
    public AccuracyConfiguration() {}
}
