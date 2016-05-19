/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigverification;

import dtw.DynTimeWarping;
import java.util.logging.Level;
import java.util.logging.Logger;
import reader.SigReader;

/**
 *
 * @author Simone Raimondi
 */
public class SigVerification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 1; i < 5; i++) {
            try {
                Signature sig1 = SigReader.readSignature("../../Signature_data/enrollment/001-g-01.txt");
                //Signature sig2 = SigReader.readSignature("../../Signature_data/enrollment/003-g-0" + i + ".txt");
                
                Signature sig2 = SigReader.readSignature("../../Signature_data/verification/001-0" + i + ".txt");

                sig1.normalizeData();
                sig2.normalizeData();

                // Try to compute distance 
                float[][] f_v_1 = sig1.featureVectors();
                float[][] f_v_2 = sig2.featureVectors();

                float[][] DTW_M = DynTimeWarping.DTWDistance(f_v_1, f_v_2);

                System.out.println(DTW_M[DTW_M.length - 1][DTW_M[0].length - 1]);

            } catch (Exception ex) {
                Logger.getLogger(SigVerification.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
