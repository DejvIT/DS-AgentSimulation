package agents;

import OSPABA.*;
import OSPStat.Stat;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="90"
public class Canteen extends Agent
{
    private Stat lunchLengthStat;
    private int incoming;
    private int eating;
    private int outcoming;
    
    private boolean validation;
    
    public Canteen(int id, Simulation mySim, Agent parent)
    {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
        this.lunchLengthStat = new Stat();
        this.incoming = 0;
        this.eating = 0;
        this.outcoming = 0;
    }

    public Stat getLunchLengthStat() {
        return this.lunchLengthStat;
    }

    public int getIncoming() {
        return this.incoming;
    }

    public int getEating() {
        return this.eating;
    }

    public int getOutcoming() {
        return this.outcoming;
    }
    
    public void addIncoming() {
        this.incoming++;
    }
    
    public void removeIncoming() {
        this.incoming--;
    }
    
    public void addEating() {
        this.eating++;
    }
    
    public void removeEating() {
        this.eating--;
    }
    
    public void addOutcoming() {
        this.outcoming++;
    }
    
    public void removeOutcoming() {
        this.outcoming--;
    }

    public boolean isValidation() {
        return this.validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }
    
	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new CanteenManager(Id.canteenManager, mySim(), this);
		new GoCanteen(Id.goCanteen, mySim(), this);
		new DoLunch(Id.doLunch, mySim(), this);
		addOwnMessage(Mc.toLunch);
	}
	//meta! tag="end"
}
