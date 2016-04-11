package ann;

import ann.util.Number;
import ann.util.Utility;
import static ann.util.Utility.addUp;
import static ann.util.Utility.addUpDiv;
import static ann.util.Utility.arraysAreEqual;
import static ann.util.Utility.concat;
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
        
        File f = new File("src/data/image.png");
        BufferedImage im = ImageIO.read(f);
        
        im = Utility.binarize(im);
        File outputfile = new File("src/data/bw.png");
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
        
        normalize(trainingNumbers);
        normalize(testingNumbers);
        System.out.println("Input normalized."); 
        
        
        int featureSize = 2*trainingNumbers.get(0).features[0].length;
        int elements = 10000;
        
        double[][] input = new double[elements][featureSize];
        double[][] output = new double[elements][10];
        
        for(int i = 0; i<elements; i++){//System.out.println(Arrays.toString(divideArray(addUp(addUp(trainingNumbers.get(i).histogramV,trainingNumbers.get(i).histogramH),addUp(trainingNumbers.get(i).transitionsH,trainingNumbers.get(i).transitionsV)),4)));
            input[i] = concat(trainingNumbers.get(i).features[0],trainingNumbers.get(i).features[1]);//addUpDiv(4,trainingNumbers.get(i).histogramV,trainingNumbers.get(i).histogramH,trainingNumbers.get(i).transitionsV, trainingNumbers.get(i).transitionsH,trainingNumbers.get(i).firstBlackV,trainingNumbers.get(i).histogramH);
            
            output[i] = new double[10];
            for(int j = 0; j<10; j++)
                if(trainingNumbers.get(i).number==j)
                    output[i][j] = 1;
            else
                  output[i][j] = 0;  
        }
        
        BasicNetwork network = new BasicNetwork(); 
        network.addLayer(new BasicLayer(null, true, featureSize));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 200)); 
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 10)); 
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
            input[i] = concat(testingNumbers.get(i).features[0],testingNumbers.get(i).features[1]);//addUpDiv(4,testingNumbers.get(i).histogramV,testingNumbers.get(i).histogramH,testingNumbers.get(i).transitionsV,testingNumbers.get(i).transitionsH,testingNumbers.get(i).firstBlackV,testingNumbers.get(i).histogramH);
            
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
        for(Number n : num)
            for(double[] feature: n.features)
                norm(feature);      
    }
    
    private static void norm(double[] arr){
        double min = Double.MAX_VALUE; 
        double max = 0;
        for(int i = 0; i<arr.length; i++){
            if(arr[i] < min)
                min = arr[i];
            if(arr[i] > max)
                max = arr[i];  
            }
        for(int i = 0; i<arr.length; i++){
            if(max-min !=0)
                arr[i] = (arr[i]-min)/(max-min);
        }
    }
}
/*
                n.histogramV[i] = n.histogramV[i] /28;
                n.transitionsH[i] = n.transitionsH[i] /28;
                n.transitionsV[i] = n.transitionsV[i] /28;
*/