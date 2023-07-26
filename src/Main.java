import entities.EpicTask;
import entities.Status;
import entities.SubTask;
import entities.Task;
import interfaces.TaskManager;
import logic.*;

import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Сделал Таск", "Сделал сделал",  Status.DONE, Instant.now().minus(Duration.ofDays(2)), Duration.ofDays(2));
        Task task2 = new Task("Сделать Таск", "Сделат сделать",  Status.NEW, Instant.now(), Duration.ofDays(2));
        System.out.println(task);



        EpicTask epicTask = new EpicTask("Сделать ЕпикТаск", "Начать Закончить");
        SubTask subTask1 = new SubTask("Начать ЕпикТаск", "Начать начать", Status.DONE, 2, Instant.now().plus(Duration.ofDays(3)), Duration.ofDays(2));
        SubTask subTask2 = new SubTask("Начать ЕпикТаск", "Начать начать", Status.DONE, 2, Instant.now().minus(Duration.ofDays(20)), Duration.ofDays(7));
        System.out.println(epicTask);



        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        System.out.println(taskManager.showEpicTasksList());
        System.out.println(taskManager.getPrioritizedTasks());




        taskManager.getById(0);
        taskManager.getById(2);
        taskManager.getById(1);

        taskManager.deleteById(0);

        System.out.println(taskManager.showTasksList().toString());
        System.out.println(taskManager.showEpicTasksList().toString());
        System.out.println(taskManager.showSubTasksList().toString());
        System.out.println(taskManager.getHistoryManager().getHistory());
        System.out.println();


        FileBackedTasksManager back = new FileBackedTasksManager("C:\\Users\\EV\\Desktop\\pro\\moe.csv");
        back.createTask(task);
        back.createTask(task2);
        back.createEpicTask(epicTask);
        back.createSubTask(subTask1);

        back.getById(0);
        back.getById(2);
        back.getById(1);

        System.out.println(back.showTasksList());
        System.out.println(back.showEpicTasksList());
        System.out.println(back.showSubTasksList());
        System.out.println(back.getHistoryManager().getHistory());
        System.out.println();

        FileBackedTasksManager newback = FileBackedTasksManager.loadFromFile("C:\\Users\\EV\\Desktop\\pro\\moe.csv");
        System.out.println(newback.showTasksList());
        System.out.println(newback.showEpicTasksList());
        System.out.println(newback.showSubTasksList());
        System.out.println(newback.getHistoryManager().getHistory());
        System.out.println();

        String path = "C:\\Users\\EV\\Desktop\\pro\\moe.csv";

        FileBackedTasksManager manager = new FileBackedTasksManager(path);
/*      manager.createEpicTask(new EpicTask("TestEpic", "Epic Testing"));
        manager.createTask(new Task("Task", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15)));
        manager.createSubTask(new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now().plus(Duration.ofDays(1)), Duration.ofHours(15)));
        manager.getById(1);
        manager.getById(2);*/

        /*Task task = new Task("Сделал Таск", "Сделал сделал",  Status.DONE, Instant.now().minus(Duration.ofDays(2)), Duration.ofDays(2));
        Task task2 = new Task("Сделать Таск", "Сделат сделать",  Status.NEW, Instant.now(), Duration.ofDays(2));
        System.out.println(task);



        EpicTask epicTask = new EpicTask("Сделать ЕпикТаск", "Начать Закончить");
        SubTask subTask1 = new SubTask("Начать ЕпикТаск", "Начать начать", Status.DONE, 2, Instant.now().plus(Duration.ofDays(3)), Duration.ofDays(2));
        SubTask subTask2 = new SubTask("Начать ЕпикТаск", "Начать начать", Status.DONE, 2, Instant.now().minus(Duration.ofDays(20)), Duration.ofDays(7));

   */    /* manager.createTask(task);
        manager.createTask(task2);
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask1);

        manager.getById(0);
        manager.getById(2);
        manager.getById(1);*/
        manager.createTask(new Task("Task", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15)));
        manager.createEpicTask(new EpicTask("TestEpic", "Epic Testing"));
        manager.createSubTask(new SubTask("SubTask1", "Sub Testing", Status.NEW, 1, Instant.now().plus(Duration.ofDays(1)), Duration.ofHours(15)));
        manager.getById(0);

        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile("C:\\Users\\EV\\Desktop\\pro\\moe.csv");

        System.out.println(managerFromFile.showTasksList());
        System.out.println(managerFromFile.showEpicTasksList());
        System.out.println(managerFromFile.showSubTasksList());

        System.out.println(manager.showEpicTasksList());
    }
}
