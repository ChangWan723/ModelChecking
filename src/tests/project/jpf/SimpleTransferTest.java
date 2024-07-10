package project.jpf;

import gov.nasa.jpf.util.TypeRef;
import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import src.controller.SimpleTransCtrl;
import src.repository.AccountRepo;
import src.repository.DefaultAccountRepo;


public class SimpleTransferTest extends TestJPF {
    private static final AccountRepo accountRepo = DefaultAccountRepo.getInstance();
    final double EPSILON = 0.001;

    static final TypeRef PROPERTY = new TypeRef("gov.nasa.jpf.listener.PreciseRaceDetector");
    static final String LISTENER = "+listener=gov.nasa.jpf.listener.PreciseRaceDetector";

    private static final String[] JPF_ARGS = new String[]{
            "+listener=gov.nasa.jpf.listener.PreciseRaceDetector",
            "+classpath=build/project"
    };

    @Test
    public void testRace_by_PreciseRaceDetector() {
        if (verifyNoPropertyViolation(JPF_ARGS)) {
            new SimpleTransCtrl().transToEachOther(1, 2, 10);
        }
    }

    @Test
    public void testRace_by_assertion() {
        new SimpleTransCtrl().transToEachOther(1, 2, 10);

        assertTrue(Math.abs(accountRepo.queryBalance(1) - 1000) < EPSILON);
        assertTrue(Math.abs(accountRepo.queryBalance(2) - 1000) < EPSILON);
    }
}

