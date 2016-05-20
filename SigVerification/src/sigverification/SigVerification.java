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
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Simone Raimondi
 */
public class SigVerification {
    
    /**
     * Number of genuine signature
     */
    static final private int NUMBER_GS = 5;
    static final private int NUMBER_VS = 45;
    
    /**
     * Helper class that holds information for the signature dissimilarity process
     */
    static public class SignatureInfo implements Comparable<SignatureInfo> {
        // Id of the genuine signature
        public int genuine_sig_id;
        // Id of the verification signature
        public int verification_sig_id;
        // Distance of the two signature
        public float distance;
        
        /**
         * Empty constructor
         */
        public SignatureInfo() {}

        /**
         * Overload compreTo method
         * @param si Other SignatureInfo structure
         * @return Negative, zero or positive according to the distance between the two Signatures
         */
        @Override
        public int compareTo(SignatureInfo si) {
            return (int)(distance - si.distance);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //List containing all the Users
        ArrayList<User> Users = new ArrayList();

        // Try to open the users file and load all the signatures
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("../../Signature_data/users.txt"));
            // Read file line by line
            String line;

            while ((line = br.readLine()) != null) {
                // Create new User using the current line as UserID and hardcoded
                // values for the number of genuine Signatures and number of Signatures to verify
                User user = new User(line, NUMBER_GS, NUMBER_VS);
                Users.add(user);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SigVerification.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SigVerification.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Create file for output
        PrintWriter out;
        try {
            // Output file
            out = new PrintWriter("../../Signature_dissimilarty.txt");
            
            // Create list to store the info for the signature
            ArrayList<SignatureInfo> signature_infos = new ArrayList<>();
            
            // Loop over all user and compute the dissimilarty 
            // between the genuine signature and the verification signature
            for (User user : Users) {                
                // Loop over all the genuine signature
                for (int gen_sig = 0; gen_sig < NUMBER_GS; gen_sig++) {
                    // Clear list to store the info for the signature
                    signature_infos.clear();
                    // Get genuine signature feature vector
                    float[][] gen_sig_vec = user.getGenuineSignatures()[gen_sig].featureVectors();
                    
                    // Loop over all the verification signature
                    for (int ver_sig = 0; ver_sig < NUMBER_VS; ver_sig++) {
                        // Get the two signature feature vectors
                        float[][] ver_sig_vec = user.getVerificationSignatures()[ver_sig].featureVectors();
                        // Compute distance
                        float[][] DTW_M = DynTimeWarping.DTWDistance(gen_sig_vec, ver_sig_vec);
                        // Insert data into signature information list
                        SignatureInfo info = new SignatureInfo();
                        // Get distance from corner element
                        info.distance = DTW_M[DTW_M.length - 1][DTW_M[0].length - 1];
                        // Set genuine and verification ids
                        info.verification_sig_id = ver_sig + 1;
                        info.genuine_sig_id = gen_sig + 1;
                        // Add info to the list
                        signature_infos.add(info);
                    }
                    
                    // Sort the list of signature comparison info
                    Collections.sort(signature_infos);
                    
                    // Print data to file
                    // Print user ID
                    out.print(user.getUserID() + " ");
                    for (SignatureInfo inf : signature_infos) {               
                        // Print the id of the genuine signature and the verification signature
                        if (inf.genuine_sig_id >= 10) {
                            out.print(Integer.toString(inf.genuine_sig_id) + "-");
                        } else {
                            out.print("0" + Integer.toString(inf.genuine_sig_id) + "-");
                        }
                        if (inf.verification_sig_id >= 10) {
                            out.print(Integer.toString(inf.verification_sig_id) + " "); 
                        } else {
                            out.print("0" + Integer.toString(inf.verification_sig_id) + " ");   
                        }
                        // Print distance normalized
                        out.print(Float.toString(inf.distance / signature_infos.get(signature_infos.size() - 1).distance) + " ");
                    }
                    out.print("\n");
                }
            }
            
            // Close file
            out.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SigVerification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
