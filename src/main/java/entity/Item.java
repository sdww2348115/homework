package entity;

import java.math.BigDecimal;

/**
 * Created by sdww on 15-10-30.
 */
public class Item {

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
