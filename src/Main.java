import entities.EpicTask;
import entities.Status;
import entities.SubTask;
import entities.Task;
import interfaces.TaskManager;
import logic.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Сделал Таск", "Сделал, сделал",  Status.DONE);
        Task task2 = new Task("Сделать Таск", "Сделать, сделать",  Status.NEW);

        EpicTask epicTask = new EpicTask("Сделать ЕпикТаск", "Начать, Закончить",  Status.NEW);
        SubTask subTask = new SubTask(2,"Начать ЕпикТаск", "Начать, начать", Status.DONE);
        SubTask subTask2 = new SubTask(2,"Закончить ЕпикТаск", "Закончить, Закончить", Status.IN_PROGRESS);
        SubTask subTask3 = new SubTask(2, "SIUUUUUU", "Вдох, SIUUUUU", Status.NEW);

        EpicTask epicTask2 = new EpicTask("Проявление радости", "Big thing",  Status.NEW);


        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createEpicTask(epicTask2);


        System.out.println(taskManager.showTasksList().toString());
        System.out.println(taskManager.showEpicTasksList().toString());
        System.out.println(taskManager.showSubTasksList().toString());
        System.out.println();



        taskManager.getById(5);
        taskManager.getById(2);
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.getById(1);
        taskManager.getById(3);
        taskManager.getById(4);
        taskManager.getById(1);
/*        taskManager.getById(0);
        taskManager.getById(3);*/
        System.out.println(taskManager.getHistoryManager().getHistory());
        taskManager.deleteById(2);
        System.out.println(taskManager.getHistoryManager().getHistory());
    }
}
