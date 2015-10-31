package entity;

import java.math.BigDecimal;

/**
 * Created by sdww on 15-10-31.
 */
public abstract class Tax {

    /**
     * 根据商品信息得出相应的税率
     * @param item
     * @return 该商品应缴纳相应税税率
     */
    public abstract BigDecimal getTaxRat(Item item);
}
