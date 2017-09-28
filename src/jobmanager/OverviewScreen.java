package jobmanager;

import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import jobmanager.model.Job;

//This screen gives an overview for what jobs are active and has links to all
//of the other screens
public class OverviewScreen extends ManagerScreen {

    //Variables for Array
    ArrayList<Job> jobs;
    int rows;
    int rowsMax;
    int rowHeight;
    int columns;
    int columnsMax;
    int columnWidth;

    Rectangle2D primaryScreenBounds;

    //Arbitrary value that compensates for loss in accuracy of height of top bar and bottom bar
    final int ADDITIONAL_HEIGHT = 38;

    //Restriction for how small the boxes can be
    final int MIN_COLUMN_WIDTH = 400;
    final int MIN_ROW_HEIGHT = 200;

    //Member variables for top bar
    Text titleText;
    HBox hboxTopBar;

    //Member Variables for center area
    FlowPane tiles;
    int titleTextSize;
    int descTextSize;

    int titleLabelHeight;
    int descLabelHeight;

    int spacingHeight;
    int titleSpacingHeight;
    final int MAX_SPACING = 30;

    //Member Variables for bottom bar
    Button addJobButton;
    HBox hboxBottomBar;

    //Other variables
    Scene scene;
    BorderPane borderPane;

    //Initializes the arraylist of jobs and related variables
    private void initJobList() {
        jobs = application.getJobList();

        calculateRows();
        calculateColumns();
    }

    //Initializes the elements in the top section of the borderpane
    private void initTopBar() {
        //HBox holding the text at the top
        hboxTopBar = new HBox();
        hboxTopBar.setId("topRow");
        hboxTopBar.setAlignment(Pos.CENTER);
        hboxTopBar.setPrefWidth(window.getWidth());

        //Text at the top of the screen
        titleText = new Text("Midwest Prefabrication Inc.");
        titleText.setId("titleText");
        titleText.setTextAlignment(TextAlignment.CENTER);

        hboxTopBar.getChildren().addAll(titleText);
    }

    //Initializes the elements in the bottom section of the borderpane
    private void initBottomBar() {
        //HBox holding buttoms at the bottom of the screen
        hboxBottomBar = new HBox();
        hboxBottomBar.setId("bottomRow");
        hboxBottomBar.setAlignment(Pos.CENTER);
        hboxBottomBar.setPrefWidth(window.getWidth());

        //Button that links to add job screen
        addJobButton = new Button("Add Job");
        addJobButton.setMinWidth(100);
        addJobButton.getStyleClass().add("bottomButton");

        hboxBottomBar.getChildren().addAll(addJobButton);
    }

    private void initCenterArea() {
        updateCenterContent();
    }

    //Initializes all of the components in the scene
    private void initComponents() {
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        initTopBar();
        initBottomBar();

        borderPane = new BorderPane();
        borderPane.setTop(hboxTopBar);
        borderPane.setBottom(hboxBottomBar);
    }

    public void createUI() {
        initComponents();

        Pane root = new Pane();
        root.getChildren().addAll(borderPane);
        scene = new Scene(root, window.getWidth(), window.getHeight());

        //Adding CSS file to scene
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("OverviewScreen.css").toExternalForm());

        window.setScene(scene);

        //Necessary so the top and bottom bars return the proper height
        hboxTopBar.applyCss();
        hboxTopBar.layout();
        hboxBottomBar.applyCss();
        hboxBottomBar.layout();

        window.show();

        //Have to do this after window.show() so that values of top and bottom
        //can be computed properly
        initJobList();
        initCenterArea();

        buttonEvents();

        //Resizes the width of the elements to fit the size of the screen
        scene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            //Arbitrary value that compensates for loss in accuracy of height of top bar and bottom bar
            final int ADDITIONAL_WIDTH = 16;

            hboxTopBar.setPrefWidth(window.getWidth() - ADDITIONAL_WIDTH);
            hboxBottomBar.setPrefWidth(window.getWidth() - ADDITIONAL_WIDTH);

            calculateColumns();
            updateCenterContent();
        });

        //Resizes the height of the elements to fit the size of the screen
        scene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            calculateRows();
            updateCenterContent();
        });

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            if (y > hboxTopBar.prefHeight(-1) && y < (window.getHeight() - hboxBottomBar.prefHeight(-1))) {
                int column = (int) ((x + 10) / columnWidth);
                int row = (int) ((y - hboxTopBar.prefHeight(-1)) / rowHeight);

                int location = (row * columns) + column;
                if (location > jobs.size() - 1) {
                    return;
                }

                //Creating a stage for a popup window
                JobEditScreen editJobScreen = new JobEditScreen();
                Stage popup = new Stage();
                popup.initModality(Modality.APPLICATION_MODAL);
                popup.setMinWidth(500);
                popup.setMinHeight(500);

                editJobScreen.setStage(popup);
                editJobScreen.setApp(application);
                boolean jobListChanged = editJobScreen.createUI(location);
                if (jobListChanged) {
                    jobs = application.getJobList();
                    calculateColumns();
                    calculateRows();
                    updateCenterContent();
                }
            }
        });

        window.maximizedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            if (t1) {
                primaryScreenBounds = Screen.getPrimary().getVisualBounds();

                //set Stage boundaries to visible bounds of the main screen
                window.setX(primaryScreenBounds.getMinX());
                window.setY(primaryScreenBounds.getMinY());
                window.setWidth(primaryScreenBounds.getWidth());
                window.setHeight(primaryScreenBounds.getHeight());
            }

            calculateColumns();
            calculateRows();
            updateCenterContent();
        });
    }

    private void buttonEvents() {
        addJobButton.setOnAction((ActionEvent e) -> {
            JobEditScreen editJobScreen = new JobEditScreen();

            //Creating a stage for a popup window
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setMinWidth(500);
            popup.setMinHeight(500);

            editJobScreen.setStage(popup);
            editJobScreen.setApp(application);
            boolean jobListChanged = editJobScreen.createUI();
            if (jobListChanged) {
                jobs = application.getJobList();
                calculateColumns();
                calculateRows();
                updateCenterContent();
            }
        });
    }

    //Calculates how many columns there can be and the width of each
    private void calculateColumns() {
        columnsMax = (int) (window.getWidth() / MIN_COLUMN_WIDTH);
    }

    //Calculates how many rows there can be and the height of each
    private void calculateRows() {
        rowsMax = (int) ((window.getHeight() - hboxTopBar.prefHeight(-1) - hboxBottomBar.prefHeight(-1) - ADDITIONAL_HEIGHT) / MIN_ROW_HEIGHT);
    }

    //Draws a new flowpane when called to adjust sizes of elements in window
    private void updateCenterContent() {
        //Calculating how many rows and columns
        if (jobs.isEmpty() || jobs.size() == 1) {
            rows = 1;
            columns = 1;
        }

        //Computing optimal size of columns and rows
        int rowSize;
        int columnSize;
        double sqrt = Math.sqrt(jobs.size());
        
        if (sqrt - Math.floor(sqrt) == 0)
        {
            rowSize = (int) sqrt;
            columnSize = (int) sqrt;
        }
        else if (sqrt - Math.floor(sqrt) < .5)
        {
            rowSize = (int) sqrt + 1;
            columnSize = (int) sqrt;
        }
        else
        {
            rowSize = (int) sqrt + 1;
            columnSize = (int) sqrt + 1;
        }

        if (rowSize <= rowsMax) {
            rows = rowSize;
        } else {
            rows = rowsMax;
        }

        if (columnSize <= columnsMax) {
            columns = columnSize;
        } else {
            columns = columnsMax;
        }
        
        rowHeight = (int) ((window.getHeight() - hboxTopBar.prefHeight(-1) - hboxBottomBar.prefHeight(-1) - ADDITIONAL_HEIGHT) / rows);
        columnWidth = (int) (window.getWidth() / columns);

        tiles = new FlowPane();
        tiles.setPrefWrapLength(window.getWidth());

        int counter;
        if (jobs.size() > (rows * columns)) {
            counter = rows * columns;
        } else {
            counter = jobs.size();
        }

        for (int i = 0; i < counter; i++) {
            Job job = jobs.get(i);

            setTextAndElementSize();

            //Holds the information about the job
            VBox vBox = new VBox();
            vBox.setPrefSize(columnWidth - (5 * (5 - columns)), rowHeight);
            vBox.setAlignment(Pos.TOP_CENTER);
            vBox.setSpacing(spacingHeight);

            //Element - Additional spacing between title and text below
            Label spacingLabel = new Label();
            spacingLabel.setPrefHeight(titleSpacingHeight);

            //Element - Job Title
            Label titleLabel = new Label(job.getName());
            titleLabel.setPrefHeight(titleLabelHeight);
            titleLabel.setFont(Font.font("Verdana", titleTextSize));
            titleLabel.setUnderline(true);
            titleLabel.getStyleClass().add("tileTitleText");

            //Element - Job Number
            Label numLabel = new Label("Job #" + job.getJobNumber());
            numLabel.setPrefHeight(descLabelHeight);
            numLabel.setFont(Font.font("Verdana", descTextSize));
            numLabel.getStyleClass().add("tileTitleText");

            //Element - Job Description
            Label descLabel = new Label(" - " + job.getDescription());
            descLabel.setPrefHeight(descLabelHeight);
            descLabel.setFont(Font.font("Verdana", descTextSize));
            descLabel.getStyleClass().add("tileTitleText");

            //Element - HBox holding Job Number and Job Description
            HBox numAndDescBox = new HBox();
            numAndDescBox.getChildren().addAll(numLabel, descLabel);

            //Element - Job Number
            Label assembliesLabel = new Label("Assemblies Remaining: " + (job.getTotalAssemblies() - job.getCompletedAssemblies()));
            assembliesLabel.setPrefHeight(descLabelHeight);
            assembliesLabel.setFont(Font.font("Verdana", descTextSize));
            assembliesLabel.getStyleClass().add("tileTitleText");

            //Element - Job Description
            Label hoursLabel = new Label(" - Estimated Hours Remaining: " + (job.getEstimatedHours() - job.getTotalHours()));
            hoursLabel.setPrefHeight(descLabelHeight);
            hoursLabel.setFont(Font.font("Verdana", descTextSize));
            hoursLabel.getStyleClass().add("tileTitleText");

            //Element - HBox holding Job Number and Job Description
            HBox assembliesAndHoursBox = new HBox();
            assembliesAndHoursBox.getChildren().addAll(assembliesLabel, hoursLabel);

            //Setting css for vBox
            vBox.getStyleClass().add("tiles");

            //Adding elements to the vBox
            vBox.getChildren().addAll(titleLabel, spacingLabel, numAndDescBox, assembliesAndHoursBox);

            //Adding vbox to tiles
            tiles.getChildren().add(vBox);
        }

        borderPane.setLeft(tiles);
    }

    //Adjusts text size and text holder size based on row height and column width
    private void setTextAndElementSize() {
        titleTextSize = columnWidth / 15;
        descTextSize = columnWidth / 40;
        if (columns == 1 && rows == 3) {
            titleTextSize = columnWidth / 25;
            descTextSize = columnWidth / 50;
        }
        if (columns == 1 && rows == 4) {
            titleTextSize = columnWidth / 40;
            descTextSize = columnWidth / 70;
        }

        titleLabelHeight = rowHeight / 10 * 2;
        descLabelHeight = rowHeight / 10 * 1;

        titleSpacingHeight = rowHeight / 20;
        if (titleSpacingHeight > MAX_SPACING) {
            titleSpacingHeight = MAX_SPACING;
        }

        spacingHeight = rowHeight / 20;
        if (spacingHeight > MAX_SPACING) {
            spacingHeight = MAX_SPACING;
        }
    }
}
