package project.scenario;

import src.controller.InterestCtrl;

public class InterestScenario {
    public static void main(String[] args) {
        calculateCompoundInterest(1, 5);
    }

    public static void calculateCompoundInterest(int account, int years) {
        long amount = new InterestCtrl().calculateCompoundInterest(account, years);

        System.out.println(amount);
    }
}
