package tests;

import entities.EpicTask;
import entities.Status;
import entities.SubTask;
import entities.Task;
import interfaces.HistoryManager;
import logic.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;


class HistoryManagerTest {

    private HistoryManager manager;
    private Task testTask;
    private EpicTask testEpic;
    private SubTask testSub1;
    private SubTask testSub2;

    @BeforeEach
    void create(){
        manager = new InMemoryHistoryManager();
         testTask = new Task(0,"Task1", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15));
         testEpic = new EpicTask(1,"TestEpic", "Epic Testing");
         testSub1 = new SubTask(2,"SubTask1", "Sub Testing", Status.NEW, 0, Instant.now().plus(Duration.ofDays(1)), Duration.ofHours(15));
         testSub2 = new SubTask(3,"SubTask2", "Sub Testing", Status.NEW, 0, Instant.now().plus(Duration.ofDays(2)), Duration.ofHours(15));


    }

    @Test
    void shouldReturnEmptyHistoryInitially() {
        Assertions.assertTrue(manager.getHistory().isEmpty(),"Изначально история не пуста");
        manager.add(testTask);
        manager.add(testEpic);
        manager.add(testSub1);
        manager.add(testTask);
        Assertions.assertNotNull(manager.getHistory(), "Задачи не добавляются в историю");
    }

    @Test
    void shouldReturnHistorySizeRelevantToTasks() {
        manager.add(testTask);
        manager.add(testEpic);
        manager.add(testSub1);
        Assertions.assertNotNull(manager.getHistory(), "Задачи не добавляются в историю");
        Assertions.assertEquals(3,manager.getHistory().size(),"Размер истории не соответствует требуемому");
    }


    @Test
    void shouldReturnHistoryWithoutDuplicates() {
        manager.add(testTask);
        manager.add(testEpic);
        manager.add(testSub1);
        manager.add(testTask);
        Assertions.assertEquals(3,manager.getHistory().size(),"Запрос идентичных задач дублирует их в истории");

    }

    @Test
    void shouldPutDuplicateTaskToEndOfHistory() {
        manager.add(testTask);
        manager.add(testEpic);
        manager.add(testSub1);
        manager.add(testTask);
        Assertions.assertEquals(testTask,manager.getHistory().get(2),"Повторный запрос задачи не ставит ее в конец списка");

    }

    @Test
    void shouldRemoveTaskFromHistoryByRequest() {
        manager.add(testTask);
        manager.remove(testTask);
        Assertions.assertTrue(manager.getHistory().isEmpty(),"Задача не удаляется из истории");
    }

    @Test
    void shouldChangeHistoryCorrectlyWhenRemoveFirstTask(){
        manager.add(testTask);
        manager.add(testEpic);
        manager.add(testSub1);
        manager.remove(testTask);
        Assertions.assertEquals(2,manager.getHistory().size(),"Задача не удаляется из начала истории");
        Assertions.assertEquals(testEpic,manager.getHistory().get(0), "При удалении первой задачи, остальные задачи неверно смещаются в истории");

    }

    @Test
    void shouldChangeHistoryCorrectlyWhenRemoveMiddleTask(){
        manager.add(testTask);
        manager.add(testEpic);
        manager.add(testSub1);
        manager.remove(testEpic);
        Assertions.assertEquals(2,manager.getHistory().size(),"Задача не удаляется из середины истории");
        Assertions.assertEquals(testTask,manager.getHistory().get(0), "При удалении задачи из середины списка, остальные задачи неверно смещаются в истории");
        Assertions.assertEquals(testSub1,manager.getHistory().get(1), "При удалении задачи из середины списка, остальные задачи неверно смещаются в истории");
    }

    @Test
    void shouldChangeHistoryCorrectlyWhenRemoveLastTask(){
        manager.add(testTask);
        manager.add(testEpic);
        manager.add(testSub1);
        manager.remove(testSub1);
        Assertions.assertEquals(2,manager.getHistory().size(),"Задача не удаляется из конца истории");
        Assertions.assertEquals(testTask,manager.getHistory().get(0), "При удалении задачи из конца списка, остальные задачи неверно смещаются в истории");
        Assertions.assertEquals(testEpic,manager.getHistory().get(1), "При удалении задачи из конца списка, остальные задачи неверно смещаются в истории");
    }

    @Test
    void shouldThrowExceptionWhenRemoveTaskFromEmptyHistory(){
       Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> manager.remove(testSub1));
       Assertions.assertEquals("История не содержит данной задачи",exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRemoveIncorrectTask(){
        manager.add(testTask);
        manager.add(testEpic);
        manager.add(testSub1);
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> manager.remove(testSub2));
        Assertions.assertEquals("История не содержит данной задачи",exception.getMessage());
    }

}