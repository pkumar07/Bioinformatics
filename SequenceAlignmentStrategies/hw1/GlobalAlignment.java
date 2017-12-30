package hw1;

import java.util.Map;

public class GlobalAlignment {
    
    public static int[][] scoreTable;
    public static int[][] scoreTableDirection;
    public static String a;
    public static String b;
    public static PQNode node;
 
    public static void initializeScoreTable(){
        //Initialize first row
        for( int col = 1; col< scoreTable[0].length; col++){
            scoreTable[0][col] = col * Hw1.m;   
            scoreTableDirection[0][col] = -1;
        }

        //Initialize first column
        for( int row = 1; row< scoreTable.length; row++){
            scoreTable[row][0] = row * Hw1.m; 
            scoreTableDirection[row][0] = 1; 
        }

        //Computation of the values for the score matrix and the direction matix
        for(int row = 1; row< scoreTable.length; row++){
            for(int col = 1; col < scoreTable[row].length; col++){
                int index1 = Hw1.alphabet_map.get(Character.toUpperCase(a.charAt(row-1)));
                int index2 = Hw1.alphabet_map.get(Character.toUpperCase(b.charAt(col-1)));
                int x =  (scoreTable[row-1][col] + Hw1.m);
                int y =  (scoreTable[row][col-1] + Hw1.m);
                int z =  (scoreTable[row-1][col-1] + Hw1.scoring_matrix.get(index1).get(index2));

                if( x >= y && x >= z){ //Vertical
                    scoreTable[row][col] = x; 
                    scoreTableDirection[row][col] = 1;
                }
                else if( y >= x && y >= z){ //Horizontal
                    scoreTable[row][col] =  y;
                    scoreTableDirection[row][col] = -1;
                }
                else if( z >= x && z >= y){ //Diagonal
                    scoreTable[row][col] =  z;
                    scoreTableDirection[row][col] = 0;
                }   
             
            }
        }
    
    }
    
    public static void backtrackScoreTable(){
        int i = a.length();
        int j = b.length();
        node.a = new StringBuilder(); //Aligned sequence B
        node.b = new StringBuilder(); //Aligned sequence B

        while(i>0 || j > 0){
            if( i>0 && j>0 && scoreTableDirection[i][j] == 0 ){ //Diagonal
                node.a.insert(0, a.charAt(i-1));
                node.b.insert(0, b.charAt(j-1));
                i--;
                j--;
            }
            else if( i> 0 && scoreTableDirection[i][j] == 1){ //vertical
                node.a.insert(0,a.charAt(i-1));
                node.b.insert(0,'.');
                i--;
            }
            else if (j>0 && scoreTableDirection[i][j] == -1){ //horizontal
                node.a.insert(0,'.');
                node.b.insert(0,b.charAt(j-1));
                j--;
            }    
        }
    
    }
    public static void doGlobalAlignmentUtil(){
        //Initializes the score table and backtracks to find the alignment
        scoreTable = new int[a.length() + 1][b.length() + 1];
        scoreTableDirection = new int[a.length() + 1][b.length() + 1];
        GlobalAlignment.initializeScoreTable();
        GlobalAlignment.backtrackScoreTable();
   
    }
    public static void globalAlignment(){
        for(Map.Entry<String,String> entry_query : Hw1.queryStrings.entrySet()){
           // long start = System.currentTimeMillis();
            for(Map.Entry<String,String> entry_database : Hw1.databaseStrings.entrySet()){
                a = entry_query.getValue();
                b = entry_database.getValue();
                node = new PQNode(entry_query.getKey(),entry_database.getKey());;
               
                doGlobalAlignmentUtil();
                node.score = scoreTable[a.length()][b.length()];
                DisplayOutput.queue.add(node);
                
                if(DisplayOutput.queue.size() > Hw1.k)
                    DisplayOutput.queue.poll();
                //System.out.println(node.score);
            }
          //  long end = System.currentTimeMillis();
          //  System.out.println(end - start);
        }

      DisplayOutput.printQueue();
    }
  
  
}
