public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, String description, int epicID, String status) {
        super(name, description, status);
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicID=" + epicID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskID=" + taskID +
                '}';
    }

    public int getEpicID() {
        return epicID;
    }
}
