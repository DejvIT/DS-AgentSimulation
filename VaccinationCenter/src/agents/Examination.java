package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPRNG.UniformDiscreteRNG;
import OSPStat.Stat;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;
import java.util.ArrayList;
import vaccinationcenter.agents.Staff;

//meta! id="4"
public class Examination extends Agent
{
    private int quantity;
    private ArrayList<UniformDiscreteRNG> generators;
    private ArrayList<Staff> personal;
    private SimQueue<MessageForm> queue;
    private Stat waitingStat;
    
    public Examination(int id, Simulation mySim, Agent parent)
    {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
        this.generators = new ArrayList<>();
        this.personal = new ArrayList<>();
        this.queue = new SimQueue<>(new WStat(mySim()));
        this.waitingStat = new Stat();

        for (int i = 0; i < this.quantity; i++) {
            this.generators.add(new UniformDiscreteRNG(0, i));
            this.personal.add(new Staff());
        }
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public void addQuantity() {
        this.quantity++;
    }
    
    public int getRandomStaffIndex(int interval) {
        return this.generators.get(interval - 1).sample();
    }

    public ArrayList<Staff> getAvailablePeronal() {
        ArrayList<Staff> available = new ArrayList<>();
        for (Staff staff : this.personal) {
            if (!staff.getIsBusy() && !staff.getIsOnLunch()) {
                available.add(staff);
            }
        }
        return available;
    }
    
    public ArrayList<Staff> getHungryStaff() {
        ArrayList<Staff> hungry = new ArrayList<>();
        if (isLunchTime()) {
            for (Staff staff : this.personal) {
                if (!staff.getIsFed() && !staff.getIsOnLunch()) {
                    hungry.add(staff);
                }
            }
        }
        return hungry;
    }
    
    public boolean isLunchTime() {
        return mySim().currentTime() >= 13500;
    }
    
    public boolean isCanteenAvailable() {
        int onLunch = 0;
        for (Staff staff : this.personal) {
            if (staff.getIsOnLunch()) {
                onLunch++;
            }
        }
        
        return onLunch < (this.personal.size() / 2);
    }
   
    public Stat getWaitingStat()
    { 
        return this.waitingStat; 
    }
	
    public WStat getQueueStat()
    { 
        return this.queue.lengthStatistic(); 
    }
	
    public SimQueue<MessageForm> getQueue()
    { 
        return this.queue; 
    }

    public ArrayList<Staff> getPersonal() {
        return this.personal;
    }
    
    public String getStaffStatus() {
        String status = "";
        for (int i = 0; i < this.personal.size(); i++) {
            String lunch = "";
            double lunchTime = 0d;
            if (this.personal.get(i).getLunchStart() > 0) {
                lunch += timeToString(this.personal.get(i).getLunchStart());
            }
            if (this.personal.get(i).getLunchEnd() > 0) {
                lunch += "-" + timeToString(this.personal.get(i).getLunchEnd());
                lunchTime = this.personal.get(i).getLunchEnd() - this.personal.get(i).getLunchStart();
            }
            status += (i+1) + ". Working: " + (this.personal.get(i).getIsBusy() ? "X" : "O") 
                    + " - " + String.format("%.2f", ((this.personal.get(i).getWorkingTime() / (mySim().currentTime() - lunchTime)) * 100)) + " %" 
                    + " | Break: " + ((this.personal.get(i).getIsOnLunch() ? "X" : "O" ))
                    + " | Lunch: "  + lunch
                    + "\n";
        }
        return status;
    }
    
    private String timeToString(double simTime) {
        String time = "";
        
        int hours = (int) Math.floor(simTime / 3600);
        int minutes = (int) Math.floor((simTime / 60) % 60);
        int seconds = (int) (simTime - (hours * 3600) - (minutes * 60));
        if (hours < 2) {
            time += ("0" + (8 + hours));
        } else {
            time += (8 + hours);
        }
        time += ":";
        if (minutes < 10) {
            time += ("0" + minutes);
        } else {
            time += minutes;
        }
        time += ":";
        if (seconds < 10) {
            time += ("0" + seconds);
        } else {
            time += seconds;
        }
        return time;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ExaminationManager(Id.examinationManager, mySim(), this);
		new DoExamination(Id.doExamination, mySim(), this);
		addOwnMessage(Mc.lunchTime);
		addOwnMessage(Mc.requestLunch);
		addOwnMessage(Mc.examinate);
	}
	//meta! tag="end"
}
