package model;


import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


public class Task {
    private String type;
    private String name;
    private String info;
    private Integer id = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
    private TaskStatus taskStatus;

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public Integer getId() {
        return id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(name, task.name) && Objects.equals(info, task.info) && Objects.equals(id, task.id) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, info, id, taskStatus);
    }

    public Task(String name, String info, TaskStatus taskStatus) {
        this.name = name;
        this.info = info;
        this.taskStatus = taskStatus;
    }

    public Task(String type, String name, String info, String id, TaskStatus taskStatus) {
        this.name = name;
        this.info = info;
        this.taskStatus = taskStatus;
        this.id = Integer.valueOf(id);
        this.type = type;
    }

    public Task(String name, String info) {
        this.name = name;
        this.info = info;
    }


}
