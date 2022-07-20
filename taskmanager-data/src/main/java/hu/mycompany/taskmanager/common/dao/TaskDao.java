/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    List<Task> findAll();
    
    void finish(int Id);
    
    List<Task> findAllByAfterCreatedAndIsCompletedAndPriority(OffsetDateTime createdAt, boolean isCompleted, Priority priority);
    
    boolean existsById(int id);
}
