package tests;

import entities.EpicTask;
import entities.Status;
import entities.SubTask;
import logic.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;


class EpicTaskTest {
    /*В этом месте вы оставили замечание по поводу отсутствия привязки подзадач к эпикам.
    * Все подзадачи привязываются к эпикe, указанному в конструкоре(поле epicId),добавляются в его список подзадач и обновляют его статус и время,
    * если это не работает то нижестоящие тесты показали бы это.
    * Если вы говорили не об этом, извиняюсь, я не понял замечание*/

    private InMemoryTaskManager manager;
    private EpicTask epic;
    private SubTask testSub1;
    private SubTask testSub2;

    @BeforeEach
    void createManager() {
        manager = new InMemoryTaskManager();
        epic = new EpicTask("TestEpic", "Epic Testing");
        testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
        testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 0, Instant.now().plus(Duration.ofDays(2)), Duration.ofDays(2));
    }


    @Test
    void shouldReturnNewWhenSubTasksListEmpty() {
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }


    @Test
    void shouldReturnNewWhenSubTasksListNew() {
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void shouldReturnNewWhenSubTasksListDone() {
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void shouldReturnNewWhenSubTasksListNewAndDone() {
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void shouldReturnNewWhenSubTasksListNewAndInProgress() {
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }


}