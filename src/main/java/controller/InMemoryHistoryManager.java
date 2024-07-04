package controller;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyList = new ArrayList<>(10);
    ArrayList<Task> historyCopy = new ArrayList<>();
    static int index = 0;
    public void updateHistory(Task task) {
        if(historyList.size() != 10) {
            historyList.add(task);
        } else {
            historyList.remove(index);
            historyList.add(index, task);
            index++;
            if(index == 10) {
                index = 0;
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
