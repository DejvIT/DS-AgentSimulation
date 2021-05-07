package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="109"
public class DoRefill extends Process
{
    private static final TriangularRNG rng = new TriangularRNG(6d, 10d, 40d);
    
    public DoRefill(int id, Simulation mySim, CommonAgent myAgent)
    {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="Syringes", id="110", type="Start"
    public void processStart(MessageForm message)
    {
        double holdForTime = 0d;
        if (!myAgent().isValidation()) {
            for (int i = 0; i < 20; i++) {
                holdForTime += this.rng.sample();
            }
        }
        hold(holdForTime, message);
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
                throw new UnsupportedOperationException("DoRefill - not supported default message.");
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
    public Syringes myAgent()
    {
        return (Syringes)super.myAgent();
    }

}