package org.home;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;


public class Task {
    private String name;
    private String info;
    private final Integer id = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    private TaskStatus taskStatus;



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

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
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


}
