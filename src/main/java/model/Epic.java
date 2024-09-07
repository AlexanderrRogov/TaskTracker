package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final TaskType taskType = TaskType.EPIC;

    private TaskStatus epicStatus = TaskStatus.NEW;

    private final ArrayList<Integer> subTaskIds = new ArrayList<>();



    private LocalDateTime endTime = LocalDateTime.MIN;

    public void setSubTaskInfo(Subtask subtask) {
        this.subTaskIds.add(subtask.getId());
        this.startTime = getSubtaskStartTime(subtask.getStartTime());
        this.endTime = getSubtaskEndTime(subtask.getEndTime());
        this.duration = Duration.between(this.startTime, endTime) ;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    private LocalDateTime getSubtaskEndTime(LocalDateTime subtaskEndTime) {
        if(this.endTime.isBefore(subtaskEndTime)) {
            this.endTime = subtaskEndTime;
        }
        return this.endTime;
    }

    private LocalDateTime getSubtaskStartTime(LocalDateTime subtaskLocalDateTime){
        if(this.startTime.isBefore(subtaskLocalDateTime)) {
           this.startTime = subtaskLocalDateTime;
        }
        return this.startTime;
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void cleanSubtaskIds() {
        subTaskIds.clear();
    }

    @Override
    public LocalDateTime getEndTime() {
        var endTime=  startTime.toInstant(ZoneOffset.UTC).plus(duration);
        return LocalDateTime.ofInstant(endTime, ZoneOffset.UTC);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", info='" + getInfo() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + epicStatus +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime().format(formatter) +
                ", duration=" + duration +
                '}';
    }



    public Epic(String name, String info) {
        super(name, info);
    }

    public Epic(Task task) {
        super(task.getName(), task.getInfo(), task.getTaskStatus(), task.getStartTime(), task.getDuration());
    }




    public Epic updateEpicStatus(ArrayList<Subtask> subtaskArrayList) {
        if(subtaskArrayList.isEmpty()) {
            epicStatus = TaskStatus.NEW;
        } else {
           subTaskStatusReveal(subtaskArrayList);
        }
        return this;
    }



    private void subTaskStatusReveal(ArrayList<Subtask> subtaskArrayList) {
        ArrayList<TaskStatus> done = new ArrayList<>();
        ArrayList<TaskStatus> inProgress = new ArrayList<>();
        ArrayList<TaskStatus> n = new ArrayList<>();
        for (Subtask subtask : subtaskArrayList) {
            if(subTaskIds.contains(subtask.getId())) {
              if(subtask.getTaskStatus().equals(TaskStatus.IN_PROGRESS)) {
                  inProgress.add(TaskStatus.IN_PROGRESS);
              } else if (subtask.getTaskStatus().equals(TaskStatus.NEW)) {
                  n.add(TaskStatus.NEW);
              } else {
                  done.add(TaskStatus.DONE);
              }
            }
        }
        if(n.size() == subtaskArrayList.size()) {
            epicStatus = TaskStatus.NEW;
        } else if (!inProgress.isEmpty()) {
            epicStatus = TaskStatus.IN_PROGRESS;
        } else if (done.size() == subtaskArrayList.size()) {
            epicStatus = TaskStatus.DONE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return epicStatus == epic.epicStatus && Objects.equals(subTaskIds, epic.subTaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicStatus, subTaskIds);
    }
}
