package project.jpf;

import org.junit.Test;
import project.scenario.UserScenario;
import gov.nasa.jpf.util.test.TestJPF;


public class UserCtrlTest extends TestJPF {
    private static final String[] JPF_ARGS = new String[]{
            "+listener=gov.nasa.jpf.listener.PreciseRaceDetector"
    };

    @Test
    public void testConcurrentRegisterUser_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            UserScenario.concurrentUserCreation(20);
        }
    }
}

