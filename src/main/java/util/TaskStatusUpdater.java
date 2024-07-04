package util;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

public class TaskStatusUpdater {
    public Task updateTaskStatus(Task task, TaskStatus taskStatus) {
        if(!(task instanceof Epic)) {
            if(task instanceof Subtask) {
                return new Subtask(task.getName(), task.getInfo(), taskStatus);
            } else {
                return new Task(task.getName(), task.getInfo(), taskStatus);
            }
        }
        return null;
    }
}
