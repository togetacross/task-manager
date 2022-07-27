package hu.mycompany.taskmanager.common.dao;

import hu.mycompany.taskmanager.common.entity.Task;
import hu.mycompany.taskmanager.common.entity.WorkLog;
import hu.mycompany.taskmanager.common.util.EntityManagerFactoryUtil;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.hibernate.annotations.QueryHints;

/**
 *
 * @author Gerdan Tibor
 */
public class WorkLogDaoImpl implements WorkLogDao {

    private final EntityManagerFactory entityManagerFactory;

    public WorkLogDaoImpl() {
        this.entityManagerFactory = EntityManagerFactoryUtil.getEntityManagerFactory();
    }

    @Override
    public void save(WorkLog workLog, int taskId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Task task = entityManager.getReference(Task.class, taskId);
        workLog.setTask(task);
        entityManager.getTransaction().begin();
        entityManager.persist(workLog);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void update(WorkLog workLog) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.merge(workLog);
            entityTransaction.commit();
        } catch (RuntimeException ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<WorkLog> findAllByTaskId(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<WorkLog> workLogList = entityManager.createQuery("SELECT w FROM WorkLog w WHERE w.task.id = :taskId ORDER BY w.workStart DESC", WorkLog.class)
                .setParameter("taskId", id)
                .setHint(QueryHints.READ_ONLY, true)
                .getResultList();
        entityManager.close();
        return workLogList;
    }

    @Override
    public Optional<WorkLog> findLastStartTimeByTaskIdAndEndTimeIsNull(int taskId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<WorkLog> typedQuery = entityManager.createQuery(
                "SELECT DISTINCT w FROM WorkLog w WHERE "
                + "w.task.id = :taskId AND w.workEnd IS NULL AND "
                + "w.workStart = (SELECT MAX(w.workStart) FROM WorkLog w)",
                WorkLog.class);
        typedQuery.setParameter("taskId", taskId);
        try {
            return Optional.ofNullable(typedQuery.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

}
