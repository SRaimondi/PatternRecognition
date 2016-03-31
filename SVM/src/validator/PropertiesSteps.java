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
public class PropertiesSteps {
    /* Types of SVM we want to test */
    public int[] svm_types;
    
    /* Types of kernel to use */
    public int[] kernel_types;
    
    /* Data for gamma */
    public double gamma_end;
    public double gamma_step;
    
    /* Data on C */
    public double C_end;
    public double C_step;
    
    public PropertiesSteps() {}
}
