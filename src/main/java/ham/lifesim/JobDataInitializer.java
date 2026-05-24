/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ham.lifesim;

import java.util.ArrayList;
import java.util.List;

public class JobDataInitializer {

    /**
     * Hardcodes and returns the baseline master list of jobs for the game.
     * Separating this keeps your manager classes clean and readable!
     */
    public static List<Job> initializeMasterJobList() {
        List<Job> masterList = new ArrayList<>();

        // Format: ID, Title, Hourly Wage, Req. Smarts, Req. Degree
        masterList.add(new Job("WENDYS_COOK", "Fast Food Fry Cook", 15.00, 0, "None"));
        masterList.add(new Job("RETAIL_CLERK", "Retail Sales Associate", 16.50, 20, "None"));
        masterList.add(new Job("BOOKKEEPER", "Junior Bookkeeper", 22.00, 45, "None"));
        masterList.add(new Job("DATA_ANALYST", "Data Analyst", 35.00, 60, "Computer Science"));
        masterList.add(new Job("ACCOUNTANT", "Corporate Accountant", 45.00, 65, "Accounting"));

        return masterList;
    }
}
