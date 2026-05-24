/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ham.lifesim;

public class Game {
    // Core Character Stats
    private int ageYears;
    private int ageWeeks;
    private double health;     // 0.0 to 100.0
    private double energy;     // 0.0 to 100.0
    private double happiness;  // 0.0 to 100.0
    private double bankBalance;

    // Time Allocation Metrics (Must total 168 hours)
    private int hoursSleep;
    private int hoursWork;
    private int hoursStudy;
    private int hoursSocial;
    private int hoursExercise;

    // Economic Engine Baseline
    private double hourlyWage;
    
    // Managers
    private final JobManager jobManager = new JobManager();
    private final TimeManager timeManager = new TimeManager();

    /**
     * Constructor: Initializes a brand new life simulation (starting young)
     */
    public Game() {
        this.ageYears = 18; // Start at adulthood baseline
        this.ageWeeks = 0;
        this.health = 100.0;
        this.energy = 100.0;
        this.happiness = 80.0;
        this.bankBalance = 1000.0; // Starting seed money

        // Default balanced schedule baseline (Total = 168)
        this.hoursSleep = 56;  // 8 hours a day
        this.hoursWork = 40;   // Standard full-time job
        this.hoursStudy = 0;
        this.hoursSocial = 62; // Remaining leisure/social time
        this.hoursExercise = 10;
        
        this.hourlyWage = 15.00; // Baseline entry-level job income
    }

    /**
     * The heart of your automation engine. 
     * This method runs exactly once per simulated week.
     */
    public void tickWeeklyLoop() {
        // 1. Advance the clock
        ageWeeks++;
        if (ageWeeks >= 52) {
            ageWeeks = 0;
            ageYears++;
        }

            //1b. update life
        jobManager.updateWeeklyJobMetrics(hoursWork);
        updateHourlyWage();
        int workHours = timeManager.getHoursWork();
        int sleepHours = timeManager.getHoursSleep();
        
        // 2. Financial Automation (Income vs basic survival expenses)
        double grossIncome = hoursWork * hourlyWage;
        double taxes = grossIncome * 0.15; // Simplified 15% flat tax flat rate
        double basicLivingCost = 250.00;   // Weekly food and rent baseline
        
        bankBalance += (grossIncome - taxes - basicLivingCost);

        // 3. Health & Energy Accumulation Logic
        // Penalty if sleeping less than 7 hours a day (49 hours/week)
        if (hoursSleep < 49) {
            energy = Math.max(0, energy - (49 - hoursSleep) * 1.5);
            health = Math.max(0, health - 1.0);
        } else {
            energy = Math.min(100, energy + 5.0);
        }

        // Exercise bonus
        if (hoursExercise >= 5 && energy > 20) {
            health = Math.min(100, health + 0.5);
        }
    }
    
    //Updaters
    private void updateHourlyWage(){
        // Instead of a fixed hourlyWage field inside Game:
        hourlyWage = (jobManager.getCurrentJob() != null) ? jobManager.getCurrentJob().getHourlyWage() : 0.0;
        
    }

    // --- GETTERS AND SETTERS ---
    // These allow your UI classes to read the values safely without breaking them
    public int getAgeYears() {
        return ageYears;
    }

    public int getAgeWeeks() {
        return ageWeeks;
    }

    public double getHealth() {
        return health;
    }

    public double getEnergy() {
        return energy;
    }

    public double getHappiness() {
        return happiness;
    }

    public double getBankBalance() {
        return bankBalance;
    }
 
    /**
     * Allows the UI and other components to safely access the job subsystem.
     * @return 
     */
    public JobManager getJobManager() {
        return this.jobManager;
    }
    public TimeManager getTimeManager() {
        return this.timeManager;
    }

    // Setters for the time allocation so sliders can update the model
    public void setSchedule(int sleep, int work, int study, int social, int exercise) {
        if ((sleep + work + study + social + exercise) == 168) {
            this.hoursSleep = sleep;
            this.hoursWork = work;
            this.hoursStudy = study;
            this.hoursSocial = social;
            this.hoursExercise = exercise;
        }
    }
    // Array to map our 12 month names cleanly
    private static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    /**
     * Translates the current game week (0-51) into the corresponding month name
     * using the 4-4-5 fiscal calendar framework.
     * @return 
     */
    public String getCurrentMonthName() {
        // Convert 0-indexed tracking to a 1-52 calendar range for clean comparison math
        int weekOfYear = this.ageWeeks + 1;

        if (weekOfYear <= 4) {
            return MONTHS[0];  // Jan (4 weeks)
        }
        else if (weekOfYear <= 8) {
            return MONTHS[1];  // Feb (4 weeks)
        }
        else if (weekOfYear <= 13) {
            return MONTHS[2];  // Mar (5 weeks)
        }
        else if (weekOfYear <= 17) {
            return MONTHS[3];  // Apr (4 weeks)
        }
        else if (weekOfYear <= 21) {
            return MONTHS[4];  // May (4 weeks)
        }
        else if (weekOfYear <= 26) {
            return MONTHS[5];  // June (5 weeks)
        }
        else if (weekOfYear <= 30) {
            return MONTHS[6];  // July (4 weeks)
        }
        else if (weekOfYear <= 34) {
            return MONTHS[7];  // Aug (4 weeks)
        }
        else if (weekOfYear <= 39) {
            return MONTHS[8];  // Sept (5 weeks)
        }
        else if (weekOfYear <= 43) {
            return MONTHS[9];  // Oct (4 weeks)
        }
        else if (weekOfYear <= 47) {
            return MONTHS[10]; // Nov (4 weeks)
        }
        return MONTHS[11]; // Dec (Remaining weeks up to 52)
    }
}
