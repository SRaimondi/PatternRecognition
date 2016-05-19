/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.vecmath.Point2f;
import sigverification.Signature;

/**
 *
 * @author Simone Raimondi
 * 
 * Reads a signature form an input file, format is expected to be the same as
 * in the slides, no error correction provided
 */
public class SigReader {    
    /**
     * Reads an input signature file and create class
     * @param file_name Name of the file to read
     * @return Signature class created from file
     * @throws Exception Throws exception if file can not be read
     */
    public static Signature readSignature(final String file_name) throws Exception {
        // Try to open the file
        BufferedReader br;
        br = new BufferedReader(new FileReader(file_name));
        
        // Create temporary data storage to read the file
        ArrayList<ArrayList<Float>> data = new ArrayList<>();

        // Read file line by line
        String line;
        while ((line = br.readLine()) != null) {
            // Split line using the space separator
            String[] elements = line.split(" ");

            /* Add elements to ArrayList */
            ArrayList<Float> line_list = new ArrayList<>();

            /* Add line to the return list */
            for (String element : elements) {
                line_list.add(Float.parseFloat(element));
            }

            data.add(line_list);
        }
        
        // Create signature
        Signature sig = new Signature(data.size());
        
        // Insert data into signature
        int index = 0;
        for (ArrayList<Float> entry : data) {
            // Inster time step
            sig.time_steps[index] = entry.get(0);
            // Insert pen position
            sig.pen_position[index] = new Point2f(entry.get(1), entry.get(2));
            // Insert pressure
            sig.pressure[index] = entry.get(3);
            // Insert pen up / down
            sig.pen_up[index] = entry.get(4) == 1;
            // Insert azimuth and inclination
            sig.azimuth[index] = entry.get(5);
            sig.inclination[index] = entry.get(6);
            
            // Increment index
            index++;
        }
        
        // Close the file
        br.close();
        
        return sig;
    }
}
