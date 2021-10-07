package com.devmountain.training.util;

public class HQLStatementUtil {

    /*
     * Major related HQL Statements
     */
    public static final String HQL_SELECT_ALL_MAJORS = "From Major";
    public static final String HQL_SELECT_MAJOR_BY_ID = "FROM Major as m where m.id = :id";
    public static final String HQL_SELECT_MAJOR_BY_NAME = "FROM Major as m where m.name = :name";

//    public static final String HQL_DELETE_MAJOR_BY_ID = "DELETE Major as m where m.id = :id";
    public static final String HQL_DELETE_MAJOR_BY_NAME = "DELETE Major as m where m.name = :name";

    /*
     * Project related HQL Statements
     */
    public static final String HQL_SELECT_ALL_PROJECTS = "From Project";
    public static final String HQL_SELECT_PROJECT_BY_ID = "FROM Project as project where project.id = :id";
    public static final String HQL_SELECT_PROJECT_BY_NAME = "FROM Project as project where project.name = :name";

//    public static final String HQL_DELETE_PROJECT_BY_ID = "DELETE Project as project where project.id = :id";
    public static final String HQL_DELETE_PROJECT_BY_NAME = "DELETE Project as project where project.name = :name";

    /*
     * Project related HQL Statements
     */
    public static final String HQL_SELECT_STUDENT_ID_LOGIN_NAME = "SELECT id From Student as student where student.loginName = :loginName";
    public static final String HQL_SELECT_ALL_STUDENTS = "From Student";
    public static final String HQL_SELECT_STUDENT_BY_ID = "FROM Student as student where student.id = :id";
    public static final String HQL_SELECT_STUDENT_BY_LOGIN_NAME = "FROM Student as student where student.loginName = :loginName";

    public static final String HQL_DELETE_STUDENT_BY_LOGIN_NAME = "DELETE Student as student where student.loginName = :loginName";

}
