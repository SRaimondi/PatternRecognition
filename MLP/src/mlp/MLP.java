/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlp;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import reader.CSVReader;

/**
 *
 * @author Cedric
 */
public class MLP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //create training and test sets
            Instances trainingSet = CSVReader.getData("../../data/train.csv");
            Instances testSet = CSVReader.getData("../../data/test.csv");

            //System.out.println(trainingSet.firstInstance());
            //build and test multilayer perceptron with arbitrary parameters
            MultilayerPerceptron MLP = buildMLP(0.1, 200, 70, trainingSet);
            testMLP(MLP, testSet);

        } catch (Exception ex) {
            Logger.getLogger(MLP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * build a multilayer perceptron using the given parameters and the training
     * set
     *
     * @param learningRate the learning rate for the training
     * @param numberEpochs number of training epochs
     * @param numberNeurons number of neurons in the hidden layer
     * @param trainingSet the training set
     * @return
     * @throws Exception
     */
    public static MultilayerPerceptron buildMLP(double learningRate, int numberEpochs, int numberNeurons, Instances trainingSet) throws Exception {
        MultilayerPerceptron mlp = new MultilayerPerceptron();
        //set parameters
        mlp.setLearningRate(learningRate);
        mlp.setTrainingTime(numberEpochs);
        mlp.setHiddenLayers("" + numberNeurons);
        //build multilayer perceptron
        mlp.buildClassifier(trainingSet);
        return mlp;
    }

    /**
     * the trained multilayer perceptron tries to classify the instances in the
     * test set
     *
     * @param mlp a trained multilayer perceptron
     * @param testSet the test set
     * @throws Exception
     */
    public static void testMLP(MultilayerPerceptron mlp, Instances testSet) throws Exception {
        for (int i = 0; i < testSet.numInstances(); i++) {
            double classifier = mlp.classifyInstance(testSet.instance(i));
            System.out.print("Number classified as: " + classifier);
            System.out.println(" / Actual number:" + testSet.instance(i).classValue());
        }

    }

}
