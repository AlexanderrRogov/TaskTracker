package org.home;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TaskManager {
    private HashMap<Integer, Task> taskList = new HashMap<>();

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public Task getTaskById(Integer id) {
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        } else {
            for (Task task : taskList.values()) {
                if (task instanceof Epic) {
                    for (Integer key : ((Epic) task).getSubtaskList().keySet()) {
                        if (key.equals(id)) {
                            return ((Epic) task).getSubtaskList().get(id);
                        }
                    }
                }
            }
        }
        System.out.println("Задача с ID " + id + " не найдена");
        return null;
    }

    public void removeAllTasks() {
        taskList.clear();
    }

    public void addTask(Task task) {
        if (taskList.containsKey(task.getId())) {
            System.out.println("Такая задача уже существует");
        } else {
            if (task instanceof Subtask) {
                Epic epic = (Epic) taskList.get(((Subtask) task).getEpicId());
                epic.putTaskToSubtaskList((Subtask) task);
                checkEpicStatus(epic);
            } else {
                taskList.put(task.getId(), task);
            }
        }
    }

    public void showAllTasks() {
        for (Task task : taskList.values()) {
            System.out.println(task.toString());
            if (task instanceof Epic) {
                for (Subtask subtask : ((Epic) task).getSubtaskList().values()) {
                    System.out.println(subtask.toString());
                }
            }
        }
    }

    public void updateTask(Task task) {
        if(taskList.containsKey(task.getId())) {
            taskList.put(task.getId(), task);
            System.out.println(task.getClass().getName() + " c ID "+ task.getId()+ " обновлён"  );
        } else {
            if(task instanceof Subtask) {
            for (Task t : taskList.values()) {
                if(t instanceof Epic) {
                    if(Objects.equals(t.getId(), ((Subtask) task).getEpicId())) {
                        ArrayList<Subtask> subtaskArrayListNew = new ArrayList<>();
                        for (Integer key : ((Epic) t).getSubtaskList().keySet()) {
                            if (key.equals(task.getId())) {
                                subtaskArrayListNew.add((Subtask) task);
                            }
                        }
                        if(!subtaskArrayListNew.isEmpty()) {
                            ((Epic) t).getSubtaskList().put(task.getId(), subtaskArrayListNew.get(0));
                            checkEpicStatus((Epic) t);
                            taskList.put(t.getId(), t);
                            System.out.println("Новый SUBTASK добавлен в EPIC " + t.getId());
                        }
                    }
                    }
                }
            }
        }
    }

    public void removeById(Integer id) {
        if(taskList.containsKey(id)) {
            taskList.remove(id);
            System.out.println("Task с ID " + id + " удалён");
        } else {
            for (Task task : taskList.values()) {
                if(task instanceof Epic) {
                    for(Integer key : ((Epic) task).getSubtaskList().keySet()) {
                        if (key.equals(id)) {
                            ((Epic) task).getSubtaskList().remove(id);
                            checkEpicStatus((Epic) task);
                            System.out.println("Task с ID " + id + " удалён");
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Subtask> getSubtaskListById(Integer id) {
       if(taskList.containsKey(id)) {
           if(taskList.get(id) instanceof Epic) {
               return new ArrayList<>(((Epic) taskList.get(id)).getSubtaskList().values());
           } else {
               System.out.println("В Epic нет Task с ID " + id);
               return null;
           }
       } else {
           System.out.println("В списке нет EPIC с ID " + id);
           return null;
       }
    }

    private void checkEpicStatus(Epic epic) {
        if(!epic.getTaskStatus().equals(TaskStatus.DONE)) {
            ArrayList<TaskStatus> taskStatuses = new ArrayList<>();
            for (Subtask subtask : epic.getSubtaskList().values()) {
                if(subtask.getTaskStatus().equals(TaskStatus.DONE)) {
                    epic.setTaskStatus(TaskStatus.DONE);
                } else if (subtask.getTaskStatus().equals(TaskStatus.IN_PROGRESS)) {
                    epic.setTaskStatus(TaskStatus.IN_PROGRESS);
                    break;
                } else if (epic.getSubtaskList().isEmpty()) {
                    epic.setTaskStatus(TaskStatus.NEW);
                } else if (subtask.getTaskStatus().equals(TaskStatus.NEW)) {
                    epic.setTaskStatus(TaskStatus.NEW);
                }
            }
        }
        taskList.put(epic.getId(), epic);
    }
}
