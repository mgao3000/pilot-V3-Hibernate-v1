package com.devmountain.training.hibernate;

import com.devmountain.training.dao.ProjectDao;
import com.devmountain.training.entity.Major;
import com.devmountain.training.entity.Project;
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

public class ProjectDaoHibernateImpl extends AbstractDaoHibernateImpl implements ProjectDao {
    private Logger logger = LoggerFactory.getLogger(ProjectDaoHibernateImpl.class);

    @Override
    public Project save(Project project) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            transaction = session.beginTransaction();
//            long id = (long) session.save(project);
//            if(id == 0) {
//                logger.error("Fail to insert a project = {}", project);
//                throw new TrainingEntitySaveOrUpdateFailedException("Fail to insert a project = " + project);
//            } else {
//                project.setId(id);
//            }
            session.saveOrUpdate(project);
            transaction.commit();
        } catch (Exception e) {
            logger.error("fail to insert a project, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return project;
    }

    @Override
    public Project update(Project project) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(project);
            transaction.commit();
            return project;
        } catch (Exception e) {
            logger.error("fail to update project, error={}", e.getMessage());
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
    public boolean deleteByName(String projectName) {
        int deleteCount = 0;
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Query<Major> query = session.createQuery(HQLStatementUtil.HQL_DELETE_PROJECT_BY_NAME);
            query.setParameter("name", projectName);
            deleteCount = query.executeUpdate();
            transaction.commit();
        } catch (HibernateException he) {
            logger.error("fail to delete project by projectName={}, error={}",
                    projectName, he.getMessage());
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
    public boolean deleteById(Long projectId) {
        boolean deleteResult = false;
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            deleteResult = deleteById(session, Project.class, projectId);
            transaction.commit();
        } catch (Exception e) {
            logger.error("fail to delete project by projectId={}, error={}", projectId, e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return deleteResult;
    }

    @Override
    public boolean delete(Project project) {
        return deleteById(project.getId());
    }

    @Override
    public List<Project> getProjects() {
        List<Project> projectList = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            Query<Project> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_PROJECTS);

            projectList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all projects, error={}", he.getMessage());
        }
        return projectList;
    }

    @Override
    public Project getProjectById(Long id) {
        Project retrievedProject = null;
        if(id != null) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Project> query = session.createQuery(HQLStatementUtil.HQL_SELECT_PROJECT_BY_ID);
                query.setParameter("id", id);

                retrievedProject = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve project by projectId={}, error={}", id, he.getMessage());
            }
        }
        return retrievedProject;
    }

    @Override
    public Project getProjectByName(String projectName) {
        Project retrievedProject = null;
        if(!StringUtils.isEmpty(projectName)) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Project> query = session.createQuery(HQLStatementUtil.HQL_SELECT_PROJECT_BY_NAME);
                query.setParameter("name", projectName);

                retrievedProject = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve Project by projectName={}, error={}", projectName, he.getMessage());
            }
        }
        return retrievedProject;
    }

}
