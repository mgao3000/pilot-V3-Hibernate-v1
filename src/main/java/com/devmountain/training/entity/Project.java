package com.devmountain.training.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PROJECT")
public class Project {

    public Project() {
    }

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name ="name")
    private String name;

    @Column(name ="description")
    private String description;

    @Column(name = "create_date")
    private LocalDate createDate;

    @ManyToMany(mappedBy = "projects", fetch = FetchType.EAGER)
//    @JoinTable(name = "student_project",
//            joinColumns = { @JoinColumn(name = "project_id") },
//            inverseJoinColumns = { @JoinColumn(name = "student_id") }
//    )
    private Set<Student> students;

    public Set<Student> getStudents() {
        if(students == null)
            students = new HashSet<Student>();
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    /*
     *  this is a convenient utility kind of method to add the relationship between
     *  this project and this input student
     */
    public void addStudent(Student student) {
        this.getStudents().add(student);
        student.getProjects().add(this);
    }

    /*
     *  this is a convenient utility kind of method to remove the relationship
     *  between this project and this input student
     */
    public void removeStudent(Student student) {
        this.getStudents().remove(student);
        student.getProjects().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equals(getId(), project.getId()) && Objects.equals(getName(), project.getName()) && Objects.equals(getDescription(), project.getDescription()) && Objects.equals(getCreateDate(), project.getCreateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getCreateDate());
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
