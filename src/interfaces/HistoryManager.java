package interfaces;

import entities.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    List getHistory();
    void add(Task task);
    void remove(Task task);
}
