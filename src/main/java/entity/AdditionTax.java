package entity;

import java.math.BigDecimal;

/**
 * Created by sdww on 15-10-31.
 */
public class AdditionTax extends Tax {

    private BigDecimal rate;

    private GoodsClassifier classifier;

    public AdditionTax(String additionPath, BigDecimal rate) {
        classifier = new GoodsClassifier(additionPath);
        this.rate = rate;
    }

    @Override
    public BigDecimal getTaxRat(Item item){
        if(classifier.isExempt(item.getName())) {
            return rate;
        } else {
            return new BigDecimal(0);
        }
    }
}
