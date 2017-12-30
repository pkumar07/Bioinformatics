package hw1;

public class PQNode {
    public StringBuilder a;
    public StringBuilder b;
    public String id_a;
    public String id_b;
    public int score;
    public int startPos_a = 0;
    public int startPos_b = 0;

    public PQNode(String id_a, String id_b){
       
        this.id_a = id_a;
        this.id_b = id_b;
    }
    public PQNode(){     
    }
 
}