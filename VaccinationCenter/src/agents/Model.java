package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="1"
public class Model extends Agent
{
    public Model(int id, Simulation mySim, Agent parent)
    {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
        MyMessage enviromentMessage = new MyMessage(mySim());
        enviromentMessage.setCode(Mc.initEnviroment);
        enviromentMessage.setAddressee(Id.enviroment);
        manager().notice(enviromentMessage);
        
        MyMessage centerMessage = new MyMessage(mySim());
        centerMessage.setCode(Mc.initCenter);
        centerMessage.setAddressee(Id.vaccinationCenter);
        manager().notice(centerMessage);
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ModelManager(Id.modelManager, mySim(), this);
		addOwnMessage(Mc.arrived);
		addOwnMessage(Mc.departure);
	}
	//meta! tag="end"
}
