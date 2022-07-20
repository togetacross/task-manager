/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mycompany.taskmanager.common.dao;

import hu.mycompany.taskmanager.common.entity.Priority;
import hu.mycompany.taskmanager.common.entity.Task;
import hu.mycompany.taskmanager.common.util.EntityManagerFactoryUtil;
import java.time.OffsetDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.hibernate.annotations.QueryHints;

/**
 *
 * @author Gerdan Tibor
 */
public class TaskDaoImpl implements TaskDao {

    private final EntityManagerFactoryUtil entityManagerFactoryUtil;
    private EntityManager entityManager;

    public TaskDaoImpl() {
        entityManagerFactoryUtil = new EntityManagerFactoryUtil();
    }

    @Override
    public void save(Task task) {
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(task);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<Task> findAll() {
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Task> typedQuery = entityManager.createQuery("Select t from Task t", Task.class);
        List<Task> taskList = typedQuery
                .setHint(QueryHints.READ_ONLY, true)
                .getResultList();
        entityManager.close();
        return taskList;
    }

    @Override
    public void finish(int Id) {
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        Task task = entityManager.find(Task.class, Id);
        task.setCompleted(true);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<Task> findAllByAfterCreatedAndIsCompletedAndPriority(OffsetDateTime createdAt, boolean isCompleted, Priority priority) {
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
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
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
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
