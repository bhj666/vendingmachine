package vendingmachine;

import vendingmachine.exception.AmountNotRoundedException;

import java.math.BigDecimal;
import java.util.HashMap;

public class VendingMachine {

    private final CashState machineCashState;
    private final CashState newTransactionState;
    private final VendingTransactionValidator transactionValidator;
    private final VendingMachineChangeCalculator vendingMachineChangeCalculator;

    public VendingMachine(CashState machineCashState,
                          VendingTransactionValidator transactionValidator,
                          VendingMachineChangeCalculator vendingMachineChangeCalculator) {
        this.machineCashState = machineCashState;
        this.vendingMachineChangeCalculator = vendingMachineChangeCalculator;
        this.newTransactionState = new CashState(new HashMap<>());
        this.transactionValidator = transactionValidator;
    }

    public CashState getMachineState() {
        return this.machineCashState;
    }

    public void addMoney(CashState cashPaid) {
        machineCashState.add(cashPaid);
        newTransactionState.add(cashPaid);
    }

    public boolean buyProduct(BigDecimal productPrice) {
        try {
            if (productPrice.precision() > 2) {
                throw new AmountNotRoundedException();
            }
            transactionValidator.validate(newTransactionState.getFullAmount(), productPrice);
            machineCashState.remove(vendingMachineChangeCalculator.calculateChange(machineCashState, newTransactionState.getFullAmount().subtract(productPrice)));
        } catch (Exception e) {
            machineCashState.remove(newTransactionState);
            return false;
        } finally {
            newTransactionState.clear();
        }
        return true;
    }
}
