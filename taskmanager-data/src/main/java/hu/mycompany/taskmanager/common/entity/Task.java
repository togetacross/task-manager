package hu.mycompany.taskmanager.common.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Gerdan Tibor
 */
@Entity
@Table(name = "task")
@Data
public class Task implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    @Column(name = "is_completed")
    private boolean isCompleted;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;
    
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkLog> workLogList = new ArrayList<>();
    
    private void addWorkLog(WorkLog workLog) {
        workLogList.add(workLog);
        workLog.setTask(this);
    }
    
    private void removeWorkLog(WorkLog workLog) {
        workLogList.remove(workLog);
        workLog.setTask(this);
    }   
}
