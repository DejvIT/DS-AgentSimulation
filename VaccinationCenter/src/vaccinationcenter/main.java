/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter;

import simulation.MySimulation;
import vaccinationcenter.gui.GUI;

/**
 *
 * @author davidpavlicko
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//        MySimulation sim = new MySimulation(540, 5, 6, 3);	
//        System.out.println("VALIDATION MODE IS " + (sim.isValidation() ? "ON" : "OFF") + "!\n");
//        sim.simulate(10);
//        
//        System.out.println("Arrivals " + sim.vaccinationCenter().getArrivals().mean());
//        System.out.println("Departures " + sim.vaccinationCenter().getDepartures().mean());
//        System.out.println("Cooling " + sim.vaccinationCenter().getCoolingLengths().mean());
//        System.out.println("Registration waiting " + sim.vaccinationCenter().getRegistrationWaitings().mean());
//        System.out.println("Examination waiting " + sim.vaccinationCenter().getExaminationWaitings().mean());
//        System.out.println("Vaccination waiting " + sim.vaccinationCenter().getVaccinationWaitings().mean());
//        System.out.println("Registration queue " + sim.vaccinationCenter().getRegistrationQueueLengths().mean());
//        System.out.println("Examination queue " + sim.vaccinationCenter().getExaminationQueueLengths().mean());
//        System.out.println("Vaccination queue " + sim.vaccinationCenter().getVaccinationQueueLengths().mean());
//        System.out.println("Registration efficiency " + sim.vaccinationCenter().getRegistrationEfficiency().mean());
//        System.out.println("Examination efficiency " + sim.vaccinationCenter().getExaminationEfficiency().mean());
//        System.out.println("Vaccination efficiency " + sim.vaccinationCenter().getVaccinationEfficiency().mean());
//        System.out.println("Waiting room filling " + sim.vaccinationCenter().getWaitingRoomFilling().mean());
//        
//        System.out.println("Syringes refill waiting " + sim.vaccinationCenter().getRefillWaitings().mean());
//        System.out.println("Syringes refill queue " + sim.vaccinationCenter().getRefillQueueLengths().mean());
//        System.out.println("Lunch time " + sim.vaccinationCenter().getLunchLengths().mean());

        GUI g = new GUI();
        g.setVisible(true); 

    }
    
}
