import java.util.ArrayList;
public class Epic extends Task {
    ArrayList<Integer> mySubtasksID = new ArrayList<>();
    public Epic(String name, String description) {
        super(name, description,"NEW" );
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
}
