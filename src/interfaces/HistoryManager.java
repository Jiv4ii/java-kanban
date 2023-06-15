package interfaces;

import entities.Task;
import logic.CustomLinkedList;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();
    void add(Task task);
    void remove(Task task);
}
