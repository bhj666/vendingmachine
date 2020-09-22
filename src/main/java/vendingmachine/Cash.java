package vendingmachine;

import java.math.BigDecimal;
import java.util.Arrays;

public enum Cash {

    ONE_CENT(BigDecimal.valueOf(0.01)),
    TWO_CENTS(BigDecimal.valueOf(0.02)),
    FIVE_CENTS(BigDecimal.valueOf(0.05)),
    TEN_CENTS(BigDecimal.valueOf(0.1)),
    TWENTY_CENTS(BigDecimal.valueOf(0.2)),
    FIFTY_CENTS(BigDecimal.valueOf(0.5)),
    ONE_DOLLAR(BigDecimal.valueOf(1)),
    TWO_DOLLARS(BigDecimal.valueOf(2)),
    FIVE_DOLLARS(BigDecimal.valueOf(5));

    private final BigDecimal amount;

    Cash(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public static Cash of(BigDecimal amount) {
        return Arrays.stream(values()).filter(c -> c.getAmount().compareTo(amount) == 0).findFirst().orElse(null);
    }
}
