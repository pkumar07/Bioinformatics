package hw1;

import java.util.Map;

public class LocalAlignment {
    
    public static int[][] scoreTable;
    public static int[][] scoreTableDirection;
    public static String a;
    public static String b;
    public static PQNode node;
    public static int max_value= Integer.MIN_VALUE;
    public static int max_i = -1;
    public static int max_j = -1;

    public static void backtrackScoreTable(){
        int i = max_i;
        int j = max_j;
        node.a = new StringBuilder(); //Aligned sequence A
        node.b = new StringBuilder(); //Aligned sequence B

        while(scoreTable[i][j] != 0){
            if(scoreTableDirection[i][j] == 2){ //Diagonal
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
                
                scoreTable[row][col] = Math.max(0, Math.max(Math.max(x, y), z));
               
                if( scoreTable[row][col] == x) //vertical
                    scoreTableDirection[row][col] = 1;
                else if( scoreTable[row][col] == y) //horizontal                  
                    scoreTableDirection[row][col] = -1;
                else if( scoreTable[row][col] == z) //Diagonal
                    scoreTableDirection[row][col] = 2;
                else  
                    scoreTableDirection[row][col] = 0;
                //Find max value in entire matrix
                if(scoreTable[row][col] > max_value){
                    max_i = row;
                    max_j = col;
                    max_value = scoreTable[row][col];
                }            
            }
        }
        
    }
    public static void doLocalAlignmentUtil(){
        //Initializes the score table and backtracks to find the alignment
        scoreTable = new int[a.length() + 1][b.length() + 1];
        scoreTableDirection = new int[a.length() + 1][b.length() + 1];
        LocalAlignment.initializeScoreTable();
        LocalAlignment.backtrackScoreTable();
    }
    
    public static void localAlignment(){
        for(Map.Entry<String,String> entry_query : Hw1.queryStrings.entrySet()){
          //  long start = System.currentTimeMillis();
            for(Map.Entry<String,String> entry_database : Hw1.databaseStrings.entrySet()){
                a = entry_query.getValue();
                b = entry_database.getValue();
                
                node = new PQNode(entry_query.getKey(),entry_database.getKey());
                doLocalAlignmentUtil();
                node.score = scoreTable[max_i][max_j];
                DisplayOutput.queue.add(node);

                if(DisplayOutput.queue.size() > Hw1.k)
                    DisplayOutput.queue.poll();  
                
                max_i = 0;
                max_j = 0;
                max_value = Integer.MIN_VALUE;
               // System.out.println(node.score);
               
            }
        //    long end = System.currentTimeMillis();
          //  System.out.println(end - start);
        }
        DisplayOutput.printQueue();
       
    }
}
