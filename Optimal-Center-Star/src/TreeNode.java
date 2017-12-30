import java.util.ArrayList;
import java.util.HashMap;

public class TreeNode {
    private char c;
    private HashMap<Character, TreeNode> children;
    private ArrayList<Integer> list;
    private boolean isLeaf;
    private boolean isConsumed;

    public TreeNode() {
        this.children = new HashMap<>();
        this.isLeaf = false;
        this.isConsumed = false;

    }

    public TreeNode(char c) {
        this.c = c;
        this.isLeaf = false;
        this.children = new HashMap<>();
        this.isConsumed = false;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public void setConsumed(boolean isConsumed) {
        this.isConsumed = isConsumed;
    }

    public ArrayList<Integer> getList() {
        return list;
    }

    public void setList(ArrayList<Integer> list) {
        this.list = list;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public HashMap<Character, TreeNode> getChildren() {
        return children;
    }

    public void setChildren(HashMap<Character, TreeNode> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
}
