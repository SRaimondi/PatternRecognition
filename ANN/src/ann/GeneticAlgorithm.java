package ann;

import java.util.Random;

/**
 *
 * @author jonathan
 */
public class GeneticAlgorithm {
    int population;
    int length;
    double mutationRate;
    double crossoverRate;
    int[][] chromosomes;
    Random rng = new Random();
    
    public GeneticAlgorithm(int length){
        this(length, 100, 0.001, 0.6);
    }
    public GeneticAlgorithm(int length, int pop, double mutationRate, double crossoverRate){
        this.length = length;
        this.population = pop;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate; 
        chromosomes = new int[population][length];
        for(int[] c: chromosomes)
            for(int i: c)
                i = rng.nextBoolean() ? 1 : 0;
        
        while(true){//while termination criterion not fullfilled, ??no(small) change in fitness??
            //select population subset R c P, n best chromosomes
            //generate children population from R; TNG(R);
            
        }
        
        
        
    }

    private double fitness(int[] chromosome){
        
        
        return 0;
    }
    private int[][] TNG(int[][] parents){
        int[][] pop = new int[population][length];
        int loc = 0;
        while(loc<population){
            int father = rng.nextInt(parents.length);
            int mother;
            do{
                mother = rng.nextInt(parents.length);
            }while(mother==father);
            int[] u = copy(parents[father]);
            int[] v = copy(parents[mother]);
            if(rng.nextDouble()<crossoverRate){
                crossover(u,v,"single");
            }
            int[] w = copy(u);
            int[] z = copy(v);
            if(rng.nextDouble()<mutationRate){
                mutate(w);
                mutate(z);
            }
            pop[loc++] = w;
            pop[loc++] = z;
        }
        return pop;
    }
    private int[] copy(int[] a){
        int[] b = new int[a.length];
        for(int i=0;i<a.length;i++)
            b[i] = a[i];
        return b;    
    }
    private void mutate(int[] chromosome){
        int t = rng.nextInt(chromosome.length);
        chromosome[t] = chromosome[t] == 1 ? 0 : 1;
    }
    private void crossover(int[] ch1, int[] ch2, String type){
        if(type.equals("single")){
            int t = rng.nextInt(ch1.length-1)+1; 
            int temp;
            for(int i = t; i<ch1.length; i++){
                temp = ch1[i];
                ch1[i] = ch2[i];
                ch2[i] = temp;
            }
        }else if(type.equals("double")){
            int t = rng.nextInt(ch1.length-2)+1; 
            int z = rng.nextInt(ch1.length-t)+t; 
            int temp;
            for(int i = t; i<=z; i++){
                temp = ch1[i];
                ch1[i] = ch2[i];
                ch2[i] = temp;
            }
        }
    }
}
