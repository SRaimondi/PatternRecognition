/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import libsvm.svm_node;
import libsvm.svm_problem;

/**
 *
 * @author Simone Raimondi
 */
public class CSVReader {
    /**
     * 
     * @param file_name Name of the file we want to open
     * @param matrix Storage for the loaded file
     * @throws java.lang.Exception
     */
    private static void readCSVFile(String file_name, List<ArrayList<Integer>> matrix) throws Exception {
        BufferedReader br;
        br = new BufferedReader(new FileReader(file_name));
        
        /* Read file line by line */
        String line;
        
        while ((line = br.readLine()) != null) {
            /* Split line using the coma separator */
            String[] elements = line.split(",");
    
            /* Add elements to ArrayList */
            ArrayList<Integer> line_list = new ArrayList<>();
            
            /* Add line to the return list */
            for (String element : elements) {
                line_list.add(Integer.parseInt(element));
            }
            
            matrix.add(line_list); 
        }

        br.close();
        System.out.println("Loaded file: " + file_name);
    }
    
    /**
     * This method sets up a svm_problem form a given input file
     * @param file_name Name of the file to use for the problem
     * @return An svm_problem class that contains the loaded data
     */
    public static svm_problem setupSVMProblem(String file_name) {
        svm_problem problem = new svm_problem();
        
        /* Load matrix from file */
        List<ArrayList<Integer>> file = new ArrayList<>();
        try {
            readCSVFile(file_name, file);
        } catch (Exception e) {
            System.out.println("Error reading file: " + file_name);
            System.out.println("Error message: " + e.getMessage());
            return null;
        }
        
        /* Set fields */
        problem.l = file.size();
        
        /* Allocate space for the y values */
        problem.y = new double[problem.l];
        /* Insert values into array */
        for (int i = 0; i < problem.l; i++) {
            problem.y[i] = file.get(i).get(0);
        }
        
        /* Create node structures */
        problem.x = new svm_node[problem.l][];
        
        /* Loop over each line and add sparse elements to the x problem field */
       for (int i = 0; i < problem.l; i++) {
           ArrayList<Integer> line = file.get(i);
           /* Count number of non-zero entries */
           int non_zero = 0;
           for (int element = 1; element < line.size(); element++) {
               if (line.get(element) != 0) {
                   non_zero++;
               }
           }
           
           /* Allocate space */
           if (non_zero != 0) {
               problem.x[i] = new svm_node[non_zero];
           }
           
           /* Insert nodes values */
           int index = 0;
           for (int element = 1; element < line.size(); element++) {
               if (line.get(element) != 0) {
                   problem.x[i][index] = new svm_node();
                   problem.x[i][index].index = element - 1;
                   /* Add value, normalized */
                   problem.x[i][index].value = (double)line.get(element) / 255.0;
                   index++;
               }
           }
       }
        
        return problem;
    }
    
    /**
     * This method creates a list of svm_node form a given input file
     * @param file_name Name of the file to load the nodes from
     * @return A 2d array representing the loaded data
     */
    public static svm_node[][] setupSVMNodesArray(String file_name) {
        /* Load matrix from file */
        List<ArrayList<Integer>> file = new ArrayList<>();
        try {
            readCSVFile(file_name, file);
        } catch (Exception e) {
            System.out.println("Error reading file: " + file_name);
            System.out.println("Error message: " + e.getMessage());
            return null;
        }
        
        /* Allocate space for all nodes */
        svm_node[][] nodes = new svm_node[file.size()][];
        
        /* Insert nodes values */
        for (int i = 0; i < file.size(); i++) {
           ArrayList<Integer> line = file.get(i);
           /* Count number of non-zero entries */
           int non_zero = 0;
           for (int element = 1; element < line.size(); element++) {
               if (line.get(element) != 0) {
                   non_zero++;
               }
           }
           
           /* Allocate space */
           if (non_zero != 0) {
               nodes[i] = new svm_node[non_zero];
           }
           
           /* Insert nodes values */
           int index = 0;
           for (int element = 1; element < line.size(); element++) {
               if (line.get(element) != 0) {
                   nodes[i][index] = new svm_node();
                   nodes[i][index].index = element - 1;
                   /* Add value, normalized */
                   nodes[i][index].value = (double)line.get(element) / 255.0;
                   index++;
               }
           }
       }
        
        return nodes;
    } 
}
