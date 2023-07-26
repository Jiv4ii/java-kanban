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

    private static InMemoryTaskManager manager;

    @BeforeEach
     void createManager() {
        manager = new InMemoryTaskManager();
    }


    @Test
     void shouldReturnNewWhenSubTasksListEmpty() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }


    @Test
     void shouldReturnNewWhenSubTasksListNew() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
        SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 0, Instant.now().plus(Duration.ofDays(2)), Duration.ofDays(2));
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
     void shouldReturnNewWhenSubTasksListDone() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.DONE, 0, Instant.now(), Duration.ofHours(15));
        SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.DONE, 0, Instant.now().plus(Duration.ofDays(2)), Duration.ofDays(2));
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
     void shouldReturnNewWhenSubTasksListNewAndDone() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
        SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.DONE, 0, Instant.now().plus(Duration.ofDays(2)), Duration.ofDays(2));
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
     void shouldReturnNewWhenSubTasksListNewAndInProgress() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.IN_PROGRESS, 0, Instant.now(), Duration.ofHours(15));
        SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.IN_PROGRESS, 0, Instant.now().plus(Duration.ofDays(2)), Duration.ofDays(2));
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }


}