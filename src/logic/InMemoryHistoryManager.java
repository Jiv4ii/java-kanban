package logic;

import entities.Task;
import interfaces.HistoryManager;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {


    private final CustomLinkedList history;

    public InMemoryHistoryManager() {
        history = new CustomLinkedList();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public void remove(Task task) {
        history.removeNode(history.getNode(task));
    }
}


