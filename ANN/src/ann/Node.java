package ann;

import java.util.Random;


public class Node {
    double[] weight;
    double error = 0;
    double value = Double.MAX_VALUE;
    Random rng = new Random();
    
    public Node(int num){
        weight = new double[num];
        for(int i = 0; i<weight.length; i++)
            weight[i] = rng.nextDouble();
       
    }  
    
    public void initWeight(int layerBeforeSize){
        weight = new double[layerBeforeSize];
        for(int i = 0; i < layerBeforeSize; i++)
            weight[i] = rng.nextDouble();
    }
}
