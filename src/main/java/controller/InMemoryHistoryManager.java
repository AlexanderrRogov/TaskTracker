package controller;

import model.Task;
import model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final DoublyLinkedList<Task> doublyLinkedList = new DoublyLinkedList<>();
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        if(historyMap.isEmpty()){
            doublyLinkedList.insertAtBeginning(task);
            historyMap.put(task.getId(), doublyLinkedList.head);
        } else {
            if (historyMap.get(task.getId()) != null) {
                Node<Task> node = historyMap.get(task.getId());
                removeNode(node);
                remove(task.getId());
            }
            doublyLinkedList.insertAtEnd(task);
            historyMap.put(task.getId(), doublyLinkedList.tail);
        }
    }

    @Override
    public List<Task> getHistory() {
        return doublyLinkedList.getTasks();
    }

    public void remove(int id) {
        historyMap.remove(id);
    }

    public void removeNode(Node<Task> node) {
        doublyLinkedList.deleteNode(node);
    }

    public static class DoublyLinkedList<T>  {

        Node<T> head;
        Node<T> tail;

        public DoublyLinkedList()
        {
            this.head = null;
            this.tail = null;
        }

        public void insertAtBeginning(T data)
        {
            Node<T> temp = new Node<>(data);
            if (head == null) {
                head = temp;
                tail = temp;
            }
            else {
                temp.next = head;
                head.prev = temp;
                head = temp;
            }
        }

        public void insertAtEnd(T data)
        {
            Node<T> temp = new Node<>(data);
            if (tail == null) {
                head = temp;
            }
            else {
                tail.next = temp;
                temp.prev = tail;
            }
            tail = temp;
        }

        private void deleteNode(Node<T> node) {
            if (node != null) {
                if (node.prev != null)
                    node.prev.next = node.next;
                else
                    head = node.next;
                if (node.next != null)
                    node.next.prev = node.prev;
                else
                    tail = node.prev;
            }
        }

        public ArrayList<T> getTasks() {
            ArrayList<T> tasks = new ArrayList<>();
            Node<T> node = head;
            while (node != null ) {
                tasks.add(node.data);
                node = node.next;
            }
            return tasks;
        }
    }

}





