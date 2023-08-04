package tests;

import entities.EpicTask;
import entities.SubTask;
import entities.Task;

import logic.FileBackedTasksManager;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.List;


public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    String currentDirectory = System.getProperty("user.dir");

    // Указываем имя файла
    String fileName = "bebra.css";

    String path = currentDirectory + File.separator + fileName;
    @BeforeEach
    void create() {
        manager = new FileBackedTasksManager(path);
        emptyManager = new FileBackedTasksManager(currentDirectory + File.separator + "bebra2.css");
        manager.createEpicTask(testEpic);
        manager.createTask(testTask);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
    }

    @Test
    void shouldReturnSameTaskById(){
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.load(path);
        Task taskFromBacked = managerFromFile.getById(1);
        EpicTask epicFromBacked = (EpicTask) managerFromFile.getById(0);
        SubTask subFromBacked = (SubTask) managerFromFile.getById(2);
        Assertions.assertEquals(testTask,taskFromBacked,"Восстановление Задач из файла некорректно");
        Assertions.assertEquals(testEpic,epicFromBacked,"Восстановление Эпиков из файла некорректно");
        Assertions.assertEquals(testSub1,subFromBacked,"Восстановление Подзадач из файла некорректно");
    }

    @Test
    void shouldReturnSameTaskLists(){
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.load(path);
        Collection<Task> tasksFromBacked = managerFromFile.showTasksList();
        Collection<EpicTask> epicsFromBacked =  managerFromFile.showEpicTasksList();
        Collection<SubTask> subsFromBacked =  managerFromFile.showSubTasksList();
        Assertions.assertIterableEquals(manager.showTasksList(),tasksFromBacked,"Восстановление списка Задач из файла некорректно");
        Assertions.assertIterableEquals(manager.showEpicTasksList(),epicsFromBacked,"Восстановление списка Эпиков из файла некорректно");
        Assertions.assertIterableEquals(manager.showSubTasksList(),subsFromBacked,"Восстановление списка Подзадач из файла некорректно");
    }

    @Test
    void shouldReturnMessageWhenManagerIsEmpty(){
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubTasks();
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> FileBackedTasksManager.load("bebra2.css"));
        Assertions.assertEquals("Файл пуст", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyHistoryWithoutGetById(){
        FileBackedTasksManager managerFromFile = FileBackedTasksManager.load(path);
        List<Task> historyFromFile = managerFromFile.getHistory();
        Assertions.assertTrue(historyFromFile.isEmpty(),"Без вызова задач история должна быть пуста");
    }

    @Test
    void shouldReturnHistory(){
        manager.getById(0);
        manager.getById(1);
        List<Task> expectedHistory = List.of(manager.getById(0),manager.getById(1));

        FileBackedTasksManager managerFromFile = FileBackedTasksManager.load(path);
        List<Task> historyFromFile = managerFromFile.getHistory();

        Assertions.assertEquals(expectedHistory,historyFromFile,"Без вызова задач история должна быть пуста");
    }

}
