package model;

import java.util.Objects;

public class Subtask extends Task {

    TaskStatus taskStatus;

    public Subtask(String name, String info, TaskStatus taskStatus) {
        super(name, info, taskStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask subtask)) return false;
        if (!super.equals(o)) return false;
        return getTaskStatus() == subtask.getTaskStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTaskStatus());
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
