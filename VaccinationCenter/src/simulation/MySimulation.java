package simulation;

import OSPABA.*;
import OSPStat.Stat;
import agents.*;
import vaccinationcenter.agents.Staff;

public class MySimulation extends Simulation
{
    private final int orders;
    private final int workers;
    private final int doctors;
    private final int nurses;
    
    private final boolean validation;
    private final boolean empiricArrivals;
    
    //Global Statistics
    private Stat finishTime;
    //S2
    private Stat arrivals;
    private Stat departures;
    private Stat coolingLengths;
    private Stat registrationWaitings;
    private Stat registrationEfficiency;
    private Stat registrationQueueLengths;
    private Stat examinationWaitings;
    private Stat examinationEfficiency;
    private Stat examinationQueueLengths;
    private Stat vaccinationWaitings;
    private Stat vaccinationEfficiency;
    private Stat vaccinationQueueLengths;
    private Stat waitingRoomFilling;
    //End of S2
    
    //S3
    private Stat refillWaitings;
    private Stat refillQueueLengths;
    private Stat lunchLengths;
    //End of S3
    
    public MySimulation(int orders, int workers, int doctors, int nurses, boolean validation, boolean empiricArrivals)
    {
        this.orders = orders;
        this.workers = workers;
        this.doctors = doctors;
        this.nurses = nurses;
        
        this.validation = validation;
        this.empiricArrivals = empiricArrivals;
        
        init();
    }

    public int getOrders() {
        return this.orders;
    }

    public int getWorkers() {
        return this.workers;
    }

    public int getDoctors() {
        return this.doctors;
    }

    public int getNurses() {
        return this.nurses;
    }

    public boolean isValidation() {
        return this.validation;
    }
    
    public boolean isEmpiricArrivals() {
        return this.empiricArrivals;
    }
    
    //Statistics
    public Stat getFinishTime() {
        return this.finishTime;
    }
    
    public Stat getArrivals() {
        return this.arrivals;
    }

    public Stat getDepartures() {
        return this.departures;
    }

    public Stat getCoolingLengths() {
        return this.coolingLengths;
    }

    public Stat getRegistrationWaitings() {
        return this.registrationWaitings;
    }

    public Stat getRegistrationEfficiency() {
        return this.registrationEfficiency;
    }

    public Stat getRegistrationQueueLengths() {
        return this.registrationQueueLengths;
    }

    public Stat getExaminationWaitings() {
        return this.examinationWaitings;
    }

    public Stat getExaminationEfficiency() {
        return this.examinationEfficiency;
    }

    public Stat getExaminationQueueLengths() {
        return this.examinationQueueLengths;
    }

    public Stat getVaccinationWaitings() {
        return this.vaccinationWaitings;
    }

    public Stat getVaccinationEfficiency() {
        return this.vaccinationEfficiency;
    }

    public Stat getVaccinationQueueLengths() {
        return this.vaccinationQueueLengths;
    }
    
    public Stat getWaitingRoomFilling() {
        return this.waitingRoomFilling;
    }

    public Stat getRefillWaitings() {
        return this.refillWaitings;
    }

    public Stat getRefillQueueLengths() {
        return this.refillQueueLengths;
    }

    public Stat getLunchLengths() {
        return this.lunchLengths;
    }
    
    public void resetExaminationStats() {
        this.examinationWaitings = new Stat();
        this.examinationQueueLengths = new Stat();
        this.examinationEfficiency = new Stat();
    }
    //End of Statistics
    
    @Override
    public void prepareSimulation()
    {
        super.prepareSimulation();
        // Create global statistcis
        this.finishTime = new Stat();
        
        this.arrivals = new Stat();
        this.departures = new Stat();
        this.coolingLengths = new Stat();
        this.registrationWaitings = new Stat();
        this.registrationEfficiency = new Stat();
        this.registrationQueueLengths = new Stat();
        this.examinationWaitings = new Stat();
        this.examinationEfficiency = new Stat();
        this.examinationQueueLengths = new Stat();
        this.vaccinationWaitings = new Stat();
        this.vaccinationEfficiency = new Stat();
        this.vaccinationQueueLengths = new Stat();
        this.waitingRoomFilling = new Stat();
        
        this.refillWaitings = new Stat();
        this.refillQueueLengths = new Stat();
        this.lunchLengths = new Stat();
        
        enviroment().setOrders(this.orders);
        enviroment().setValidation(this.validation);
        enviroment().setEmpiricArrivals(this.empiricArrivals);
        
        registration().setQuantity(this.workers);
        examination().setQuantity(this.doctors);
        vaccination().setQuantity(this.nurses);
        
        vaccinationCenter().setValidation(this.validation);
        canteen().setValidation(this.validation);
        syringes().setValidation(this.validation);
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Reset entities, queues, local statistics, etc...
    }

    @Override
    public void replicationFinished()
    {
        // Collect local statistics into global, update UI, etc...
        /*
            "Dummy message" je sprava ktora sa prida a ihned odoberie do frontov 
            v simulacii aby sa bral pre statistiky cas ukoncenia replikacie nie 
            poslednej zmeny daneho frontu, ktory moze byt pre registraciu napriklad aj 
            o hodiny, co je samozrejme zle.
        */
        //Start of dummy message
        MyMessage dummyMessage = new MyMessage(this);
        registration().getQueue().enqueue(dummyMessage);
        registration().getQueue().dequeue();
        examination().getQueue().enqueue(dummyMessage);
        examination().getQueue().dequeue();
        vaccination().getQueue().enqueue(dummyMessage);
        vaccination().getQueue().dequeue();
        syringes().getQueue().enqueue(dummyMessage);
        syringes().getQueue().dequeue();
        //End of dummy message
        
        getFinishTime().addSample(currentTime());
        
        getCoolingLengths().addSample(currentTime() - 32400);
        getArrivals().addSample(enviroment().getArrivals());
        getDepartures().addSample(enviroment().getDepartures());

        getRegistrationWaitings().addSample(registration().getWaitingStat().mean());
        getExaminationWaitings().addSample(examination().getWaitingStat().mean());
        getVaccinationWaitings().addSample(vaccination().getWaitingStat().mean());

        getRegistrationQueueLengths().addSample(registration().getQueueStat().mean());
        getExaminationQueueLengths().addSample(examination().getQueueStat().mean());
        getVaccinationQueueLengths().addSample(vaccination().getQueueStat().mean());

        double efficiency = 0.0;
        for (Staff personal : registration().getPersonal()) {
            efficiency += (personal.getWorkingTime() / (currentTime() - (personal.getLunchEnd() - personal.getLunchStart())));
        }
        getRegistrationEfficiency().addSample(efficiency / registration().getPersonal().size());

        efficiency = 0.0;
        for (Staff personal : examination().getPersonal()) {
            efficiency += (personal.getWorkingTime() / (currentTime() - (personal.getLunchEnd() - personal.getLunchStart())));
        }
        getExaminationEfficiency().addSample(efficiency / examination().getPersonal().size());

        efficiency = 0.0;
        for (Staff personal : vaccination().getPersonal()) {
            efficiency += (personal.getWorkingTime() / (currentTime() - (personal.getLunchEnd() - personal.getLunchStart())));
        }
        getVaccinationEfficiency().addSample(efficiency / vaccination().getPersonal().size());

        getWaitingRoomFilling().addSample(waitingRoom().getFillingStat().mean());

        getRefillWaitings().addSample(syringes().getWaitingStat().mean());
        getRefillQueueLengths().addSample(syringes().getQueueStat().mean());
        
        getLunchLengths().addSample(canteen().getLunchLengthStat().mean());
        
        super.replicationFinished();
    }

    @Override
    public void simulationFinished()
    {
        // Dysplay simulation results
        super.simulationFinished();
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setModel(new Model(Id.model, this, null));
		setEnviroment(new Enviroment(Id.enviroment, this, model()));
		setVaccinationCenter(new VaccinationCenter(Id.vaccinationCenter, this, model()));
		setCanteen(new Canteen(Id.canteen, this, vaccinationCenter()));
		setRegistration(new Registration(Id.registration, this, vaccinationCenter()));
		setExamination(new Examination(Id.examination, this, vaccinationCenter()));
		setVaccination(new Vaccination(Id.vaccination, this, vaccinationCenter()));
		setWaitingRoom(new WaitingRoom(Id.waitingRoom, this, vaccinationCenter()));
		setSyringes(new Syringes(Id.syringes, this, vaccination()));
	}

	private Model _model;

public Model model()
	{ return _model; }

	public void setModel(Model model)
	{_model = model; }

	private Enviroment _enviroment;

public Enviroment enviroment()
	{ return _enviroment; }

	public void setEnviroment(Enviroment enviroment)
	{_enviroment = enviroment; }

	private VaccinationCenter _vaccinationCenter;

public VaccinationCenter vaccinationCenter()
	{ return _vaccinationCenter; }

	public void setVaccinationCenter(VaccinationCenter vaccinationCenter)
	{_vaccinationCenter = vaccinationCenter; }

	private Canteen _canteen;

public Canteen canteen()
	{ return _canteen; }

	public void setCanteen(Canteen canteen)
	{_canteen = canteen; }

	private Registration _registration;

public Registration registration()
	{ return _registration; }

	public void setRegistration(Registration registration)
	{_registration = registration; }

	private Examination _examination;

public Examination examination()
	{ return _examination; }

	public void setExamination(Examination examination)
	{_examination = examination; }

	private Vaccination _vaccination;

public Vaccination vaccination()
	{ return _vaccination; }

	public void setVaccination(Vaccination vaccination)
	{_vaccination = vaccination; }

	private WaitingRoom _waitingRoom;

public WaitingRoom waitingRoom()
	{ return _waitingRoom; }

	public void setWaitingRoom(WaitingRoom waitingRoom)
	{_waitingRoom = waitingRoom; }

	private Syringes _syringes;

public Syringes syringes()
	{ return _syringes; }

	public void setSyringes(Syringes syringes)
	{_syringes = syringes; }
	//meta! tag="end"
}
