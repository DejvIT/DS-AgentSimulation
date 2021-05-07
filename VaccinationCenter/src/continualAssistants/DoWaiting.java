package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="27"
public class DoWaiting extends Process
{
    private static final UniformContinuousRNG rng = new UniformContinuousRNG(0d,1d);
    
    public DoWaiting(int id, Simulation mySim, CommonAgent myAgent)
    {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="WaitingRoom", id="28", type="Start"
    public void processStart(MessageForm message)
    {
        if (this.rng.sample() < 0.95) {
            hold(15 * 60, message);
        } else {
            hold(30 * 60, message);
        }
        
        message.setCode(Mc.finish);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            case Mc.finish:
                assistantFinished(message);
            break;
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("DoWaiting - not supported default message.");
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
    public WaitingRoom myAgent()
    {
        return (WaitingRoom)super.myAgent();
    }

}
