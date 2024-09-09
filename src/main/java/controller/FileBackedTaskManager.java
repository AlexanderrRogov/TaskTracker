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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
        ArrayList<String[]> tempArr = Arrays.stream(task.toString().split(",")).map(t -> t.split("=")).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> formatArr = new ArrayList<>();
        formatArr.add(type);
        tempArr.stream().map(x -> x[1].replace("'", "").replace("}", "")).forEach(formatArr::add);
        return formatArr;
    }

    public void save(Task task) {
        File file = new File("src/main/resources/Storage/TaskStorage.csv");
        if (file.exists() && !file.isDirectory()) {
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
            writer.write("type, name, description, id, status, startTime, endTime, duration" + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private void writeToStorage(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Storage/TaskStorage.csv", true))) {
            String listString = taskToString(task).stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
            listString = convertDurationToString(task, listString);
            writer.write(listString + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private String convertDurationToString(Task task, String listString) {
        var duration = Long.toString(task.getDuration().toMinutes());
        var tempArr = listString.split(", ");
        tempArr[7] = duration;
        return Arrays.stream(tempArr).map(Object::toString).collect(Collectors.joining(", "));
    }

    private static HashMap<TaskType, ArrayList<? extends Task>> read() {
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<Task> tasks;
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Storage/TaskStorage.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                taskList.add(line);
            }
            taskList.remove(0);
            tasks = taskList.stream().map(FileBackedTaskManager::fromString).collect(Collectors.toCollection(ArrayList::new));
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
        tasks.forEach(task -> {
            if (task.getType().toUpperCase().equals(TaskType.SUBTASK.name())) {
                convertedTasksSubtask.add(new Subtask(task));
            }
            if (task.getType().toUpperCase().equals(TaskType.EPIC.name())) {
                convertedTasksEpic.add(new Epic(task));
            }
            if (task.getType().toUpperCase().equals(TaskType.TASK.name())) {
                convertedTasks.add(task);
            }
        });
        taskTypeArrayListHashMap.put(TaskType.EPIC, convertedTasksEpic);
        taskTypeArrayListHashMap.put(TaskType.TASK, convertedTasks);
        taskTypeArrayListHashMap.put(TaskType.SUBTASK, convertedTasksSubtask);
        return taskTypeArrayListHashMap;
    }

    public static Task fromString(String value) {
        var taskData = value.split(", ");
        return new Task(taskData[0], taskData[1], taskData[2], taskData[3], statusConverter(taskData[4]), localDateTimeFromString(taskData[5]), durationFromString(taskData[7]));
    }

    private static Duration durationFromString(String value) {
        long l = Long.parseLong(value);
        return Duration.ofMinutes(l);
    }

    private static LocalDateTime localDateTimeFromString(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
    }

    private static TaskStatus statusConverter(String status) {
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
        super.addEpic(epic);
        save(epic);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save(task);
    }

    @Override
    public Task deleteTask(Integer id) {
        save(super.deleteTask(id));
        return null;
    }

    @Override
    public Subtask deleteSubtask(Integer id) {
        save(super.deleteSubtask(id));
        return null;
    }

    @Override
    public Epic deleteEpic(Integer id) {
       save(super.deleteEpic(id));
       return null;
    }

    @Override
    public void updateEpicStatus(Integer epicId) {
        epics.put(epicId, epics.get(epicId).updateEpicStatus(getSubtasksForEpic(epicId)));
        save(epics.get(epicId));
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save(task);
    }

    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save(subtask);
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
