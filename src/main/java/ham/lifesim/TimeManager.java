/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ham.lifesim;

/**
 *
 * @author Jerus
 */
public class TimeManager {
    // Current allocations
    private int hoursSleep;
    private int hoursWork;
    private int hoursStudy;
    private int hoursSocial;
    private int hoursExercise;

    // The golden constraint
    private static final int MAX_WEEKLY_HOURS = 168;

    public int getUnallocatedHours() {
        int totalAllocated = hoursSleep + hoursWork + hoursStudy + hoursSocial + hoursExercise;
        return MAX_WEEKLY_HOURS - totalAllocated;
    }

    public boolean updateHoursWork(int proposedHours, int maxJobHours) {
        // 1. Check if it violates the job's personal max capacity limit
        if (proposedHours > maxJobHours) {
            return false;
        }

        // 2. Check if there are enough free hours in the week left to fulfill this change
        int currentTotalWithoutWork = hoursSleep + hoursStudy + hoursSocial + hoursExercise;
        if (currentTotalWithoutWork + proposedHours > MAX_WEEKLY_HOURS) {
            return false; // Not enough hours in the week!
        }

        this.hoursWork = proposedHours;
        return true;
    }
    
    
    // --- GETTERS AND SETTERS ---
    public int getHoursSleep(){ return this.hoursSleep; }
    public int getHoursWork(){ return this.hoursWork; }
    public int getHoursStudy(){ return this.hoursStudy; }
    public int getHoursSocial(){ return this.hoursSocial; }
    public int getHoursExercise(){ return this.hoursExercise; }
    
}
