package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


public class Epic  {

    private TaskStatus epicStatus = TaskStatus.NEW;

    private String name;
    private String info;
    private final Integer id = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);

    private final ArrayList<Integer> subTaskIds = new ArrayList<>();
    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public Integer getId() {
        return id;
    }

    public void setSubTaskIds(Integer id) {
        this.subTaskIds.add(id);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void cleanSubtaskIds() {
        subTaskIds.clear();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", id=" + id +
                ", taskStatus=" + epicStatus +
                '}';
    }

    public Epic(String name, String info) {
        this.name = name;
        this.info = info;
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
        if (!(o instanceof Epic epic)) return false;
        return epicStatus == epic.epicStatus && Objects.equals(getName(), epic.getName()) && Objects.equals(getInfo(), epic.getInfo()) && Objects.equals(getId(), epic.getId()) && Objects.equals(getSubTaskIds(), epic.getSubTaskIds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicStatus, getName(), getInfo(), getId(), getSubTaskIds());
    }
}
