package continualAssistants;

import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
import simulation.*;
import agents.*;
import java.util.PriorityQueue;

//meta! id="15"
public class DoArrival extends Scheduler
{
    private static final UniformContinuousRNG rng = new UniformContinuousRNG(0d,1d);
    private double dailySkipped;
    
    //Experiment 3
    private static final EmpiricRNG empiricRng =  new EmpiricRNG(
        new EmpiricPair(new UniformContinuousRNG(60d, 1200d), 0.3), 
        new EmpiricPair(new UniformContinuousRNG(1200d, 3600d), 0.4), 
        new EmpiricPair(new UniformContinuousRNG(3600d, 4800d), 0.2),
        new EmpiricPair(new UniformContinuousRNG(4800d, 14400d), 0.1)
    );
    private PriorityQueue<Double> arrivals;
    
    public DoArrival(int id, Simulation mySim, CommonAgent myAgent)
    {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication()
    {
        super.prepareReplication();
        // Setup component for the next replication
        this.dailySkipped = getDailySkipped();
        
        if (myAgent().isEmpiricArrivals() && !myAgent().isValidation()) {
            planArrivals();
        }
    }

    //meta! sender="Enviroment", id="16", type="Start"
    public void processStart(MessageForm message)
    {
        if (myAgent().isEmpiricArrivals() && !myAgent().isValidation()) {
            double holdFor = this.arrivals.poll();
            if (holdFor < 0) {
                hold(0, message);
            } else {
                hold(holdFor, message);
            }
        } else {
            hold(0, message);
        }
        message.setCode(Mc.finish);
    }
    
    public double getDailySkipped() {
        double expectedArrivals = (double) myAgent().getOrders();
        
        int max = (int) Math.round(expectedArrivals * (24/540.0));
        int min = (int) Math.round(expectedArrivals * (5/540.0));
        UniformDiscreteRNG rng = new UniformDiscreteRNG(min, max);
       
        return rng.sample() / (expectedArrivals);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message)
    {
        switch (message.code())
        {
            case Mc.finish:
                if (myAgent().isEmpiricArrivals() && !myAgent().isValidation()) {
                    while(!this.arrivals.isEmpty()) {
                        MessageForm copy = message.createCopy();
                        double holdFor = this.arrivals.poll();
                        if (holdFor < 0) {
                            hold(0, copy);
                        } else {
                            hold(holdFor, copy);
                        }
                    }
                    assistantFinished(message);
                } else {
                    if (mySim().currentTime() < (32400 - (32400 / myAgent().getOrders()))) {
                        MessageForm copy = message.createCopy();
                        hold(32400d / (myAgent().getOrders()), copy);
                    }
                    if (this.dailySkipped < this.rng.sample()) {
                        assistantFinished(message);
                    }
                }
            break;
            default:
                System.out.println(((MyMessage)message).getLog());
                throw new UnsupportedOperationException("DoArrival - not supported default message.");
        }
    }
    
    private void planArrivals() {
        this.arrivals = new PriorityQueue<>();
        
        int orders = myAgent().getOrders();
        double planningTime = mySim().currentTime();
        for (int i = 0; i < orders; i++) {
            if (this.dailySkipped < this.rng.sample()) {
                if (this.rng.sample() < 0.1) {
                    this.arrivals.add(planningTime);
                } else {
                    double empiricSample = (double) this.empiricRng.sample();
                    this.arrivals.add(planningTime - empiricSample);
                }
            }
            planningTime += (32400d / orders);
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
    public Enviroment myAgent()
    {
        return (Enviroment)super.myAgent();
    }

}
