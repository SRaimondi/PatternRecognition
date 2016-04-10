package ann.util;

import java.awt.image.BufferedImage;

/**
 * @author jonathan
 */
public class Number {
    public static int imageSize = 28;
    public final int number;
    public double[] lined = new double[imageSize*imageSize];
    public int[][] picture = new int[imageSize][imageSize];
    public double[] histogramV = new  double[imageSize];
    public double[] histogramH = new  double[imageSize];
    public double[] transitionsV = new  double[imageSize];
    public double[] transitionsH = new  double[imageSize];
    public Number(String line){
        String[] data = line.split(",");
        number = Integer.parseInt(data[0]);
        for(int i = 0; i<imageSize*imageSize; i++){
            int t = Integer.parseInt(data[i+1]);
            lined[i] = t;
            picture[i%imageSize][i/imageSize] = t; //data[i+1] since data[0] is the number 
        }
        histogramV = histogramV(picture);
        histogramH = histogramH(picture);
        transitionsV = transitionsV(picture);
        transitionsH = transitionsH(picture);
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
        public static double[] histogramV(int[][] pic){//vertical histogram
        double[] hist = new double[imageSize+1];//
        for(int k = 0; k < imageSize;k++)
            for(int i = 0; i < imageSize; i++)
                if(pic[k][i] != 0 ) //not white
                    hist[i+1]++;//count number of non-white pixels per vertical line, add one so the 0-column and -row can be set to maxValue in dtw
        return hist;
    }
    public static double[] histogramH(int[][] pic){//horizontal histogram
        double[] hist = new double[imageSize+1];//
        for(int k = 0; k < imageSize;k++)
            for(int i = 0; i < imageSize; i++)
                if(pic[k][i] != 0 ) //not white
                    hist[i+1]++;//count number of non-white pixels per horizontal line, add one so the 0-column and -row can be set to maxValue in dtw
        return hist;
    }
    public static double[] transitionsV(int[][] pic){//vertical transitions
        double[] transitions = new double[imageSize+1];
        try{
            for(int k = 0; k < imageSize;k++)
                for(int i = 0; i < imageSize; i++)
                    if(pic[k][i-1] != pic[k][i] ) 
                        transitions[i]++;
        }catch(ArrayIndexOutOfBoundsException e){}
        return transitions;
    }
    public static double[] transitionsH(int[][] pic){//vertical transitions
        double[] transitions = new double[imageSize+1];
        try{
            for(int k = 0; k < imageSize;k++)
                for(int i = 0; i < imageSize; i++)
                    if(pic[k-1][i] != pic[k][i] )
                        transitions[k]++;
        }catch(ArrayIndexOutOfBoundsException e){}
        return transitions;
    }
}
