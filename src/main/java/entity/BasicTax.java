package entity;

import java.math.BigDecimal;

/**
 * Created by sdww on 15-10-31.
 */
public class BasicTax extends Tax{

    /*
    免税商品名单，包括书籍/食品/药品
     */
    private GoodsClassifier exemptGoods;

    private BigDecimal rate;

    public BasicTax(String path, BigDecimal rate) {
        exemptGoods = new GoodsClassifier(path);
        this.rate = rate;
    }

    @Override
    public BigDecimal getTaxRat(Item item) {
        if(!exemptGoods.isExempt(item.getName())) {
            return rate;
        } else {
            return new BigDecimal(0);
        }
    }

}
