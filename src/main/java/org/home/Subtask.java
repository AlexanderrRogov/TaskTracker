package org.home;

public class Subtask extends Task {

    private final Integer epicId;

    public Subtask(String name, String info, TaskStatus taskStatus, Integer epicId) {
        super(name, info, taskStatus);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", info='" + getInfo() + '\'' +
                ", id=" + getId() +
                ", epicId=" + epicId +
                ", taskStatus=" + getTaskStatus() +
                '}';
    }
}
