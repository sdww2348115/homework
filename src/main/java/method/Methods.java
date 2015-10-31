package method;

import entity.Item;
import entity.GoodsClassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sdww on 15-10-30.
 */
public class Methods {
    //标准行错误输出
    public final String ERRINFO = "oh~ something was wrong in this line\n";
    //检测单行输入是否带量词关键字，即“ of ”
    public final String CLASSFIER_REG = "\\s(\\w*)\\sof";
    //检测单行输入是否为进口，即“ imported ”
    public final String IMPORTED_KEY = " imported ";
    //商品分类器，主要用来判断商品是否为免税商品
    public GoodsClassifier exemptClassifier;
    //商品分类器，主要用来判断商品是否为免税商品
    public GoodsClassifier chargeClassifier;

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
                Item newItem = getGoodInfo(line);
                calculateTax(newItem);
                itemList.add(newItem);
                outputStr.append(newItem.toString());
            } catch (Exception e) {
                outputStr.append(ERRINFO);
            }
        }
        BigDecimal totalTaxs = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        for(Item item : itemList) {
            totalTaxs = totalTaxs.add(item.getTax());
            totalPrice = totalPrice.add(item.getTotalPrice());
        }
        outputStr.append("Sales Taxes: " + totalTaxs + "\nTotal: " + totalPrice + "\n");
        return new String(outputStr);
    }

    /**
     * 解析每行文本得出商品信息
     * @param str 每行的文本信息
     * @return 商品的相关信息
     * @throws Exception
     */
    public Item getGoodInfo(String str) throws Exception{
        Item item = new Item();
        //首先判断该商品是否为进口商品
        if(str.contains(IMPORTED_KEY)) {
            item.setImported(true);
            str = str.replace(IMPORTED_KEY, " ");
        }
        //判断该商品是否含有量词
        Pattern pattern = Pattern.compile("\\s(\\w*)\\sof");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()) {
            item.setClassifier(matcher.group());
            str = str.replaceAll("\\s(\\w*)\\sof", "");
        }
        //数量
        pattern = Pattern.compile("^[0-9]*");
        matcher = pattern.matcher(str);
        if(matcher.find()) {
            item.setNumber(Integer.valueOf(matcher.group()));
            str = str.replaceAll("^[0-9]*\\s", "");
        }
        //存储其他商品属性
        String[] chs = str.split(" ");
        StringBuffer name = new StringBuffer(chs[0]);
        for(int i = 1; i < chs.length; i++) {
            //of 之后为该商品价格，exception由上层函数catch
            if(chs[i].equals("at")) {
                Double dPrice = Double.valueOf(chs[i + 1]);
                item.setPrice(BigDecimal.valueOf(dPrice));
                break;
            }
            //多单词商品名
            name.append(" " + chs[i]);
        }
        item.setName(new String(name));
        return item;
    }

    /**
     * 计算商品税与总价
     * @param item
     */
    private void calculateTax(Item item) {
        BigDecimal taxRat = new BigDecimal(0);
        //如果该商品为进口商品
        if(item.isImported()) {
            taxRat = taxRat.add(new BigDecimal(0.05));
        }
        //根据该商品基本税是否免税
        if(exemptClassifier != null && !exemptClassifier.isExempt(item.getName())) {
            taxRat = taxRat.add(new BigDecimal(0.1));
        }
        if(chargeClassifier != null && chargeClassifier.isExempt(item.getName())) {
            taxRat = taxRat.add(new BigDecimal(0.05));
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
