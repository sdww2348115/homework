package entity;

import java.math.BigDecimal;

/**
 * Created by sdww on 15-10-31.
 */
public class ImportDuty extends Tax {

    private BigDecimal rate;

    public ImportDuty(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal getTaxRat(Item item){
        if(item.isImported()) {
            return rate;
        } else {
            return new BigDecimal(0);
        }
    }
}
