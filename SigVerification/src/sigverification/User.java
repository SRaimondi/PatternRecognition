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
//String containing the user's ID according to the 'users.txt'-file

    final String UserID;
//number of genuine signatures
    final int numberGS;
//number of signatures to verify
    final int numberVS;
//array containing the genuine signatures
    Signature[] genuineSignatures;
//arry containing the signatures to verify
    Signature[] verificationSignatures;

    /**
     * Constructor
     *
     * @param UserID ID of the user according to the users.txt
     * @param numberGS Number of genuine signatures
     * @param numberVS Number of signatures that need verification
     */
    public User(String UserID, int numberGS, int numberVS) {
        this.UserID = UserID;
        this.numberGS = numberGS;
        this.numberVS = numberVS;
        //allocate space for the arrays
        genuineSignatures = new Signature[numberGS];
        verificationSignatures = new Signature[numberVS];
        //get the signatures
        this.initializeUser();
    }

    /**
     * returns the array containing the genuine signatures
     *
     * @return Signature array containing the genuine signatures
     */
    public Signature[] getGenuineSignatures() {
        return genuineSignatures;
    }

    /**
     * returns the array containing the signatures to verify
     *
     * @return Signature array containing the signatures to verify
     */
    public Signature[] getVerificationSignatures() {
        return verificationSignatures;
    }

    /**
     * read out all the signatures and put them into the corresponding array
     */
    private void initializeUser() {
        Signature sig;

        //read out all genuine signatures and put them into genuineSignatures[]
        for (int i = 1; i <= numberGS; i++) {
            try {
                if (i < 10) {
                    sig = SigReader.readSignature("../../Signature_data/enrollment/" + UserID + "-g-0" + i + ".txt");
                } else {
                    sig = SigReader.readSignature("../../Signature_data/enrollment/" + UserID + "-g-" + i + ".txt");
                }

                sig.normalizeData();
                genuineSignatures[i - 1] = sig;

            } catch (Exception ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        //read out all signatures to verify and put them into verificationSignatures[]
        for (int i = 1; i <= numberVS; i++) {
            try {
                if (i < 10) {
                    sig = SigReader.readSignature("../../Signature_data/verification/" + UserID + "-0" + i + ".txt");
                } else {
                    sig = SigReader.readSignature("../../Signature_data/verification/" + UserID + "-" + i + ".txt");
                }

                sig.normalizeData();
                verificationSignatures[i - 1] = sig;

            } catch (Exception ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
