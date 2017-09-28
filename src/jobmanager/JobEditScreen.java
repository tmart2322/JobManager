package jobmanager;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jobmanager.model.Job;

public class JobEditScreen extends ManagerScreen {

    Job job;

    //Returns to creating window telling if anything was changed
    private boolean jobListChanged = false;

    //Size of text
    final int TEXT_SIZE = 20;

    //VBox that holds all components
    VBox root;

    //Job Number info
    HBox jobNum;
    Label jobNumLabel;
    TextField jobNumField;
    Label jobNumNeeded;

    //Job Name info
    HBox jobName;
    Label jobNameLabel;
    TextField jobNameField;
    Label jobNameNeeded;

    //Job Description info
    VBox jobDesc;
    Label jobDescLabel;
    TextArea jobDescField;
    Label jobDescNeeded;

    //Total Assemblies Needed info
    HBox assemblies;
    Label assembliesLabel;
    TextField assembliesField;
    Label assembliesNeeded;

    //Estimated Hours info
    HBox hours;
    Label hoursLabel;
    TextField hoursField;
    Label hoursNeeded;

    //Buttons
    HBox buttons;
    Button saveButton;
    Button cancelButton;
    Button removeButton;

    //Blank window for creating a new job
    public boolean createUI() {
        initComponents();

        saveButton.setText("Add");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (inputChecking()) {
                    jobListChanged = true;
                    job = new Job(Integer.parseInt(jobNumField.getText()),
                            jobNameField.getText(), jobDescField.getText(),
                            Integer.parseInt(assembliesField.getText()),
                            Integer.parseInt(hoursField.getText()));
                    application.addJob(job);
                    window.close();
                }
            }
        });

        root = new VBox();
        root.setSpacing(15);
        root.getChildren().addAll(jobNum, jobName, assemblies, hours, jobDesc, buttons);

        Scene scene = new Scene(root, window.getWidth(), window.getHeight());

        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("JobEditScreen.css").toExternalForm());

        window.setScene(scene);

        window.setTitle("Add Job");

        window.setResizable(false);

        window.showAndWait();

        return jobListChanged;
    }

    //Editing an existing job
    public boolean createUI(int index) {
        job = application.getJobList().get(index);

        initComponents();

        //Remove Button
        Image imageRemove = new Image(getClass().getResourceAsStream("remove.png"));
        ImageView imageViewRemove = new ImageView(imageRemove);
        imageViewRemove.setFitWidth(25);
        imageViewRemove.setFitHeight(25);
        removeButton = new Button("Remove", imageViewRemove);
        removeButton.setPrefHeight(40);
        removeButton.setPrefWidth(150);
        removeButton.setFont(Font.font("Verdana", TEXT_SIZE));
        removeButton.setId("removeButton");
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                jobListChanged = true;
                application.removeJob(index);
                window.close();
            }
        });

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (inputChecking()) {
                    jobListChanged = true;
                    ArrayList<Job> jobList = application.getJobList();
                    job = new Job(Integer.parseInt(jobNumField.getText()),
                            jobNameField.getText(), jobDescField.getText(),
                            Integer.parseInt(assembliesField.getText()),
                            Integer.parseInt(hoursField.getText()));
                    jobList.set(index, job);
                    application.setJobList(jobList);
                    window.close();
                } else {
                    System.out.println("Incorrect input!");
                }
            }
        });

        cancelButton.setPrefWidth(150);
        saveButton.setPrefWidth(150);
        buttons.getChildren().add(removeButton);

        jobNumField.setText(Integer.toString(job.getJobNumber()));
        jobNameField.setText(job.getName());
        jobDescField.setText(job.getDescription());
        assembliesField.setText(Integer.toString(job.getTotalAssemblies()));
        hoursField.setText(Integer.toString(job.getEstimatedHours()));

        root = new VBox();
        root.setSpacing(15);
        root.getChildren().addAll(jobNum, jobName, assemblies, hours, jobDesc, buttons);

        Scene scene = new Scene(root, window.getWidth(), window.getHeight());

        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("JobEditScreen.css").toExternalForm());

        window.setScene(scene);

        window.setTitle("Add Job");

        window.setResizable(false);

        window.showAndWait();

        return jobListChanged;
    }

    private void initComponents() {
        //Job Number info
        jobNumLabel = new Label("Job Number: ");
        jobNumLabel.setPrefHeight(40);
        jobNumLabel.setPrefWidth(160);
        jobNumLabel.setFont(Font.font("Verdana", TEXT_SIZE));
        jobNumLabel.setAlignment(Pos.CENTER_RIGHT);
        jobNumLabel.getStyleClass().add("labelText");

        jobNumField = new TextField();
        jobNumField.setPrefHeight(40);
        jobNumField.setPrefWidth(300);
        jobNumField.setFont(Font.font("Verdana", TEXT_SIZE));

        jobNum = new HBox(jobNumLabel, jobNumField);
        jobNum.setAlignment(Pos.CENTER_LEFT);
        jobNum.getStyleClass().add("HBox");

        //Job Name info
        jobNameLabel = new Label("Job Name: ");
        jobNameLabel.setPrefHeight(40);
        jobNameLabel.setPrefWidth(160);
        jobNameLabel.setFont(Font.font("Verdana", TEXT_SIZE));
        jobNameLabel.setAlignment(Pos.CENTER_RIGHT);
        jobNameLabel.getStyleClass().add("labelText");

        jobNameField = new TextField();
        jobNameField.setPrefHeight(40);
        jobNameField.setPrefWidth(300);
        jobNameField.setFont(Font.font("Verdana", TEXT_SIZE));

        jobName = new HBox(jobNameLabel, jobNameField);
        jobName.setAlignment(Pos.CENTER_LEFT);
        jobName.getStyleClass().add("HBox");

        //Job Assemblies info
        assembliesLabel = new Label("Assemblies: ");
        assembliesLabel.setPrefHeight(40);
        assembliesLabel.setPrefWidth(160);
        assembliesLabel.setFont(Font.font("Verdana", TEXT_SIZE));
        assembliesLabel.setAlignment(Pos.CENTER_RIGHT);
        assembliesLabel.getStyleClass().add("labelText");

        assembliesField = new TextField();
        assembliesField.setPrefHeight(40);
        assembliesField.setPrefWidth(300);
        assembliesField.setFont(Font.font("Verdana", TEXT_SIZE));

        assemblies = new HBox(assembliesLabel, assembliesField);
        assemblies.setAlignment(Pos.CENTER_LEFT);
        assemblies.getStyleClass().add("HBox");

        //Job Hours info
        hoursLabel = new Label("Est. Hours: ");
        hoursLabel.setPrefHeight(40);
        hoursLabel.setPrefWidth(160);
        hoursLabel.setFont(Font.font("Verdana", TEXT_SIZE));
        hoursLabel.setAlignment(Pos.CENTER_RIGHT);
        hoursLabel.getStyleClass().add("labelText");

        hoursField = new TextField();
        hoursField.setPrefHeight(40);
        hoursField.setPrefWidth(300);
        hoursField.setFont(Font.font("Verdana", TEXT_SIZE));

        hours = new HBox(hoursLabel, hoursField);
        hours.setAlignment(Pos.CENTER_LEFT);
        hours.getStyleClass().add("HBox");

        //Job Description info
        jobDescLabel = new Label("Description");
        jobDescLabel.setPrefHeight(40);
        jobDescLabel.setPrefWidth(160);
        jobDescLabel.setFont(Font.font("Verdana", TEXT_SIZE));
        jobDescLabel.setAlignment(Pos.CENTER);
        jobDescLabel.getStyleClass().add("labelText");

        jobDescField = new TextArea();
        jobDescField.setPrefHeight(80);
        jobDescField.setPrefWidth(300);
        jobDescField.setFont(Font.font("Verdana", TEXT_SIZE));
        jobDescField.setWrapText(true);

        jobDesc = new VBox(jobDescLabel, jobDescField);
        jobDesc.setAlignment(Pos.CENTER);
        jobDesc.getStyleClass().add("HBox");

        //Buttons
        //Save Button
        Image imageCheck = new Image(getClass().getResourceAsStream("checkmark.png"));
        ImageView imageViewCheck = new ImageView(imageCheck);
        imageViewCheck.setFitWidth(25);
        imageViewCheck.setFitHeight(25);
        saveButton = new Button("Save", imageViewCheck);
        saveButton.setPrefHeight(40);
        saveButton.setPrefWidth(260);
        saveButton.setFont(Font.font("Verdana", TEXT_SIZE));
        saveButton.setId("saveButton");

        //Cancel button
        Image imageCancel = new Image(getClass().getResourceAsStream("cancel.png"));
        ImageView imageViewCancel = new ImageView(imageCancel);
        imageViewCancel.setFitWidth(25);
        imageViewCancel.setFitHeight(25);
        cancelButton = new Button("Cancel", imageViewCancel);
        cancelButton.setPrefHeight(40);
        cancelButton.setPrefWidth(260);
        cancelButton.setFont(Font.font("Verdana", TEXT_SIZE));
        cancelButton.setId("cancelButton");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                jobListChanged = false;
                window.close();
            }
        });

        //Hbox for buttons
        buttons = new HBox(saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setSpacing(20);
        buttons.getStyleClass().add("HBox");
    }

    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean inputChecking() {
        boolean goodInput = true;
        if (jobNumField.getText().equals("")) {
            System.out.println("Job number cannot be blank!");
            goodInput = false;
        } else if (!isInteger(jobNumField.getText())) {
            System.out.println("Job number must be a number!");
            goodInput = false;
        }

        if (assembliesField.getText().equals("")) {
            System.out.println("Job assemblies cannot be blank!");
            goodInput = false;
        } else if (!isInteger(assembliesField.getText())) {
            System.out.println("Job assemblies must be a number!");
            goodInput = false;
        }

        if (hoursField.getText().equals("")) {
            System.out.println("Job hours cannot be blank!");
            goodInput = false;
        } else if (!isInteger(hoursField.getText())) {
            System.out.println("Job hours must be a number!");
            goodInput = false;
        }

        if (jobNameField.getText().equals("")) {
            System.out.println("Job name cannot be blank!");
            goodInput = false;
        }

        if (jobDescField.getText().equals("")) {
            System.out.println("Job description cannot be blank!");
            goodInput = false;
        }
        return goodInput;
    }
}
