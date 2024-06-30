package org.home;

import java.util.ArrayList;
import java.util.HashMap;


public class Epic extends Task {
   //private final ArrayList<Subtask> subtaskList = new ArrayList<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", info='" + getInfo() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + getTaskStatus() +
                '}';
    }

    public Epic(String name, String info, TaskStatus taskStatus) {
        super(name, info, taskStatus);
    }

    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void putTaskToSubtaskList(Subtask task) {
        subtaskList.put(task.getId(), task);
    }
}
