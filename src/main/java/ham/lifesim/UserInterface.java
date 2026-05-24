/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ham.lifesim;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UserInterface extends Application {

    // Instantiate our game core logic brain
    private final Game gameEngine = new Game();
    

    // Dashboard UI Components
    private Label ageLabel;
    private Label healthLabel;
    private Label energyLabel;
    private Label balanceLabel;
    
    // Career UI Components
    private ListView<String> jobListView;
    private Label jobTitleDetailLabel;
    private Label jobWageDetailLabel;
    private Label jobReqsDetailLabel;
    private Button applyButton;
    private Label currentJobStatusLabel;
    
    // --- FINANCES UI (INCOME STATEMENT COMPONENTS) ---
    private Label statementTitleLabel;
    private Label revSalaryLabel;
    private Label grossRevenueLabel;
    private Label expTaxesLabel;
    private Label expLivingLabel;
    private Label totalExpensesLabel;
    private Label netIncomeLabel;
    
    // --- TIME ALLOCATION UI COMPONENTS ---
    private Label hoursRemainingLabel;
    private Slider sleepSlider, workSlider, studySlider, socialSlider, exerciseSlider;
    private Label sleepValLabel, workValLabel, studyValLabel, socialValLabel, exerciseValLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Life Simulator - Engine Sandbox");

        VBox mainLayout = new VBox();
        TabPane tabPane = new TabPane();
        VBox.setVgrow(tabPane, Priority.ALWAYS); 
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // --- 1. BUILD DASHBOARD TAB WITH LIVE METRICS ---
        Tab dashboardTab = new Tab("Dashboard");
        VBox dashboardContent = new VBox(15); // 15px spacing between elements
        dashboardContent.setStyle("-fx-padding: 30px; -fx-alignment: center-left;");

        // Initialize text readouts
        ageLabel = new Label("Age: " + gameEngine.getAgeYears() + " (" + gameEngine.getCurrentMonthName() + ", Week " + (gameEngine.getAgeWeeks() + 1) + ")");
        healthLabel = new Label("Health: " + gameEngine.getHealth() + "%");
        energyLabel = new Label("Energy: " + gameEngine.getEnergy() + "%");
        balanceLabel = new Label("Bank Balance: $" + String.format("%.2f", gameEngine.getBankBalance()));

        // Style the text a bit so it's easy to read
        String labelStyle = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;";
        ageLabel.setStyle(labelStyle);
        healthLabel.setStyle(labelStyle);
        energyLabel.setStyle(labelStyle);
        balanceLabel.setStyle(labelStyle);

        // Add labels to dashboard container
        dashboardContent.getChildren().addAll(ageLabel, healthLabel, energyLabel, balanceLabel);
        dashboardTab.setContent(dashboardContent);
        
        

        // --- 2. BUILD THE FUNCTIONAL CAREER TAB ---
        Tab careerTab = new Tab("Career & Education");
        HBox careerContent = new HBox(20); // Side-by-side layout (List on left, details on right)
        careerContent.setStyle("-fx-padding: 20px;");

        // Left Side: The List of available jobs
        jobListView = new ListView<>();
        HBox.setHgrow(jobListView, Priority.ALWAYS);
        
        // Populate the list view with titles from our JobManager
        for (Job job : gameEngine.getJobManager().getAvailableJobs()) {
            jobListView.getItems().add(job.getTitle());
        }

        // Right Side: Job Details Panel
        VBox detailsPanel = new VBox(15);
        detailsPanel.setStyle("-fx-padding: 10px; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-pref-width: 350px;");
        
        currentJobStatusLabel = new Label("Current Status: Unemployed");
        currentJobStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #aa0000;");
        
        jobTitleDetailLabel = new Label("Select a job from the market");
        jobTitleDetailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        jobWageDetailLabel = new Label("Wage: --");
        jobReqsDetailLabel = new Label("Requirements: --");
        
        applyButton = new Button("Apply for Job");
        applyButton.setDisable(true); // Disable until they select something

        detailsPanel.getChildren().addAll(currentJobStatusLabel, new Separator(), jobTitleDetailLabel, jobWageDetailLabel, jobReqsDetailLabel, applyButton);
        
        careerContent.getChildren().addAll(jobListView, detailsPanel);
        careerTab.setContent(careerContent);

        // --- 3. EVENT LISTENERS (UI INTERACTIONS) ---
        
        // Listen for when a user clicks an item in the Job List
        jobListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int index = newValue.intValue();
            if (index >= 0) {
                Job selectedJob = gameEngine.getJobManager().getAvailableJobs().get(index);
                
                // Update the detail text panel
                jobTitleDetailLabel.setText(selectedJob.getTitle());
                jobWageDetailLabel.setText(String.format("Wage: $%.2f / hour", selectedJob.getHourlyWage()));
                jobReqsDetailLabel.setText("Requires: Smarts " + selectedJob.getRequiredSmarts() + ", Degree: " + selectedJob.getRequiredDegree());
                
                applyButton.setDisable(false);
            }
        });
        
        

        // Action when "Apply for Job" button is clicked
        applyButton.setOnAction(event -> {
            int index = jobListView.getSelectionModel().getSelectedIndex();
            Job selectedJob = gameEngine.getJobManager().getAvailableJobs().get(index);
            
            // For now, passing hardcoded 100 smarts and "None" degree baseline to test hiring
            boolean hired = gameEngine.getJobManager().tryApplyForJob(selectedJob, 100, "None");
            
            if (hired) {
                currentJobStatusLabel.setText("Current Status: Employed as " + selectedJob.getTitle());
                currentJobStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #00aa00;");
                refreshWorkSliderConstraints();
            } else {
                // Flash red if requirements fail
                Alert alert = new Alert(Alert.AlertType.WARNING, "You do not meet the qualifications for this job!");
                alert.showAndWait();
            }
        });
        
        // --- 2. FINANCES TAB (INCOME STATEMENT STYLE) ---
        Tab financesTab = new Tab("Finances");
        VBox financesContent = new VBox(10);
        financesContent.setStyle("-fx-padding: 40px; -fx-font-family: 'Courier New'; -fx-font-size: 15px;");

        // Statement Header
        statementTitleLabel = new Label("PERSONAL INCOME STATEMENT\nFor the Month of January");
        statementTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-alignment: center;");
        
        // Revenue Section
        Label revHeader = new Label("REVENUE / INFLOWS");
        revHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        revSalaryLabel     = new Label("  Gross Wages/Salary:        $0.00");
        grossRevenueLabel  = new Label("TOTAL REVENUE:               $0.00");
        grossRevenueLabel.setStyle("-fx-font-weight: bold;");

        // Expenses Section
        Label expHeader = new Label("EXPENSES / OUTFLOWS");
        expHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #c62828;");
        expTaxesLabel      = new Label("  Income Tax Deductions:     $0.00");
        expLivingLabel     = new Label("  Basic Survival Expenses:   $0.00");
        totalExpensesLabel = new Label("TOTAL EXPENSES:              $0.00");
        totalExpensesLabel.setStyle("-fx-font-weight: bold;");

        // Bottom Line Net Income
        Separator netSeparator = new Separator();
        netIncomeLabel     = new Label("NET SURPLUS / (DEFICIT):     $0.00");
        netIncomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Assemble the Statement layout
        financesContent.getChildren().addAll(
            statementTitleLabel, new Separator(),
            revHeader, revSalaryLabel, grossRevenueLabel, new Separator(),
            expHeader, expTaxesLabel, expLivingLabel, totalExpensesLabel,
            netSeparator, netIncomeLabel
        );
        financesTab.setContent(financesContent);

        // --- 2. TIME ALLOCATION TAB (LOCKING SLIDERS) ---
        Tab timeTab = new Tab("Time Allocation");
        VBox timeContent = new VBox(20);
        timeContent.setStyle("-fx-padding: 30px;");

        // Central Tracker Header
        hoursRemainingLabel = new Label("Hours Allocated: 168 / 168 (0 Hours Unassigned)");
        hoursRemainingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        // Manufacture our custom "snapping/locking" sliders
        sleepSlider = createLockingSlider(56); // Starting base values matching constructor
        workSlider = createLockingSlider(40);
        studySlider = createLockingSlider(0);
        socialSlider = createLockingSlider(62);
        exerciseSlider = createLockingSlider(10);

        // Value text readouts to show next to sliders
        sleepValLabel    = new Label("Sleep: 56 hrs/wk");
        workValLabel     = new Label("Work: 40 hrs/wk");
        studyValLabel    = new Label("Study: 0 hrs/wk");
        socialValLabel   = new Label("Social: 62 hrs/wk");
        exerciseValLabel = new Label("Exercise: 10 hrs/wk");

        String sliderLabelStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-pref-width: 150px;";
        sleepValLabel.setStyle(sliderLabelStyle);
        workValLabel.setStyle(sliderLabelStyle);
        studyValLabel.setStyle(sliderLabelStyle);
        socialValLabel.setStyle(sliderLabelStyle);
        exerciseValLabel.setStyle(sliderLabelStyle);

        // Build interactive rows layout
        timeContent.getChildren().add(hoursRemainingLabel);
        timeContent.getChildren().add(new Separator());
        timeContent.getChildren().add(new HBox(15, sleepValLabel, sleepSlider));
        timeContent.getChildren().add(new HBox(15, workValLabel, workSlider));
        timeContent.getChildren().add(new HBox(15, studyValLabel, studySlider));
        timeContent.getChildren().add(new HBox(15, socialValLabel, socialSlider));
        timeContent.getChildren().add(new HBox(15, exerciseValLabel, exerciseSlider));

        timeTab.setContent(timeContent);
        
        // --- 5. SLIDER ACTION LISTENERS (REAL-TIME INPUT MATCHING) ---
        // Setup change listeners for every single slider to validate total time allocation
        sleepSlider.valueProperty().addListener((obs, oldV, newV) -> recalculateScheduleAllocation());
        workSlider.valueProperty().addListener((obs, oldV, newV) -> recalculateScheduleAllocation());
        studySlider.valueProperty().addListener((obs, oldV, newV) -> recalculateScheduleAllocation());
        socialSlider.valueProperty().addListener((obs, oldV, newV) -> recalculateScheduleAllocation());
        exerciseSlider.valueProperty().addListener((obs, oldV, newV) -> recalculateScheduleAllocation());
        // --- 4. ASSEMBLE REMAINING PLACEHOLDER TABS ---
        Tab wealthTab = createEmptyTab("Wealth & Market", "Manage your stock portfolios, bonds, and real estate here.");
        

        tabPane.getTabs().addAll(dashboardTab, timeTab, financesTab, wealthTab, careerTab);
        mainLayout.getChildren().add(tabPane);
        
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // --- 3. START THE GAME LOOP TIMELINE ---
        updateDashboardUI();
        updateFinanceUI();
        refreshWorkSliderConstraints();
        setupGameLoop();
    }

    /**
     * Sets up a background timer that fires every 2 seconds to simulate a week passing.
     */
    private void setupGameLoop() {
        // Define a keyframe that triggers our tick action every 2 seconds
        KeyFrame frame = new KeyFrame(Duration.seconds(2.0), event -> {
            
            // 1. Run the background logic processing (math updates)
            gameEngine.tickWeeklyLoop();
            
            // 2. Refresh the UI labels with the brand new calculated data
            updateDashboardUI();
            updateFinanceUI();
        });

        // Assemble the timeline loop
        Timeline gameLoop = new Timeline(frame);
        gameLoop.setCycleCount(Timeline.INDEFINITE); // Keep running forever
        gameLoop.play(); // Start the clock!
    }

    /**
     * Pulls latest values from the engine and pushes them to the screen elements.
     */
    private void updateDashboardUI() {
        ageLabel.setText("Age: " + gameEngine.getAgeYears() + " (" + gameEngine.getCurrentMonthName() + ", Week " + (gameEngine.getAgeWeeks() + 1) + ")");
        healthLabel.setText("Health: " + String.format("%.1f", gameEngine.getHealth()) + "%");
        energyLabel.setText("Energy: " + String.format("%.1f", gameEngine.getEnergy()) + "%");
        balanceLabel.setText("Bank Balance: $" + String.format("%.2f", gameEngine.getBankBalance()));
    }
    
    private void updateFinanceUI() {
        statementTitleLabel.setText("PERSONAL INCOME STATEMENT\nFor the Month of " + gameEngine.getCurrentMonthName());

        // 1. Determine how many weeks are in the current 4-4-5 month dynamically
        int currentWeek = gameEngine.getAgeWeeks() + 1;
        int weeksInThisMonth = 4;
        if (currentWeek == 9 || currentWeek == 13 || currentWeek == 22 || currentWeek == 26 || 
            currentWeek == 35 || currentWeek == 39 || currentWeek == 48 || currentWeek == 52) {
            weeksInThisMonth = 5;
        }

        // 2. Extract weekly baseline data from our game models
        int currentWorkHoursInput = (int) workSlider.getValue();
        //checking if current hours exceeds limit
        Job currentJob = gameEngine.getJobManager().getCurrentJob();

        if (currentJob == null) {
            // decide your fallback behavior
            // option A: no job means no work allowed
            return; // or set to 0, or throw, depending on design
        }

        int min = currentJob.getMinHours();
        int max = currentJob.getMaxHours();

        currentWorkHoursInput = Math.max(min, Math.min(currentWorkHoursInput, max));
        
        
        
        double hourlyWage = (gameEngine.getJobManager().getCurrentJob() != null) ? gameEngine.getJobManager().getCurrentJob().getHourlyWage() : 0.0;
        double weeklyGrossIncome = currentWorkHoursInput * hourlyWage; 
        double weeklyTaxes = weeklyGrossIncome * 0.15;
        double weeklyLivingExpenses = 250.00;

        // 3. Multiply by calendar period modifier (4 or 5 weeks)
        double monthlyGross = weeklyGrossIncome * weeksInThisMonth;
        double monthlyTaxes = weeklyTaxes * weeksInThisMonth;
        double monthlyLiving = weeklyLivingExpenses * weeksInThisMonth;
        double monthlyExpenses = monthlyTaxes + monthlyLiving;
        double monthlyNetIncome = monthlyGross - monthlyExpenses;

        // 4. Update Statement Labels
        revSalaryLabel.setText(String.format("  Gross Wages/Salary:        $%,.2f", monthlyGross));
        grossRevenueLabel.setText(String.format("TOTAL REVENUE:               $%,.2f", monthlyGross));

        expTaxesLabel.setText(String.format("  Income Tax Deductions:     $%,.2f", monthlyTaxes));
        expLivingLabel.setText(String.format("  Basic Survival Expenses:   $%,.2f", monthlyLiving));
        totalExpensesLabel.setText(String.format("TOTAL EXPENSES:              $%,.2f", monthlyExpenses));

        netIncomeLabel.setText(String.format("NET SURPLUS / (DEFICIT):     $%,.2f", monthlyNetIncome));
        
        // Highlight deficits vs profits
        if (monthlyNetIncome >= 0) {
            netIncomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        } else {
            netIncomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #c62828;");
        }
    }
    
    /**
     * Factory method creating specialized sliders that jump/lock explicitly 
     * to single whole hours (integers) only.
     */
    private Slider createLockingSlider(int startingValue) {
        Slider slider = new Slider(0, 168, startingValue);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true); // Forces the cursor slider hook to lock perfectly to whole units
        HBox.setHgrow(slider, Priority.ALWAYS);
        return slider;
    }

    /**
     * Live processing mechanism running every time a slider slides.
     * Computes totals and validates limits dynamically.
     */
    private void recalculateScheduleAllocation() {
        // 1. Read all the "active choice" sliders first
        int work = (int) workSlider.getValue();
        int study = (int) studySlider.getValue();
        int social = (int) socialSlider.getValue();
        int exercise = (int) exerciseSlider.getValue();

        // 2. Calculate exactly how many hours are left out of 168 for sleep
        int hoursLeftForSleep = 168 - (work + study + social + exercise);

        // 3. Dynamic Buffer Safeguard: 
        // If they cranked other sliders so high that hoursLeftForSleep drops below 0,
        // we need to push back on the slider they just dragged.
        if (hoursLeftForSleep < 0) {
            // This means they went over budget, so we force Sleep's max track to 0
            sleepSlider.setMax(0);
            sleepSlider.setValue(0);
        } else {
            // The magic line: Sleep's ceiling dynamically shrinks or grows!
            sleepSlider.setMax(hoursLeftForSleep);

            // Automatically push sleep to fill up the exact remainder of the 168-hour week
            sleepSlider.setValue(hoursLeftForSleep);
        }

        // 4. Now read the final locked integer value of sleep for the backend
        int sleep = (int) sleepSlider.getValue();

        // --- (The rest of your label updates and gameEngine.setSchedule call stay exactly the same!) ---
        sleepValLabel.setText("Sleep: " + sleep + " hrs/wk");
        workValLabel.setText("Work: " + work + " hrs/wk");
        studyValLabel.setText("Study: " + study + " hrs/wk");
        socialValLabel.setText("Social: " + social + " hrs/wk");
        exerciseValLabel.setText("Exercise: " + exercise + " hrs/wk");

        hoursRemainingLabel.setText("Hours Allocated: 168 / 168 (Sleep dynamically adjusted)");
        gameEngine.setSchedule(sleep, work, study, social, exercise);
    }
    
    private void refreshWorkSliderConstraints() {
    Job currentJob = gameEngine.getJobManager().getCurrentJob();

    if (currentJob != null) {
        // Fetch constraints from the active job contract
        int minAllowed = currentJob.getMinHours(); // e.g., 37
        int maxAllowed = currentJob.getMaxHours(); // e.g., 40

        workSlider.setMin(minAllowed);
        workSlider.setMax(maxAllowed);

        // Clamping current value safely inside new bounds so the slider doesn't glitch
        if (workSlider.getValue() < minAllowed) workSlider.setValue(minAllowed);
        if (workSlider.getValue() > maxAllowed) workSlider.setValue(maxAllowed);
    } else {
        // Unemployed baseline
        workSlider.setMin(0);
        workSlider.setMax(0); // Can't allocate work hours if you don't have a job!
        workSlider.setValue(0);
    }
}
    
    private Tab createEmptyTab(String title, String placeholderText) {
        Tab tab = new Tab(title);
        VBox contentContainer = new VBox();
        contentContainer.setStyle("-fx-padding: 20px; -fx-alignment: center;");
        Label placeholderLabel = new Label(placeholderText);
        placeholderLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");
        contentContainer.getChildren().add(placeholderLabel);
        tab.setContent(contentContainer);
        return tab;
    }
}