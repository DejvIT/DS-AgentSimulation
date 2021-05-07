package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="24"
public class DoVaccination extends Process
{
    private static final TriangularRNG rng = new TriangularRNG(20d, 75d, 100d);
    
    public DoVaccination(int id, Simulation mySim, CommonAgent myAgent)
    {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="Vaccination", id="25", type="Start"
    public void processStart(MessageForm message)
    {
        hold(this.rng.sample(), message);
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
                throw new UnsupportedOperationException("DoVaccination - not supported default message.");
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
    public Vaccination myAgent()
    {
        return (Vaccination)super.myAgent();
    }

}
