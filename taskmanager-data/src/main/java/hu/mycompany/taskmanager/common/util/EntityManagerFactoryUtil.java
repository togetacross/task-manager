package hu.mycompany.taskmanager.common.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Gerdan Tibor
 */
public class EntityManagerFactoryUtil {

    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("taskmanager");
        } catch (Throwable ex) {
            System.err.println("EntityManager factory creation has an error occured! " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
    
    public static void close(){
        entityManagerFactory.close();
    }
}
