package com.example.labor_6.Controller;

import com.example.labor_6.Exceptions.NullValueException;
import com.example.labor_6.Model.Course;
import com.example.labor_6.Model.Student;
import com.example.labor_6.Model.Teacher;
import com.example.labor_6.Repository.CourseJdbcRepository;
import com.example.labor_6.Repository.StudentsJdbcRepository;
import com.example.labor_6.Repository.TeachersJdbcRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationSystem {

    private StudentsJdbcRepository studRepo;
    private TeachersJdbcRepository teachRepo;
    private CourseJdbcRepository courseRepo;

    public RegistrationSystem(StudentsJdbcRepository studRepo, TeachersJdbcRepository teachRepo, CourseJdbcRepository courseRepo) {
        this.studRepo = studRepo;
        this.teachRepo = teachRepo;
        this.courseRepo = courseRepo;
    }

    /** adds a Student to the repository
     * @param s - Student
     * @return true
     * @throws Exception when his credits are > 30
     */
    public boolean addStudent(Student s) throws Exception {
        if(s.getTotalCredits() > 30)
            throw new Exception("Max number of credits exceeded!");
        this.studRepo.save(s);
        return true;
    }

    /**
     * Deletes a Student from the repository
     * @param id identifies the object by the id
     * @return true
     * @throws SQLException if connection to the database failed
     */
    public boolean deleteStudent(Long id) throws SQLException {
        this.studRepo.delete(id);
        return true;
    }

    /**
     * returns all Students from the repository
     * @return all Students
     * @throws SQLException if connection to the database failed
     */
    public List<Student> getAllStudents() throws SQLException, NullValueException {
        return this.studRepo.findAll();
    }

    /**
     * returns the Student with the id 'id'
     * @param id identifier
     * @return Student
     * @throws SQLException if connection to the database failed
     * @throws NullValueException if student is null
     */
    public Student findOne(Long id) throws SQLException, NullValueException {
        return this.studRepo.findOne(id);
    }

    public List<Student> sortStudentsByCredits() throws SQLException, NullValueException {
        List<Student> sortedStudents = this.getAllStudents()
                .stream()
                .sorted(Comparator.comparing(Student::getTotalCredits))
                .collect(Collectors.toList());

        return sortedStudents;
    }

    public List<Student> filterStudentsZeroCredits() throws SQLException, NullValueException {
        List<Student> filteredStudents = this.getAllStudents()
                .stream()
                .filter(s->s.getTotalCredits()==0)
                .collect(Collectors.toList());

        return filteredStudents;
    }

    public List<Student> enrolledStudents(String course) throws SQLException, NullValueException {
        if(course==null)
            return null;
        if(this.courseRepo.findOne(course)!=null){
            List<Student> studsEnrolled = new ArrayList<>();

            for(Student s : this.studRepo.findAll()){
                if(s.getEnrolledCoursesName().contains(course))
                    studsEnrolled.add(s);
            }

            return studsEnrolled;
        }
        return null;
    }

    public boolean addCourse(Course c) throws SQLException, NullValueException {
        this.courseRepo.save(c);
        return true;
    }

    public boolean deleteCourse(String course) throws SQLException, NullValueException {
        this.courseRepo.delete(course);
        return true;
    }


    public List<Course> getAllCourses() throws SQLException, NullValueException {
        return this.courseRepo.findAll();
    }

    public Course findOne(String name) throws SQLException, NullValueException {
        return this.courseRepo.findOne(name);
    }

    public List<Course> sortCoursesByCredits() throws SQLException, NullValueException {
        List<Course> sortedCourses = this.getAllCourses()
                .stream()
                .sorted(Comparator.comparing(Course::getCredits))
                .collect(Collectors.toList());

        return sortedCourses;
    }

    public List<Course> filterCoursesZeroStudents() throws SQLException, NullValueException {
        List<Course> filteredCourses = this.getAllCourses()
                .stream()
                .filter(c->c.getStudentsEnrolled().size() == 0)
                .collect(Collectors.toList());

        return filteredCourses;
    }

    public List<Course> availableCourses() throws SQLException, NullValueException {
        List<Course> availableCourses = this.getAllCourses()
                .stream()
                .filter(c->c.getStudentsEnrolled().size() != c.getMaxEnrollment())
                .collect(Collectors.toList());

        return availableCourses;
    }

    /**
     * Enrolls a Student to a Course
     * @param course Course object
     * @param student Student object
     * @return true if added
     * @throws Exception if student/course does not exist, stud already enrolled, max credits exceeded or course has no free places
     */
    public boolean registerStudentToCourse(Course course, Student student) throws Exception {

        //check if course exists in repo
        if (course == null || courseRepo.findOne(course.getName()) == null) {
            throw new Exception("Non-existing course id!");
        }

        //check if student exists in repo
        if (student == null || studRepo.findOne(student.getStudentId()) == null) {
            throw new Exception("Non-existing student id!");
        }
        List<Student> courseStudents = course.getStudentsEnrolled();
        //check if course has free places
        if (courseStudents.size() == course.getMaxEnrollment()) {
            throw new Exception("Course has no free places!");
        }

        //check if student is already enrolled

        boolean found = courseStudents
                .stream()
                .anyMatch(s -> s.getStudentId() == student.getStudentId());

        if (found)
            throw new Exception("Student already enrolled!");

        //if student has over 30 credits after enrolling
        int studCredits = student.getTotalCredits() + course.getCredits();
        if (studCredits > 30)
            throw new Exception("Total number of credits exceeded!");

        //add stud to course
        //update course repo
        courseStudents.add(student);
        course.setStudentsEnrolled(courseStudents);
        courseRepo.update(course);


        //update enrolled courses of stud
        List<Course> studCourses = student.getEnrolledCourses();
        studCourses.add(course);
        student.setEnrolledCourses(studCourses);

        //update studs Repo
        studRepo.update(student);

        return true;
    }

    public void modifyCredits(Course c) throws NullValueException, SQLException {

        this.courseRepo.update(c);
    }

    public Teacher findOneTeach(Long id) throws SQLException, NullValueException {
        return teachRepo.findOne(id);
    }

    public List<Teacher> getAllTeachers() throws SQLException, NullValueException {
        return this.teachRepo.findAll();
    }

    public List<Student> getStudsEnrolledToTeacher(Long teacherId) throws SQLException, NullValueException {
        List<Long> studId = this.teachRepo.getEnrolledStuds(teacherId);
        List<Student> students = new ArrayList<>();
        for(Long id : studId){
            students.add(this.studRepo.findOne(id));
        }

        return students;
    }

    public boolean addTeacher(Teacher t) throws SQLException {
        this.teachRepo.save(t);
        return true;
    }
}
