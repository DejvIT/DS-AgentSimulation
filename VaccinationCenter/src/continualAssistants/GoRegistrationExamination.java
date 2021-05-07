package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="126"
public class GoRegistrationExamination extends Process
{
    private static final UniformContinuousRNG rng = new UniformContinuousRNG(40d,90d);
    
    public GoRegistrationExamination(int id, Simulation mySim, CommonAgent myAgent)
    {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="VaccinationCenter", id="127", type="Start"
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
                throw new UnsupportedOperationException("GoRegistrationExamination - not supported default message.");
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