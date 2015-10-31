package method;

import entity.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sdww on 15-10-30.
 */
public class Methods {
    //标准行错误输出
    public final String ERRINFO = "oh~ something was wrong in this line\n";

    public List<Tax> taxes = new LinkedList<Tax>();

    public Methods(String loadPath, String additionPath) {
        taxes.add(new ImportDuty(new BigDecimal(0.05)));
        taxes.add(new BasicTax(loadPath, new BigDecimal(0.1)));
        taxes.add(new AdditionTax(additionPath, new BigDecimal(0.05)));
    }

    /**
     * 总输入函数，将输入分为以行为单位
     * @param path 输入文件的path
     * @return  每行输入文件的字符串
     */
    public List<String> getInputs(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        List<String> list = new LinkedList<String>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String lineString = null;
            while((lineString = reader.readLine()) != null) {
                //去除空白行干扰
                if(!lineString.isEmpty()) {
                    //简单处理该行有多个空格的情况
                    lineString =  lineString.replaceAll("\\s{2,}", " ");
                    list.add(lineString);
                }
            }
            reader.close();
        } catch(IOException e) {
            System.out.println("file open faild, please check your input");
        } finally {
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return list;
    }

    /**
     * 分行处理输入，并得到最终的输出
     * @param input 每行的输入数据
     * @return 输出字符串
     */
    public String process(List<String> input) {
        List<Item> itemList = new LinkedList<Item>();
        StringBuffer outputStr = new StringBuffer();
        for(String line:input) {
            try {
                //从文本中读取&分析商品信息
                Item newItem = Item.getInstance(line);
                calculateTax(newItem);
                itemList.add(newItem);
                outputStr.append(newItem.toString());
            } catch (Exception e) {
                outputStr.append(ERRINFO);
            }
        }
        BigDecimal totalTaxes = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        for(Item item : itemList) {
            totalTaxes = totalTaxes.add(item.getTax());
            totalPrice = totalPrice.add(item.getTotalPrice());
        }
        outputStr.append("Sales Taxes: " + totalTaxes + "\nTotal: " + totalPrice + "\n");
        return new String(outputStr);
    }

    /**
     * 计算商品税与总价
     * @param item
     */
    private void calculateTax(Item item) {
        BigDecimal taxRat = new BigDecimal(0);
        for(Tax tax:taxes) {
            taxRat = taxRat.add(tax.getTaxRat(item));
        }
        BigDecimal totalTax = item.getPrice().multiply(taxRat);
        //totalTax以0.05向上取整
        totalTax = totalTax.setScale(2, BigDecimal.ROUND_HALF_UP);
        //首先计算税除以0.05的余数
        BigDecimal remainder = totalTax.remainder(new BigDecimal(0.05));
        //如果余数不等于0,则加上值补全为0.05的整数倍
        if(remainder.compareTo(BigDecimal.ZERO) > 0) {
            totalTax = totalTax.add((new BigDecimal(0.05)).subtract(remainder));
        }
        totalTax = totalTax.setScale(2, BigDecimal.ROUND_HALF_UP);
        item.setTax(totalTax);
        item.setTotalPrice(totalTax.add(item.getPrice()));
    }
}
