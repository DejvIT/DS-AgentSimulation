package simulation;

import OSPABA.*;
import vaccinationcenter.agents.Person;
import vaccinationcenter.agents.Staff;

public class MyMessage extends MessageForm
{
    private Staff staff;
    private Person person;
    private String log = "\n====<Message Log>====\n";
    
    public MyMessage(Simulation sim)
    {
        super(sim);
    }

    public MyMessage(MyMessage original)
    {
        super(original);
        // copy() is called in superclass
        this.staff = ((MyMessage)original).staff;
        this.person = ((MyMessage)original).person;
    }

    @Override
    public MessageForm createCopy()
    {
        return new MyMessage(this);
    }

    @Override
    protected void copy(MessageForm message)
    {
        super.copy(message);
        MyMessage original = (MyMessage)message;
        // Copy attributes
    }
    
    public Person getPerson() {
        return this.person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }
    
    public Staff getStaff() {
        return this.staff;
    }
    
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    
    public String getLog() {
        return (this.log + "====<Message Log>====\n");
    }
    
    public void addLog(String log) {
        this.log += (String.format("%.2f", mySim().currentTime()) + ": " +log + "\n");
    }
}
