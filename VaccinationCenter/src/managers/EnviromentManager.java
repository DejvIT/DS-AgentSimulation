package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import vaccinationcenter.agents.Person;
import vaccinationcenter.agents.Staff;

//meta! id="2"
public class EnviromentManager extends Manager
{
    public EnviromentManager(int id, Simulation mySim, Agent myAgent)
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

    //meta! sender="Model", id="13", type="Notice"
    public void processInitEnviroment(MessageForm message)
    {
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.doArrival));
        startContinualAssistant(message);
    }

    //meta! sender="DoArrival", id="16", type="Finish"
    public void processFinish(MessageForm message)
    {
        myAgent().addArrival();
        ((MyMessage)message).setPerson(new Person(mySim().currentTime(), myAgent().getArrivals()));
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " arrived to the system and went to the registration.");
        
        message.setCode(Mc.arrived);
        message.setAddressee(mySim().findAgent(Id.model));
        notice(message);
    }

    //meta! sender="Model", id="43", type="Notice"
    public void processDeparture(MessageForm message)
    {
        ((MyMessage)message).getPerson().setDeparture(mySim().currentTime());
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " left the system.");
        myAgent().addDeparture();
        
//        System.out.println(((MyMessage)message).getLog());

        if (myAgent().getArrivals() == myAgent().getDepartures() && mySim().currentTime() >= 32400) {
            mySim().stopReplication();
        }
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("EnviromentManager - not supported default message.");
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
		case Mc.departure:
			processDeparture(message);
		break;

		case Mc.initEnviroment:
			processInitEnviroment(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public Enviroment myAgent()
    {
        return (Enviroment)super.myAgent();
    }

}
