package ann;

public class Layer {
    Node[] nodes;
    Layer next;
    Layer pre;

    /*
    public Layer(int nodes, Layer prev){
        this.nodes = new Node[nodes];
        next = prev;
        for(Node n: this.nodes)
            n = new Node(prev.nodes.length);   
    }*/
    public Layer(int nodes, Layer prev, int features){
        this.nodes = new Node[nodes];
        pre = prev;
        for(int i = 0; i < this.nodes.length; i++){
            if(prev == null)
                this.nodes[i] = new Node(features);   
            else
                this.nodes[i] = new Node(prev.nodes.length);   
            
        }
        //System.out.println(this.nodes[0]);
    }
    public void setWeights(double d){
        for(Node n: nodes)
            for(double w: n.weight)
                w = d;
    }/*
    public void randomWeights(){
        for(Node n: nodes)
            for(double w: n.weight){
                w = rng.nextDouble();
            }
    }*/
}
