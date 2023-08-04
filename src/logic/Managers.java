package logic;

import interfaces.HistoryManager;
import interfaces.TaskManager;

public  class Managers {
    private Managers(){}

    public static TaskManager getDefault(String path){
        return new HttpTaskManager(path);
    }

    public  static HistoryManager getHistory(){
        return new InMemoryHistoryManager();
    }


}
