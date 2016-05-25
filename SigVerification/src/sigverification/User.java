/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigverification;

import java.util.logging.Level;
import java.util.logging.Logger;
import reader.SigReader;

/**
 *
 * @author Cedric
 */
public class User {
    // String containing the user's ID according to the 'users.txt'-file
    private final String UserID;
    // Array containing the genuine signatures
    private final Signature[] genuineSignatures;
    // Arry containing the signatures to verify
    private final Signature[] verificationSignatures;

    /**
     * Constructor
     *
     * @param UserID ID of the user according to the users.txt
     * @param numberGS Number of genuine signatures
     * @param numberVS Number of signatures that need verification
     * @param path Path for file location
     */
    public User(String UserID, int numberGS, int numberVS, final String path) {
        this.UserID = UserID;
        //allocate space for the arrays
        genuineSignatures = new Signature[numberGS];
        verificationSignatures = new Signature[numberVS];
        //get the signatures
        this.initializeUser(numberGS, numberVS, path);
    }
    
    /**
     * Returns the UserID
     * @return A String containing the user ID
     */
    public String getUserID() {
        return UserID;
    }

    /**
     * Returns the array containing the genuine signatures
     *
     * @return Signature array containing the genuine signatures
     */
    public Signature[] getGenuineSignatures() {
        return genuineSignatures;
    }

    /**
     * Returns the array containing the signatures to verify
     *
     * @return Signature array containing the signatures to verify
     */
    public Signature[] getVerificationSignatures() {
        return verificationSignatures;
    }

    /**
     * Read out all the signatures and put them into the corresponding array
     */
    private void initializeUser(final int numberGS, final int numberVS, final String path) {
        Signature sig;

        // Read out all genuine signatures and put them into genuineSignatures[]
        for (int i = 1; i <= numberGS; i++) {
            try {
                if (i < 10) {
                    sig = SigReader.readSignature(path + "enrollment/" + UserID + "-g-0" + i + ".txt");
                } else {
                    sig = SigReader.readSignature(path + "/enrollment/" + UserID + "-g-" + i + ".txt");
                }

                sig.normalizeData();
                genuineSignatures[i - 1] = sig;

            } catch (Exception ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // Read out all signatures to verify and put them into verificationSignatures[]
        for (int i = 1; i <= numberVS; i++) {
            try {
                if (i < 10) {
                    sig = SigReader.readSignature(path + "verification/" + UserID + "-0" + i + ".txt");
                } else {
                    sig = SigReader.readSignature(path + "verification/" + UserID + "-" + i + ".txt");
                }

                sig.normalizeData();
                verificationSignatures[i - 1] = sig;

            } catch (Exception ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
