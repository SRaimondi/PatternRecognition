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
    static final private String PATH = "../../Test-Datasets/TestSignatures/";
    
    /**
     * Helper class that holds information for the signature dissimilarity process
     */
    static public class SignatureInfo implements Comparable<SignatureInfo> {
        // Id of the genuine signature
        public int user_id;
        // Id of the verification signature
        public int verification_sig_id;
        // Distance of the genuine signature to the verification one
        public float[] distance;
        // Minimum distance;
        public float min_distance;
        
        
        /**
         * Empty constructor
         */
        public SignatureInfo() {
            distance = new float[NUMBER_GS];
            min_distance = Float.POSITIVE_INFINITY;
        }
        
        public void setMinimumDistance() {
            for (float d : distance) {
                if (d < min_distance) {
                    min_distance = d;
                }
            }
        }

        /**
         * Overload compreTo method
         * @param si Other SignatureInfo structure
         * @return Negative, zero or positive according to the distance between the two Signatures
         */
        @Override
        public int compareTo(SignatureInfo si) {
            return (int)(min_distance - si.min_distance);
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
            br = new BufferedReader(new FileReader(PATH + "users.txt"));
            // Read file line by line
            String line;

            while ((line = br.readLine()) != null) {
                // Create new User using the current line as UserID and hardcoded
                // values for the number of genuine Signatures and number of Signatures to verify
                User user = new User(line, NUMBER_GS, NUMBER_VS, PATH);
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
            out = new PrintWriter("../../signature_test_result.csv");
            
            // Create list to store the info for the signatures
            ArrayList<SignatureInfo> signature_infos = new ArrayList<>();
            
            // Loop over all user and compute the dissimilarty 
            // between the genuine signature and the verification signature
            for (User user : Users) {    
                // Clear list to store the info for the signature
                signature_infos.clear();
                // Loop over all the verifictaion signature
                for (int ver_sig = 0; ver_sig < NUMBER_VS; ver_sig++) {
                    // Create signature info structure
                    SignatureInfo info = new SignatureInfo();
                    // Set the user ID
                    info.user_id = Integer.parseInt(user.getUserID());
                    // Get the two signature feature vectors
                    float[][] ver_sig_vec = user.getVerificationSignatures()[ver_sig].featureVectors();
                    // Loop over all the genuine signature
                    for (int gen_sig = 0; gen_sig < NUMBER_GS; gen_sig++) {
                        // Get genuine signature feature vector
                        float[][] gen_sig_vec = user.getGenuineSignatures()[gen_sig].featureVectors();
                        // Compute distance
                        float[][] DTW_M = DynTimeWarping.DTWDistance(gen_sig_vec, ver_sig_vec);
                        // Get distance from corner element
                        info.distance[gen_sig] = DTW_M[DTW_M.length - 1][DTW_M[0].length - 1];
                        // Set verification ids
                        info.verification_sig_id = ver_sig + 1;
                    }
                    
                    // compute minimum distance
                    info.setMinimumDistance();
                
                    // Add info to the list
                    signature_infos.add(info);
                }   
                                
                // Sort the list of signature comparison info
                Collections.sort(signature_infos);
                // Print data to file
                // Print user ID
                out.print(user.getUserID() + ",");
                //for (SignatureInfo inf : signature_infos) {     
                for (int s = 0; s < signature_infos.size(); s++) {
                    SignatureInfo inf = signature_infos.get(s);
                    // Print the id of the genuine signature and the verification signature
                    if (inf.verification_sig_id >= 10) {
                        out.print(user.getUserID() + "-" + Integer.toString(inf.verification_sig_id) + ","); 
                    } else {
                        out.print(user.getUserID() + "-" + "0" + Integer.toString(inf.verification_sig_id) + ",");   
                    }
                    // Print distance scaled from 0 - 100
                    out.print(Integer.toString((int)(inf.min_distance)));
                    // Print comma separator
                    if (s != signature_infos.size() - 1) {
                        out.print(",");
                    }
                }
                out.print("\n");
            }
            
            // Close file
            out.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SigVerification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
