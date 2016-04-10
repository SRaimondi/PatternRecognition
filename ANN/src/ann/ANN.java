package ann;

import java.io.IOException;
import ann.util.Number;
import java.util.ArrayList;
import java.util.List;


public class ANN {
    static Layer inputLayer;
    static Layer outputLayer;
    static Layer[] layers;
    static double learningRate = 0.1;
    static int numFeatures;
    
    
    public ANN(int[] args){//{n,x,m} n,x,m num of nodes in layer 1,2,3
        layers = new Layer[args.length];
        Layer prev = null;
        numFeatures = 1;
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
       double[] featureVector = num.lined;
        int classLabel = num.number;
        
        for(int i = 0; i < inputLayer.nodes.length; i++){
            double weightedInput = 0;
            for(int k = 0; k<numFeatures; k++){
                weightedInput += inputLayer.nodes[i].weight[k]*featureVector[i];
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
        }else{//one output node per class
            double biggest = Double.NEGATIVE_INFINITY;
            int index = 0;
            List<Integer> not = new ArrayList<>();
            for(int j = 0; j<10; j++){
                for(int i = 0; i<outputLayer.nodes.length; i++)
                    if(outputLayer.nodes[i].value > biggest && !not.contains(i)){
                        index = i;
                        biggest = outputLayer.nodes[i].value;
                    }
                if(index != classLabel){
                    outputLayer.nodes[index].error = -outputLayer.nodes[index].value;
                    not.add(index);
                }else{
                    j=10;//break
                }
                biggest = Double.NEGATIVE_INFINITY;
            }
                    
            
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
                for(int i = 0; i<numFeatures; i++){
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
        }
       
        
    }
    public int test(Number num){

        double[] featureVector = num.lined;

        for(int i = 0; i < inputLayer.nodes.length; i++){
            double weightedInput = 0;
            for(int k = 0; k<numFeatures; k++){
                weightedInput += inputLayer.nodes[i].weight[k]*featureVector[i];
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
            for(int i = 0; i<outputLayer.nodes.length; i++){
                System.out.print(outputLayer.nodes[i].value+" ");
                if(outputLayer.nodes[i].value >= 0.9){System.out.println("");
                    return i;
                }
            }
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
