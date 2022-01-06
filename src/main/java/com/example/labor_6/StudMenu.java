package com.example.labor_6;

import com.example.labor_6.Controller.RegistrationSystem;
import com.example.labor_6.Exceptions.NullValueException;
import com.example.labor_6.Model.Course;
import com.example.labor_6.Model.Student;
import com.example.labor_6.Repository.CourseJdbcRepository;
import com.example.labor_6.Repository.StudentsJdbcRepository;
import com.example.labor_6.Repository.TeachersJdbcRepository;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class StudMenu{

    VBox mainLayout;
    Button button1, button2;
    TextField field;
    ListView<String> listView;

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    CourseJdbcRepository courseJdbcRepository;
    StudentsJdbcRepository studentsJdbcRepository;
    TeachersJdbcRepository teachersJdbcRepository;

    private RegistrationSystem controller;


    public Parent initialize() throws SQLException {

         courseJdbcRepository = new CourseJdbcRepository();
         studentsJdbcRepository = new StudentsJdbcRepository();
         teachersJdbcRepository = new TeachersJdbcRepository();

        this.controller = new RegistrationSystem(studentsJdbcRepository,teachersJdbcRepository,courseJdbcRepository);

        //vbox
        mainLayout = new VBox();
        mainLayout.setPrefWidth(300);
        mainLayout.setSpacing(20);
        mainLayout.setPadding(new Insets(10,10,10,10));

        //listView
        listView = new ListView<>();
        listView.setPrefWidth(530);

        //button
        button1 = new Button("Show credits");
        button2 = new Button("Enroll");

        button1.setOnAction(e->showCredits());
        button2.setOnAction(e->enroll());

        //label
        Label enroll = new Label("Enter the Name of the Course you want to enroll to");

        //form
        field = new TextField();

        //add gui
        mainLayout.getChildren().addAll(button1, listView);
        mainLayout.getChildren().addAll(enroll);
        mainLayout.getChildren().addAll(field);
        mainLayout.getChildren().addAll(button2);

        return mainLayout;
    }

    public void showCredits(){
        try {
            listView.getItems().clear();
            int credits = controller.findOne(this.id).getTotalCredits();
            listView.getItems().addAll("current credits : " +  credits);
        } catch (SQLException | NullValueException e) {
            e.printStackTrace();
        }
    }

    public void enroll(){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        String name = field.getText();
        Student s = null;

        try {
            s = controller.findOne(this.id);
        } catch (SQLException | NullValueException e) {
            e.printStackTrace();
        }

        Course c = null;
        try {
            c = controller.findOne(name);
        } catch (SQLException | NullValueException e) {
            e.printStackTrace();
        }

        if(c==null){
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Course does not exist!");
            alert.showAndWait();
        }else if(s!= null && s.getEnrolledCoursesName().contains(c.getName())){
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Student is already enrolled!");
            alert.showAndWait();
        }else if(c.getMaxEnrollment() == c.getStudentsEnrolled().size()){
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Course is full!");
            alert.showAndWait();
        }
        else if(s!=null && (s.getTotalCredits()+c.getCredits()>30)){
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Maximum number of credits exceeded!");
            alert.showAndWait();
        }
        else{
            try {
                controller.registerStudentToCourse(c,s);
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succes!");
                alert.setHeaderText("");
                alert.setContentText("Student enrolled succesfully!");
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

