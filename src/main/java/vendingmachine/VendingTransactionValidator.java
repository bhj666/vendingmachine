package vendingmachine;

import vendingmachine.exception.AmountNotSufficientException;

import java.math.BigDecimal;

public class VendingTransactionValidator {

    public void validate(BigDecimal cashPaid, BigDecimal price) throws AmountNotSufficientException {
        checkIfAmountPaidIsEnough(cashPaid, price);
    }

    private void checkIfAmountPaidIsEnough(BigDecimal cashPaid, BigDecimal price) throws AmountNotSufficientException {
        if (cashPaid.compareTo(price) < 0) {
            throw new AmountNotSufficientException(String.format("Money paid %s is less than product price %s", cashPaid, price));
        }
    }
}
