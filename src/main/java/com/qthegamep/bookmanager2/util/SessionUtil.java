package com.qthegamep.bookmanager2.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * This class is an utility helper class responsible for opening and closing sessions
 * and transactional sessions with the database.
 */
@Slf4j
@UtilityClass
public class SessionUtil {

    private SessionFactory sessionFactory;

    private Session session;
    private Transaction transaction;

    static {
        buildSessionFactory();
    }

    /**
     * This method creates new session factory if old one was closed.
     */
    public void createNewSessionFactory() {
        log.info("Preparing to create new session factory");

        if (sessionFactory == null || sessionFactory.isClosed()) {
            buildSessionFactory();
            log.info("Preparing to create new session factory was done successful");
        } else {
            log.info("Preparing to create new session factory was done successful! " +
                    "New session factory was not created because it was not closed yet"
            );
        }
    }

    /**
     * This method opens the session if session was not created or opened yet.
     *
     * @return session with the database.
     */
    public Session openSession() {
        log.info("Preparing to open hibernate session");

        if (session != null && session.isOpen()) {
            log.info("Preparing to open hibernate session was done successful! " +
                    "New session was not opened because it was already opened"
            );
            return session;
        }

        session = sessionFactory.openSession();
        log.info("Preparing to open hibernate session was done successful! New session was opened");

        return session;
    }

    /**
     * This method closes the session if session is created or opened.
     */
    public void closeSession() {
        log.info("Preparing to close hibernate session");

        if (session != null && session.isOpen()) {
            session.close();
            log.info("Preparing to close hibernate session was done successful! This session was closed");
        } else {
            log.info("Preparing to close hibernate session was done successful! " +
                    "This session was not close because it was already closed"
            );
        }
    }

    /**
     * This method opens the transactional session.
     *
     * @return transactional session with the database.
     */
    public Session openTransactionSession() {
        log.info("Preparing to open hibernate transaction session");

        openSession();

        log.info("Preparing to begin transaction");

        transaction = session.beginTransaction();
        log.info("Preparing to open hibernate transaction session was done successful! Transaction was started");

        return session;
    }

    /**
     * This method commit transaction if transaction was created and is active and closes the session.
     */
    public void closeTransactionSession() {
        log.info("Preparing to close hibernate transaction session");

        if (transaction != null && transaction.isActive()) {
            transaction.commit();
            log.info("Transaction was committed");
        } else {
            log.info("Transaction was not committed because it was not created yet");
        }

        closeSession();

        log.info("Preparing to close hibernate transaction session was done successful");
    }

    /**
     * This method closes the transactional session and session factory.
     * Use this method in the end of the application.
     */
    public void shutdown() {
        log.info("Preparing to shutdown hibernate session factory");

        closeTransactionSession();
        closeSessionFactory();

        log.info("Preparing to shutdown hibernate session factory was done successful");
    }

    private void buildSessionFactory() {
        log.info("Preparing to build session factory");

        sessionFactory = new Configuration().configure().buildSessionFactory();

        log.info("Preparing to build session factory was done successful");
    }

    private void closeSessionFactory() {
        log.info("Preparing to close session factory");

        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            log.info("Preparing to close session factory was done successful! This session factory was closed");
        } else {
            log.info("Preparing to close session factory was done successful! " +
                    "This session factory was not closed because it was not built or it was already closed"
            );
        }
    }
}
