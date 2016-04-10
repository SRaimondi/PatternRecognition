package ann;

import ann.util.Number;
import ann.util.Utility;
import static ann.util.Utility.addUp;
import static ann.util.Utility.arraysAreEqual;
import static ann.util.Utility.divideArray;
import static ann.util.Utility.highestEntry;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;


public class Input{
    static double learningRate = 0.1;
    static double momentum = 0.1;
    
    public static void main(String[] args)throws IOException{

        BufferedImage im = ImageIO.read(Input.class.getResourceAsStream("image.png"));

        im = Utility.binarize(im);
        File outputfile = new File("src/ann/bw.png");
        ImageIO.write(im, "png", outputfile);
        
        
        BufferedReader trainRead = new BufferedReader(new InputStreamReader(Input.class.getResourceAsStream("train.csv")));
        BufferedReader test = new BufferedReader(new InputStreamReader(Input.class.getResourceAsStream("test.csv")));

        List<String> training = new ArrayList<>();
        List<String> testing = new ArrayList<>();
        String l;
        while((l = trainRead.readLine()) != null)
            training.add(l.trim());
        while((l = test.readLine()) != null)
            testing.add(l.trim());
    
        List<Number> trainingNumbers = new ArrayList<>();
        List<Number> testingNumbers = new ArrayList<>();
        for(int i = 0; i<training.size(); i++)
            trainingNumbers.add(new Number(training.get(i)));
        for(int i = 0; i<testing.size(); i++)
            testingNumbers.add(new Number(testing.get(i)));
        
        //normalize(trainingNumbers);
        //normalize(testingNumbers);
        System.out.println("Input normalized."); 
        
        
        int featureSize = trainingNumbers.get(0).histogramV.length;
        int elements = 10000;
        
        double[][] input = new double[elements][featureSize];
        double[][] output = new double[elements][10];
        
        for(int i = 0; i<elements; i++){
            input[i] = divideArray(addUp(addUp(trainingNumbers.get(i).histogramV,trainingNumbers.get(i).histogramH),addUp(trainingNumbers.get(i).transitionsH,trainingNumbers.get(i).transitionsV)),4);
            
            output[i] = new double[10];
            for(int j = 0; j<10; j++)
                if(trainingNumbers.get(i).number==j)
                    output[i][j] = 1;
            else
                  output[i][j] = 0;  
        }
        
        BasicNetwork network = new BasicNetwork(); 
        network.addLayer(new BasicLayer(null ,false,featureSize));
        network.addLayer(new BasicLayer(new ActivationSigmoid() ,false,100)); 
        network.addLayer(new BasicLayer(new ActivationSigmoid() ,false ,10)); 
        network. getStructure().finalizeStructure(); 
        network. reset();        
        
        MLDataSet trainingSet = new BasicMLDataSet(input, output); 
        
        Backpropagation train = new Backpropagation(network , trainingSet);
        System.out.println("TrainingSet created."); 
        
        int epoch = 1; 
        do{
            train.iteration(); 
            if(epoch%250==0)
                System.out.println("Epoch #" + epoch + "Error:" + train.getError()); 
            epoch++; 
        }while((epoch<5000) && train.getError() > 0.01); 
        System.out.println("Network trained."); 
        
        for(int i = 0; i<elements; i++){
            input[i] = divideArray(addUp(addUp(testingNumbers.get(i).histogramV,testingNumbers.get(i).histogramH),addUp(testingNumbers.get(i).transitionsH,testingNumbers.get(i).transitionsV)),4);//divideArray(addUp(testingNumbers.get(i).histogramV,testingNumbers.get(i).histogramH),2);
            
            output[i] = new double[10];
            for(int j = 0; j<10 ; j++)
                if(testingNumbers.get(i).number==j)
                    output[i][j] = 1;
            else
                  output[i][j] = 0;  
        }
        MLDataSet testingSet = new BasicMLDataSet(input, output); 
        System.out.println("TestingSet created.");         
        
        double correct = 0;
        System.out.println("Neural Network Results :"); 
        for(MLDataPair pair : testingSet ){
            final MLData out = network.compute(pair.getInput()); 
            System.out.println("Actual=" + Arrays.toString(out.getData()) + ",ideal=" + Arrays.toString(pair.getIdeal().getData())); 
            if(highestEntry(out.getData())==highestEntry(pair.getIdeal().getData()))
                correct++;
        }
        System.out.println("Accuracy: " +correct/elements);
    }

    
    private static void normalize(List<Number> num){
        double min = 0; 
        double max = 28;
        for(Number n : num)
            for(int i = 0; i<n.histogramH.length; i++){
                n.histogramH[i] = n.histogramH[i] /28;
                n.histogramV[i] = n.histogramV[i] /28;
                n.transitionsH[i] = n.transitionsH[i] /28;
                n.transitionsV[i] = n.transitionsV[i] /28;
            }
                
    }
    
}
