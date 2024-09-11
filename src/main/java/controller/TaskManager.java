package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    Task updateTask(Task task);

    ArrayList<Subtask> getSubtasks();

    ArrayList<Subtask> getSubtasksForEpic(Integer id);

    void addTask(Task task);

    void updateSubtask(Task subtask);

    Subtask createSubtask(Task subtask);

    Epic createEpic(Task epic);

    ArrayList<Epic> getEpics();

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    Task deleteTask(Integer id);

    Subtask deleteSubtask(Integer id);

    Epic deleteEpic(Integer id);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    void updateEpicStatus(Integer epicId);

    List<Task> getHistory();

    Task createTask(Task task);

    List<Task> getPrioritizedTasks();

    boolean hasIntersection(Task task);
}
