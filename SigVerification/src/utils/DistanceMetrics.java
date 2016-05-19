/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Simone Raimondi
 * 
 * This class contains some static method to compute distance metrics
 */
public class DistanceMetrics {
    /**
     * Computes Euclidean distance between two vector
     * @param v1 Input vector one
     * @param v2 Input vector two
     * @return Euclidean distance between v1 and v2
     */
    static public float EuclideanDistance(final float[] v1, final float[] v2) {
        // Check that vectors are of the same size
        assert(v1.length == v2.length);
        // Compute Euler distance
        float sum = 0.f;
        for (int i = 0; i < v1.length; i++) {
            // Compute delta
            float delta = v1[i] - v2[i];
            // Add delta squared
            sum += delta * delta;
        }
        
        // Return square root of the sum
        return ((float)Math.sqrt(sum));
    }
}
