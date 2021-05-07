package agents;

import OSPABA.*;
import OSPStat.Stat;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="56"
public class VaccinationCenter extends Agent
{
    private int comingRegExam;
    private int comingExamVacc;
    private int comingVaccWait;
    
    private boolean validation;
    
    public VaccinationCenter(int id, MySimulation mySim, Agent parent)
    {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
        this.comingRegExam = 0;
        this.comingExamVacc = 0;
        this.comingVaccWait = 0;
    }

    public int getComingRegExam() {
        return this.comingRegExam;
    }

    public int getComingExamVacc() {
        return this.comingExamVacc;
    }

    public int getComingVaccWait() {
        return this.comingVaccWait;
    }
    
    public void addComingRegExam() {
        this.comingRegExam++;
    }
    
    public void removeComingRegExam() {
        this.comingRegExam--;
    }
    
    public void addComingExamVacc() {
        this.comingExamVacc++;
    }
    
    public void removeComingExamVacc() {
        this.comingExamVacc--;
    }
    
    public void addComingVaccWait() {
        this.comingVaccWait++;
    }
    
    public void removeComingVaccWait() {
        this.comingVaccWait--;
    }

    public boolean isValidation() {
        return this.validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }
    
	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new VaccinationCenterManager(Id.vaccinationCenterManager, mySim(), this);
		new GoExaminationVaccination(Id.goExaminationVaccination, mySim(), this);
		new GoRegistrationExamination(Id.goRegistrationExamination, mySim(), this);
		new DoCanteen(Id.doCanteen, mySim(), this);
		new GoVaccinationWaiting(Id.goVaccinationWaiting, mySim(), this);
		addOwnMessage(Mc.arrived);
		addOwnMessage(Mc.fromLunch);
		addOwnMessage(Mc.vaccined);
		addOwnMessage(Mc.requestLunch);
		addOwnMessage(Mc.initCenter);
		addOwnMessage(Mc.registered);
		addOwnMessage(Mc.examined);
		addOwnMessage(Mc.departure);
	}
	//meta! tag="end"
}
