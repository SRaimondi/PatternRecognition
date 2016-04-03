package ann;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Input{
        
    
    public static void main(String[] args)throws IOException{
        
        
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
        
        int num = trainingNumbers.get(0).lined.length;
        ANN ann = new ANN(new int[]{num,50,10});
        
        for(int i = 0; i<5000; i++)
            ann.train(trainingNumbers.get(i));
        
        double correct = 0;
        for(int i = 0; i<5000; i++){
            int t = ann.test(testingNumbers.get(i));
            System.out.println("actual: "+testingNumbers.get(i).number+" ann:"+t);
            if(testingNumbers.get(i).number == t)
                correct++;
        }
        System.out.println("Accuracy:"+correct/5000);
        
    }
    public static class Number{
        static int imageSize = 28;
        final int number;
        public int[] lined = new int[imageSize*imageSize];
        int[][] picture = new int[imageSize][imageSize];
        Number(String line){
            String[] data = line.split(",");
            number = Integer.parseInt(data[0]);
            for(int i = 0; i<imageSize*imageSize; i++){
                int t = Integer.parseInt(data[i+1]);
                lined[i] = t;
                picture[i%imageSize][i/imageSize] = t; //data[i+1] since data[0] is the number 
            }
        }
        public BufferedImage getImage(){
            BufferedImage graph = new BufferedImage( imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
            for(int i = 0; i<imageSize; i++) 
                for(int k = 0; k<imageSize; k++) 
                    graph.setRGB(i, k, greyToRGB(picture[i][k]));
            return graph;
        }
        private int greyToRGB(int grey){
            if(grey == 0)
                return 0xFFFFFF;
            int rgb = 0;
            rgb -= grey;
            rgb = rgb << 8;
            rgb -= grey; 
            rgb = rgb << 8;
            rgb -= grey; 
            return rgb;
        }

    }
}
