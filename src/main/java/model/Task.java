package model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


public class Task {
    private String type;
    private String name;
    private String info;
    private Integer id = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
    private TaskStatus taskStatus;
    protected Duration duration;
    protected LocalDateTime startTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime().format(formatter) +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(type, task.type) && Objects.equals(name, task.name) && Objects.equals(info, task.info) && Objects.equals(id, task.id) && taskStatus == task.taskStatus && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, info, id, taskStatus, duration, startTime);
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

    public String getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        var endTime=  startTime.toInstant(ZoneOffset.UTC).plus(duration);
        return LocalDateTime.ofInstant(endTime, ZoneOffset.UTC);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Task(String name, String info, TaskStatus taskStatus, String startTime, String duration) {
        this.name = name;
        this.info = info;
        this.taskStatus = taskStatus;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.between(this.startTime, LocalDateTime.parse(duration, formatter));
    }

    public Task(String name, String info, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.info = info;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String type, String name, String info, String id, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.info = info;
        this.taskStatus = taskStatus;
        this.id = Integer.valueOf(id);
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String info) {
        this.name = name;
        this.info = info;
    }


}
