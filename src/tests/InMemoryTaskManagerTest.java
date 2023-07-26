package tests;

import logic.InMemoryTaskManager;

import org.junit.jupiter.api.BeforeEach;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void create() {
        manager = new InMemoryTaskManager();
        emptyManager = new InMemoryTaskManager();
        manager.createEpicTask(testEpic);
        manager.createTask(testTask);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
    }

}

