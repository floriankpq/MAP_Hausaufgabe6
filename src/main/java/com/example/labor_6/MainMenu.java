package com.example.labor_6;

import com.example.labor_6.Controller.RegistrationSystem;
import com.example.labor_6.Exceptions.NullValueException;
import com.example.labor_6.Model.Student;
import com.example.labor_6.Model.Teacher;
import com.example.labor_6.Repository.CourseJdbcRepository;
import com.example.labor_6.Repository.StudentsJdbcRepository;
import com.example.labor_6.Repository.TeachersJdbcRepository;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class MainMenu extends Application implements EventHandler<ActionEvent> {

    Stage window;
    Button button;
    RadioButton stud,teach;
    ToggleGroup answer;
    HBox radios;
    VBox mainLayout;
    TextField field;

    private final RegistrationSystem controller;

    public MainMenu() throws SQLException {
        CourseJdbcRepository courseJdbcRepository = new CourseJdbcRepository();
        StudentsJdbcRepository studentsJdbcRepository = new StudentsJdbcRepository();
        TeachersJdbcRepository teachersJdbcRepository = new TeachersJdbcRepository();

        this.controller = new RegistrationSystem(studentsJdbcRepository,teachersJdbcRepository,courseJdbcRepository);
    }

    @Override
    public void start(Stage stage){
        //window
        window = stage;
        window.setTitle("Login Window");

        //vbox
        mainLayout = new VBox();
        mainLayout.setPrefWidth(300);
        mainLayout.setSpacing(20);
        mainLayout.setPadding(new Insets(10,10,10,10));

        //radios
        radios = new HBox();
        radios.setPrefWidth(300);
        radios.setSpacing(20);
        radios.setPadding(new Insets(0,0,0,0));

        // label
        Label question = new Label("Choose your user : ");
        Label id = new Label("ID :");

        //group+radio
        answer = new ToggleGroup();
        stud = new RadioButton("Student");
        stud.setToggleGroup(answer);
        teach = new RadioButton("Teacher");
        teach.setToggleGroup(answer);

        //form
        field = new TextField();

        //button
        button = new Button("Log in");
        button.setOnAction(this);

        //add elems to GUI
        radios.getChildren().addAll(stud,teach);
        mainLayout.getChildren().addAll(question);
        mainLayout.getChildren().addAll(radios);
        mainLayout.getChildren().addAll(id);
        mainLayout.getChildren().addAll(field,button);

        //center
        radios.setAlignment(Pos.CENTER);
        mainLayout.setAlignment(Pos.CENTER);

        //show window
        Scene main = new Scene(mainLayout, 300, 200);
        window.setScene(main);
        window.show();

    }

    @Override
    public void handle(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if(stud.isSelected()){
            long id = Long.parseLong(field.getText());
            Student s = null;

            try{
                s = this.controller.findOne(id);
            }catch (SQLException | NullValueException e){
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText("Error connecting to database");
                alert.showAndWait();
            }

            if(s==null){
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText("Student does not exist!");
                alert.showAndWait();
            }
            else{
                StudMenu studMenu = new StudMenu();
                studMenu.setId(id);
                Parent parent = null;
                try {
                    parent = studMenu.initialize();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle(s.getFirstName()+" "+s.getLastName());
                stage.setScene(new Scene(parent, 400, 250));
                stage.show();
            }

        }
        else if(teach.isSelected()){

            Long id = Long.parseLong(field.getText());
            Teacher t = null;

            try{
                t = this.controller.findOneTeach(id);
            }catch (SQLException | NullValueException e){
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText("Error connecting to database");
                alert.showAndWait();
            }

            if(t==null){
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText("Teacher does not exist!");
                alert.showAndWait();
            }
            else{
                TeachMenu teachMenu = new TeachMenu();
                teachMenu.setId(id);
                Parent parent = null;
                try {
                    parent = teachMenu.initialize();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle(t.getFirstName()+" "+t.getLastName());
                stage.setScene(new Scene(parent, 250, 120));
                stage.show();
            }
        }
        else{
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("None selected!");
            alert.showAndWait();
        }
    }
}