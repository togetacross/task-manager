package hu.mycompany.taskmanager.common.dao;

import hu.mycompany.taskmanager.common.entity.Priority;
import hu.mycompany.taskmanager.common.entity.Task;
import java.time.OffsetDateTime;
import java.util.List;

/**
 *
 * @author Gerdan Tibor
 */
public interface TaskDao {
    
    void save(Task task);
    
    void deleteById(int taskId);
    
    List<Task> findAll();
    
    void updateIsCompleted(int Id, boolean isCompleted);
    
    List<Task> findAllByAfterCreatedAndIsCompletedAndPriority(OffsetDateTime createdAt, boolean isCompleted, Priority priority);
    
    boolean existsById(int id);
}
