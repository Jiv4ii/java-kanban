package logic;

import entities.*;
import interfaces.TaskManager;

import java.io.*;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final File file;

    public FileBackedTasksManager(String path) {
        super();
        file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("The file at the given path already exists");
        }

    }

    @Override
    public Task getById(int searchId) {
        super.getById(searchId);
        save();
        return super.getById(searchId);
    }

    @Override
    public void createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteById(int searchId) {
        super.deleteById(searchId);
        save();
    }

    public void taskFromString(String value) {
        String[] puzzle = value.split(",");
        Status status = getStatus(puzzle);

        super.createTask(new Task(Integer.parseInt(puzzle[0]), puzzle[2], puzzle[4], status));

    }


    private void subTaskFromString(String value) {
        String[] puzzle = value.split(",");
        Status status = getStatus(puzzle);
        super.createSubTask(new SubTask(Integer.parseInt(puzzle[0]), puzzle[2], puzzle[4], status, Integer.parseInt(puzzle[5])));

    }

    private void epicTaskFromString(String value) {
        String[] puzzle = value.split(",");
        Status status = getStatus(puzzle);
        super.createEpicTask(new EpicTask(Integer.parseInt(puzzle[0]), puzzle[2], puzzle[4], status));
    }

    private  Status getStatus(String[] puzzle) {
        Status status = null;
        // по ТЗ status всегда будет корректна, это затычка чтобы status всегда была проинициализированна.
            switch (puzzle[3]) {
                case "NEW":
                    status = Status.NEW;
                    break;
                case "IN_PROGRESS":
                    status = Status.IN_PROGRESS;
                    break;
                case "DONE":
                    status = Status.DONE;
                    break;
            }
        return status;
}


    private void save() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)
        )) {
            for (Task task : super.showTasksList()) {
                writer.write(task.toString());
                writer.newLine();
            }
            for (EpicTask epicTask : super.showEpicTasksList()) {
                writer.write(epicTask.toString());
                writer.newLine();
            }
            for (SubTask subTask : super.showSubTasksList()) {
                writer.write(subTask.toString());
                writer.newLine();
            }
            writer.newLine();
            if (super.getHistoryManager().getHistory() != null) {
                for (Task task : super.getHistoryManager().getHistory()) {
                    if (super.getHistoryManager().getHistory().indexOf(task) == super.getHistoryManager().getHistory().size() - 1) {
                        writer.write(task.getId() + "");
                        break;
                    }
                    writer.write(task.getId() + ",");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных", e);
        }
    }

    public static FileBackedTasksManager loadFromFile(String path)  {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.out.println("alarm");
        }
        FileBackedTasksManager loadedFromFile = new FileBackedTasksManager(path);
        for (String line : lines) {
            if (line.isEmpty()) {
                break;
            }
            switch (line.split(",")[1]) {
                case "Task":
                    loadedFromFile.taskFromString(line);
                    break;
                case "SubTask":
                    loadedFromFile.subTaskFromString(line);
                    break;
                case "EpicTask":
                    loadedFromFile.epicTaskFromString(line);
                    break;
            }
        }
        String[] history = lines.get(lines.size() - 1).split(",");
        for (String s : history) {
            loadedFromFile.getById(Integer.parseInt(s));
        }
        return loadedFromFile;

    }
}



