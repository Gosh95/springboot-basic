package org.prgrms.deukyun.voucherapp.voucher.entity;

import java.util.UUID;

/**
 * 정률 할인 바우처
 */
public class PercentDiscountVoucher implements Voucher {

    private final UUID id;
    public final long percent;

    public PercentDiscountVoucher(UUID id, long percent) {
        this.id = id;
        this.percent = percent;
    }


    @Override
    public UUID getVoucherId() {
        return id;
    }

    @Override
    public long discount(long beforeDiscount) {
        return beforeDiscount * (percent / 100);
    }
}
