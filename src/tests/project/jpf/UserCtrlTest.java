package project.jpf;

import org.junit.Test;
import project.scenario.UserScenario;
import gov.nasa.jpf.util.test.TestJPF;


public class UserCtrlTest extends TestJPF {
    private static final String[] JPF_ARGS = new String[]{
            "+listener=gov.nasa.jpf.listener.PreciseRaceDetector"
    };

    @Test
    public void testRegisterUser_in_tread_pool_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            UserScenario.concurrentUserCreationByThreadPool(2);
        }
    }

    @Test
    public void testRegisterUser_in_two_tread_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            UserScenario.createUsersWithTwoThreads(2);
        }
    }
}

