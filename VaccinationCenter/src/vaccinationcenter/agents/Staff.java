/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.agents;

import OSPABA.Simulation;


/**
 *
 * @author davidpavlicko
 */
public class Staff {
    
    private boolean isBusy;
    
    private boolean onLunch;
    private boolean fed = false;
    private double lunchStart;
    private double lunchEnd;
    
    private double workingTime;
    private double start;
    
    private int syringes;
    private double syringesFillingStart;
    private double syringesFillingEnd;
    
    public Staff() {}

    public boolean getIsBusy() {
        return this.isBusy;
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public boolean getIsOnLunch() {
        return this.onLunch;
    }

    public void setOnLunch(boolean onLunch) {
        this.onLunch = onLunch;
    }
    
    public boolean getIsFed() {
        return this.fed;
    }
    
    public void setFed() {
        this.fed = true;
    }
    
    public double getLunchStart() {
        return this.lunchStart;
    }
    
    public void setLunchStart(double start) {
        this.lunchStart = start;
    }
    
    public double getLunchEnd() {
        return this.lunchEnd;
    }
    
    public void setLunchEnd(double end) {
        this.lunchEnd = end;
    }
    
    public double getWorkingTime() {
        return this.workingTime;
    }
    
    public void addWorkingTime(double time) {
        this.workingTime += time;
    }

    public double getStart() {
        return this.start;
    }

    public void setStart(double start) {
        this.start = start;
    }
    
    public int getSyringes() {
        return this.syringes;
    }

    public void setSyringes(int syringes) {
        this.syringes = syringes;
    }
    
    public void useSyringe() {
        this.syringes--;
    }

    public double getSyringesFillingStart() {
        return this.syringesFillingStart;
    }

    public double getSyringesFillingEnd() {
        return this.syringesFillingEnd;
    }

    public void setSyringesFillingStart(double syringesFillingStart) {
        this.syringesFillingStart = syringesFillingStart;
    }

    public void setSyringesFillingEnd(double syringesFillingEnd) {
        this.syringesFillingEnd = syringesFillingEnd;
    }
    
    public void reset() {
        this.isBusy = false;
        this.onLunch = false;
        this.workingTime = 0.0;
        this.start = 0.0;
    }
}
