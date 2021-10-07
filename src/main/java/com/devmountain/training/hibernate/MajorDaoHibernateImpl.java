package com.devmountain.training.hibernate;

import com.devmountain.training.dao.MajorDao;
import com.devmountain.training.entity.Major;
import com.devmountain.training.exception.TrainingEntitySaveOrUpdateFailedException;
import com.devmountain.training.util.HQLStatementUtil;
import com.devmountain.training.util.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MajorDaoHibernateImpl extends AbstractDaoHibernateImpl implements MajorDao {
    private Logger logger = LoggerFactory.getLogger(MajorDaoHibernateImpl.class);

    @Override
    public Major save(Major major) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(major);
            transaction.commit();
        } catch (Exception e) {
            logger.error("fail to insert a major, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return major;
    }

    @Override
    public Major update(Major major) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(major);
            transaction.commit();
            return major;
        } catch (Exception e) {
            logger.error("fail to update major, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    /**
     * Using HQL to do deletion
     */
    public boolean deleteByName(String majorName) {
        int deleteCount = 0;
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Query<Major> query = session.createQuery(HQLStatementUtil.HQL_DELETE_MAJOR_BY_NAME);
            query.setParameter("name", majorName);
            deleteCount = query.executeUpdate();
            transaction.commit();
        } catch (HibernateException he) {
            logger.error("fail to delete major by majorName={}, error={}",
                    majorName, he.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }

        return (deleteCount > 0) ? true : false;
    }

    @Override
    /**
     * Deleting a persistent instance
     */
    public boolean deleteById(Long majorId) {
        boolean deleteResult = false;
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            deleteResult = deleteById(session, Major.class, majorId);
            transaction.commit();
        } catch (Exception e) {
            logger.error("fail to delete major by majorId={}, error={}", majorId, e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return deleteResult;
    }

    @Override
    public boolean delete(Major major) {
        return deleteById(major.getId());
    }

    @Override
    public List<Major> getMajors() {
        List<Major> majorList = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            Query<Major> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_MAJORS);

            majorList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all majors, error={}", he.getMessage());
        }
        return majorList;
    }

    @Override
    public Major getMajorById(Long id) {
        Major retrievedMajor = null;
        if(id != null) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Major> query = session.createQuery(HQLStatementUtil.HQL_SELECT_MAJOR_BY_ID);
                query.setParameter("id", id);

                retrievedMajor = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve major by majorId={}, error={}", id, he.getMessage());
            }
        }
        return retrievedMajor;
    }

    @Override
    public Major getMajorByName(String majorName) {
        Major retrievedMajor = null;
        if(!StringUtils.isEmpty(majorName)) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Major> query = session.createQuery(HQLStatementUtil.HQL_SELECT_MAJOR_BY_NAME);
                query.setParameter("name", majorName);

                retrievedMajor = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve major by majorName={}, error={}", majorName, he.getMessage());
            }
        }
        return retrievedMajor;
    }

}
