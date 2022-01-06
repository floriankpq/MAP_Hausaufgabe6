package com.example.labor_6;

import com.example.labor_6.Controller.RegistrationSystem;
import com.example.labor_6.Exceptions.NullValueException;
import com.example.labor_6.Model.Student;
import com.example.labor_6.Repository.CourseJdbcRepository;
import com.example.labor_6.Repository.StudentsJdbcRepository;
import com.example.labor_6.Repository.TeachersJdbcRepository;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.sql.SQLException;

public class TeachMenu {

    VBox mainLayout;
    Button button1;
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
        button1 = new Button("Show Enrolled Students");
        button1.setOnAction(e->showEnrolled());

        //add gui
        mainLayout.getChildren().addAll(button1, listView);

        return mainLayout;
    }

    public void showEnrolled(){
        try {
            listView.getItems().clear();
            for(Student s : controller.getStudsEnrolledToTeacher(this.id)){
                listView.getItems().add(s.toString());
            }
        } catch (SQLException | NullValueException e) {
            e.printStackTrace();
        }
    }
}
