package model;

import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {

    private TaskStatus epicStatus = TaskStatus.NEW;

    private final ArrayList<Integer> subTaskIds = new ArrayList<>();



    public void setSubTaskIds(Integer id) {
        this.subTaskIds.add(id);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void cleanSubtaskIds() {
        subTaskIds.clear();
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", info='" + getInfo() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + epicStatus +
                '}';
    }

    public Epic(String name, String info) {
        super(name, info);
    }

    public Epic(Task task) {
        super(task.getName(), task.getInfo());
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
