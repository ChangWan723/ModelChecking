package project.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import project.testcase.TransExample;

@RunWith(Parameterized.class)
public class DefaultTransactionTest {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[10][0];
    }

    @Test
    public void testTrans() {
        int[] result = TransExample.transExample();

        Assert.assertEquals(1000, result[0]);
        Assert.assertEquals(1000, result[1]);
    }
}
