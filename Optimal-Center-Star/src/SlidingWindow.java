import java.util.ArrayList;

class InnerObject {
    private int startPos_Pi;
    private int startPos_Pj;

    public InnerObject(int startPos_Pi, int startPos_Pj) {
        this.startPos_Pi = startPos_Pi;
        this.startPos_Pj = startPos_Pj;
    }

    public int getStartPos_Pi() {
        return startPos_Pi;
    }

    public void setStartPos_Pi(int startPos_Pi) {
        this.startPos_Pi = startPos_Pi;
    }

    public int getStartPos_Pj() {
        return startPos_Pj;
    }

    public void setStartPos_Pj(int startPos_pj) {
        this.startPos_Pj = startPos_pj;
    }
}

class OuterObject {
    private int frequency = 0;
    private ArrayList<InnerObject> position;

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public ArrayList<InnerObject> getPosition() {
        return position;
    }

    public void setPosition(ArrayList<InnerObject> position) {
        this.position = position;
    }
}

public class SlidingWindow {

    public static OuterObject doSlidingWindow(TreeNode root_Pi, String Pj, int r) {
        int current_pos = 0;
        int start_pos = 0;
        int end = start_pos + r; //start+r = end shouldn't be greater than r
        OuterObject outer_object = new OuterObject();
        ArrayList<InnerObject> innerObjectArrayList = new ArrayList<>();
        TreeNode root = root_Pi;
        boolean flag = false;

        while (current_pos < Pj.length()) {
            start_pos = current_pos;
            end = (start_pos + r < Pj.length()) ? start_pos + r : Pj.length();

            while (current_pos < end && root.getChildren().containsKey(Pj.charAt(current_pos))) {
                root = root.getChildren().get(Pj.charAt(current_pos));
                current_pos++;
                flag = true;
            }

            if (current_pos == end && end - start_pos == r) {
                //Case 1: Loop breaks when all the chars have matched
                //Update the entire data structure

                if (root.getList().size() != 0) {

                    InnerObject inner_object = new InnerObject(root.getList().remove(0), start_pos);
                    if (!root.isConsumed()) {
                        outer_object.setFrequency(outer_object.getFrequency() + 1);
                        root.setConsumed(true);
                    }
                    innerObjectArrayList.add(inner_object);
                }

            } else if (flag == true) {
                //Case 2: Partial matching -> Slide window
                flag = false;
            } else {
                flag = false;
                current_pos++;
            }
            root = root_Pi;
        }
        outer_object.setPosition(innerObjectArrayList); //insert in the outer arraylist
        return outer_object;
    }


}