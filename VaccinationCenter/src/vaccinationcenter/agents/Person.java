/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.agents;

/**
 *
 * @author davidpavlicko
 */
public class Person {
    
    private final double arrival;
    private final int order;
    private double registrationWaitingStart;
    private double registrationWaitingEnd;
    private double examinationWaitingStart;
    private double examinationWaitingEnd;
    private double vaccinationWaitingStart;
    private double vaccinationWaitingEnd;
    private double waitingStart;
    private double waitingEnd;
    private double departure;
    
    public Person(double arrivedAt, int order) {
        this.arrival = arrivedAt;
        this.order = order;
    }

    public double getArrival() {
        return this.arrival;
    }

    public int getOrder() {
        return this.order;
    }

    public double getRegistrationWaitingStart() {
        return this.registrationWaitingStart;
    }

    public double getRegistrationWaitingEnd() {
        return this.registrationWaitingEnd;
    }

    public double getExaminationWaitingStart() {
        return this.examinationWaitingStart;
    }

    public double getExaminationWaitingEnd() {
        return this.examinationWaitingEnd;
    }

    public double getVaccinationWaitingStart() {
        return this.vaccinationWaitingStart;
    }

    public double getVaccinationWaitingEnd() {
        return this.vaccinationWaitingEnd;
    }

    public double getWaitingStart() {
        return this.waitingStart;
    }

    public double getWaitingEnd() {
        return this.waitingEnd;
    }

    public double getDeparture() {
        return this.departure;
    }

    public void setRegistrationWaitingStart(double registrationWaitingStart) {
        this.registrationWaitingStart = registrationWaitingStart;
    }

    public void setRegistrationWaitingEnd(double registrationWaitingEnd) {
        this.registrationWaitingEnd = registrationWaitingEnd;
    }

    public void setExaminationWaitingStart(double examinationWaitingStart) {
        this.examinationWaitingStart = examinationWaitingStart;
    }

    public void setExaminationWaitingEnd(double examinationWaitingEnd) {
        this.examinationWaitingEnd = examinationWaitingEnd;
    }

    public void setVaccinationWaitingStart(double vaccinationWaitingStart) {
        this.vaccinationWaitingStart = vaccinationWaitingStart;
    }

    public void setVaccinationWaitingEnd(double vaccinationWaitingEnd) {
        this.vaccinationWaitingEnd = vaccinationWaitingEnd;
    }

    public void setWaitingStart(double waitingStart) {
        this.waitingStart = waitingStart;
    }

    public void setWaitingEnd(double waitingEnd) {
        this.waitingEnd = waitingEnd;
    }

    public void setDeparture(double departure) {
        this.departure = departure;
    }
}
