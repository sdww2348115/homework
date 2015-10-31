package method;

import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sdww on 15-10-30.
 */
public class MethodsTest extends TestCase {

    public void testGetInputs() throws Exception {
        Methods methods = new Methods();
        List<String> excepts = new LinkedList<String>();
        excepts.add("1 book at 12.49");
        excepts.add("1 music CD at 14.99");
        excepts.add("1 chocolate bar at 0.85");
        List<String> actuals = methods.getInputs("./input/input1.txt");
        assertEquals(excepts.get(0), actuals.get(0));
    }
}