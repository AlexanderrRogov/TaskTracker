package util;

import controller.HistoryManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;
import controller.TaskManager;
import model.Subtask;
import model.TaskStatus;

public class Managers {

    public static HistoryManager getDefaultHistory() { return new InMemoryHistoryManager();}
    public static  <T extends TaskManager> T getDefault() {
        return (T) new InMemoryTaskManager();
    }
}
