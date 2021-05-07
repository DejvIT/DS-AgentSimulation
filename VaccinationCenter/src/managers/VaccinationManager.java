package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import java.util.ArrayList;
import vaccinationcenter.agents.Staff;

//meta! id="5"
public class VaccinationManager extends Manager
{
    public VaccinationManager(int id, Simulation mySim, Agent myAgent)
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

    //meta! sender="Syringes", id="102", type="Notice"
    public void processRefilled(MessageForm message)
    {
//        System.out.println(((MyMessage)message).getLog());
        ((MyMessage)message).getStaff().setIsBusy(false);
        ((MyMessage)message).getStaff().addWorkingTime(mySim().currentTime() - ((MyMessage)message).getStaff().getStart());
        ((MyMessage)message).setStaff(null);
        
        processDequeue();
    }

    //meta! sender="VaccinationCenter", id="66", type="Notice"
    public void processVaccine(MessageForm message)
    {
        ArrayList<Staff> available = myAgent().getAvailablePeronal();
        if (available.size() > 0) {
            Staff randomStaff = available.get(myAgent().getRandomStaffIndex(available.size()));
            randomStaff.setIsBusy(true);
            randomStaff.setStart(mySim().currentTime());
            ((MyMessage)message).setStaff(randomStaff);
            ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " started vaccination process without waiting.");
            
            message.setCode(Mc.start);
            message.setAddressee(myAgent().findAssistant(Id.doVaccination));
            startContinualAssistant(message);
        } else {
            ((MyMessage)message).getPerson().setVaccinationWaitingStart(mySim().currentTime());
            ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " started waiting in vaccination queue.");
            myAgent().getQueue().enqueue(message);
        }
    }
    
    private void processDequeue() {
        
        ArrayList<Staff> available = myAgent().getAvailablePeronal(); 
        
        if (myAgent().getQueue().size() > 0 && available.size() > 0) {
            MyMessage nextMessage = (MyMessage)myAgent().getQueue().dequeue();
            ((MyMessage)nextMessage).getPerson().setVaccinationWaitingEnd(mySim().currentTime());
            
            Staff randomStaff = available.get(myAgent().getRandomStaffIndex(available.size()));
            randomStaff.setIsBusy(true);
            randomStaff.setStart(mySim().currentTime());
            ((MyMessage)nextMessage).setStaff(randomStaff);
            ((MyMessage)nextMessage).addLog("Person n." + ((MyMessage)nextMessage).getPerson().getOrder() + " finished waiting and started vaccination process.");
            
            nextMessage.setCode(Mc.start);
            nextMessage.setAddressee(myAgent().findAssistant(Id.doVaccination));
            startContinualAssistant(nextMessage);
        }
    }
    
    public void processRefill(Staff staff) {
        MyMessage refillMessage = new MyMessage(mySim());
        refillMessage.setStaff(staff);
        refillMessage.setCode(Mc.refill);
        refillMessage.setAddressee(mySim().findAgent(Id.syringes));
        notice(refillMessage);
    }

    //meta! sender="DoVaccination", id="25", type="Finish"
    public void processFinish(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " finished vaccination.");
        myAgent().getWaitingStat().addSample(((MyMessage)message).getPerson().getVaccinationWaitingEnd() - ((MyMessage)message).getPerson().getVaccinationWaitingStart());
        
        message.setCode(Mc.vaccined);
        message.setAddressee(mySim().findAgent(Id.vaccinationCenter));
        notice(message);

        ((MyMessage)message).getStaff().useSyringe();
        
        if (((MyMessage)message).getStaff().getSyringes() < 1) {
            
            processRefill(((MyMessage)message).getStaff());
            
        } else {
            ((MyMessage)message).getStaff().setIsBusy(false);
            ((MyMessage)message).getStaff().addWorkingTime(mySim().currentTime() - ((MyMessage)message).getStaff().getStart());
            
            if (!((MyMessage)message).getStaff().getIsFed() && myAgent().isLunchTime() && myAgent().isCanteenAvailable()) {
                MyMessage lunchMessage = new MyMessage(mySim());
                lunchMessage.setStaff(((MyMessage)message).getStaff());
                processRequestLunch(lunchMessage);
            }
            
            ((MyMessage)message).setStaff(null);
            processDequeue();
        }
    }

    //meta! sender="VaccinationCenter", id="97", type="Request"
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

    //meta! sender="VaccinationCenter", id="136", type="Notice"
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
                throw new UnsupportedOperationException("VaccinationManager - not supported default message.");
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
		case Mc.refilled:
			processRefilled(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.lunchTime:
			processLunchTime(message);
		break;

		case Mc.vaccine:
			processVaccine(message);
		break;

		case Mc.requestLunch:
			processRequestLunch(message);
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
