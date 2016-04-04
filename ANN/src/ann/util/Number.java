package ann.util;

import java.awt.image.BufferedImage;

/**
 * @author jonathan
 */
public class Number {
    public static int imageSize = 28;
    public final int number;
    public int[] lined = new int[imageSize*imageSize];
    public int[][] picture = new int[imageSize][imageSize];
    public Number(String line){
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
