import entities.EpicTask;
import entities.SubTask;
import entities.Task;
import logic.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("Сделал Таск", "Сделал, сделал",  "Done");
        Task task2 = new Task("Сделать Таск", "Сделать, сделать",  "NEW");

        EpicTask epicTask = new EpicTask("Сделать ЕпикТаск", "Начать, Закончить",  "NEW");
        SubTask subTask = new SubTask(2,"Начать ЕпикТаск", "Начать, начать", "DONE");
        SubTask subTask2 = new SubTask(2,"Закончить ЕпикТаск", "Закончить, Закончить", "IN_PROGRESS");

        EpicTask epicTask2 = new EpicTask("Проявление радости", "Big thing",  "NEW");
        SubTask subTask3 = new SubTask(5, "SIUUUUUU", "Вдох, SIUUUUU", "NEW");

        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);
        taskManager.createEpicTask(epicTask2);
        taskManager.createSubTask(subTask3);

        System.out.println(taskManager.showTasksList().toString());
        System.out.println(taskManager.showEpicTasksList().toString());
        System.out.println(taskManager.showSubTasksList().toString());
        System.out.println();

        task2 = new Task("Сделать Таск", "Сделать, сделать",  "IN_PROGRESS", 1);
        taskManager.updateTask(task2);
        subTask2 = new SubTask(2, "Закончить ЕпикТаск", "Закончить, Закончить", "DONE",4);
        taskManager.updateSubTask(subTask2);

        System.out.println(taskManager.showTasksList().toString());
        System.out.println(taskManager.showEpicTasksList().toString());
        System.out.println(taskManager.showSubTasksList().toString());
        System.out.println();

        taskManager.deleteById(2);

        System.out.println(taskManager.showTasksList().toString());
        System.out.println(taskManager.showEpicTasksList().toString());
        System.out.println(taskManager.showSubTasksList().toString());
        System.out.println();

        System.out.println(taskManager.getById(5));



    }
}
