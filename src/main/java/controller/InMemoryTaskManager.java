package controller;


import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
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

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        for(Epic epic : epics.values()) {
            if(epic.getSubTaskIds().contains(subtask.getId())) {
                updateEpicStatus(epic.getId());
            }
        }
    }
    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTask(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public Subtask getSubtask(int id) {
        historyManager.addTask(subtasks.get(id));
        return subtasks.get(id);
    }
    @Override
    public Epic getEpic(int id) {
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.clear();
    }

    @Override
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
    @Override
    public void deleteEpic(Integer id) {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(0);
        }
        subtasks.clear();
    }
    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void updateEpicStatus(Integer epicId) {
        epics.put(epicId, epics.get(epicId).updateEpicStatus(getSubtasksForEpic(epicId)));
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
