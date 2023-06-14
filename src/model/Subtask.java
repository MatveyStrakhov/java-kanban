package model;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicID;

    public Subtask(String name, String description, int epicID, TaskStatus status) {
        super(name, description, status,TaskType.SUBTASK);
        this.epicID = epicID;
    }
    public Subtask(String name, String description, int epicID, TaskStatus status, LocalDateTime startTime, int duration)
    {
        super(name, description, status,TaskType.SUBTASK,startTime,duration);
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "epicID=" + epicID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskID=" + taskID + '\'' + ", startTime=" + startTime + '\'' + ", duration=" + getDuration().toString()+
                '}';
    }

    public int getEpicID() {
        return epicID;
    }
}
