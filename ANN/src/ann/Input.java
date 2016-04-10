package ann;

import ann.util.Number;
import ann.util.Utility;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;


public class Input{
        
    
    public static void main(String[] args)throws IOException{

        BufferedImage im = ImageIO.read(Input.class.getResourceAsStream("image.png"));

        im = Utility.binarize(im);
        File outputfile = new File("src/ann/bw.png");
        ImageIO.write(im, "png", outputfile);
        System.out.println("done");
        
        BufferedReader train = new BufferedReader(new InputStreamReader(Input.class.getResourceAsStream("train.csv")));
        BufferedReader test = new BufferedReader(new InputStreamReader(Input.class.getResourceAsStream("test.csv")));

        List<String> training = new ArrayList<>();
        List<String> testing = new ArrayList<>();
        String l;
        while((l = train.readLine()) != null)
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
        
        int num = trainingNumbers.get(0).histogramV.length;
        
                            
         /*
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);  
        DataSet trainingSet = new  DataSet(2, 1);
        trainingSet.addRow (new DataSetRow (new double[]{0, 0},  new double[]{0})); 
        trainingSet.addRow (new DataSetRow (new double[]{0, 1},  new double[]{1})); 
        trainingSet.addRow (new DataSetRow (new double[]{1, 0},  new double[]{2})); 
        trainingSet.addRow (new DataSetRow (new double[]{1, 1},  new double[]{1})); 

        neuralNetwork.learn(trainingSet);
        neuralNetwork.setInput(1, 0);  
        neuralNetwork.calculate();  
        double[] networkOutput = neuralNetwork.getOutput();
        System.out.println(Arrays.toString(networkOutput));
        
        /**/
       
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(num,10); 
        DataSet trainingSet = new  DataSet(num, 10);
        int elements = 100;
        
        for(int i = 0; i<elements; i++){
            double[] vec = new double[10];
            for(int j = 0; j<10; j++)
                if(trainingNumbers.get(i).number==j)
                    vec[j] = 1;
            else
                  vec[j] = 0;  
            trainingSet.addRow(new DataSetRow(trainingNumbers.get(i).histogramV,  vec)); 
        }
        System.out.println("data set!");
        neuralNetwork.learn(trainingSet);
        System.out.println("learnt!");
        double correct = 0;
        for(int i = 0; i<elements; i++){
            neuralNetwork.setInput(testingNumbers.get(i).histogramV); 
            neuralNetwork.calculate();  
            double[] networkOutput = neuralNetwork.getOutput();
            //System.out.println(Arrays.toString(networkOutput));
            //System.out.println("actual: "+testingNumbers.get(i).number+" ann:"+t);
            if(Math.abs(networkOutput[testingNumbers.get(i).number]-1) <=0.1)
                correct++;
        }
        System.out.println("Accuracy:"+correct/elements);
        /*
        neuralNetwork.setInput(1, 1);  
        neuralNetwork.calculate();  
        double[] networkOutput = neuralNetwork.getOutput(); 
        
        ANN ann = new ANN(new int[]{num,100,10});
        
        for(int i = 0; i<1000; i++)
            ann.train(trainingNumbers.get(i));
        
        double correct = 0;
        for(int i = 0; i<1000; i++){
            int t = ann.test(testingNumbers.get(i));
            System.out.println("actual: "+testingNumbers.get(i).number+" ann:"+t);
            if(testingNumbers.get(i).number == t)
                correct++;
        }
        System.out.println("Accuracy:"+correct/1000);
        */
    }

    private static void normalize(List<Number> num){
        double min = 0; 
        double max = 255;
        for(Number n : num)
            for(int i = 0; i<n.lined.length; i++)
                n.lined[i] = n.lined[i] /255;
                
    }
    
}
