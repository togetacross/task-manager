package hu.mycompany.taskmanager.service;

import hu.mycompany.taskmanager.common.dao.TaskDao;
import hu.mycompany.taskmanager.common.dao.TaskDaoImpl;
import hu.mycompany.taskmanager.common.entity.Priority;
import hu.mycompany.taskmanager.common.entity.Task;
import hu.mycompany.taskmanager.util.TimeUtil;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Gerdan Tibor
 */
public class TaskService {

    private final TaskDao taskDao;

    public TaskService() {
        taskDao = new TaskDaoImpl();
    }

    public void save(String title, String description, int priority) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCreatedAt(OffsetDateTime.now(ZoneId.systemDefault()));
        task.setCompleted(false);
        switch (priority) {
            case 1:
                task.setPriority(Priority.LOW);
                break;
            case 2:
                task.setPriority(Priority.MEDIUM);
                break;
            case 3:
                task.setPriority(Priority.HIGH);
                break;
            default:
                task.setPriority(Priority.LOW);
                break;
        }
        taskDao.save(task);
    }

    public void finishTask(int taskId) {
        if (taskDao.existsById(taskId)) {
            taskDao.finish(taskId);
        }
    }

    public boolean isExistsTaskId(int taskId) {
        return taskDao.existsById(taskId);
    }

    public List<String[]> loadAll() {
        return convertListToTaskArrayList(taskDao.findAll());
    }

    public List<String[]> loadAllByAfterCreatedAndIsCompletedAndPriority(String dateFormat, boolean isCompleted, int priorityLevel) {
        Priority priority = null;
        switch (priorityLevel) {
            case 1:
                priority = Priority.LOW;
                break;
            case 2:
                priority = Priority.MEDIUM;
                break;
            case 3:
                priority = Priority.HIGH;
                break;
            default:
                break;
        }
        LocalDate date = TimeUtil.parseLocalDate(dateFormat);
        OffsetDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
        List<Task> taskList = taskDao.findAllByAfterCreatedAndIsCompletedAndPriority(
                dateTime, isCompleted, priority);
        return convertListToTaskArrayList(taskList);               
    }

    private List<String[]> convertListToTaskArrayList(List<Task> taskList) {
        return taskList
                .stream()
                .map(task -> new String[]{String.valueOf(
                     task.getId()),
                        task.getTitle(),
                        task.getDescription(),
                        TimeUtil.formatLocalDateTime(task.getCreatedAt().toLocalDateTime()),
                        task.isCompleted() ? "Yes" : "No"
                    })
                .collect(Collectors.toList());
    }
}
