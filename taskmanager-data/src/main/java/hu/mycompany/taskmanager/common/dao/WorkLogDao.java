/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mycompany.taskmanager.common.dao;

import hu.mycompany.taskmanager.common.entity.WorkLog;
import java.time.OffsetDateTime;
import java.util.List;

/**
 *
 * @author Gerdan Tibor
 */
public interface WorkLogDao {
    
    void save(WorkLog workLog, int taskId);
    
    WorkLog findLast(int taskId);
    
    void updateWithEndTime(int id, OffsetDateTime offsetDateTime);
    
    List<WorkLog> findAllByTaskId(int id);
}
