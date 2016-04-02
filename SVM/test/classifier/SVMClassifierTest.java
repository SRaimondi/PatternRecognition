/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.util.ArrayList;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import reader.CSVReader;
import validator.AccuracyConfiguration;
import validator.CrossValidator;
import validator.PropertiesSteps;

/**
 *
 * @author Dominic
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({svm.class,CrossValidator.class})
public class SVMClassifierTest {
    
    public SVMClassifierTest() {
    }

    /**
     * Test of classifyElements method, of class SVMClassifier.
     */
    @Test
    public void testClassifyElements() {
        System.out.println("classifyElements");
        svm_model model = null;
        svm_problem classify_problem = CSVReader.setupSVMProblem("../SVM/test/csvReaderTest.csv");
        double[] classification = new double[2];
        double expResult = 1.0;
        
        //would be simpler but not working for some reason
        mockStatic(svm.class);
        when(svm.svm_predict
            (Matchers.anyObject(), Matchers.anyObject()))
        .thenReturn(1.0).thenReturn(10.0);
        
        double result = SVMClassifier.classifyElements(model, classify_problem, classification);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of optimizeParameters method, of class SVMClassifier.
     */
    @Test
    public void testOptimizeParameters() {
        System.out.println("optimizeParameters");
        svm_problem problem = null;
        int cross_validation_n_folds = 0;
        PropertiesSteps params_step = null;
        
        svm_parameter startig_params = new svm_parameter();
        startig_params.svm_type = svm_parameter.EPSILON_SVR;
        startig_params.kernel_type = svm_parameter.NU_SVR;
        startig_params.degree = 7;
        startig_params.gamma = 6;
        startig_params.coef0 = 1;
        startig_params.nu = 0.99;
        startig_params.cache_size = 15;
        startig_params.C = 6;
        startig_params.eps = 1e-9;
        startig_params.p = 0.2;
        startig_params.shrinking = 3;
        startig_params.probability = 1;
        startig_params.nr_weight = 8;
        startig_params.weight_label = new int[]{1,17};
        startig_params.weight = new double[]{6,99,14};
      
        
        AccuracyConfiguration a=new AccuracyConfiguration();
        a.accuracy=new double[]{0.5,0};
        AccuracyConfiguration b=new AccuracyConfiguration();
        b.accuracy=new double[]{0.3,0};
        AccuracyConfiguration c=new AccuracyConfiguration();
        c.accuracy=new double[]{0.4,0};
        
        AccuracyConfiguration expResult=new AccuracyConfiguration();
        expResult.accuracy=new double[]{1.0,0};
        expResult.C=0.1;
        expResult.gamma=0.2;
        expResult.kernel_type=svm_parameter.EPSILON_SVR;
        expResult.svm_type=svm_parameter.C_SVC;
        
        AccuracyConfiguration e=new AccuracyConfiguration();
        e.accuracy=new double[]{0.4,0};
        //would be simpler but not working for some reason
        PowerMockito.spy(CrossValidator.class);
        PowerMockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((ArrayList<AccuracyConfiguration>)args[4]).add(a);
                ((ArrayList<AccuracyConfiguration>)args[4]).add(b);
                ((ArrayList<AccuracyConfiguration>)args[4]).add(c);
                ((ArrayList<AccuracyConfiguration>)args[4]).add(expResult);
                ((ArrayList<AccuracyConfiguration>)args[4]).add(e);
                return null;
            }
        }).when(CrossValidator.class);CrossValidator.produceCrossValidationData
            (Matchers.any(svm_problem.class), Matchers.any(svm_parameter.class), Matchers.anyInt(), Matchers.any(PropertiesSteps.class), Matchers.any(ArrayList.class));    
        
        svm_parameter result = SVMClassifier.optimizeParameters(problem, startig_params, cross_validation_n_folds, params_step);
        
        assertEquals(expResult.C, result.C, 0.0001);
        assertEquals(expResult.gamma, result.gamma, 0.0001);
        assertEquals(expResult.kernel_type, result.kernel_type);
        assertEquals(expResult.svm_type, result.svm_type);
    }
    
}
