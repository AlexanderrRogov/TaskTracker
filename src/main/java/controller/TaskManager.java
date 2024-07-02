package controller;


import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private Integer subtaskId;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> tasks = new ArrayList<>(subtasks.values());
        return tasks;
    }

    public ArrayList<Subtask> getSubtasksForEpic(Integer id) {
        ArrayList<Subtask> epicSub = new ArrayList<>();
        if(id != 0) {
            if (!epics.get(id).getSubTaskIds().isEmpty()) {
                for (Subtask subtask : subtasks.values()) {

                    for (Integer i : epics.get(id).getSubTaskIds()) {
                        if (subtask.getId().equals(i)) {
                            epicSub.add(subtask);
                        }
                    }
                }
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        return epicSub;
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        for(Epic epic : epics.values()) {
            if(epic.getSubTaskIds().contains(subtask.getId())) {
                updateEpicStatus(epic.getId());
            }
        }
    }
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> tasks = new ArrayList<>(epics.values());
        return tasks;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void deleteTask(Integer id) {
        tasks.clear();
    }
    public void deleteSubtask(Integer id) {
        subtasks.remove(id);
        for (Epic epic : epics.values()) {
            if (epic.getSubTaskIds().contains(id))
            {
                epic.getSubTaskIds().remove(id);
                updateEpicStatus(epic.getId());
            }
        }
    }
    public void deleteEpic(Integer id) {
        epics.clear();
        subtasks.clear();
    }

    public void deleteTasks() {
        tasks.clear();
    }
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(0);
        }
        subtasks.clear();
    }
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    private void updateEpicStatus(Integer epicId) {
        epics.put(epicId, epics.get(epicId).updateEpicStatus(getSubtasksForEpic(epicId)));
    }
}
