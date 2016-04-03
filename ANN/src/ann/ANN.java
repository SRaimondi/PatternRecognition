package ann;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import ann.Input.Number;


public class ANN {
    static Layer inputLayer;
    static Layer outputLayer;
    static Layer[] layers;
    static double learningRate = 0.1;
    static int numFeatures;
    
    
    public ANN(int[] args){//{n,x,m} n,x,m num of nodes in layer 1,2,3
        layers = new Layer[args.length];
        Layer prev = null;
        numFeatures = args[0];
        for(int i = 0; i < args.length; i++ ){
            layers[i] = new Layer(args[i], prev, numFeatures);
            prev = layers[i];
        }
        inputLayer = layers[0];
        outputLayer = layers[args.length-1];
        //System.out.println(layers[0].nodes[0].weight);
        for(int i = 0; i < layers.length-2; i++ ){
            layers[i].next = layers[i+1];
        }
        //initializeStuff();
    }
    public void train(Number num) throws IOException {
        int[] featureVector = num.lined;
        int classLabel = num.number;
        
        for(int i = 0; i < inputLayer.nodes.length; i++){
            double weightedInput = 0;
            for(int k = 0; k<featureVector.length; k++){
                weightedInput += inputLayer.nodes[i].weight[k]*featureVector[k];
            }    
            inputLayer.nodes[i].value = activate(weightedInput);
        }
        for(int i = 1; i < layers.length; i++){
            for(int d = 0; d<layers[i].nodes.length; d++){
                double weightedInput = 0;
                for(int k = 0; k<layers[i-1].nodes.length; k++){
                    weightedInput += layers[i].nodes[d].weight[k]*layers[i-1].nodes[k].value;
                }    
                layers[i].nodes[d].value = activate(weightedInput);         //System.out.println(activate(weightedInput));
            }
        }    

        if(outputLayer.nodes.length == 1){//only one output node
                       
            outputLayer.nodes[0].error =  outputLayer.nodes[0].value - Math.pow(10,classLabel);                           
            System.out.println("Num: "+num.number+" "+outputLayer.nodes[0].error );

            for(int i = layers.length-2; i>=0; i--){
                for(int k = 0; k<layers[i].nodes.length; k++){
                    double weightedError = 0;
                    for(Node n: layers[i+1].nodes){
                        weightedError += n.error*n.weight[k];
                        //System.out.println(weightedError);
                    }
                    layers[i].nodes[k].error = weightedError;

                }
            }
            for(int k = 0; k < inputLayer.nodes.length; k++){
                for(int i = 0; i<featureVector.length; i++){
                    inputLayer.nodes[k].weight[i] = inputLayer.nodes[k].weight[i] + learningRate*inputLayer.nodes[k].error*derActivate(inputLayer.nodes[k].value)*featureVector[i];// if add activation function, put derivative here  
                }
            }
            for(int i = 1; i<layers.length; i++){                  
                for(Node n: layers[i].nodes){
                    for(int k = 0; k<layers[i-1].nodes.length; k++){
                        n.weight[k] = n.weight[k] + learningRate*n.error*derActivate(n.value)*layers[i-1].nodes[k].value;// if add activation function, put derivative here  
                    }
                }
            }
        }else{//one node per class
            for(int i = 0; i<outputLayer.nodes.length; i++)
                if(outputLayer.nodes[i].value >= 0.9 && !(i == classLabel))
                    outputLayer.nodes[i].error =  -outputLayer.nodes[i].value;   
                else if(outputLayer.nodes[i].value < 0.9 && i == classLabel)
                    outputLayer.nodes[i].error =  outputLayer.nodes[i].value; 
        }
        
    }
    public int test(Number num){

        int[] featureVector = num.lined;

        for(int i = 0; i < inputLayer.nodes.length; i++){
            double weightedInput = 0;
            for(int k = 0; k<featureVector.length; k++){
                weightedInput += inputLayer.nodes[i].weight[k]*featureVector[k];
            }    
            inputLayer.nodes[i].value = activate(weightedInput);
        }
        for(int i = 1; i < layers.length; i++){
            for(int d = 0; d<layers[i].nodes.length; d++){
                double weightedInput = 0;
                for(int k = 0; k<layers[i-1].nodes.length; k++){
                    weightedInput += layers[i].nodes[d].weight[k]*layers[i-1].nodes[k].value;
                }    
                layers[i].nodes[d].value = activate(weightedInput);  
            }
        }    

        if(outputLayer.nodes.length == 1){//only one output node
            double trim = ((double)((int)(Math.log10(outputLayer.nodes[0].value)*100)))/100;
            //System.out.println(outputLayer.nodes[0y].value);
            return (int) trim;
        }else{//one node per class
            for(int i = 0; i<outputLayer.nodes.length; i++)
                if(outputLayer.nodes[i].value >= 0.9)
                    return i;
        }              
        return 11;
    }

    public double activate(double in){
        return Math.tan(in);
    }

    public double derActivate(double in){
        return Math.tanh(in);
    }
}