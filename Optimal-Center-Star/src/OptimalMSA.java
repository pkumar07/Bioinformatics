import java.io.*;
import java.util.*;

public class OptimalMSA {

    public static int max_string_length = 0;
    public static HashMap<String, String> databaseStrings = null;
    public static int max_frequency = -1;
    public static HashMap<String, OuterObject> Nij = new HashMap<>();
    public static String ref_id = "";
    public static DPResult max_string_result;

    public static int initialize_databaseStringsMap(String filename) {
        databaseStrings = new HashMap<>();
        BufferedReader br = null;
        FileReader fr = null;
        int length = 0;
        int count = 0;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine = br.readLine();
            String temp = "";
            String id = Arrays.asList(sCurrentLine.split(" ")).get(0);


            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith(">")) {
                    databaseStrings.put(id, temp);
                    count++;
                    length += temp.length();
                    id = Arrays.asList(sCurrentLine.split(" ")).get(0);
                    temp = "";
                } else {
                    temp = temp + sCurrentLine;
                }
            }
            databaseStrings.put(id, temp);
            count++;
            length += temp.length();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
            }
        }
        return length / count;
    }

    public static void main(String[] args) {
        String dataFile = "/Users/Jiya/Desktop/OptimalMSA/src/data.txt";
        String alphabetFile = "/Users/Jiya/Desktop/OptimalMSA/src/alphabet.txt";
        String scoreMatrixFile = "/Users/Jiya/Desktop/OptimalMSA/src/scoringmatrix.txt";
        int L = initialize_databaseStringsMap(dataFile); //L is average length
        int r = (int) Math.sqrt(L);
        int sum = 0;
        ArrayList<DPResult> dp_result = new ArrayList<DPResult>();

        String reference_string = "";
        HashMap<String, OuterObject> temp_Nij = new HashMap<>();

        //measuring only main computation
        long startTime = System.currentTimeMillis();

        FileWriter writer = null;
        PrintWriter printWriter = null;

        try {

            writer = new FileWriter("resultGlobalMSA.txt");

            printWriter = new PrintWriter(writer);

            for (Map.Entry<String, String> entryString_i : databaseStrings.entrySet()) {
                String pi = entryString_i.getValue();
                String pi_id = entryString_i.getKey();
                sum = 0;
                //Making keyword tree for each Pi in the database
                KeywordTree Ti = new KeywordTree();
                TreeNode root = Ti.buildTree(pi, r);

                //Calling sliding window on rest of the dataset with root pointing to keyword tree
                for (Map.Entry<String, String> entryString_j : databaseStrings.entrySet()) {
                    String pj = entryString_j.getValue();
                    if (entryString_j.getKey() == pi_id)
                        continue;
                    else {
                        //Call sliding window
                        OuterObject outer_object = SlidingWindow.doSlidingWindow(root, pj, r);
                        temp_Nij.put(entryString_j.getKey(), outer_object);
                    }
                }

                //Get frequency of outer_objects in temp_Nij
                for (Map.Entry<String, OuterObject> entry : temp_Nij.entrySet()) {
                    sum += entry.getValue().getFrequency();
                }

                //If current sum is greater than the global max_frequency then update Nij
                if (sum > max_frequency) {
                    Nij = temp_Nij;
                    max_frequency = sum;
                    ref_id = pi_id;

                }
            }

            reference_string = databaseStrings.get(ref_id);

            for (Map.Entry<String, String> entryString_j : databaseStrings.entrySet()) {
                String pj = entryString_j.getValue();
                String pj_id = entryString_j.getKey();
                int dp_align_length = 0;
                if (entryString_j.getKey() == ref_id)
                    continue;
                else {
                    DPResult result = PairwiseAlignment.doDP(reference_string, pj, pj_id, Nij.get(entryString_j.getKey()).getPosition(), r, scoreMatrixFile, alphabetFile);
                    dp_align_length = result.reference_result.split("#").length;

                    if (dp_align_length > max_string_length) {
                        if (max_string_length == 0)
                            max_string_result = result;
                        else {
                            dp_result.add(max_string_result);
                            max_string_result = result;
                        }
                        max_string_length = dp_align_length;
                    } else {
                        dp_result.add(result);
                    }

                }
            }

//            System.out.println(ref_id);

            //Multiple Sequence Alignment

            String id_array[] = new String[dp_result.size() + 2];
            ArrayList<ArrayList<String>> alignments = new ArrayList<ArrayList<String>>();

            String[] ref1 = max_string_result.reference_result.split("#");
            alignments.add(0, new ArrayList<String>(Arrays.asList(ref1)));
            String[] align2 = max_string_result.target_result.split("#");
            alignments.add(1, new ArrayList<String>(Arrays.asList(align2)));
            id_array[0] = ref_id;
            id_array[1] = max_string_result.target_id;
            int row = 2;

            for (int x = 0; x < dp_result.size(); x++) {

                String[] seq_A = dp_result.get(x).reference_result.split("#");
                String[] seq_B = dp_result.get(x).target_result.split("#");
                ArrayList<String> inner = new ArrayList<>();
                id_array[row] = dp_result.get(x).target_id;

                int col = 0, m = 0;
                while (alignments.get(0).get(col).equals("-")) {
                    col++;
                    inner.add("-");
                }

                for (; m < seq_A.length && col < alignments.get(0).size(); m++) {
                    if (alignments.get(0).get(col).equals(seq_A[m])) {
                        inner.add(seq_B[m]);
                    } else if (seq_A[m].equals("-") && !alignments.get(0).get(col).equals("-")) {
                        inner.add(seq_B[m]);
                        for (int g = 0; g < row; g++) {
                            ArrayList<String> temp = alignments.get(g);
                            temp.add(m, "-");
                        }
                    } else if (alignments.get(0).get(col).equals("-")) {
                        inner.add("-");
                        m--;
                    }
                    col++;
                }

                if (m > seq_A.length && col < alignments.get(0).size()) {
                    while (col++ >= 0) {
                        inner.add("-");
                    }
                }

                Collections.reverse(inner);
                alignments.add(row, inner);
                row++;
            }

            int maxLength = Integer.MIN_VALUE;
            for (ArrayList<String> innerList : alignments) {
                if (innerList.size() > maxLength) {
                    maxLength = innerList.size();
                }
            }

//            System.out.println("MaxLength: " + maxLength);

            for (int k = 0; k < dp_result.size() + 2; k++) {
                ArrayList<String> inner = alignments.get(k);
                inner.trimToSize();
                if (inner.size() < maxLength) {
                    for (int l = inner.size(); l < maxLength; l++) {
                        inner.add("-");
                    }
                }
            }

            //replace pos with char
            for (int x = 0; x < dp_result.size() + 2; x++) {
                String id = id_array[x];
                String s = databaseStrings.get(id);
                for (int y = 0; y < maxLength; y++) {
                    if (!alignments.get(x).get(y).equals("-")) {
                        alignments.get(x).set(y, ("" + s.charAt(Integer.parseInt(alignments.get(x).get(y)))));
                    }
                }
            }

            long endTime = System.currentTimeMillis();
            String time = ((endTime - startTime)) + "\n";
            System.out.println(time);

            //printing
            for (int x = 0; x < dp_result.size() + 2; x++) {
                String res = id_array[x] + ": " + alignments.get(x).size() + ": ";
                for (int y = 0; y < maxLength - 1; y++) {
                    res += alignments.get(x).get(y) + ", ";
                }
                res += alignments.get(x).get(maxLength - 1);
//                System.out.println(res);
                printWriter.println(res);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printWriter.close();
        }

    }
}