package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Subtask> getSubtasksForEpic(Integer id);

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    ArrayList<Epic> getEpics();

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    void deleteTask(Integer id);

    void deleteSubtask(Integer id);

    void deleteEpic(Integer id);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    void updateEpicStatus(Integer epicId);

    List<Task> getHistory();
}
