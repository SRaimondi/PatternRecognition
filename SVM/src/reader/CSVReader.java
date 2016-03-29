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
    public static void readCSVFile(String file_name, List<ArrayList<Integer>> matrix) throws Exception {
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
    }
}
