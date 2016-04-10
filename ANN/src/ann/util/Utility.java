package ann.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author jonathan
 */
public class Utility {
    public static int highestEntry(double[] arr){
        double highest = 0;
        int index = -1;
        for(int i = 0; i<arr.length; i++)
            if(arr[i]>highest){
                highest = arr[i];
                index = i;
            }
        return index;
    }
    
    public static double[] addUpDiv(double by, double[] arr1, double[]... arrays){
        double[] arr = Arrays.copyOf(arr1,arr1.length);
        for(double[] a: arrays)
            for(int i = 0; i<a.length; i++)
                arr[i] += a[i];
        for(int i = 0; i<arr.length; i++)
            arr[i] /= by;     
        return arr;
    }
    
    public static double[] addUp(double[] arr1, double[] arr2){
        if(arr1.length!=arr2.length)
            return null;
        double[] arr = new double[arr1.length];
        for(int i = 0; i<arr1.length; i++)
            arr[i] = arr1[i] + arr2[i];
        return arr;
    }
    public static double[] divideArray(double[] arr1, double by){
        double[] arr = new double[arr1.length];
        for(int i = 0; i<arr1.length; i++)
            arr[i] = arr1[i]/by;
        return arr;
    }
    
    public static boolean arraysAreEqual(double[] arr1, double[] arr2, double error){
        boolean equality = false;
        if(arr1.length!=arr2.length)
            return false;
        for(int i = 0; i<arr1.length; i++)
            if(Math.abs(arr1[i]-arr2[i])>error)
                return false;
        return true;
    }
    public static BufferedImage binarize(BufferedImage original) {
        int red;
        int newPixel;

        int threshold = otsuTreshold(original);

        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);
            }
        }

        return binarized;
    }
    private static int otsuTreshold(BufferedImage original) {
        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;

            if(wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;

    }    

    // Return histogram of grayscale image
    public static int[] imageHistogram(BufferedImage input) {
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
    }
    // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
    }
    
}
