/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mycompany.taskmanager.common.dao;

import hu.mycompany.taskmanager.common.entity.Task;
import hu.mycompany.taskmanager.common.entity.WorkLog;
import hu.mycompany.taskmanager.common.util.EntityManagerFactoryUtil;
import java.time.OffsetDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Gerdan Tibor
 */
public class WorkLogDaoImpl implements WorkLogDao {

    private final EntityManagerFactoryUtil entityManagerFactoryUtil;
    private EntityManager entityManager;

    public WorkLogDaoImpl() {
        entityManagerFactoryUtil = new EntityManagerFactoryUtil();
    }

    @Override
    public void save(WorkLog workLog, int taskId) {
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        Task task = entityManager.getReference(Task.class, taskId);
        workLog.setTask(task);
        entityManager.persist(workLog);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<WorkLog> findAllByTaskId(int id) {
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<WorkLog> typedQuery = entityManager.createQuery("SELECT w FROM WorkLog w WHERE w.task.id = :taskId ORDER BY w.workStart DESC", WorkLog.class);
        typedQuery.setParameter("taskId", id);
        List<WorkLog> workLogList = typedQuery.getResultList();
        entityManager.close();
        return workLogList;
    }

    @Override
    public void updateWithEndTime(int id, OffsetDateTime offsetDateTime) {
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        WorkLog workLog = entityManager.find(WorkLog.class, id);
        workLog.setWorkEnd(offsetDateTime);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public WorkLog findLast(int taskId) {
        entityManager = entityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<WorkLog> typedQuery = entityManager.createQuery("SELECT DISTINCT w FROM WorkLog w WHERE w.task.id = :taskId AND w.workStart = (SELECT MAX(w.workStart) FROM WorkLog w)", WorkLog.class);
        typedQuery.setParameter("taskId", taskId);
        WorkLog workLog = typedQuery.getSingleResult();
        entityManager.close();
        return workLog;
    }

}
