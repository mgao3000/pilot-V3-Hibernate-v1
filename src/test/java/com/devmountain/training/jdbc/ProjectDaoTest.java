package com.devmountain.training.jdbc;

import com.devmountain.training.entity.Major;
import com.devmountain.training.entity.Project;
import com.devmountain.training.entity.Student;
import com.devmountain.training.hibernate.MajorDaoHibernateImpl;
import com.devmountain.training.hibernate.ProjectDaoHibernateImpl;
import com.devmountain.training.hibernate.StudentDaoHibernateImpl;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;

public class ProjectDaoTest extends AbstractDaoTest {
    private Logger logger = LoggerFactory.getLogger(ProjectDaoTest.class);

    @BeforeClass
    public static void setupOnce() {
        majorDao = new MajorDaoHibernateImpl();
        studentDao = new StudentDaoHibernateImpl();
        projectDao = new ProjectDaoHibernateImpl();
    }

    @AfterClass
    public static void teardownOnce() {
        majorDao = null;
        studentDao = null;
        projectDao = null;
    }

    @Before
    public void setup() {
        tempMajorName = "Random-Majoe" + getRandomInt(1, 1000);
        tempLoginName = "Random-LoginName" + getRandomInt(1, 1000);
        tempEmail = "Test" + getRandomInt(1, 1000) + "@google.com";
        tempProjectName = "Project-" + getRandomInt(1, 1000);
    }

    @After
    public void teardown() {
        tempLoginName = null;
        tempEmail = null;
        tempMajorName = null;
        tempProjectName = null;
    }

    @Test
    public void getProjectsTest() {
        List<Project> projectList = projectDao.getProjects();
        assertEquals(10, projectList.size());
//        displayProjects(projectList);
    }

    @Test
    public void getProjectByIdTest() {
        /*
         * Pick up a random Project from DB
         */
        Project randomProject = getRandomProject();
        if(randomProject == null) {
            logger.error("there is no project being found in database, please double check DB connection!");
        } else {
            Long projectId = randomProject.getId();
            Project retrievedProject = projectDao.getProjectById(projectId);
            assertProjects(randomProject, retrievedProject);
//            displayProject(retrievedProject);
        }
    }

    @Test
    public void getProjectByNameTest() {
        /*
         * Pick up a random Project from DB
         */
        Project randomProject = getRandomProject();
        if(randomProject == null) {
            logger.error("there is no Project being found in database, please double check DB connection!");
        } else {
            String projectName = randomProject.getName();
            Project retrievedProject = projectDao.getProjectByName(projectName);
            assertProjects(randomProject, retrievedProject);
            displayProject(retrievedProject);
        }
    }

    @Test
    public void saveProjectTest() {
        Project project = createProjectByName(tempProjectName);
        Project savedProject = projectDao.save(project);
        assertNotNull(savedProject);
        assertEquals(project.getName(), savedProject.getName());
        assertEquals(project.getDescription(), savedProject.getDescription());
        /*
         * Now clean up the saved Project from DB Major table
         */
        boolean deleteSuccessfulFlag = projectDao.delete(savedProject);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void deleteProjectTest() {
        Project project = createProjectByName(tempProjectName);
        Project savedProject = projectDao.save(project);
        /*
         * Now delete the saved Project from DB Major table
         */
        boolean deleteSuccessfulFlag = projectDao.delete(savedProject);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void deleteProjectByIdTest() {
        Project project = createProjectByName(tempProjectName);
        Project savedProject = projectDao.save(project);
        /*
         * Now delete the saved Project from DB Major table
         */
        boolean deleteSuccessfulFlag = projectDao.deleteById(savedProject.getId());
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void deleteProjectByNameTest() {
        Project project = createProjectByName(tempProjectName);
        Project savedProject = projectDao.save(project);
        /*
         * Now delete the saved Project from DB Major table
         */
        boolean deleteSuccessfulFlag = projectDao.deleteByName(savedProject.getName());
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void updateProjectTest() {
        Project originalProject = getRandomProject();
        String originalProjectDesc = originalProject.getDescription();
        String modifiedProjectDesc = originalProjectDesc + "---newUpdate";
        originalProject.setDescription(modifiedProjectDesc);
        /*
         * Now start doing update operation
         */
        Project updatedProject = projectDao.update(originalProject);
        assertProjects(originalProject, updatedProject);

        /*
         * now reset ProjectModel description back to the original value
         */
        originalProject.setDescription(originalProjectDesc);
        updatedProject = projectDao.update(originalProject);
        assertProjects(originalProject, updatedProject);
    }

    @Test
    public void addStudentsToProjectTest() {
        //Step 0: create a temp Major
        Major major = createMajorByName(tempMajorName);
        Major majorSaved = majorDao.save(major);
//        logger.info("=============, addStudentsToProjectTest(), majorSaved={}", majorSaved);

        //Step 1.1: create a temp Student No.1
        Student student1 = createStudentByLoginNameAndEmail(tempLoginName, tempEmail);
        student1.setMajor(majorSaved);
        Student studentSaved1 = studentDao.save(student1);
//        logger.info("=============, addStudentsToProjectTest(), studentSaved1={}", studentSaved1);

        //Step 1.2: create a temp Student No.1
        Student student2 = createStudentByLoginNameAndEmail(tempLoginName+"-2", "a2-" + tempEmail);
        student2.setMajor(majorSaved);
        Student studentSaved2 = studentDao.save(student2);
//        logger.info("=============, addStudentsToProjectTest(), studentSaved2={}", studentSaved2);

        //Step 2.1  create a temp project No.1
        Project project1 = createProjectByName(tempProjectName);
        project1.addStudent(studentSaved1);
        Project projectDSaved1 = projectDao.save(project1);

        //Step 2.2: create a temp project No.2
        Project project2 = createProjectByName(tempProjectName+"-2");
        project2.addStudent(studentSaved1);
        project2.addStudent(studentSaved2);
        Project projectDSaved2 = projectDao.save(project2);

        //Conduct assertions
        assertEquals(1, projectDSaved1.getStudents().size());
        assertEquals(2, projectDSaved2.getStudents().size());

        assertEquals(2, studentSaved1.getProjects().size());
        assertEquals(1, studentSaved2.getProjects().size());

        //Now clean up
        boolean deleteSuccessfulFlag = majorDao.delete(majorSaved);
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.deleteById(projectDSaved1.getId());
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.deleteById(projectDSaved2.getId());
        assertEquals(true, deleteSuccessfulFlag);
    }

}
