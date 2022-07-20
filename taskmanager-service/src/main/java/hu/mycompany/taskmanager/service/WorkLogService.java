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

/**
 *
 * @author Gerdan Tibor
 */
public class WorkLogService {

    private final WorkLogDao workLogDao;

    public WorkLogService() {
        workLogDao = new WorkLogDaoImpl();
    }

    public void start(int taskId, String comment) {
        if (workLogDao.findLast(taskId).getWorkEnd() != null) {
            WorkLog workLog = new WorkLog();
            workLog.setWorkStart(OffsetDateTime.now(ZoneId.systemDefault()));
            workLog.setComment(comment);
            workLogDao.save(workLog, taskId);
        } else {
            System.out.println("There is already a work in progress!");
        }
    }

    public void finsh(int taskId) {
        WorkLog workLog = workLogDao.findLast(taskId);
        if (workLog.getWorkEnd() == null) {
            workLogDao.updateWithEndTime(workLog.getId(), OffsetDateTime.now(ZoneId.systemDefault()));
        } else {
            System.out.println("There is no work in progress!");
        }
    }

    public List<String[]> getAllByTaskId(int id) {
        return convertListToTaskArrayList(workLogDao.findAllByTaskId(id));
    }

    private List<String[]> convertListToTaskArrayList(List<WorkLog> workLogList) {
        return workLogList
                .stream()
                .map(workLog -> {
                    String durationFormat;
                    String workStart = TimeUtil.formatLocalDateTime(workLog.getWorkStart().toLocalDateTime());
                    String workEnd;
                    if (workLog.getWorkEnd() != null) {
                        Duration duration = TimeUtil.getDuration(workLog.getWorkStart(), workLog.getWorkEnd());
                        durationFormat = TimeUtil.formatDuration(duration);
                        workEnd = TimeUtil.formatLocalDateTime(workLog.getWorkEnd().toLocalDateTime());
                    } else {
                        durationFormat = "-";
                        workEnd = "In progress";
                    }
                    return new String[]{
                        durationFormat,
                        workStart + " - " + workEnd,
                        workLog.getComment()
                    };
                })
                .collect(Collectors.toList());
    }

}
