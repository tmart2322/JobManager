/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jobmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import java.util.ArrayList;
import javafx.scene.image.Image;

import jobmanager.model.Job;

public class JobManager extends Application {
    private Stage window;
    private ArrayList<Job> jobs;
    
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        
        initArrayListJobs();
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        
        //set Stage boundaries to visible bounds of the main screen
        window.setX(primaryScreenBounds.getMinX());
        window.setY(primaryScreenBounds.getMinY());
        window.setWidth(primaryScreenBounds.getWidth());
        window.setHeight(primaryScreenBounds.getHeight());
        window.setMaximized(true);
        
        //Initializes first window
        createOverviewScreen();
        
        //Shows the window
        window.setTitle("Job Manager");
        //window.getIcons().add(new Image("file:icon.png"));
        window.show();
    }
    
    private void initArrayListJobs() {
        jobs = new ArrayList<>();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public ArrayList<Job> getJobList() {
        return jobs;
    }
    
    public void setJobList(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }
    
    public void addJob(Job job)
    {
        jobs.add(job);
    }
    
    public void removeJob(int index)
    {
        jobs.remove(index);
    }
    
    private void createJobEditScreen() { 
        JobEditScreen jobEditScreen = new JobEditScreen();
        jobEditScreen.setApp(this);
        jobEditScreen.setStage(window);
        jobEditScreen.createUI();
    }
    
    private void createOverviewScreen() {
        OverviewScreen overview = new OverviewScreen();
        overview.setApp(this);
        overview.setStage(window);
        overview.createUI();
    }
    
    public void changeScreen(int screenNum) {
        if (screenNum == 1) {
            createOverviewScreen();
        }
    }
}
