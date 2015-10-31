package entity;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sdww on 15-10-30.
 */
public class Item {

    //检测单行输入是否带量词关键字，即“ of ”
    public static final String CLASSFIER_REG = "\\s(\\w*)\\sof";
    //检测单行输入是否为进口，即“ imported ”
    public static final String IMPORTED_KEY = " imported ";

    //商品数量
    private int number;
    //商品是否为进口
    private boolean imported;
    //商品量词（包括of）
    private String classifier;
    //商品名称
    private String name;
    //商品价格（不含税）
    private BigDecimal price;
    //该商品税值
    private BigDecimal tax;
    //商品含税价格
    private BigDecimal totalPrice;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 解析每行文本得出商品信息
     * @param str 每行的文本信息
     * @return 商品的相关信息
     * @throws Exception
     */
    public static Item getInstance(String str) throws Exception{
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

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(number);
        if(true == imported) {
            str.append(" imported");
        }
        if(null != classifier) {
            str.append(classifier);
        }
        str.append(" " + name + ":");
        str.append(" " + String.valueOf(totalPrice) + "\n");
        return new String(str);
    }
}
