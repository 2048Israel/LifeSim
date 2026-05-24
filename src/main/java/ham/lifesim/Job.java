/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ham.lifesim;

public class Job {
    private final String id;
    private final String title;
    private final double hourlyWage;
    private int minHours;
    private int maxHours;
    
    // Prerequisites to get the job
    private final int requiredSmarts;
    private final String requiredDegree;

    // Instance variables tracking player progress in THIS specific job
    private double performance; // 0.0 to 100.0

    public Job(String id, String title, double hourlyWage,int minHours, int maxHours, int requiredSmarts, String requiredDegree) {
        this.id = id;
        this.title = title;
        this.hourlyWage = hourlyWage;
        this.minHours = minHours;
        this.maxHours = maxHours;
        this.requiredSmarts = requiredSmarts;
        this.requiredDegree = requiredDegree;
        this.performance = 0.0; // Starts at zero experience
    }

    // --- GETTERS & PERFORMANCE MODIFIERS ---
    public String getId() { return id; }
    public String getTitle() { return title; }
    public double getHourlyWage() { return hourlyWage; }
    public int getMinHours() { return minHours; }
    public int getMaxHours() { return maxHours; }
    public int getRequiredSmarts() { return requiredSmarts; }
    public String getRequiredDegree() { return requiredDegree; }
    public double getPerformance() { return performance; }

    public void improvePerformance(double amount) {
        this.performance = Math.min(100.0, this.performance + amount);
    }

    public void decayPerformance(double amount) {
        this.performance = Math.max(0.0, this.performance - amount);
    }
}