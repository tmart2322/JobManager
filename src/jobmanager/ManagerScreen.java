package jobmanager;

import javafx.stage.Stage;

public abstract class ManagerScreen {
    protected JobManager application;
    protected Stage window;
    
    //Sets the main app
    public void setApp(JobManager application) {
        this.application = application;
    }
    
    //Sets the window the screen should be displayed in
    public void setStage(Stage window)
    {
        this.window = window;
    }
}