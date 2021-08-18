package org.prgrms.kdt.engine.voucher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {
    Optional<Voucher> findById(UUID voucherId);
    void insertVoucher(Voucher voucher);
}
