/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigverification;

import javax.vecmath.Point2f;

/**
 *
 * @author Simone Raimondi
 * 
 * This class holds the information of a loaded signature from a text file
 */
public class Signature {
    /**
     * Array containing the time steps
     */
    public float time_steps[];
    
    /**
     * Array of the (x, y) position of the pen
     */
    public Point2f pen_position[];
    
    /**
     * Pressure of the pen
     */
    public float pressure[];
    
    /**
     * Change in pen up / down
     */
    public boolean pen_up[];
    
    /**
     * Azimuth of the pen
     */
    public float azimuth[];
    
    /**
     * Inclination of the pen
     */
    public float inclination[];
    
    /**
     * Number of entries in the signature
     */
    public final int size;
    
    /**
     * Size of the feature vector
     */
    static private final int FEATURE_VECTOR_SIZE = 5;
    
    /**
     * Position of the elements in the feature vector
     */
    static private final int POS_X = 0;
    static private final int POS_Y = 1;
    static private final int V_X = 2;
    static private final int V_Y = 3;
    static private final int PRESSURE = 4;
    
    /**
     * Define boundaries for the input values,
     * taken from the paper on the data at page 3
     */
    static private final float P_MIN = 0.f;
    static private final float P_MAX = 1024.f;
    static private final float AZ_MIN = 0.f;
    static private final float AZ_MAX = 360.f;
    static private final float INC_MIN = 30.f;
    static private final float INC_MAX = 90.f;
    
    /**
     * Constructor that allocates space for the signature data
     * @param num_entries Number of entries for the signature
     */
    public Signature(final int num_entries) {
        // Set size
        size = num_entries;
        // Allocate space for time steps
        time_steps = new float[num_entries];
        // Allocate space for pen position
        pen_position = new Point2f[num_entries];
        // Allocate space for pressure
        pressure = new float[num_entries];
        // Allocate space for pen up / down
        pen_up = new boolean[num_entries];
        // Allocate space for azimuth and inclination
        azimuth = new float[num_entries];
        inclination = new float[num_entries];
    }
    
    /**
     * Computes the feature vector at a given index
     * @param index Index for which you want to get the feature vector
     * @return A float array representing the feature vector
     */
    public float[] featureVectorAtIndex(final int index) {
        if (index < 0 || index > size) {
            System.out.println("Error, requested out of bound feature vector!");
            return null;
        }
        
        // Compute feature vector and return it
        return createFeatureVectorAt(index);   
    }
    
    /**
     * Computes the feature vector at a given time step
     * @param ts Time step for which you want to get the feature vector
     * @return A float array representing the feature vector
     */
    public float[] featureVectorAtTimeStep(final float ts) {
        int index = 0;
        // Look for the index of the time step we are looking for
        for (; index < size; index++) {
            if (time_steps[index] == ts) {
                break;
            }
            // If index not found, return null
            if (index == size - 1) {
                System.out.println("Error, could not find the feature vector for the given time step!");
                return null;
            }
        }
        
        return createFeatureVectorAt(index);
    }
    
    /**
     * Computes all the feature vectors of the signature
     * @return A double indexed array that contains all the feature vectors
     */
    public float[][] featureVectors() {
        // Allocate space for all vectors
        float[][] vectors = new float[size][];
        
        // Insert data
        for (int i = 0; i < size; i++) {
            vectors[i] = createFeatureVectorAt(i);
        }
        
        return vectors;
    }
    
    /**
     * Normalize the following data in the Signature
     * 
     * pressure,
     * azimuth and inclination ->   normalized between 0 and 1
     *                              according to the local minimum and maximum
     */
    public void normalizeData() {
        // Normalize the data of the siganture
        for (int i = 0; i < size; i++) {            
            // Normalize pressure
            pressure[i] = (pressure[i] - P_MIN) / (P_MAX - P_MIN);
            
            // Normalize azimuth and inclination
            azimuth[i] = (azimuth[i] - AZ_MIN) / (AZ_MAX - AZ_MIN);
            inclination[i] = (inclination[i] - INC_MIN) / (INC_MAX - INC_MIN);
        }
    }
    
    /**
     * Creates the feature vector at given index
     * @param index Index for which you want to get the feature vector
     * @return A float array representing the feature vector
     */
    private float[] createFeatureVectorAt(final int index) {
        // Create feature vector, as suggested in the slides
        float feature_vec[] = new float[FEATURE_VECTOR_SIZE];
        
        // Insert position in the vetor
        feature_vec[POS_X] = pen_position[index].x;
        feature_vec[POS_Y] = pen_position[index].y;
        
        // Compute velocities to the next time step
        // Check if we are at the last element, then we set the speed to 0
        if (index == size - 1) {
            feature_vec[V_X] = 0.f;
            feature_vec[V_Y] = 0.f;
        } else {
            // Compute speeds
            Point2f delta = new Point2f(pen_position[index + 1]);
            delta.sub(pen_position[index]);
            // Divide by delta t so we get the speed
            float delta_t = time_steps[index + 1] - time_steps[index];
            // Check that delt_t is not zero
            assert(delta_t > 10e-5);
            // Divide delta in space by delta_t to get the speed
            delta.scale(1.f / delta_t);
            // Set speed values
            feature_vec[V_X] = delta.x;
            feature_vec[V_Y] = delta.y;
        }
        
        // Set pressure in feature vector
        feature_vec[PRESSURE] = pressure[index];
        
        return feature_vec;
    }
}
