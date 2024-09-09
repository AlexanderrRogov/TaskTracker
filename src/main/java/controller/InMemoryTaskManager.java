package controller;


import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    List<Task> intersections = new ArrayList<>();

    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
                subtasks.values().forEach(subtask -> {
                    epics.get(id).getSubTaskIds().stream().filter(i -> subtask.getId().equals(i)).map(i -> subtask).forEach(epicSub::add);
                });
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
        if (IsIntersect(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
           intersections.forEach(x->System.out.println("Есть пересечения с" + x.getClass() + " ID " + x.getId()));
        }
    }

    @Override
    public Task createTask(Task task) {
        if (IsIntersect(task)) {
            var taskWithId = new Task(task);
            tasks.put(taskWithId.getId(), taskWithId);
            prioritizedTasks.add(task);
            return taskWithId;
        } else {
            intersections.forEach(x->System.out.println("Есть пересечения с" + x.getClass() + " ID " + x.getId()));
        }
        return null;
    }

    @Override
    public Subtask createSubtask(Task subtask) {
        if(IsIntersect(subtask)) {
            var subtaskWithId = new Subtask(subtask);
            prioritizedTasks.add(subtaskWithId);
            subtasks.put(subtaskWithId.getId(), subtaskWithId);
            epics.values().stream().filter(epic -> epic.getSubTaskIds().contains(subtaskWithId.getId())).forEach(epic -> {
                updateEpicStatus(epic.getId());
            });
            return subtaskWithId;
        } else {
            intersections.forEach(x->System.out.println("Есть пересечения с" + x.getClass() + " ID " + x.getId()));
        }
        return null;
    }

    @Override
    public Epic createEpic(Task epic) {
        if (IsIntersect(epic)) {
            var epicWithId = new Epic(epic);
            prioritizedTasks.add(epic);
            epics.put(epic.getId(), epicWithId);
            return epicWithId;
        } else {
            intersections.forEach(x->System.out.println("Есть пересечения с" + x.getClass() + " ID " + x.getId()));
        }
        return null;
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
    public Task deleteTask(Integer id) {
        prioritizedTasks.remove(tasks.get(id));
        return tasks.remove(id);
    }

    @Override
    public Subtask deleteSubtask(Integer id) {
        prioritizedTasks.remove(subtasks.get(id));
        for (Epic epic : epics.values()) {
            if (epic.getSubTaskIds().contains(id))
            {
                epic.getSubTaskIds().remove(id);
                updateEpicStatus(epic.getId());
            }
        }
        return subtasks.remove(id);
    }

    @Override
    public Epic deleteEpic(Integer id) {
        prioritizedTasks.remove(epics.get(id));
        return epics.remove(id);
    }

    @Override
    public void deleteTasks() {
        prioritizedTasks.removeAll(tasks.values());
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        prioritizedTasks.removeAll(subtasks.values());
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }
    @Override
    public void deleteEpics() {
        prioritizedTasks.removeAll(epics.values());
        prioritizedTasks.removeAll(subtasks.values());
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void updateEpicStatus(Integer epicId) {
        prioritizedTasks.remove(epics.get(epicId));
        prioritizedTasks.add(epics.put(epicId, epics.get(epicId).updateEpicStatus(getSubtasksForEpic(epicId))));
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
      return new ArrayList<>(prioritizedTasks);

    }

    public Task updateTask(Task task) {
        if(IsIntersect(task)) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            prioritizedTasks.add(task);
            tasks.put(task.getId(), task);
            return task;
        } else {
            intersections.forEach(x->System.out.println("Есть пересечения с" + x.getClass() + " ID " + x.getId()));
        }
        return null;
    }

    public void updateSubtask(Subtask subtask) {
        if(IsIntersect(subtask)) {
            prioritizedTasks.remove(subtasks.get(subtask.getId()));
            prioritizedTasks.add(subtask);
            subtasks.put(subtask.getId(), subtask);
            epics.values().stream().filter(epic -> epic.getSubTaskIds().contains(subtask.getId())).forEach(epic -> {
                updateEpicStatus(epic.getId());
            });
        } else {
            intersections.forEach(x->System.out.println("Есть пересечения с" + x.getClass() + " ID " + x.getId()));
        }
    }

    public Boolean IsIntersect(Task task) {
      intersections = getPrioritizedTasks().stream().filter(x ->
                 x.getStartTime().isAfter(task.getStartTime()) && x.getEndTime().isBefore(task.getEndTime()) && !x.getId().equals(task.getId())
              || x.getStartTime().equals(task.getStartTime()) && x.getEndTime().equals(task.getEndTime()) && !x.getId().equals(task.getId())
              || x.getStartTime().isBefore(task.getStartTime()) && x.getEndTime().isAfter(task.getStartTime()) && !x.getId().equals(task.getId())
              || x.getStartTime().isBefore(task.getStartTime()) && x.getEndTime().isBefore(task.getEndTime())
                         && x.getEndTime().isAfter(task.getStartTime()) && !x.getId().equals(task.getId())
              || x.getStartTime().isAfter(task.getStartTime()) && x.getEndTime().isAfter(task.getEndTime())
                         && x.getStartTime().isBefore(task.getEndTime()) && !x.getId().equals(task.getId())
              || x.getStartTime().equals(task.getStartTime()) && !x.getId().equals(task.getId())
              || x.getEndTime().equals(task.getEndTime()) && !x.getId().equals(task.getId())).toList();
        return intersections.isEmpty();
    }
}
