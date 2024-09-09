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

    Task deleteTask(Integer id);

    Subtask deleteSubtask(Integer id);

    Epic deleteEpic(Integer id);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    void updateEpicStatus(Integer epicId);

    List<Task> getHistory();
}
