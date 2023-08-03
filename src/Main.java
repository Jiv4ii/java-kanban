import logic.HttpTaskManager;
import HTTP.KVServer;
import entities.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args)  throws IOException{
        new KVServer().start();
       HttpTaskManager manager = new HttpTaskManager("http://localhost:8078");
         Task testTask = new Task("Task", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15));
         EpicTask testEpic = new EpicTask("TestEpic", "Epic Testing");
         SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 1, Instant.now().plus(Duration.ofDays(1)), Duration.ofHours(15));
         SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 1, Instant.now().plus(Duration.ofDays(2)), Duration.ofHours(15));
         manager.createTask(testTask);
         manager.createEpicTask(testEpic);
         manager.createSubTask(testSub1);
        System.out.println(manager.getHistory());
         HttpTaskManager manager1 =HttpTaskManager.loadFromServer("http://localhost:8078");
        System.out.println(manager1.showTasksList());
        System.out.println(manager1.showEpicTasksList().get(0).getEpicsSubTasks());
        System.out.println(manager1.showSubTasksList());
        System.out.println(manager1.getHistory());
        System.out.println(manager1.getPrioritizedTasks());






    }
}
