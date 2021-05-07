package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="6"
public class WaitingRoomManager extends Manager
{
    public WaitingRoomManager(int id, Simulation mySim, Agent myAgent)
    {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null)
        {
            petriNet().clear();
        }
    }

    //meta! sender="VaccinationCenter", id="65", type="Notice"
    public void processWaiting(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " started waiting after vaccination.");
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.doWaiting));
        startContinualAssistant(message);
        
        myAgent().addWaiting();
    }

    //meta! sender="DoWaiting", id="28", type="Finish"
    public void processFinish(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " finished waiting after vaccination.");
        message.setCode(Mc.departure);
        message.setAddressee(mySim().findAgent(Id.vaccinationCenter));
        notice(message);
        
        myAgent().getFillingStat().addSample(myAgent().getCurrentFill());
        myAgent().removeWaiting();
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("WaitingRoomManager - not supported default message.");
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.waiting:
			processWaiting(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public WaitingRoom myAgent()
    {
        return (WaitingRoom)super.myAgent();
    }

}
