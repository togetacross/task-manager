package hu.mycompany.taskmanager.common.dao;

import hu.mycompany.taskmanager.common.entity.Priority;
import hu.mycompany.taskmanager.common.entity.Task;
import hu.mycompany.taskmanager.common.util.EntityManagerFactoryUtil;
import java.time.OffsetDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import org.hibernate.annotations.QueryHints;

/**
 *
 * @author Gerdan Tibor
 */
public class TaskDaoImpl implements TaskDao {

    private final EntityManagerFactory entityManagerFactory;

    public TaskDaoImpl() {
        this.entityManagerFactory = EntityManagerFactoryUtil.getEntityManagerFactory();
    }

    @Override
    public void save(Task task) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(task);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void deleteById(int taskId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Task task = entityManager.find(Task.class, taskId);
        entityManager.remove(task);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<Task> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Task> typedQuery = entityManager.createQuery("Select t from Task t", Task.class);
        List<Task> taskList = typedQuery
                .setHint(QueryHints.READ_ONLY, true)
                .getResultList();
        entityManager.close();
        return taskList;
    }

    @Override
    public void updateIsCompleted(int Id, boolean isCompeted) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            Task task = entityManager.find(Task.class, Id);
            task.setCompleted(isCompeted);
            entityTransaction.commit();
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Task> findAllByAfterCreatedAndIsCompletedAndPriority(OffsetDateTime createdAt, boolean isCompleted, Priority priority) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Task> typedQuery = entityManager.createQuery(
                "SELECT t FROM Task t WHERE t.isCompleted = :isCompleted AND (t.priority = :priority OR priority IS NOT NULL) AND t.createdAt >= :createdAt", Task.class);
        List<Task> taskList = typedQuery
                .setParameter("isCompleted", isCompleted)
                .setParameter("priority", priority)
                .setParameter("createdAt", createdAt)
                .setHint(QueryHints.READ_ONLY, true)
                .getResultList();
        entityManager.close();
        return taskList;
    }

    @Override
    public boolean existsById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Boolean> typedQuery = entityManager.createQuery(
                "SELECT CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END FROM Task t WHERE t.id = :id", Boolean.class);
        boolean isExists = typedQuery
                .setParameter("id", id)
                .setHint(QueryHints.READ_ONLY, true)
                .getSingleResult();
        entityManager.close();
        return isExists;
    }

}
