package project.jpf;

import gov.nasa.jpf.util.test.TestJPF;


public class NumericTest extends TestJPF {
    static class C2 {
        void doSomething() {
            double x = 0.5;
        }
    }

    @org.junit.Test
    public void testVars() {
        if (verifyNoPropertyViolation("+listener=.listener.NumericValueChecker",
                "+range.vars=x",
                "+range.x.var=*$C2.doSomething():x",
                "+range.x.max=0.9")) {
            C2 c2 = new C2();
            c2.doSomething();
        }
    }
}