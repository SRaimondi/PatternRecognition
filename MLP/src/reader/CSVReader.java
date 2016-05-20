/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reader;


import java.io.File;
import weka.core.Instances;
import weka.core.converters.CSVLoader;


/**
 *
 * @author Simone Raimondi,Cedric
 */
public class CSVReader {

    /**
     *read the CSV file
     * @param file_name Name of the file we want to open
     * @param loader Storage for the loaded file
     * @throws java.lang.Exception
     */
    public static void readCSVFile(String file_name, CSVLoader loader) throws Exception {

        loader.setSource(new File(file_name));
        //select options for the CSVloader (-H: no attribute names given in the first line/ -N 1: first column is a nominal attribute)
        String[] options = new String[3];
        options[0] = "-H";
        options[1] = "-N";
        options[2] = "1";
        loader.setOptions(options);

    }

    /**
     * This method creates a data set
     *
     * @param file_name Name of the file to use for the problem
     * @return An Instances-object containing the data
     */
    public static Instances getData(String file_name) {
        CSVLoader loader = new CSVLoader();
        try {
            readCSVFile(file_name, loader);
            //create data set and specify class attribute
            Instances data = loader.getDataSet();
            data.setClassIndex(0);
            
            return data;
        
        } catch (Exception e) {
            System.out.println("Error reading file: " + file_name);
            System.out.println("Error message: " + e.getMessage());
            return null;
        }
    }

}
