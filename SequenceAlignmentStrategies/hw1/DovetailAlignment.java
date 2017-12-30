package hw1;

import java.util.Map;

public class DovetailAlignment {
    public static int[][] scoreTable;
    public static int[][] scoreTableDirection;
    public static String a;
    public static String b;
    public static PQNode node;
    public static int max_i = -1;
    public static int max_j = -1;
    public static int max_value = Integer.MIN_VALUE;
    
    /*  
        DoveTail Alignment Implementation Details
        String a is the query string and b is the database string
        String a chars forms the row whereas b chars forms the col
        Direction values:
        -1 indicates coming from horizontal direction
        2 indicates coming from diagonal
        1 indicates coming from vertical
    */
    
    public static void initializeScoreTable(){
        //Initialize first row
        for( int col = 1; col< scoreTable[0].length; col++){
            scoreTable[0][col] = 0;   
            scoreTableDirection[0][col] = 0;
        }
        //Initialize first column
        for( int row = 1; row< scoreTable.length; row++){
            scoreTable[row][0] = 0; 
            scoreTableDirection[row][0] = 0; 
        }
        
        //Computation of the values for the score matrix and the direction matix
        for(int row = 1; row< scoreTable.length; row++){
            for(int col = 1; col < scoreTable[row].length; col++){
                int index1 = Hw1.alphabet_map.get(Character.toUpperCase(a.charAt(row-1)));
                int index2 = Hw1.alphabet_map.get(Character.toUpperCase(b.charAt(col-1)));
                int x =  (scoreTable[row-1][col] + Hw1.m);
                int y =  (scoreTable[row][col-1] + Hw1.m);
                int z =  (scoreTable[row-1][col-1] + Hw1.scoring_matrix.get(index1).get(index2));
                
                scoreTable[row][col] = Math.max(Math.max(x, y), z);
               
                if(scoreTable[row][col] == x) //Vertical
                    scoreTableDirection[row][col] = 1;                       
                else if(scoreTable[row][col] == y) //Horizontal
                    scoreTableDirection[row][col] = -1;
                else if(scoreTable[row][col] == z) //Diagonal
                    scoreTableDirection[row][col] = 2;
                
                //Find max value in the last row or the last col
                if((row == scoreTable.length-1 || col == scoreTable[0].length-1) && scoreTable[row][col] > max_value){
                    max_i = row;
                    max_j = col;
                    max_value = scoreTable[row][col];
                }            
            }
        }    
    }
    public static void backtrackScoreTable(){
        int i = max_i;
        int j = max_j;
        node.a = new StringBuilder(); //Aligned sequence A
        node.b = new StringBuilder(); //Aligned sequence B
        while(i>0 && j>0){
            if(scoreTableDirection[i][j] == 2 ){ //Diagonal
                node.a.insert(0, a.charAt(i-1));
                node.b.insert(0, b.charAt(j-1));
                i--;
                j--;
            }
            else if(scoreTableDirection[i][j] == 1){ //vertical
                node.a.insert(0,a.charAt(i-1));
                node.b.insert(0,'.');
                i--;
            }
            else if (scoreTableDirection[i][j] == -1){ //horizontal
                node.a.insert(0,'.');
                node.b.insert(0,b.charAt(j-1));
                j--;
            }
        }
        node.startPos_a = i+1;
        node.startPos_b = j+1;
    }
    public static void doDoveTailAlignmentUtil(){
        //Initializes the score table and backtracks to find the alignment
        scoreTable = new int[a.length() + 1][b.length() + 1];
        scoreTableDirection = new int[a.length() + 1][b.length() + 1];
        DovetailAlignment.initializeScoreTable();       
        DovetailAlignment.backtrackScoreTable();
    }
    public static void doveTailAlignment(){
        for(Map.Entry<String,String> entry_query : Hw1.queryStrings.entrySet()){
            //long start = System.currentTimeMillis();
            for(Map.Entry<String,String> entry_database : Hw1.databaseStrings.entrySet()){
                a = entry_query.getValue();
                b = entry_database.getValue();
                node = new PQNode(entry_query.getKey(),entry_database.getKey());
               
                doDoveTailAlignmentUtil();
                node.score = scoreTable[max_i][max_j];
                DisplayOutput.queue.add(node);              
                if(DisplayOutput.queue.size() > Hw1.k)
                    DisplayOutput.queue.poll();
                //Reset values for next iteration
                max_i = 0;
                max_j = 0;
                max_value = Integer.MIN_VALUE;
                //System.out.println(node.score);
            }
          //   long end = System.currentTimeMillis();
            //System.out.println(end - start);
        }
        DisplayOutput.printQueue();
    }
   
}
