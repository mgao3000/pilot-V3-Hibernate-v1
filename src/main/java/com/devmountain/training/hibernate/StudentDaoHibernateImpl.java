package com.devmountain.training.hibernate;

import com.devmountain.training.dao.StudentDao;
import com.devmountain.training.entity.Major;
import com.devmountain.training.entity.Student;
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

public class StudentDaoHibernateImpl extends AbstractDaoHibernateImpl implements StudentDao {
    private Logger logger = LoggerFactory.getLogger(StudentDaoHibernateImpl.class);


    @Override
    public Student save(Student student) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(student);
            transaction.commit();
        } catch (Exception e) {
            logger.error("fail to insert a student, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return student;
    }

    @Override
    public Student update(Student student) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(student);
            transaction.commit();
            return student;
        } catch (Exception e) {
            logger.error("fail to update student, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public boolean deleteByLoginName(String loginName) {
        boolean deleteSuccessfulFlag = false;
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Query<Long> query = session.createQuery(HQLStatementUtil.HQL_SELECT_STUDENT_ID_LOGIN_NAME);
            query.setParameter("loginName", loginName);

            Long studentId = query.uniqueResult();
            if (studentId != null) {
                Student retrievedStudent = session.load(Student.class, studentId);
                // remove the relationship with major from this student
                retrievedStudent.removeMajor();
                transaction.commit();
                deleteSuccessfulFlag = true;
            } else {
                transaction.rollback();
                ;
            }
        } catch (HibernateException he) {
            logger.error("fail to delete student by loginName={}, error={}",
                    loginName, he.getMessage());
            if (transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }

        return deleteSuccessfulFlag;
    }

    @Override
    public boolean deleteById(Long studentId) {
        boolean deleteResult = false;
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Student retrievedStudent = session.load(Student.class, studentId);
            // remove the relationship with major from this student
            retrievedStudent.removeMajor();

            //Here we only need to commit because the "orphanRemoval = true" mapping configuration
            transaction.commit();
            deleteResult = true;
        } catch (Exception e) {
            logger.error("fail to delete student by studentId={}, error={}", studentId, e.getMessage());
            if (transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return deleteResult;
    }

    @Override
    public boolean delete(Student student) {
        return deleteById(student.getId());
    }

    @Override
    public List<Student> getStudents() {
        List<Student> studentList = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            Query<Student> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_STUDENTS);

            studentList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all students, error={}", he.getMessage());
        }
        return studentList;
    }

    @Override
    public Student getStudentById(Long id) {
        Student retrievedStudent = null;
        if(id != null) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Student> query = session.createQuery(HQLStatementUtil.HQL_SELECT_STUDENT_BY_ID);
                query.setParameter("id", id);

                retrievedStudent = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve student by studentId={}, error={}", id, he.getMessage());
            }
        }
        return retrievedStudent;
    }

    @Override
    public Student getStudentByLoginName(String loginName) {
        Student retrievedStudent = null;
        if(!StringUtils.isEmpty(loginName)) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Student> query = session.createQuery(HQLStatementUtil.HQL_SELECT_STUDENT_BY_LOGIN_NAME);
                query.setParameter("loginName", loginName);

                retrievedStudent = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve student by loginName={}, error={}", loginName, he.getMessage());
            }
        }
        return retrievedStudent;
    }

}
