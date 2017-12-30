package hw1;


import java.util.PriorityQueue;
import java.util.Comparator;

public class DisplayOutput {
    
    public static PriorityQueue<PQNode> queue = new PriorityQueue<>(Hw1.k, new Comparator<PQNode>(){
        @Override
        public int compare(PQNode m, PQNode n){
            return m.score - n.score;
        }
    });
    
    public static void printQueue(){
        while(! queue.isEmpty() ){
            PQNode temp = queue.remove();         
            System.out.println("Score = " + temp.score);
            System.out.println(temp.id_a + " " + temp.startPos_a + " " +  temp.a);
            System.out.println(temp.id_b + " " + temp.startPos_b + " " +  temp.b);     
        } 
    }    
}
