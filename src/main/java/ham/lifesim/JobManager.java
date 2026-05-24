/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ham.lifesim;

import java.util.List;

public class JobManager {
    private final List<Job> availableJobs;
    private Job currentJob;

    public JobManager() {
        // Run the separate initializer file to populate our master career market
        this.availableJobs = JobDataInitializer.initializeMasterJobList();
        
        // Start unemployed or with a baseline job
        this.currentJob = null; 
    }

    /**
     * Processes job performance shifts based on how many hours the player worked.
     */
    public void updateWeeklyJobMetrics(int hoursWorked) {
        if (currentJob == null) return;

        if (hoursWorked >= 40) {
            // Working full time or overworking builds performance
            currentJob.improvePerformance(2.5 + (hoursWorked - 40) * 0.1);
        } else if (hoursWorked > 0) {
            // Part-time hours maintain or slightly decay performance baseline
            currentJob.decayPerformance(0.5);
        } else {
            // Not working at all destroys job performance
            currentJob.decayPerformance(5.0);
        }
    }

    /**
     * Basic hire validation logic
     */
    public boolean tryApplyForJob(Job job, int playerSmarts, String playerDegree) {
        if (playerSmarts >= job.getRequiredSmarts() && playerDegree.equals(job.getRequiredDegree())) {
            this.currentJob = job;
            return true;
        }
        return false;
    }

    // --- GETTERS ---
    public Job getCurrentJob() { return currentJob; }
    public List<Job> getAvailableJobs() { return availableJobs; }
}
