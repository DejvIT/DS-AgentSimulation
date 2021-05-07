package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="120"
public class DoCanteen extends Scheduler
{
    public DoCanteen(int id, Simulation mySim, CommonAgent myAgent)
    {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="VaccinationCenter", id="121", type="Start"
    public void processStart(MessageForm message)
    {
        message.setCode(Mc.finish);
        hold(10800, message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            case Mc.finish:
                if (mySim().currentTime() < 16200) {
                    MessageForm copy = message.createCopy();
                    hold(2700, copy);
                }
                assistantFinished(message);
            break;
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("DoCanteen - not supported default message.");
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public VaccinationCenter myAgent()
    {
        return (VaccinationCenter)super.myAgent();
    }

}
