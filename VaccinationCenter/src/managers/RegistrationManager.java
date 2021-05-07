package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import java.util.ArrayList;
import vaccinationcenter.agents.Staff;

//meta! id="3"
public class RegistrationManager extends Manager
{
    public RegistrationManager(int id, Simulation mySim, Agent myAgent)
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

    //meta! sender="DoRegistration", id="19", type="Finish"
    public void processFinish(MessageForm message)
    {
        ((MyMessage)message).getStaff().setIsBusy(false);
        ((MyMessage)message).getStaff().addWorkingTime(mySim().currentTime() - ((MyMessage)message).getStaff().getStart());
        
        if (!((MyMessage)message).getStaff().getIsFed() && myAgent().isLunchTime() && myAgent().isCanteenAvailable()) {
            MyMessage lunchMessage = new MyMessage(mySim());
            lunchMessage.setStaff(((MyMessage)message).getStaff());
            processRequestLunch(lunchMessage);
        }
        
        ((MyMessage)message).setStaff(null);
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " finished registration.");
        
        message.setCode(Mc.registered);
        message.setAddressee(mySim().findAgent(Id.vaccinationCenter));
        notice(message);

        myAgent().getWaitingStat().addSample(((MyMessage)message).getPerson().getRegistrationWaitingEnd() - ((MyMessage)message).getPerson().getRegistrationWaitingStart());
         
        processDequeue();
    }

    //meta! sender="VaccinationCenter", id="68", type="Notice"
    public void processRegister(MessageForm message)
    {
        ArrayList<Staff> available = myAgent().getAvailablePeronal();
        if (available.size() > 0) {
            Staff randomStaff = available.get(myAgent().getRandomStaffIndex(available.size()));
            randomStaff.setIsBusy(true);
            randomStaff.setStart(mySim().currentTime());
            ((MyMessage)message).setStaff(randomStaff);
            ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " started registration process without waiting.");
            
            message.setCode(Mc.start);
            message.setAddressee(myAgent().findAssistant(Id.doRegistration));
            startContinualAssistant(message);
        } else {
            ((MyMessage)message).getPerson().setRegistrationWaitingStart(mySim().currentTime());
            ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " started waiting in registration queue.");
            myAgent().getQueue().enqueue(message);
        }
    }

    //meta! sender="VaccinationCenter", id="95", type="Request"
    public void processRequestLunch(MessageForm message)
    {
        if (((MyMessage)message).getStaff() != null && ((MyMessage)message).getStaff().getIsFed()) {
            processDequeue();
        }
        
        if (((MyMessage)message).getStaff() == null) {
            ArrayList<Staff> hungryStaff = myAgent().getHungryStaff();
            if (hungryStaff.size() > 0) {
                for (Staff hungryUnit : hungryStaff) {
                    if (!hungryUnit.getIsFed() && !hungryUnit.getIsBusy()) {
                        ((MyMessage)message).setStaff(hungryUnit);
                        break;
                    }
                }
            }
        }

        if (((MyMessage)message).getStaff() != null && !((MyMessage)message).getStaff().getIsFed()) {
            message.setAddressee(Id.vaccinationCenter);
            message.setCode(Mc.requestLunch);
            request(message);
        }
    }

    //meta! sender="VaccinationCenter", id="134", type="Notice"
    public void processLunchTime(MessageForm message)
    {
        int half = myAgent().getQuantity() / 2;
        for (int i = 0; i < half; i++) {
            MessageForm copy = message.createCopy();
            processRequestLunch(copy);
        }
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("RegistrationManager - not supported default message.");
        }
    }
    
    private void processDequeue() {
        
        ArrayList<Staff> available = myAgent().getAvailablePeronal();
        
        if (myAgent().getQueue().size() > 0 && available.size() > 0) {
            MyMessage nextMessage = (MyMessage)myAgent().getQueue().dequeue();
            ((MyMessage)nextMessage).getPerson().setRegistrationWaitingEnd(mySim().currentTime());
            
            Staff randomStaff = available.get(myAgent().getRandomStaffIndex(available.size()));
            randomStaff.setIsBusy(true);
            randomStaff.setStart(mySim().currentTime());
            ((MyMessage)nextMessage).setStaff(randomStaff);
            ((MyMessage)nextMessage).addLog("Person n." + ((MyMessage)nextMessage).getPerson().getOrder() + " finished waiting and started registration process.");
            
            nextMessage.setCode(Mc.start);
            nextMessage.setAddressee(myAgent().findAssistant(Id.doRegistration));
            startContinualAssistant(nextMessage);
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
		case Mc.requestLunch:
			processRequestLunch(message);
		break;

		case Mc.register:
			processRegister(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.lunchTime:
			processLunchTime(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public Registration myAgent()
    {
        return (Registration)super.myAgent();
    }

}
