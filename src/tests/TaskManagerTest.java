package tests;

import entities.EpicTask;
import entities.Status;
import entities.SubTask;
import entities.Task;
import interfaces.TaskManager;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;


abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    protected T emptyManager;

    protected Task testTask = new Task("Task", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15));
    protected EpicTask testEpic = new EpicTask("TestEpic", "Epic Testing");
    protected SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now().plus(Duration.ofDays(1)), Duration.ofHours(15));
    protected SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 0, Instant.now().plus(Duration.ofDays(2)), Duration.ofHours(15));


    @Test
    void showTasksListWhenStandardBehavior() {
        Collection expected = Arrays.asList(testTask);
        Collection real = manager.showTasksList();
        Assertions.assertIterableEquals(expected, real, "Задачи не совпадают");
        Assertions.assertNotNull(manager.showTasksList(), "Задачи не были добавлены");
    }

    @Test
    void showTasksListWhenEmpty() {
        Assertions.assertTrue(emptyManager.showTasksList().isEmpty());

    }

    @Test
    void showEpicTasksListStandardBehavior() {
        Collection expected = Arrays.asList(testEpic);
        Collection real = manager.showEpicTasksList();
        Assertions.assertIterableEquals(expected, real, "Задачи не совпадают");
        Assertions.assertNotNull(manager.showEpicTasksList(), "Задачи не были добавлены");
    }

    @Test
    void showEpicTasksListWhenEmpty() {
        Assertions.assertTrue(emptyManager.showEpicTasksList().isEmpty());
    }

    @Test
    void showSubTasksListStandardBehavior() {
        Collection expected = Arrays.asList(testSub1, testSub2);
        Collection real = manager.showSubTasksList();
        Assertions.assertIterableEquals(expected, real, "Задачи не совпадают");
        Assertions.assertNotNull(manager.showSubTasksList(), "Задачи не были добавлены");
    }

    @Test
    void showSubTasksListWhenEmpty() {
        Assertions.assertTrue(emptyManager.showSubTasksList().isEmpty());
    }

    @Test
    void deleteAllTasksStandardBehavior() {
        manager.deleteAllTasks();
        Assertions.assertTrue(manager.showTasksList().isEmpty(), "Задачи не были удалены");
    }

    @Test
    void deleteAllTasksWhenEmpty() {
        Assertions.assertTrue(emptyManager.showTasksList().isEmpty());
    }


    @Test
    void deleteAllEpicsStandardBehavior() {
        manager.deleteAllEpics();
        Assertions.assertTrue(manager.showEpicTasksList().isEmpty(), "Задачи не были удалены");
    }

    @Test
    void deleteAllEpicsWhenEmpty() {
        emptyManager.deleteAllEpics();
        Assertions.assertTrue(emptyManager.showEpicTasksList().isEmpty());
    }

    @Test
    void deleteAllSubTasksStandardBehavior() {
        manager.deleteAllSubTasks();
        Assertions.assertTrue(manager.showSubTasksList().isEmpty(), "Задачи не были удалены");
    }

    @Test
    void deleteAllSubTasksWhenEmpty() {
        manager.deleteAllSubTasks();
        Assertions.assertTrue(emptyManager.showSubTasksList().isEmpty());
    }

    @Test
    void getByIdStandardBehavior() {
        Assertions.assertEquals(testTask, manager.getById(1), "Возвращена некорректная задача");
    }

    @Test
    void getByIdWhenEmpty() {
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, () -> emptyManager.getById(1));
        Assertions.assertEquals("Некорректный Id", exception.getMessage());
    }

    @Test
    void getByIdWhenIdIncorrect() {
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> manager.deleteById(5));
        Assertions.assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void createTaskStandardBehavior() {
        Assertions.assertTrue(manager.showTasksList().contains(testTask), "Задача не создана");
        Assertions.assertEquals(testTask, manager.getById(1), "Id присвоен неверно");
    }

    @Test
    void createEpicTaskStandardBehavior() {
        Assertions.assertTrue(manager.showEpicTasksList().contains(testEpic), "Задача не создана");
        Assertions.assertEquals(testEpic, manager.getById(0), "Id присвоен неверно");
    }

    @Test
    void createSubTaskStandardBehavior() {
        Assertions.assertTrue(manager.showSubTasksList().contains(testSub1), "Задача не создана");
        Assertions.assertEquals(testSub1, manager.getById(2));
    }

    @Test
    void deleteByIdStandardBehavior() {
        manager.deleteById(1);
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> manager.getById(1));
        Assertions.assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void deleteByIdWhenEmpty() {
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> emptyManager.deleteById(1));
        Assertions.assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void deleteByIdWhenIdIncorrect() {
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> manager.deleteById(5));
        Assertions.assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void updateTaskStandardBehavior() {
        Task updatedTask = new Task(1, "Task", "Task updated", Status.NEW, manager.getById(testTask.getId()).getStartTime(), manager.getById(testTask.getId()).getDuration());
        manager.updateTask(updatedTask);
        Assertions.assertEquals(updatedTask, manager.getById(1));
    }

    @Test
    void updateTaskWhenEmpty() {
        Task updatedTask = new Task(1, "Task", "Task updated", Status.NEW, manager.getById(testTask.getId()).getStartTime(), manager.getById(testTask.getId()).getDuration());
        Assertions.assertThrows(IllegalStateException.class, () -> emptyManager.updateTask(updatedTask));
    }

    @Test
    void updateTaskWhenIdIncorrect() {
        Task updatedTask = new Task(5, "Task", "Task updated", Status.NEW, manager.getById(testTask.getId()).getStartTime(), manager.getById(testTask.getId()).getDuration());
        Assertions.assertThrows(IllegalStateException.class, () -> manager.updateTask(updatedTask));
    }

    @Test
    void updateEpicTaskStandardBehavior() {
        EpicTask updatedEpic = new EpicTask(0, "EpicTask", "EpicTask updated");
        manager.updateEpicTask(updatedEpic);
        Assertions.assertEquals(updatedEpic, manager.getById(0));
    }

    @Test
    void updateEpicTaskWhenEmpty() {
        EpicTask updatedEpic = new EpicTask(0, "EpicTask", "EpicTask updated");
        Assertions.assertThrows(IllegalStateException.class, () -> emptyManager.updateTask(updatedEpic));
    }

    @Test
    void updateEpicTaskWhenIdIncorrect() {
        EpicTask updatedEpic = new EpicTask(0, "EpicTask", "EpicTask updated");
        Assertions.assertThrows(IllegalStateException.class, () -> manager.updateTask(updatedEpic));
    }

    @Test
    void updateSubTaskStandardBehavior() {
        SubTask updatedSubTask = new SubTask(2, "SubTask1", "Sub Testing", Status.NEW, 0, manager.getById(testSub1.getId()).getStartTime(), manager.getById(testSub1.getId()).getDuration());
        manager.updateSubTask(updatedSubTask);
        Assertions.assertEquals(updatedSubTask, manager.getById(2));
    }

    @Test
    void updateSubTaskWhenEmpty() {
        SubTask updatedSubTask = new SubTask(2, "SubTask1", "Sub Testing", Status.NEW, 0, manager.getById(testSub1.getId()).getStartTime(), manager.getById(testSub1.getId()).getDuration());
        Assertions.assertThrows(IllegalStateException.class, () -> emptyManager.updateTask(updatedSubTask));
    }

    @Test
    void updateSubTaskWhenIdIncorrect() {
        SubTask updatedSubTask = new SubTask(2, "SubTask1", "Sub Testing", Status.NEW, 0, manager.getById(testSub1.getId()).getStartTime(), manager.getById(testSub1.getId()).getDuration());
        Assertions.assertThrows(IllegalStateException.class, () -> manager.updateTask(updatedSubTask));
    }

    @Test
    void showEpicsSubTasksStandardBehavior() {
        ArrayList<SubTask> expected = new ArrayList<>();
        expected.addAll(Arrays.asList(testSub1, testSub2));
        EpicTask epic = (EpicTask) manager.getById(0);
        Assertions.assertEquals(expected, manager.showEpicsSubTasks(epic));
    }

    @Test
    void getPrioritizedTasks(){
        int tasksSize = manager.showTasksList().size();
        int subTasksSize = manager.showSubTasksList().size();
        int prioritizedTasksSize = manager.getPrioritizedTasks().size();
        boolean moreTasks = tasksSize+subTasksSize > prioritizedTasksSize;
        boolean lessTasks = tasksSize+subTasksSize < prioritizedTasksSize;
        Assertions.assertFalse(lessTasks,"Не все задачи внесены в список по приоритету");
        Assertions.assertFalse(moreTasks,"В списке присутствуюи дублирующиеся задачи дублирующиеся задачи");


    }
}