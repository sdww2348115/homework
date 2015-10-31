package main;
import method.Methods;

import java.util.List;

/**
 * Created by sdww on 15-10-30.
 */
public class Main {
    private static final String defaultPath = "input/input1.txt";
    private static final String defaultNonTaxPath = "load";
    private static final String defaultAddition = "addition";

    public static void main(String[] args) {
        String path = defaultPath;
        String loadPath = defaultNonTaxPath;
        if(args.length != 0) {
            path = args[0];
            if(args.length == 2) {
                loadPath = args[1];
            }
        }
        Methods methods = new Methods(loadPath, defaultAddition);
        List<String> goodsList = methods.getInputs(path);
        if(goodsList != null && goodsList.size() != 0) {
            String outputStr = methods.process(goodsList);
            System.out.print(outputStr);
        }
    }
}
