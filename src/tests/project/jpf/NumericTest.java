package project.jpf;

import gov.nasa.jpf.util.test.TestJPF;


public class NumericTest extends TestJPF {
    static class InterestCalculator {
        void calculateCompoundInterest() {
            double monthlyRate = 0.3;
        }
    }

    @org.junit.Test
    public void testVars() {
        // A bug was found when using NumericValueChecker.
        // Here's a simple example of the bug
        if (verifyNoPropertyViolation("+listener=.listener.NumericValueChecker",
                "+range.vars=monthlyRate",
                "+range.monthlyRate.var=*$InterestCalculator.calculateCompoundInterest():monthlyRate",
                "+range.monthlyRate.min=0",
                "+range.monthlyRate.max=0.5")) {
            InterestCalculator interestCalculator = new InterestCalculator();
            interestCalculator.calculateCompoundInterest();
        }
    }
}