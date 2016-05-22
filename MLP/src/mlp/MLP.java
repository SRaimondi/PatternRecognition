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
import weka.classifiers.Evaluation;
import weka.core.Debug.Random;

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
            Instances trainingSet = CSVReader.getData("../../data/train_small.csv");
            Instances testSet = CSVReader.getData("../../data/test_small.csv");
            //       System.out.println(trainingSet.firstInstance());

            /**
             * current parameters for optimization: learning rate :{0.1,0.2,0.3}
             * number of epochs: {10,20,30,40,50} number of
             * neurons:{10,20,30,40,50}
             *
             */
            MultilayerPerceptron MLP = optimizeParam(0.1, 0.3, 0.1, 10, 50, 10, 10, 50, 10, trainingSet);
            //test optimized MLP on the test set   
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
        int count = 0;
        for (int i = 0; i < testSet.numInstances(); i++) {
            double classifier = mlp.classifyInstance(testSet.instance(i));
            System.out.print("Number classified as: " + classifier);
            System.out.println(" / Actual number:" + testSet.instance(i).classValue());
            if (classifier == testSet.instance(i).classValue()) {
                count++;
            }
        }
        System.out.println("total:" + count);
    }

    /**
     * Create an optimized multlayer perceptron by optimizing learning rate,
     * number of epochs and number of neurons using 5-fold cross validation
     *
     * @param LRL lower boundary for the learning rate
     * @param LRU Upper boundary for the learning rate
     * @param LRS incremental step for the learning rate
     * @param NEL lower boundary for the number of epochs
     * @param NEU upper boundary for the number of epochs
     * @param NES incremental step for the number of epochs
     * @param NNL lower boundary for the number of neurons in the hidden layer
     * @param NNU Upper boundary for the number of neurons in the hidden layer
     * @param NNS incremental step for the number of neurons in the hidden layer
     * @param trainingSet the training set used for cross validation
     * @return a multilayer perceptron with optimised parameters
     * @throws Exception
     */
    public static MultilayerPerceptron optimizeParam(double LRL, double LRU, double LRS, int NEL, int NEU, int NES, int NNL, int NNU, int NNS, Instances trainingSet) throws Exception {
        int seed = 1, folds = 5;
        double bestErrorRate = 1;
        MultilayerPerceptron bestmlp = new MultilayerPerceptron(), mlp;
        //initialize randomizer and randomize the CV-Set
        Random r = new Random(seed);
        Instances rData = new Instances(trainingSet);
        rData.randomize(r);

        Evaluation eval, bestEval = new Evaluation(rData);
        //increment learning rate
        for (double i = LRL; i <= LRU; i += LRS) {
            //increment number of training epochs
            for (int j = NEL; j <= NEU; j += NES) {
                //increment number of neurons in hidden layer
                for (int k = NNL; k <= NNU; k += NNS) {
                    //create Evaluation object to evaluate our current MLP and create new MLP
                    eval = new Evaluation(rData);
                    mlp = new MultilayerPerceptron();
                    mlp.setLearningRate(i);
                    mlp.setTrainingTime(j);
                    mlp.setHiddenLayers("" + k);

                    for (int l = 0; l < folds; l++) {
                        //perform CV on the l-th fold
                        Instances train = rData.trainCV(folds, l);
                        Instances test = rData.testCV(folds, l);
                        //build MLP with the training set
                        mlp.buildClassifier(train);
                        //evaluate MLP using the test set
                        eval.evaluateModel(mlp, test);

                    }
                    //print out parameters and cross validation results
                    System.out.println("Learning Rate: " + i);
                    System.out.println("Number of Epochs:" + j);
                    System.out.println("Number of Neurons in the hidden layer:" + k);
                    System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
                    //search for the MLP with the lowest error rate
                    if (eval.errorRate() < bestErrorRate) {
                        bestErrorRate = eval.errorRate();
                        bestEval = eval;
                        bestmlp = mlp;
                    }
                }
            }

        }
//print out parameters and error rate of the optimized MLP
        System.out.println("Optimization Complete:");
        System.out.println("Parameters for the best Multilayer Perceptron: ");
        System.out.println("Learning rate: " + bestmlp.getLearningRate());
        System.out.println("Number of neurons in hidden Layer: " + bestmlp.getHiddenLayers());
        System.out.println("Number of training epochs: " + bestmlp.getTrainingTime());
        System.out.println(bestEval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

        return bestmlp;
    }

}
