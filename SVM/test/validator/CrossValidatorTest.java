/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import java.util.ArrayList;
import libsvm.svm;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import org.junit.Test;
import static org.junit.Assert.*;


import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import reader.CSVReader;

/**
 *
 * @author Dominic
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({svm.class,CrossValidator.class})
public class CrossValidatorTest {

    /**
     * Test of computeCrossValidationAccuracy method, of class CrossValidator.
     */
    @Test
    public void testComputeCrossValidationAccuracy() {
        System.out.println("computeCrossValidationAccuracy");
        
        svm_problem problem = CSVReader.setupSVMProblem("../SVM/test/csvReaderTest.csv");
        svm_parameter params = new svm_parameter();
        params.svm_type = svm_parameter.C_SVC;//accuracy differs for other types!
        
        int n_folds = 0;
        double[] expResult = new double[]{1.0,0.0};
        
        mockStatic(svm.class);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                //insert the right classification here
                ((double[])args[3])[0]=1.0;
                ((double[])args[3])[1]=10.0;
                return null;
            }
        }).when(svm.class);CrossValidator.computeCrossValidationAccuracy(problem, params, n_folds);
        double[] result = CrossValidator.computeCrossValidationAccuracy(problem, params, n_folds);
        
        assertArrayEquals(expResult,result,0.00001);
    }

    /**
     * Test of produceCrossValidationData method, of class CrossValidator.
     */
    @Test
    public void testProduceCrossValidationData() {
        System.out.println("produceCrossValidationData");
        svm_problem problem = CSVReader.setupSVMProblem("../SVM/test/csvReaderTest.csv");;
        svm_parameter params = new svm_parameter();
        params.svm_type = svm_parameter.EPSILON_SVR;
        params.kernel_type = svm_parameter.NU_SVR;
        params.degree = 7;
        params.gamma = 6;
        params.coef0 = 1;
        params.nu = 0.99;
        params.cache_size = 15;
        params.C = 6;
        params.eps = 1e-9;
        params.p = 0.2;
        params.shrinking = 3;
        params.probability = 1;
        params.nr_weight = 8;
        params.weight_label = new int[]{1,17};
        params.weight = new double[]{6,99,14};
        
        int n_folds = 0;
        
        PropertiesSteps steps = new PropertiesSteps();
        steps.kernel_types = new int[2]; //2 kernel types
        steps.kernel_types[0] = svm_parameter.LINEAR;
        steps.kernel_types[1] = svm_parameter.RBF;

        steps.svm_types = new int[3]; //3 svm types
        steps.svm_types[0] = svm_parameter.C_SVC;
        steps.svm_types[1] = svm_parameter.EPSILON_SVR;
        steps.svm_types[2] = svm_parameter.NU_SVR;

        steps.C_end = 95; //initial: 6; 6,12,24,48=4 c-steps
        steps.C_step = 2;//double c each step

        steps.gamma_end = 95;//inital: 6; 6,24=2 gamma-steps
        steps.gamma_step = 4;//quadruple each step
        
        ArrayList<AccuracyConfiguration> result_2 = new ArrayList<>();
        double[] mockAcc=new double[]{1.0,0};
        //would be simpler but not working for some reason
        /*PowerMockito.spy(CrossValidator.class);
        when(CrossValidator.computeCrossValidationAccuracy
            (Matchers.anyObject(), Matchers.anyObject(), Matchers.anyInt()))
        .thenReturn(mockAcc);*/
        PowerMockito.spy(CrossValidator.class);
        doAnswer(new Answer<double[]>() {
            public double[] answer(InvocationOnMock invocation) {
                return mockAcc;
            }
        }).when(CrossValidator.class);CrossValidator.computeCrossValidationAccuracy(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyInt());
        
        CrossValidator.produceCrossValidationData(problem, params, n_folds, steps, result_2);
        
        assertEquals(2*3*4*2,result_2.size());
    }

    /**
     * Test of copyParameters method, of class CrossValidator.
     */
    @Test
    public void testCopyParameters() {
        System.out.println("copyParameters");
        svm_parameter expResult = new svm_parameter();

        // default values
        expResult.svm_type = svm_parameter.EPSILON_SVR;
        expResult.kernel_type = svm_parameter.NU_SVR;
        expResult.degree = 7;
        expResult.gamma = Math.pow(2, -10);
        expResult.coef0 = 1;
        expResult.nu = 0.99;
        expResult.cache_size = 15;
        expResult.C = 3;
        expResult.eps = 1e-9;
        expResult.p = 0.2;
        expResult.shrinking = 3;
        expResult.probability = 1;
        expResult.nr_weight = 8;
        expResult.weight_label = new int[]{1,17};
        expResult.weight = new double[]{6,99,14};
        
        svm_parameter result = CrossValidator.copyParameters(expResult);
        
        assertEquals(expResult.svm_type,result.svm_type);
        assertEquals(expResult.kernel_type,result.kernel_type);
        assertEquals(expResult.degree,result.degree);
        assertEquals(expResult.gamma,result.gamma,0.0001);
        assertEquals(expResult.coef0,result.coef0,0.0001);
        assertEquals(expResult.nu,result.nu,0.0001);
        assertEquals(expResult.cache_size,result.cache_size,0.0001);
        assertEquals(expResult.C,result.C,0.0001);
        assertEquals(expResult.eps,result.eps,0.0001);
        assertEquals(expResult.p,result.p,0.0001);
        assertEquals(expResult.shrinking,result.shrinking);
        assertEquals(expResult.probability,result.probability);
        assertEquals(expResult.nr_weight,result.nr_weight);
        assertArrayEquals(expResult.weight_label,result.weight_label);
        assertArrayEquals(expResult.weight,result.weight,0.0001);
    }
}
