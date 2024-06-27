package project.JPF;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import project.testcase.TransExample;


public class DefaultTransactionTest extends TestJPF {
    private static final String[] JPF_ARGS = new String[]{
            "+listener+=,gov.nasa.jpf.listener.PreciseRaceDetector",
            "+classpath=build/project"
    };

    @Test
    public void testRace() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            TransExample.transExample();
        }
    }
}
