package logic;

import entities.InMemoryHistoryManager;
import entities.InMemoryTaskManager;

public abstract class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public  static HistoryManager getHistoryDefault(){
        return new InMemoryHistoryManager();
    }

}
