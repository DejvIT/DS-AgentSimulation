package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="114"
public class DoLunch extends Process
{
    private static final TriangularRNG rng = new TriangularRNG(300d, 900d, 1800d);
    
    public DoLunch(int id, Simulation mySim, CommonAgent myAgent)
    {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="Canteen", id="115", type="Start"
    public void processStart(MessageForm message)
    {
        double holdForTime = 0d;
        if (!myAgent().isValidation()) {
            holdForTime = this.rng.sample();
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
                throw new UnsupportedOperationException("DoLunch - not supported default message.");
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
    public Canteen myAgent()
    {
        return (Canteen)super.myAgent();
    }

}