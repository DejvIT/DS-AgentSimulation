package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="1"
public class ModelManager extends Manager
{
    public ModelManager(int id, Simulation mySim, Agent myAgent)
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

    //meta! sender="Enviroment", id="37", type="Notice"
    public void processArrived(MessageForm message)
    {
        message.setCode(Mc.arrived);
        message.setAddressee(mySim().findAgent(Id.vaccinationCenter));
        notice(message);
    }

    //meta! sender="VaccinationCenter", id="63", type="Notice"
    public void processDeparture(MessageForm message)
    {
        message.setAddressee(mySim().findAgent(Id.enviroment));
        notice(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            case Mc.initCenter:
                message.setAddressee(mySim().findAgent(Id.vaccinationCenter));
                notice(message);
            break;
            case Mc.initEnviroment:
                message.setAddressee(mySim().findAgent(Id.enviroment));
                notice(message);
            break;
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("ModelManager - not supported default message.");
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
		case Mc.arrived:
			processArrived(message);
		break;

		case Mc.departure:
			processDeparture(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public Model myAgent()
    {
        return (Model)super.myAgent();
    }

}
