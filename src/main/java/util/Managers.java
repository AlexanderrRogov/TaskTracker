package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.FileBackedTaskManager;
import controller.HistoryManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;
import controller.TaskManager;
import model.Task;


public class Managers {

    public static HistoryManager getDefaultHistory() { return new InMemoryHistoryManager();}
    public static  <T extends TaskManager> T getDefault() {
        return (T) new InMemoryTaskManager();
    }
    public static  <T extends TaskManager> T getFileBackedTaskManager() {
        return (T) new FileBackedTaskManager();
    }

    private static final Gson gson  =  new GsonBuilder().registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(Task.class, new TaskSerializer())
            .setPrettyPrinting().create();

    public static Gson getGson() {
        return gson;
    }
}
