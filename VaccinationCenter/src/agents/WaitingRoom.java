package agents;

import OSPABA.*;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="6"
public class WaitingRoom extends Agent
{
    private int currentFill;
    private WStat filling;
    
    public WaitingRoom(int id, Simulation mySim, Agent parent)
    {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
        this.currentFill = 0;
        this.filling = new WStat(mySim());
    }
    
    public int getCurrentFill() {
        return this.currentFill;
    }
    
    public void addWaiting() {
        this.currentFill++;
    }
    
    public void removeWaiting() {
        this.currentFill--;
    }
    
    public WStat getFillingStat() {
        return this.filling;
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new WaitingRoomManager(Id.waitingRoomManager, mySim(), this);
		new DoWaiting(Id.doWaiting, mySim(), this);
		addOwnMessage(Mc.waiting);
	}
	//meta! tag="end"
}
