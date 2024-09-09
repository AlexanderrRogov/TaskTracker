package model;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Subtask extends Task {

    private final TaskType taskType = TaskType.SUBTASK;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Subtask(String name, String info, TaskStatus taskStatus, String startTime, String duration) {
        super(name, info, taskStatus, startTime, duration);
    }

    public Subtask(Task task) {
        super(task.getName(), task.getInfo(), task.getTaskStatus(), task.getStartTime(), task.getDuration());
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
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime().format(formatter) +
                ", duration=" + getDuration() +
                '}';
    }

}
