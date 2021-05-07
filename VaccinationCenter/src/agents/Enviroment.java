package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="2"
public class Enviroment extends Agent
{
    private int arrivals;
    private int departures;
    private int orders;
    private boolean validation;
    private boolean empiricArrivals;
    
    public Enviroment(int id, Simulation mySim, Agent parent)
    {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
        this.arrivals = 0;
        this.departures = 0;
    }
    
    public void addArrival() {
        this.arrivals++;
    }
    
    public int getArrivals() {
        return this.arrivals;
    }
    
    public void addDeparture() {
        this.departures++;
    }
    
    public int getDepartures() {
        return this.departures;
    }

    public int getOrders() {
        return this.orders;
    }
    
    public void setOrders(int orders) {
        this.orders = orders;
    }

    public boolean isValidation() {
        return this.validation;
    }

    public boolean isEmpiricArrivals() {
        return this.empiricArrivals;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public void setEmpiricArrivals(boolean empiricArrivals) {
        this.empiricArrivals = empiricArrivals;
    }
   
	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new EnviromentManager(Id.enviromentManager, mySim(), this);
		new DoArrival(Id.doArrival, mySim(), this);
		addOwnMessage(Mc.initEnviroment);
		addOwnMessage(Mc.departure);
	}
	//meta! tag="end"
}
