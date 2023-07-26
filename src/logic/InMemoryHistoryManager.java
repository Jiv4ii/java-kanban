package logic;

import entities.Task;
import interfaces.HistoryManager;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {


    private final CustomLinkedList history = new CustomLinkedList();

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public void remove(Task task) {
        if (!getHistory().contains(task)) {
            throw new IllegalStateException("История не содержит данной задачи");
        }
        history.removeNode(history.getNode(task));
    }

}


