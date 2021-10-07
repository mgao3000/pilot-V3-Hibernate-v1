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

public class MajorDaoTest extends AbstractDaoTest {
    private Logger logger = LoggerFactory.getLogger(MajorDaoTest.class);

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
        tempMajorName = "Major-" + getRandomInt(1, 1000);
        tempLoginName = "Student-login-" + getRandomInt(1, 1000);
        tempProjectName = "Project-" + getRandomInt(1, 1000);
        tempEmail = "test-" + getRandomInt(1, 1000) + "@devmountain.com";
    }

    @After
    public void teardown() {
        tempLoginName = null;
        tempEmail = null;
        tempMajorName = null;
        tempProjectName = null;
    }

    @Test
    public void getMajorsTest() {
        List<Major> majorList = majorDao.getMajors();
//        int i = 1;
//        for(Major major : majorList) {
//            logger.info("No.{} Major = {}", i, major);
//            i++;
//        }
        assertEquals(7, majorList.size());
        displayMajors(majorList);
    }

    @Test
    public void getMajorByIdTest() {
        /*
         * Pick up a random MajorModel from DB
         */
        Major randomMajor = getRandomMajor();
        if(randomMajor == null) {
            logger.error("there is no major being found in database, please double check DB connection!");
        } else {
            Long majorId = randomMajor.getId();
            Major retrievedMajorModel = majorDao.getMajorById(majorId);
            assertMajors(randomMajor, retrievedMajorModel);
        }
    }

    @Test
    public void getMajorByNameTest() {
        /*
         * Pick up a random MajorModel from DB
         */
        Major randomMajor = getRandomMajor();
        if(randomMajor == null) {
            logger.error("there is no major being found in database, please double check DB connection!");
        } else {
            String majorName = randomMajor.getName();
            Major retrievedMajor = majorDao.getMajorByName(majorName);
            assertMajors(randomMajor, retrievedMajor);
        }
    }

    @Test
    public void saveMajorOnlyTest() {
        Major major = createMajorByName(tempMajorName);
        Major savedMajor = majorDao.save(major);
        assertEquals(major.getName(), savedMajor.getName());
        assertEquals(major.getDescription(), savedMajor.getDescription());
        displayMajor(savedMajor);
        /*
         * Now clean up the saved Major from DB Major table
         */
        boolean deleteSuccessfulFlag = majorDao.delete(savedMajor);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void saveMajorWithAssociatedStudentsTest() {
        Major major = createMajorByName(tempMajorName);
        Student student1 = createStudentByLoginNameAndEmail(tempLoginName, tempEmail);
        tempLoginName = "Student-login-" + getRandomInt(1, 1000);
        tempEmail = "test-" + getRandomInt(1, 1000) + "@devmountain.com";
        Student student2 = createStudentByLoginNameAndEmail(tempLoginName, tempEmail);

        major.addStudent(student1); //student1.setMajor(major);
        major.addStudent(student2); //student2.setMajor(major);

        Major savedMajor = majorDao.save(major);
        assertEquals(major.getName(), savedMajor.getName());
        assertEquals(major.getDescription(), savedMajor.getDescription());
        assertEquals(2, savedMajor.getStudents().size());

        displayMajor(savedMajor);
        displayStudent(student1);
        displayStudent(student2);
        /*
         * Now clean up the saved Major from DB Major table
         */
        boolean deleteSuccessfulFlag = majorDao.delete(savedMajor);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void deleteMajorTest() {
        Major major = createMajorByName(tempMajorName);
        Major savedMajor = majorDao.save(major);
        /*
         * Now delete the saved Major from DB Major table
         */
        boolean deleteSuccessfulFlag = majorDao.delete(savedMajor);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void createMajorWithChildrenAndThenDeleteTest() {
        //Step 0: create a temp Major
        Major major = createMajorByName(tempMajorName);
        Major majorSaved = majorDao.save(major);

        //Step 1.1: create a temp Student No.1
        Student student1 = createStudentByLoginNameAndEmail(tempLoginName, tempEmail);
        student1.setMajor(majorSaved);
        Student studentSaved1 = studentDao.save(student1);

        //Step 1.2: create a temp Student No.2
        Student student2 = createStudentByLoginNameAndEmail(tempLoginName+"-2", 2 + tempEmail);
        student2.setMajor(majorSaved);
        Student studentSaved2 = studentDao.save(student2);

        //Step 2.1  create a temp project No.1
        Project project1 = createProjectByName(tempProjectName + "-1");
        project1.addStudent(studentSaved1);
        Project projectDSaved1 = projectDao.save(project1);

        //Step 2.2: create a temp project No.2
        Project project2 = createProjectByName(tempProjectName+"-2");
        project2.addStudent(studentSaved1);
        project2.addStudent(studentSaved2);
        Project projectDSaved2 = projectDao.save(project2);

        //Step 2.3  create a temp project No.3
        Project project3 = createProjectByName(tempProjectName+"-3");
        project3.addStudent(studentSaved1);
        Project projectDSaved3 = projectDao.save(project3);

        //Step 2.4  create a temp project No.4
        Project project4 = createProjectByName(tempProjectName+"-4");
        project4.addStudent(studentSaved1);
        project4.addStudent(studentSaved2);
        Project projectDSaved4 = projectDao.save(project4);

        //Step 2.5: create a temp project No.5
        Project project5 = createProjectByName(tempProjectName+"-5");
        project5.addStudent(studentSaved2);
        Project projectDSaved5 = projectDao.save(project5);

        //Step 2.6: create a temp project No.6
        Project project6 = createProjectByName(tempProjectName+"-6");
        Project projectDSaved6 = projectDao.save(project6);

        //Start doing assertions
        assertEquals(4, studentSaved1.getProjects().size());
        assertEquals(3, studentSaved2.getProjects().size());

        assertEquals(1, projectDSaved1.getStudents().size());
        assertEquals(2, projectDSaved2.getStudents().size());
        assertEquals(1, projectDSaved3.getStudents().size());
        assertEquals(2, projectDSaved4.getStudents().size());
        assertEquals(1, projectDSaved5.getStudents().size());
        assertEquals(0, projectDSaved6.getStudents().size());

        /*
         * Now delete the saved Major from DB Major table
         */
        boolean deleteSuccessfulFlag = majorDao.delete(majorSaved);
        assertEquals(true, deleteSuccessfulFlag);

        //Now delete those temporarily created projects using project ID
        deleteSuccessfulFlag = projectDao.delete(projectDSaved1);
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.delete(projectDSaved2);
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.delete(projectDSaved3);
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.delete(projectDSaved4);
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.delete(projectDSaved5);
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.delete(projectDSaved6);
        assertEquals(true, deleteSuccessfulFlag);

        //Now start to do multiple selection to make sure new created major, students and projects are gone!
        Major retrievedMajor = majorDao.getMajorById(majorSaved.getId());
        assertNull(retrievedMajor);

        Student retrievedStudent = studentDao.getStudentById(studentSaved1.getId());
        assertNull(retrievedStudent);
        retrievedStudent = studentDao.getStudentById(studentSaved2.getId());
        assertNull(retrievedStudent);

        Project retrievedProject = projectDao.getProjectById(projectDSaved1.getId());
        assertNull(retrievedProject);
        retrievedProject = projectDao.getProjectById(projectDSaved2.getId());
        assertNull(retrievedProject);
        retrievedProject = projectDao.getProjectById(projectDSaved3.getId());
        assertNull(retrievedProject);
        retrievedProject = projectDao.getProjectById(projectDSaved4.getId());
        assertNull(retrievedProject);
        retrievedProject = projectDao.getProjectById(projectDSaved5.getId());
        assertNull(retrievedProject);
        retrievedProject = projectDao.getProjectById(projectDSaved6.getId());
        assertNull(retrievedProject);
    }

    @Test
    public void updateMajorTest() {
        Major originalMajorModel = getRandomMajor();
        String currentMajorDesc = originalMajorModel.getDescription();
        String modifiedMajorDesc = currentMajorDesc + "---newUpdate";
        originalMajorModel.setDescription(modifiedMajorDesc);
        /*
         * Now start doing update operation
         */
        Major updatedMajor = majorDao.update(originalMajorModel);
        assertMajors(originalMajorModel, updatedMajor);

        /*
         * now reset MajorModel description back to the original value
         */
        originalMajorModel.setDescription(currentMajorDesc);
        updatedMajor = majorDao.update(originalMajorModel);
        assertMajors(originalMajorModel, updatedMajor);
    }


}
