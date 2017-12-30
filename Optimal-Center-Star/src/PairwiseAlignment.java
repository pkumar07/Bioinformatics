import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class DPResult {
    public String reference_result = "";
    public String target_result = "";
    public String target_id = "";

    public DPResult(String r, String t, String target_id) {
        reference_result = r;
        target_result = t;
        this.target_id = target_id;
    }

    public DPResult(String r, String t) {
        reference_result = r;
        target_result = t;
    }
}

public class PairwiseAlignment {


    public static DPResult doDP(String reference_string, String pj, String target_id, ArrayList<InnerObject> inner_object, int r, String scoreMatrixFile, String alphabetFile) {
        StringBuilder str1 = new StringBuilder();
        StringBuilder str2 = new StringBuilder();
        int i = 0;
        int j = 0;
        int inner_object_itr = 0;

        while (i < reference_string.length() && j < pj.length() && inner_object_itr < inner_object.size()) {
            int start_pos_Pi = inner_object.get(inner_object_itr).getStartPos_Pi();
            int start_pos_Pj = inner_object.get(inner_object_itr).getStartPos_Pj();

            DPResult result = doDPUtil(reference_string.substring(i, start_pos_Pi),
                    pj.substring(j, start_pos_Pj), i, start_pos_Pi, j, start_pos_Pj, scoreMatrixFile, alphabetFile);

            str1.append(result.reference_result);
            str2.append(result.target_result);

            i = start_pos_Pi;
            j = start_pos_Pj;

            for (int x = start_pos_Pi; x < start_pos_Pi + r; x++) {
                str1.append(x + "#");
            }

            for (int x = start_pos_Pj; x < start_pos_Pj + r; x++) {
                str2.append(x + "#");
            }

            i = i + r;
            j = j + r;
            inner_object_itr++;
        }

        if (i != reference_string.length() || j != pj.length()) {
            DPResult result = doDPUtil(reference_string.substring(i, reference_string.length()),
                    pj.substring(j, pj.length()), i, reference_string.length(), j, pj.length(), scoreMatrixFile, alphabetFile);

            str1.append(result.reference_result);
            str2.append(result.target_result);
        }

        return new DPResult(str1.toString(), str2.toString(), target_id);
    }

    private static DPResult doDPUtil(String seqA, String seqB, int seqA_start, int seqA_end, int seqB_start, int seqB_end, String scoreMatrixFile, String alphabetFile) {

        //call routines to create scoring matrix and alphabet mapping
        List<List<Float>> scoreMatrix = scoringMatrixReader(scoreMatrixFile);
        Map<Character, Integer> alphabets = mapAlphabet(alphabetFile);

        int[][] dist = new int[seqA.length() + 1][seqB.length() + 1];
        int[][] path = new int[seqA.length() + 1][seqB.length() + 1];
        final int fromLeft = 1;
        final int fromUp = 2;
        final int fromDiag = 3;
        final int gap = -10;

        for (int i = 1; i <= seqA.length(); i++) {
            dist[i][0] = gap * i;
            path[i][0] = fromUp;
        }
        for (int j = 1; j <= seqB.length(); j++) {
            dist[0][j] = gap * j;
            path[0][j] = fromLeft;
        }

        for (int i = 1; i <= seqA.length(); i++) {
            for (int j = 1; j <= seqB.length(); j++) {
                int scoreDiag = dist[i - 1][j - 1] + Math.round(scoreMatrix.get(alphabets.get(seqA.charAt(i - 1))).get(alphabets.get(seqB.charAt(j - 1))));
                int scoreLeft = dist[i][j - 1] + gap;
                int scoreUp = dist[i - 1][j] + gap;
                dist[i][j] = Math.max(Math.max(scoreDiag, scoreLeft), scoreUp);
                if (dist[i][j] == scoreDiag)
                    path[i][j] = fromDiag;
                else if (dist[i][j] == scoreLeft)
                    path[i][j] = fromLeft;
                else
                    path[i][j] = fromUp;
            }
        }

        int row = seqA.length();
        int col = seqB.length();
        String alignB = "", alignA = "";
        //needs verification
        while (row > 0 || col > 0) {
            if (path[row][col] == fromDiag) {
                alignA = --seqA_end + "#" + alignA; //seqA.charAt(row - 1) + alignA; //row - 1 + seqA_end + alignA;
                alignB = --seqB_end + "#" + alignB; //seqB.charAt(col - 1) + alignB; //col - 1 + seqB_end + alignB;
                col--;
                row--;
            } else if (path[row][col] == fromLeft) {
                alignA = "-" + "#" + alignA;
                alignB = --seqB_end + "#" + alignB; //seqB.charAt(col - 1) + alignB;  // col - 1 + seqB_end + alignB;
                col--;
            } else {
                alignA = --seqA_end + "#" + alignA; //seqA.charAt(row - 1) + alignA;  ///row - 1 + seqA_end + alignA;
                alignB = "-" + "#" + alignB;
                row--;
            }
        }

        return new DPResult(alignA, alignB);
    }

    public static ArrayList<List<Float>> scoringMatrixReader(String fileName) {
        ArrayList<List<Float>> scoringMatrix = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                line = line.replaceAll("\\s+", " ");
                List<Float> row = Stream
                        .of(line.split(" "))
                        .map(Float::valueOf)
                        .collect(Collectors.toList());
                scoringMatrix.add(row);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scoringMatrix;
    }

    public static HashMap<Character, Integer> mapAlphabet(String fileName) {
        HashMap<Character, Integer> hm = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            char[] str = line.toCharArray();
            int i = 0;
            for (char s : str) {
                hm.put(Character.toUpperCase(s), i++);
//                hm.put(Character.toLowerCase(s), i++);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hm;
    }
}