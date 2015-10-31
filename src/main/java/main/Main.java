package main;

import entity.GoodsClassifier;
import method.Methods;

import java.util.List;

/**
 * Created by sdww on 15-10-30.
 */
public class Main {
    private static final String defaultPath = "input/input.txt";
    private static final String defaultNonTaxPath = "load";
    private static final String defaultAdditional = "addition";

    public static void main(String[] args) {
        Methods methods = new Methods();
        String path = defaultPath;
        String loadPath = defaultNonTaxPath;
        //手动设置输入路径
        if(args.length > 0) {
            path = args[0];
            //手动设置load路径
            if(args.length == 2) {
                loadPath = args[1];
            }
        }
        methods.exemptClassifier = GoodsClassifier.getInstance(loadPath);
        methods.chargeClassifier = GoodsClassifier.getInstance(defaultAdditional);
        List<String> itemList = methods.getInputs(path);
        if(itemList != null && itemList.size() != 0) {
            String outputStr = methods.process(itemList);
            System.out.print(outputStr);
        }
    }
}
