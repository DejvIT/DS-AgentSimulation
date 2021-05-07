package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="90"
public class CanteenManager extends Manager
{
    public CanteenManager(int id, Simulation mySim, Agent myAgent)
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

    //meta! sender="VaccinationCenter", id="103", type="Notice"
    public void processToLunch(MessageForm message)
    {
        ((MyMessage)message).getStaff().setOnLunch(true);
        ((MyMessage)message).getStaff().setLunchStart(mySim().currentTime());
        ((MyMessage)message).addLog("Personal unit went to canteen.");
        myAgent().addIncoming();
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.goCanteen));
        startContinualAssistant(message);
    }

    //meta! sender="DoLunch", id="115", type="Finish"
    public void processFinishDoLunch(MessageForm message)
    {
        ((MyMessage)message).addLog("Personal unit finished lunch and went back to work.");
        myAgent().removeEating();
        myAgent().addOutcoming();
        ((MyMessage)message).getStaff().setFed();
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.goCanteen));
        startContinualAssistant(message);
    }

    //meta! sender="GoCanteen", id="151", type="Finish"
    public void processFinishGoCanteen(MessageForm message)
    {
        if (!((MyMessage)message).getStaff().getIsFed()) {
            ((MyMessage)message).addLog("Personal unit came to the canteen and started lunch.");
            myAgent().removeIncoming();
            myAgent().addEating();
            message.setCode(Mc.start);
            message.setAddressee(myAgent().findAssistant(Id.doLunch));
            startContinualAssistant(message);
        } else {
            ((MyMessage)message).getStaff().setFed();
            ((MyMessage)message).getStaff().setOnLunch(false);
            ((MyMessage)message).getStaff().setLunchEnd(mySim().currentTime());
            myAgent().getLunchLengthStat().addSample(((MyMessage)message).getStaff().getLunchEnd() - ((MyMessage)message).getStaff().getLunchStart());
            ((MyMessage)message).addLog("Personal unit came from lunch.");
            myAgent().removeOutcoming();
            message.setCode(Mc.fromLunch);
            message.setAddressee(mySim().findAgent(Id.vaccinationCenter));
            notice(message);
        }
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("CanteenManager - not supported default message.");
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
		case Mc.toLunch:
			processToLunch(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.doLunch:
				processFinishDoLunch(message);
			break;

			case Id.goCanteen:
				processFinishGoCanteen(message);
			break;
			}
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public Canteen myAgent()
    {
        return (Canteen)super.myAgent();
    }

}
