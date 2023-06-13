package model;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status; // NEW IN_PROGRESS DONE;
    protected int taskID;
    protected TaskType type;

    public Task(String name, String description, TaskStatus status, TaskType type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }
    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
    }

    @Override
    public String toString() {
        return "model.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskID=" + taskID +
                '}';
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public TaskType getTaskType() {
        return type;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
//    public boolean equals(Task task){
//        return this.name.equals(task.getName())&&this.description.equals(task.getDescription())&&
//                this.taskID==task.getTaskID()&&this.type.equals(getTaskType())&&this.status.equals(task.getStatus());
//
//}
}
