package model;

public class Subtask extends Task {

    public Subtask(String name, String info, TaskStatus taskStatus) {
        super(name, info, taskStatus);
    }
    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", info='" + getInfo() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + getTaskStatus() +
                '}';
    }
}
