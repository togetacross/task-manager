package hu.mycompany.taskmanager.service;

import hu.mycompany.taskmanager.common.dao.WorkLogDao;
import hu.mycompany.taskmanager.common.dao.WorkLogDaoImpl;
import hu.mycompany.taskmanager.common.entity.WorkLog;
import hu.mycompany.taskmanager.util.TimeUtil;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gerdan Tibor
 */
public class WorkLogService {
    
    private final WorkLogDao workLogDao;
    private static final Logger logger = LoggerFactory.getLogger(WorkLogService.class);
    
    public WorkLogService() {
        workLogDao = new WorkLogDaoImpl();
    }
    
    public void start(int taskId, String comment) {
        workLogDao.findLastStartTimeByTaskIdAndEndTimeIsNull(taskId).ifPresentOrElse(workLog -> {
            logger.info("There is a work in progress!");
        }, () -> {
            WorkLog newWorkLog = new WorkLog();
            newWorkLog.setWorkStart(OffsetDateTime.now(ZoneId.systemDefault()));
            newWorkLog.setComment(comment);
            workLogDao.save(newWorkLog, taskId);
        });
    }
    
    public void finsh(int taskId) {
        workLogDao.findLastStartTimeByTaskIdAndEndTimeIsNull(taskId).ifPresentOrElse(workLog -> {
            workLog.setWorkEnd(OffsetDateTime.now(ZoneId.systemDefault()));
            workLogDao.update(workLog);
        }, () -> logger.info("There is no work in progress!"));
    }
    
    public List<String[]> getAllByTaskId(int id) {
        return convertListToTaskArrayList(workLogDao.findAllByTaskId(id));
    }
    
    public String getAllWorkedTime(int taskId) {
        Duration duration = Duration.ZERO;
        long miliSeconds = workLogDao
                .findAllByTaskId(taskId)
                .stream()
                .map(workLog -> {
                    return TimeUtil.getDuration(
                            workLog.getWorkStart(),
                            workLog.getWorkEnd() != null ? workLog.getWorkEnd() : OffsetDateTime.now(ZoneId.systemDefault())
                    ).toMillis();
                }).collect(Collectors.summingLong(Long::longValue));
        return TimeUtil.formatDuration(duration.plusMillis(miliSeconds));
    }
    
    private List<String[]> convertListToTaskArrayList(List<WorkLog> workLogList) {
        return workLogList
                .stream()
                .map(workLog -> {
                    String workStart = TimeUtil.formatLocalDateTime(workLog.getWorkStart().toLocalDateTime());
                    String workEnd;
                    Duration duration;
                    if (workLog.getWorkEnd() != null) {
                        duration = TimeUtil.getDuration(workLog.getWorkStart(), workLog.getWorkEnd());
                        workEnd = TimeUtil.formatLocalDateTime(workLog.getWorkEnd().toLocalDateTime());
                    } else {
                        duration = TimeUtil.getDuration(workLog.getWorkStart(), OffsetDateTime.now(ZoneId.systemDefault()));
                        workEnd = "In progress";
                    }
                    return new String[]{
                        TimeUtil.formatDuration(duration),
                        workStart + " - " + workEnd,
                        workLog.getComment()
                    };
                })
                .collect(Collectors.toList());
    }
    
}
