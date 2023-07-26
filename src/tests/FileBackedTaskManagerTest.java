package tests;

import entities.EpicTask;
import entities.SubTask;
import entities.Task;

import logic.FileBackedTasksManager;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;


public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    String path = "C:\\Users\\EV\\Desktop\\pro\\moe.csv";
    @BeforeEach
    void create() {
        manager = new FileBackedTasksManager(path);
        emptyManager = new FileBackedTasksManager("C:\\Users\\EV\\Desktop\\pro\\moe2.csv");
        manager.createEpicTask(testEpic);
        manager.createTask(testTask);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
    }

    @Test
    void FileBackedShouldReturnSameTaskById(){
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(path);
        Task taskFromBacked = managerFromFile.getById(1);
        EpicTask epicFromBacked = (EpicTask) managerFromFile.getById(0);
        SubTask subFromBacked = (SubTask) managerFromFile.getById(2);
        Assertions.assertEquals(testTask,taskFromBacked,"Восстановление Задач из файла некорректно");
        Assertions.assertEquals(testEpic,epicFromBacked,"Восстановление Эпиков из файла некорректно");
        Assertions.assertEquals(testSub1,subFromBacked,"Восстановление Подзадач из файла некорректно");
    }

    @Test
    void FileBackedShouldReturnSameTaskLists(){
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(path);
        Collection<Task> tasksFromBacked = managerFromFile.showTasksList();
        Collection<EpicTask> epicsFromBacked =  managerFromFile.showEpicTasksList();
        Collection<SubTask> subsFromBacked =  managerFromFile.showSubTasksList();
        Assertions.assertIterableEquals(manager.showTasksList(),tasksFromBacked,"Восстановление списка Задач из файла некорректно");
        Assertions.assertIterableEquals(manager.showEpicTasksList(),epicsFromBacked,"Восстановление списка Эпиков из файла некорректно");
        Assertions.assertIterableEquals(manager.showSubTasksList(),subsFromBacked,"Восстановление списка Подзадач из файла некорректно");
    }

    @Test
    void FileBackedShouldReturnMessageWhenManagerIsEmpty(){
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubTasks();
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> FileBackedTasksManager.loadFromFile("C:\\Users\\EV\\Desktop\\pro\\moe2.csv"));
        Assertions.assertEquals("Файл пуст", exception.getMessage());
    }

    @Test
    void FileBackedShouldReturnEmptyHistoryWithoutGetById(){
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(path);
        List<Task> historyFromFile = managerFromFile.getHistoryManager().getHistory();
        Assertions.assertTrue(historyFromFile.isEmpty(),"Без вызова задач история должна быть пуста");
    }

    @Test
    void FileBackedShouldReturnHistory(){
        manager.getById(0);
        manager.getById(1);
        List<Task> expectedHistory = List.of(manager.getById(0),manager.getById(1));

        FileBackedTasksManager managerFromFile = FileBackedTasksManager.loadFromFile(path);
        List<Task> historyFromFile = managerFromFile.getHistoryManager().getHistory();

        Assertions.assertEquals(expectedHistory,historyFromFile,"Без вызова задач история должна быть пуста");
    }

}
