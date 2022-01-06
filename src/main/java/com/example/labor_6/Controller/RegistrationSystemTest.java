package com.example.labor_6.Controller;

import com.example.labor_6.Exceptions.NullValueException;
import com.example.labor_6.Model.Course;
import com.example.labor_6.Model.Student;
import com.example.labor_6.Model.Teacher;
import com.example.labor_6.Repository.CourseJdbcRepository;
import com.example.labor_6.Repository.StudentsJdbcRepository;
import com.example.labor_6.Repository.TeachersJdbcRepository;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationSystemTest {

    StudentsJdbcRepository studRepo = Mockito.mock(StudentsJdbcRepository.class);
    TeachersJdbcRepository teachRepo = Mockito.mock(TeachersJdbcRepository.class);
    CourseJdbcRepository courseRepo = Mockito.mock(CourseJdbcRepository.class);

    RegistrationSystem controller;

    Student s1 = new Student(1L,"a","a",new ArrayList<>());
    Student s2 = new Student(2L,"b","b",new ArrayList<>());
    Student s3 = new Student(3L,"c","c",new ArrayList<>());

    Teacher t1 = new Teacher(10L, "t", "t");

    Course c1 = new Course("c1", t1, 6, 20);
    Course c2 = new Course("c2", t1, 6, 6);
    Course c3 = new Course("c3", t1, 6, 10);

    Course c1copy = new Course("c", t1, 7, 30);

    @BeforeEach
    void setUp() throws Exception {
        controller = new RegistrationSystem(studRepo,teachRepo,courseRepo);
        Mockito.when(controller.addTeacher(t1)).thenReturn(true);

        Mockito.when(controller.addCourse(c1)).thenReturn(true);
        Mockito.when(controller.addCourse(c1copy)).thenReturn(null);//same id
        Mockito.when(controller.findOne("c")).thenReturn(c1);

        Mockito.when(controller.addStudent(s1)).thenReturn(true);
        Mockito.when(controller.findOne(1L)).thenReturn(s1);
    }


    @org.junit.jupiter.api.Test
    void addStudent() {
    }

    @org.junit.jupiter.api.Test
    void deleteStudent() {
    }

    @org.junit.jupiter.api.Test
    void getAllStudents() {
    }

    @org.junit.jupiter.api.Test
    void findOne() {
    }

    @org.junit.jupiter.api.Test
    void sortStudentsByCredits() {
    }

    @org.junit.jupiter.api.Test
    void filterStudentsZeroCredits() {
    }

    @org.junit.jupiter.api.Test
    void enrolledStudents() {
    }

    @org.junit.jupiter.api.Test
    void addCourse() {
    }

    @org.junit.jupiter.api.Test
    void deleteCourse() {
    }

    @org.junit.jupiter.api.Test
    void getAllCourses() {
    }

    @org.junit.jupiter.api.Test
    void testFindOne() {
    }

    @org.junit.jupiter.api.Test
    void sortCoursesByCredits() {
    }

    @org.junit.jupiter.api.Test
    void filterCoursesZeroStudents() {
    }

    @org.junit.jupiter.api.Test
    void availableCourses() {
    }

    @org.junit.jupiter.api.Test
    void registerStudentToCourse() {
    }

    @org.junit.jupiter.api.Test
    void modifyCredits() {
    }

    @org.junit.jupiter.api.Test
    void findOneTeach() {
    }

    @org.junit.jupiter.api.Test
    void getAllTeachers() {
    }

    @org.junit.jupiter.api.Test
    void getStudsEnrolledToTeacher() {
    }
}