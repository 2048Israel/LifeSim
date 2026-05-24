/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ham.lifesim;

/**
 *
 * @author Jerus
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserInterface extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Life Simulator - Engine Sandbox");

        // 1. Create the master layout container
        VBox mainLayout = new VBox();
        
        // 2. Create the TabPane container
        TabPane tabPane = new TabPane();
        
        // This ensures the tabs stretch and fill up the empty space nicely
        VBox.setVgrow(tabPane, Priority.ALWAYS); 

        // 3. Define and configure our 5 core architectural tabs
        Tab dashboardTab = createEmptyTab("Dashboard", "Welcome to LifeSim! Live status metrics will render here.");
        Tab timeTab = createEmptyTab("Time Allocation", "Manage your 168-hour weekly time budget here.");
        Tab financesTab = createEmptyTab("Finances", "Track income vs expenses and balance your cash flow here.");
        Tab wealthTab = createEmptyTab("Wealth & Market", "Manage your stock portfolios, bonds, and real estate here.");
        Tab careerTab = createEmptyTab("Career & Education", "Apply for positions and manage educational qualifications here.");

        // 4. Prevent users from closing tabs (mobile-app style layout)
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // 5. Inject the tabs into the container
        tabPane.getTabs().addAll(dashboardTab, timeTab, financesTab, wealthTab, careerTab);

        // 6. Assemble and render the scene
        mainLayout.getChildren().add(tabPane);
        
        // Default desktop display scale (800x600 resolution baseline)
        Scene scene = new Scene(mainLayout, 800, 600);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Helper factory method to keep initialization neat.
     * Generates a structural placeholder container for each sub-view.
     */
    private Tab createEmptyTab(String title, String placeholderText) {
        Tab tab = new Tab(title);
        
        // A generic vertical layout box to hold future UI elements inside this specific tab
        VBox contentContainer = new VBox();
        contentContainer.setStyle("-fx-padding: 20px; -fx-alignment: center;");
        
        // Temporary feedback component so we know the tab switches correctly
        javafx.scene.control.Label placeholderLabel = new javafx.scene.control.Label(placeholderText);
        placeholderLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");
        
        contentContainer.getChildren().add(placeholderLabel);
        tab.setContent(contentContainer);
        
        return tab;
    }
}
