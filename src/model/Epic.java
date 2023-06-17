package model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class Epic extends Task {
    private ArrayList<Integer> mySubtasksID = new ArrayList<>();
    private LocalDateTime endTime = null;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW,TaskType.EPIC);
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "mySubtasksID=" + mySubtasksID.toString() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskID=" + taskID + '\'' + ", startTime=" + startTime + '\'' + ", duration=" + getDuration()+
                 '\'' + ", endTime=" + getEndTime()+
                '}';
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime(Map<Integer, Subtask> subtasks){
        if (!mySubtasksID.isEmpty()&&getStartTime(subtasks).isPresent()){
            endTime = getStartTime(subtasks).get().plus(getDuration(subtasks));
            return endTime;}
        else {return null;}
    }
    public Duration getDuration(Map<Integer, Subtask> subtasks){
        Duration interval = Duration.ofMinutes(0);
        if (!mySubtasksID.isEmpty()) {
            for (Integer i : mySubtasksID) {
                interval = interval.plusMinutes(subtasks.get(i).getDuration().toMinutes());
            }
        }
        return interval;
        }


    public Optional<LocalDateTime> getStartTime(Map<Integer, Subtask> subtasks) {
        return mySubtasksID.stream().map((Integer i)->subtasks.get(i).getStartTime())
                .min(LocalDateTime::compareTo);
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

    public void setMySubtasksID(ArrayList<Integer> mySubtasksID) {
        this.mySubtasksID = mySubtasksID;
    }
}
