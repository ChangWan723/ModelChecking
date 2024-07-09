package project.JPF;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import project.testcase.TransExample;


public class DefaultTransactionTest extends TestJPF {
    final double EPSILON = 0.001;

    private static final String[] JPF_ARGS = new String[]{
            "+listener+=,gov.nasa.jpf.listener.PreciseRaceDetector",
            "+classpath=build/project"
    };

    @Test
    public void testRace_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            TransExample.transExample();
        }
    }

    @Test
    public void testRace() {
        double[] result = TransExample.transExample();

        assertTrue(Math.abs(result[0] - 1000) < EPSILON);
        assertTrue(Math.abs(result[1] - 1000) < EPSILON);
    }
}

