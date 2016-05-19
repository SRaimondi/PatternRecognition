/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

import utils.DistanceMetrics;

/**
 *
 * @author Simone Raimondi
 * 
 * Initial implementation of the dynamic time warping algorithm
 */
public class DynTimeWarping {
    /**
     * Simple implementation of the DTW algorithm
     * @param input_vectors_1 Array of arrays of feature vectors input 1
     * @param input_vectors_2 Array of arrays of feature vectors input 2
     * @return DTW distance matrix between input_vectors_1 and input_vectors_2
     */
    static public float[][] DTWDistance(final float[][] input_vectors_1, final float[][] input_vectors_2) {
        // Get size of the inputs, more for code cleaness
        final int n = input_vectors_1.length;
        final int m = input_vectors_2.length;
        
        // Allocate space for the algorithm
        float[][] DTW_matrix = new float[n][m];

        // Initialize matrix
        DTW_matrix[0][0] = DistanceMetrics.EuclideanDistance(input_vectors_1[0], input_vectors_2[0]);
        
        for (int i = 1; i < n; i++) {
            DTW_matrix[i][0] =  DTW_matrix[i - 1][0] + 
                                DistanceMetrics.EuclideanDistance(input_vectors_1[i], input_vectors_2[0]);
        }
        for (int j = 1; j < m; j++) {
            DTW_matrix[0][j] =  DTW_matrix[0][j - 1] + 
                                DistanceMetrics.EuclideanDistance(input_vectors_1[0], input_vectors_2[j]);
        }
        
        // Compute DTW
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                // Compute distance between the two feature vectors
                float distance = DistanceMetrics.EuclideanDistance(input_vectors_1[i], input_vectors_2[j]);
                
                // Compute new entry in the DTW matrix
                DTW_matrix[i][j] = distance + minimum(  DTW_matrix[i - 1][j],
                                                        DTW_matrix[i][j - 1],
                                                        DTW_matrix[i - 1][j - 1]);
            }
        }
        
        return DTW_matrix;
    }
    
    /**
     * Helper function to compute the minimum between three values
     * @param a First value
     * @param b Second value
     * @param c Third value
     * @return The minimum between a, b, c
     */
    static private float minimum(final float a, final float b, final float c) {
        return Math.min(a, Math.min(b, c));
    }
}
