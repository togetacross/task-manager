package hu.mycompany.taskmanager.common.dao;

import hu.mycompany.taskmanager.common.entity.WorkLog;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gerdan Tibor
 */
public interface WorkLogDao {
    
    void save(WorkLog workLog, int taskId);
    
    void update(WorkLog workLog);
    
    List<WorkLog> findAllByTaskId(int id);
    
    Optional<WorkLog> findLastStartTimeByTaskIdAndEndTimeIsNull(int taskId);
}
