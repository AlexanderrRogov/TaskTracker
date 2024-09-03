package util;

import controller.FileBackedTaskManager;
import controller.HistoryManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;
import controller.TaskManager;

public class Managers {

    public static HistoryManager getDefaultHistory() { return new InMemoryHistoryManager();}
    public static  <T extends TaskManager> T getDefault() {
        return (T) new InMemoryTaskManager();
    }
    public static  <T extends TaskManager> T getFileBackedTaskManager() {
        return (T) new FileBackedTaskManager();
    }
}
