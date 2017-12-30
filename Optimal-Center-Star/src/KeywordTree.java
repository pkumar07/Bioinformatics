import java.util.ArrayList;
import java.util.HashMap;

public class KeywordTree {
    public TreeNode root;

    public KeywordTree() {
        root = new TreeNode();
    }

    private void buildTreeUtil(String s, int position) {
        HashMap<Character, TreeNode> map = root.getChildren();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            TreeNode t;
            if (map.containsKey(c))
                t = map.get(c);
            else {
                t = new TreeNode(c);
                map.put(c, t);
            }
            map = t.getChildren();

            if (i == s.length() - 1) {

                ArrayList<Integer> list;
                if (t.isLeaf() == false) {

                    t.setLeaf(true);
                    t.setList(new ArrayList<Integer>());
                }
                list = t.getList();
                list.add(position);

            }
        }
    }

    public TreeNode buildTree(String s, int r) {
        int h = s.length() / r;
        int beginIndex = 0;
        int endIndex = 0;
        root = new TreeNode();

        for (int i = 0; i < h; i++) {
            endIndex = ((beginIndex + r) < s.length()) ? beginIndex + r : s.length();
            buildTreeUtil(s.substring(beginIndex, endIndex), beginIndex);
            beginIndex = endIndex;
        }

        return root;
    }

}