package hw1;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;



public class Hw1 {
    
    public static int m = 0;
    public static int k = 0;
    public static ArrayList<ArrayList<Integer>> scoring_matrix = null;
    public static HashMap<Character,Integer> alphabet_map = null;
    public static LinkedHashMap<String,String> queryStrings = null;
    public static HashMap<String,String> databaseStrings = null;
    
    public static void initialize_databaseStringsMap(String filename){
        databaseStrings = new HashMap<>();
        BufferedReader br = null;
        FileReader fr = null;
       // filename = "C:\\Users\\poorn\\Documents\\NetBeansProjects\\hw1\\src\\hw1\\database2.txt";
         try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine = br.readLine();
            String temp = "";    
            String id = Arrays.asList(sCurrentLine.split(" ")).get(0);
            
            
            while ((sCurrentLine = br.readLine()) != null) {
                if(sCurrentLine.startsWith(">")){
                    databaseStrings.put(id,temp);
                    id = Arrays.asList(sCurrentLine.split(" ")).get(0);   
                    temp = "";  
                }
                else{
                    temp = temp + sCurrentLine;
                }
            }
            databaseStrings.put(id,temp);
	} 
        catch (FileNotFoundException ex) { }
        catch (IOException ex) { }
        finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
                } catch (IOException ex) {}
        }
    }
    public static void initialize_queryStringsMap(String filename){
        queryStrings = new LinkedHashMap<>();
        BufferedReader br = null;
        FileReader fr = null;
       // filename = "C:\\Users\\poorn\\Documents\\NetBeansProjects\\hw1\\src\\hw1\\query.txt";
         try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine = br.readLine();
            String temp = "";
            String id = Arrays.asList(sCurrentLine.split(" ")).get(0);
            while ((sCurrentLine = br.readLine()) != null) {
                if(sCurrentLine.startsWith(">")){
                    queryStrings.put(id,temp);    
                    id = Arrays.asList(sCurrentLine.split(" ")).get(0);    
                    temp = ""; 
                }
                else{
                    temp = temp + sCurrentLine;
                }  
            }
            queryStrings.put(id,temp);   
	} 
        catch (FileNotFoundException ex) { }
        catch (IOException ex) { }
        finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
                } catch (IOException ex) {}
        }
    }
    public static void initialize_alphabetMap(String filename){
        alphabet_map = new HashMap<>();
        BufferedReader br = null;
        FileReader fr = null;
     //   filename = "C:\\Users\\poorn\\Documents\\NetBeansProjects\\hw1\\src\\hw1\\alphabet.txt";
         try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
           
            while ((sCurrentLine = br.readLine()) != null) {
                for(int i = 0; i<sCurrentLine.length(); i++){
                    alphabet_map.put(Character.toUpperCase(sCurrentLine.charAt(i)), i);
                }
            }
	} 
        catch (FileNotFoundException ex) { }
        catch (IOException ex) { }
        finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
                } catch (IOException ex) {}
        }
    }
    public static void iniitialize_Scoring_Matrix(String filename){
        scoring_matrix = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
    //    filename = "C:\\Users\\poorn\\Documents\\NetBeansProjects\\hw1\\src\\hw1\\scoringmatrix.txt";
	
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            ArrayList<Integer> temp = null;
            while ((sCurrentLine = br.readLine()) != null) {
                temp = new ArrayList<>();
                String[] words = sCurrentLine.split(" ");
                for(String word : words){
                    if(!word.equals(""))
                    temp.add(Integer.parseInt(word));
                }
                scoring_matrix.add(temp);
		
            }
	}
        catch (FileNotFoundException ex) { }
        catch (IOException ex) { }
        finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
                } catch (IOException ex) {}
        }
    }
    
 
    public static void main(String[] args) {
        //Denotes which algorithm to execute
        int algorithm = Integer.parseInt(args[0]);
        //number of nearest neighbors 
        k = Integer.parseInt(args[5]);
        //Gap penalty
        m = Integer.parseInt(args[6]);
        
        //Initialization
        iniitialize_Scoring_Matrix(args[4] + ".txt");
        initialize_alphabetMap(args[3] + ".txt");
        initialize_queryStringsMap(args[1] + ".txt");
        initialize_databaseStringsMap(args[2] + ".txt");
     
        switch(algorithm){
            case 1:
                //Global alignment
                GlobalAlignment.globalAlignment();
                break;
            case 2:
                //Local alignment
                LocalAlignment.localAlignment();
                break;
            case 3:
                //Dove-tail alignment
                DovetailAlignment.doveTailAlignment();
                break;
        }
        
        
        
    }
    
}
