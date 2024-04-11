package jm.task.core.jdbc.dao;

import com.mysql.cj.log.Log;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory factory = HibernateUtil.getSessionFactory();

    private final static UserDao INSTANCE;
    Logger LOGGER = Logger.getLogger("DAO Logger");

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE dbtest.users (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(128) NOT NULL ,
            last_name VARCHAR(128) NOT NULL ,
            age TINYINT CHECK ( age > 0 ))
            CHARACTER SET = utf8 ;
            """;

    private static final String DROP_TABLE_SQL = """
            DROP TABLE users
            """;

    private static final String CLEAN_TABLE_SQL = """
            TRUNCATE users
            """;

    public UserDaoHibernateImpl() {

    }

    static {
        INSTANCE = new UserDaoHibernateImpl();
    }

    public static UserDao getInstance(){
        return INSTANCE;
    }
    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery(CREATE_TABLE_SQL);

            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "createTable rollback failed.");
                }
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery(DROP_TABLE_SQL);

            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "dropTable rollback failed.");
                }
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "saveUser rollback failed.");
                }
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "removeUser rollback failed.");
                }
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> list = new ArrayList<>();
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            list = session.createQuery("from User").getResultList();
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "getAllUsers rollback failed.");
                }
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery(CLEAN_TABLE_SQL);

            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
