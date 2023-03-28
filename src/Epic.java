import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> mySubtasksID = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, "NEW");
    }

    @Override
    public String toString() {
        return "Epic{" +
                "mySubtasksID=" + mySubtasksID.toString() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskID=" + taskID +
                '}';
    }

    public ArrayList<Integer> getMySubtasksID() {
        return mySubtasksID;
    }

    public void removeElementFromMySubtasksID(int i) {
        mySubtasksID.remove(i);
    }

    public void clearMySubtaskID() {
        mySubtasksID.clear();
    }
}
