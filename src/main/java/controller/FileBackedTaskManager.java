package controller;

import model.Epic;
import model.ManagerSaveException;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import model.TaskType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {


    public FileBackedTaskManager() {

    }

    public static HashMap<TaskType, ArrayList<? extends Task>> loadFromFile() {
        return read();
    }

    private ArrayList<String> taskToString(Task task) {
        var type = task.toString().substring(0, task.toString().indexOf('{'));
        ArrayList<String[]> tempArr = new ArrayList<>();
        for (var t : task.toString().split(",")) {
            tempArr.add(t.split("="));
        }
        ArrayList<String> formatArr = new ArrayList<>();
        formatArr.add(type);
        for (var x : tempArr) {
            formatArr.add(x[1].replace("\'", "").replace("}", ""));
        }
        return formatArr;
    }

    public void save(Task task) {
        File f = new File("src/main/resources/Storage/TaskStorage.csv");
        if (f.exists() && !f.isDirectory()) {
            writeToStorage(task);
        } else {
            Path source = Paths.get("src/main/resources");
            Path newFolder = Paths.get(source.toAbsolutePath() + "/Storage");
            try {
                Files.createDirectories(newFolder);
                Files.createFile(Path.of(newFolder + "/TaskStorage.csv"));
                writeColumnNames();
                writeToStorage(task);
            } catch (IOException e) {
                throw new ManagerSaveException(e.getMessage());
            }
        }
    }

    private void writeColumnNames() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Storage/TaskStorage.csv", true))) {
            writer.write("type, name, description, id, status" + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private void writeToStorage(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Storage/TaskStorage.csv", true))) {
            String listString = taskToString(task).stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
            writer.write(listString + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private static HashMap<TaskType, ArrayList<? extends Task>> read() {
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<Task> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Storage/TaskStorage.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                taskList.add(line);
            }
            taskList.remove(0);
            for (var str : taskList) {
                tasks.add(fromString(str));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }

        return typeConverter(tasks);
    }

    private static HashMap<TaskType, ArrayList<? extends Task>> typeConverter(ArrayList<Task> tasks) {
        ArrayList<Epic> convertedTasksEpic = new ArrayList<>();
        ArrayList<Subtask> convertedTasksSubtask = new ArrayList<>();
        ArrayList<Task> convertedTasks = new ArrayList<>();
        HashMap<TaskType, ArrayList<? extends Task>> taskTypeArrayListHashMap = new HashMap<>();
        for (var task : tasks) {
            if(task.getType().toUpperCase().equals(TaskType.SUBTASK.name())) {
                convertedTasksSubtask.add(new Subtask(task));
            }
            if(task.getType().toUpperCase().equals(TaskType.EPIC.name())) {
                convertedTasksEpic.add(new Epic(task));
            }
            if(task.getType().toUpperCase().equals(TaskType.TASK.name())) {
                convertedTasks.add(task);
            }
        }
        taskTypeArrayListHashMap.put(TaskType.EPIC, convertedTasksEpic);
        taskTypeArrayListHashMap.put(TaskType.TASK, convertedTasks);
        taskTypeArrayListHashMap.put(TaskType.SUBTASK, convertedTasksSubtask);
        return taskTypeArrayListHashMap;
    }

    public static Task fromString(String value) {
        var taskData = value.split(", ");
        return new Task(taskData[0], taskData[1], taskData[2], taskData[3], converter(taskData[4]));
    }

    private static TaskStatus converter(String status) {
        if (TaskStatus.NEW.toString().equals(status)) {
            return TaskStatus.NEW;
        }
        if (TaskStatus.IN_PROGRESS.toString().equals(status)) {
            return TaskStatus.IN_PROGRESS;
        }
        return TaskStatus.DONE;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save(subtask);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        save(epic);
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
        save(task);
    }

    @Override
    public void deleteTask(Integer id) {
        Task deletedTask = tasks.remove(id);
        save(deletedTask);
    }

    @Override
    public void deleteSubtask(Integer id) {
        var deletedSubTask = subtasks.remove(id);
        save(deletedSubTask);
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
       var deletedEpic = epics.remove(id);
       save(deletedEpic);
    }

    @Override
    public void updateEpicStatus(Integer epicId) {
        epics.put(epicId, epics.get(epicId).updateEpicStatus(getSubtasksForEpic(epicId)));
        save(epics.get(epicId));
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicCollection =  new ArrayList<>(epics.values());
        for (Epic epic : epicCollection) {
            save(epic);
        }
        return epicCollection;
    }

    @Override
    public Task getTask(int id) {
        var task = tasks.get(id);
        historyManager.addTask(task);
        save(task);
        return task;
    }
    @Override
    public Subtask getSubtask(int id) {
        var subtask = subtasks.get(id);
        historyManager.addTask(subtask);
        save(subtask);
        return subtask;
    }
    @Override
    public Epic getEpic(int id) {
        var epic = epics.get(id);
        historyManager.addTask(epic);
        save(epic);
        return epic;
    }
}
