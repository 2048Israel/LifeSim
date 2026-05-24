/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ham.lifesim;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UserInterface extends Application {

    // Instantiate our game core logic brain
    private final Game gameEngine = new Game();

    // UI Labels that need to update dynamically every week
    private Label ageLabel;
    private Label healthLabel;
    private Label energyLabel;
    private Label balanceLabel;

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

        // --- 2. BUILD THE OTHER PLAIN CONFIGURATION TABS ---
        Tab timeTab = createEmptyTab("Time Allocation", "Manage your 168-hour weekly time budget here.");
        Tab financesTab = createEmptyTab("Finances", "Track income vs expenses and balance your cash flow here.");
        Tab wealthTab = createEmptyTab("Wealth & Market", "Manage your stock portfolios, bonds, and real estate here.");
        Tab careerTab = createEmptyTab("Career & Education", "Apply for positions and manage educational qualifications here.");

        tabPane.getTabs().addAll(dashboardTab, timeTab, financesTab, wealthTab, careerTab);
        mainLayout.getChildren().add(tabPane);
        
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // --- 3. START THE GAME LOOP TIMELINE ---
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