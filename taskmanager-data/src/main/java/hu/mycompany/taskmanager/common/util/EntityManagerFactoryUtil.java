package hu.mycompany.taskmanager.common.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Gerdan Tibor
 */
public class EntityManagerFactoryUtil {

    private static EntityManagerFactory entityManagerFactory;
    private static final String PERSISTENT_UNIT = "taskmanager";

    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENT_UNIT);
        }
        return entityManagerFactory;
    }

    public static void shutDown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
