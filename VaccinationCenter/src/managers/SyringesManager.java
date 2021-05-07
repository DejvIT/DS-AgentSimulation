package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="89"
public class SyringesManager extends Manager
{   
    public SyringesManager(int id, Simulation mySim, Agent myAgent)
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

    //meta! sender="Vaccination", id="101", type="Notice"
    public void processRefill(MessageForm message)
    {
        ((MyMessage)message).addLog("Nurse went to the next room.");
        myAgent().addIncoming();
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.goRoom));
        startContinualAssistant(message);
    }
    
    public void processDequeue() {
        if (myAgent().getQueue().size() > 0 && myAgent().getNursesInProcess() < 2) {
            myAgent().addNursesInProcess();
            MyMessage nextMessage = (MyMessage)myAgent().getQueue().dequeue();
            
            ((MyMessage)nextMessage).getStaff().setSyringesFillingEnd(mySim().currentTime());
            ((MyMessage)nextMessage).addLog("Nurse finished waiting and started syringes refill process.");
            
            nextMessage.setCode(Mc.start);
            nextMessage.setAddressee(myAgent().findAssistant(Id.doRefill));
            startContinualAssistant(nextMessage);
        }
    }

    //meta! sender="DoRefill", id="110", type="Finish"
    public void processFinishDoRefill(MessageForm message)
    {
        myAgent().removeNursesInProcess();
        ((MyMessage)message).getStaff().setSyringes(20);
        myAgent().getWaitingStat().addSample(((MyMessage)message).getStaff().getSyringesFillingEnd() - ((MyMessage)message).getStaff().getSyringesFillingStart());
        ((MyMessage)message).addLog("Nurse finished syringes refillment and went back.");
        myAgent().addOutcoming();
            
        processDequeue();
        
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.goRoom));
        startContinualAssistant(message);
    }

    //meta! sender="GoRoom", id="148", type="Finish"
    public void processFinishGoRoom(MessageForm message)
    {
        if (((MyMessage)message).getStaff().getSyringes() == 0) {
            myAgent().removeIncoming();
            if (myAgent().getNursesInProcess() < 2) {
                myAgent().addNursesInProcess();
                ((MyMessage)message).addLog("Nurse came to the room and started to refill syringes.");
                message.setCode(Mc.start);
                message.setAddressee(myAgent().findAssistant(Id.doRefill));
                startContinualAssistant(message);
            } else {
                ((MyMessage)message).addLog("Nurse started waiting in the queue.");
                ((MyMessage)message).getStaff().setSyringesFillingStart(mySim().currentTime());
                myAgent().getQueue().enqueue(message);
            }
        } else {
            ((MyMessage)message).addLog("Nurse came back.");
            myAgent().removeOutcoming();
            message.setCode(Mc.refilled);
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
                throw new UnsupportedOperationException("SyringesManager - not supported default message.");
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
			switch (message.sender().id())
			{
			case Id.goRoom:
				processFinishGoRoom(message);
			break;

			case Id.doRefill:
				processFinishDoRefill(message);
			break;
			}
		break;

		case Mc.refill:
			processRefill(message);
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
