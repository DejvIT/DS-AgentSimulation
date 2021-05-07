package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="56"
public class VaccinationCenterManager extends Manager
{
    public VaccinationCenterManager(int id, Simulation mySim, Agent myAgent)
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

    //meta! sender="Model", id="62", type="Notice"
    public void processArrived(MessageForm message)
    {
        message.setCode(Mc.register);
        message.setAddressee(mySim().findAgent(Id.registration));
        notice(message);
    }

    //meta! sender="Model", id="105", type="Notice"
    public void processInitCenter(MessageForm message)
    {
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.doCanteen));
        startContinualAssistant(message);
    }

    //meta! sender="Registration", id="69", type="Notice"
    public void processRegistered(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " went to examination.");
        myAgent().addComingRegExam();
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.goRegistrationExamination));
        startContinualAssistant(message);
    }

    //meta! sender="Examination", id="70", type="Notice"
    public void processExamined(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " went to vaccination.");
        myAgent().addComingExamVacc();
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.goExaminationVaccination));
        startContinualAssistant(message);
    }
        
    //meta! sender="Vaccination", id="71", type="Notice"
    public void processVaccined(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " went to waiting room.");
        myAgent().addComingVaccWait();
        message.setCode(Mc.start);
        message.setAddressee(myAgent().findAssistant(Id.goVaccinationWaiting));
        startContinualAssistant(message);
    }

    //meta! sender="WaitingRoom", id="72", type="Notice"
    public void processDeparture(MessageForm message)
    {
        message.setAddressee(mySim().findAgent(Id.model));
        notice(message);
    }

    //meta! sender="DoCanteen", id="121", type="Finish"
    public void processFinishDoCanteen(MessageForm message)
    {
        switch ((int) mySim().currentTime()) {
            case 10800:
                message.setAddressee(Id.registration);
                message.setCode(Mc.lunchTime);
                notice(message);
            break;
            case 13500:
                message.setAddressee(Id.examination);
                message.setCode(Mc.lunchTime);
                notice(message);
            break;
            case 16200:
                message.setAddressee(Id.vaccination);
                message.setCode(Mc.lunchTime);
                notice(message);
            break;
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("Something went wrong with DoCanteen scheduler when finished.");
        }
    }

    //meta! sender="GoRegistrationExamination", id="127", type="Finish"
    public void processFinishGoRegistrationExamination(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " came to examination.");
        myAgent().removeComingRegExam();
        message.setCode(Mc.examinate);
        message.setAddressee(mySim().findAgent(Id.examination));
        notice(message);
    }

    //meta! sender="GoExaminationVaccination", id="143", type="Finish"
    public void processFinishGoExaminationVaccination(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " came to vaccination.");
        myAgent().removeComingExamVacc();
        message.setCode(Mc.vaccine);
        message.setAddressee(mySim().findAgent(Id.vaccination));
        notice(message);
    }

    //meta! sender="GoVaccinationWaiting", id="145", type="Finish"
    public void processFinishGoVaccinationWaiting(MessageForm message)
    {
        ((MyMessage)message).addLog("Person n." + ((MyMessage)message).getPerson().getOrder() + " came to waiting room.");
        myAgent().removeComingVaccWait();
        message.setCode(Mc.waiting);
        message.setAddressee(mySim().findAgent(Id.waitingRoom));
        notice(message);
    }

    //meta! sender="Canteen", id="106", type="Notice"
    public void processFromLunch(MessageForm message)
    {
//        System.out.println(((MyMessage)message).getLog());
        message.setCode(Mc.requestLunch);
        response(message);
    }

    //meta! sender="Registration", id="95", type="Response"
    public void processRequestLunchRegistration(MessageForm message)
    {
        message.setCode(Mc.toLunch);
        message.setAddressee(Id.canteen);
        notice(message);
    }

    //meta! sender="Examination", id="96", type="Response"
    public void processRequestLunchExamination(MessageForm message)
    {
        message.setCode(Mc.toLunch);
        message.setAddressee(Id.canteen);
        notice(message);
    }

    //meta! sender="Vaccination", id="97", type="Response"
    public void processRequestLunchVaccination(MessageForm message)
    {
        message.setCode(Mc.toLunch);
        message.setAddressee(Id.canteen);
        notice(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("VaccinationCenterManager - not supported default message.");
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
			case Id.goRegistrationExamination:
				processFinishGoRegistrationExamination(message);
			break;

			case Id.doCanteen:
				processFinishDoCanteen(message);
			break;

			case Id.goExaminationVaccination:
				processFinishGoExaminationVaccination(message);
			break;

			case Id.goVaccinationWaiting:
				processFinishGoVaccinationWaiting(message);
			break;
			}
		break;

		case Mc.registered:
			processRegistered(message);
		break;

		case Mc.departure:
			processDeparture(message);
		break;

		case Mc.requestLunch:
			switch (message.sender().id())
			{
			case Id.registration:
				processRequestLunchRegistration(message);
			break;

			case Id.examination:
				processRequestLunchExamination(message);
			break;

			case Id.vaccination:
				processRequestLunchVaccination(message);
			break;
			}
		break;

		case Mc.examined:
			processExamined(message);
		break;

		case Mc.initCenter:
			processInitCenter(message);
		break;

		case Mc.fromLunch:
			processFromLunch(message);
		break;

		case Mc.arrived:
			processArrived(message);
		break;

		case Mc.vaccined:
			processVaccined(message);
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
