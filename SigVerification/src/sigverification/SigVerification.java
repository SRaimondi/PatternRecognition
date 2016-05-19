/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigverification;

import dtw.DynTimeWarping;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import reader.SigReader;
import java.util.ArrayList;

/**
 *
 * @author Simone Raimondi
 */
public class SigVerification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //List containing all the Users
        ArrayList<User> Users = new ArrayList();

        // Try to open the file
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("../../Signature_data/users.txt"));
            // Read file line by line

            String line;

            while ((line = br.readLine()) != null) {
                //create new User using the current line as UserID and hardcoded values for the number of genuine Signatures and number of Signatures to verify
                User user = new User(line, 5, 45);
                Users.add(user);

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SigVerification.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SigVerification.class.getName()).log(Level.SEVERE, null, ex);
        }

        float[][] f_v_1 = Users.get(0).getGenuineSignatures()[0].featureVectors();
        float[][] f_v_2 = Users.get(0).getVerificationSignatures()[3].featureVectors();

        float[][] DTW_M = DynTimeWarping.DTWDistance(f_v_1, f_v_2);

        System.out.println(DTW_M[DTW_M.length - 1][DTW_M[0].length - 1]);
        /* try {
            Signature sig1 = SigReader.readSignature("../../Signature_data/enrollment/001-g-01.txt");
            Signature sig2 = SigReader.readSignature("../../Signature_data/enrollment/003-g-02.txt");

            sig1.normalizeData();
            sig2.normalizeData();

            // Try to compute distance 
            float[][] f_v_1 = sig1.featureVectors();
            float[][] f_v_2 = sig2.featureVectors();

            float[][] DTW_M = DynTimeWarping.DTWDistance(f_v_1, f_v_2);

            System.out.println(DTW_M[DTW_M.length - 1][DTW_M[0].length - 1]);

        } catch (Exception ex) {
            Logger.getLogger(SigVerification.class.getName()).log(Level.SEVERE, null, ex);
        }*/

    }

}
