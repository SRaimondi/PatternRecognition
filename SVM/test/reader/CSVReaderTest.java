/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reader;

import libsvm.svm_node;
import libsvm.svm_problem;
import org.junit.Test;
import static org.junit.Assert.*;
 

/**
 *
 * @author Dominic
 */
public class CSVReaderTest {
    
    /**
     * Test of setupSVMProblem method, of class CSVReader.
     */
    @Test
    public void testSetupSVMProblem() {
        System.out.println("setupSVMProblem");
        String file_name = "../SVM/test/csvReaderTest.csv";
        svm_problem expResult = new svm_problem();
        expResult.l=2;
        expResult.x=csvAsSVMnodes();
        expResult.y=new double[]{1.0,10.0};
        
        svm_problem result = CSVReader.setupSVMProblem(file_name);
        checkIfSvmNodesAreEqual(expResult.x, result.x);
        assertEquals(expResult.l, result.l);
        assertArrayEquals(expResult.y, result.y, 0.0001);
    }

    /**
     * Test of setupSVMNodesArray method, of class CSVReader.
     */
    @Test
    public void testSetupSVMNodesArray() {
        System.out.println("setupSVMNodesArray");
        String file_name="../SVM/test/csvReaderTest.csv";
        svm_node[][] expResult=csvAsSVMnodes();    
        svm_node[][] result = CSVReader.setupSVMNodesArray(file_name);
        
        checkIfSvmNodesAreEqual(expResult, result);
    }
    
    //returns ../SVM/test/csvReaderTest.csv as svm[][]
    //csvReaderTest.csv looks like this:
    //1,2,3,4,5,6,7,8,9,10
    //10,9,8,7,6,5,4,3,2,1
    private svm_node[][] csvAsSVMnodes(){
        svm_node[][] expResult=new svm_node[2][9]; 
        for(int i=1;i<10;i++){
            expResult[0][i-1]=new svm_node();
            expResult[0][i-1].index=i-1;
            expResult[0][i-1].value=(double)(i+1)/255.0;
        }
        for(int i=1;i<10;i++){
            expResult[1][i-1]=new svm_node();
            expResult[1][i-1].index=i-1;
            expResult[1][i-1].value=(double)(10-i)/255.0;
        }
        return expResult;
    }
    
    //Checks if the arrays have the same numer of elements
    //Checks if the nodes at the same place have the same index and value
    private void checkIfSvmNodesAreEqual(svm_node[][] expected, svm_node[][] acctual){
        assertEquals(expected.length, 2);
        assertEquals(expected.length,acctual.length);
        assertEquals(expected[0].length,acctual[0].length);
        
        for(int y=0;y<acctual.length;y++){
            for(int x=0;x<acctual.length;x++){   
                assertEquals(expected[y][x].index,acctual[y][x].index);
                assertEquals(expected[y][x].value,acctual[y][x].value, 0.0001);
            }
        }
    }
}
