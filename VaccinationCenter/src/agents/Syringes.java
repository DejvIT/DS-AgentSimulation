package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="89"
public class Syringes extends Agent
{
    private SimQueue<MessageForm> queue;
    private Stat waitingStat;
    
    private int incoming;
    private int nursesInProcess;
    private int outcoming;
    
    private boolean validation;
    
    public Syringes(int id, Simulation mySim, Agent parent)
    {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
        this.queue = new SimQueue<>(new WStat(mySim()));
        this.waitingStat = new Stat();
        
        this.incoming = 0;
        this.nursesInProcess = 0;
        this.outcoming = 0;
    }
    
    public Stat getWaitingStat()
    { 
        return this.waitingStat; 
    }
	
    public WStat getQueueStat()
    { 
        return this.queue.lengthStatistic(); 
    }

    public int getIncoming() {
        return this.incoming;
    }

    public int getNursesInProcess() {
        return this.nursesInProcess;
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
    
    public void addNursesInProcess() {
        this.nursesInProcess++;
    }
    
    public void removeNursesInProcess() {
        this.nursesInProcess--;
    }
    
    public void addOutcoming() {
        this.outcoming++;
    }
    
    public void removeOutcoming() {
        this.outcoming--;
    }
	
    public SimQueue<MessageForm> getQueue()
    { 
        return this.queue; 
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
		new SyringesManager(Id.syringesManager, mySim(), this);
		new DoRefill(Id.doRefill, mySim(), this);
		new GoRoom(Id.goRoom, mySim(), this);
		addOwnMessage(Mc.refill);
	}
	//meta! tag="end"
}
