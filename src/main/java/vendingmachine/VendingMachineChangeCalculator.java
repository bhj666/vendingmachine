package vendingmachine;

import vendingmachine.exception.CashInMachineTooLowException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

public class VendingMachineChangeCalculator {

    private final static BigDecimal AMOUNT_TO_BE_LEFT_IN_MACHINE = BigDecimal.valueOf(0.73);

    public CashState calculateChange(CashState machineCashState, BigDecimal amountToReturn) throws CashInMachineTooLowException {
        var temporaryChangeState = new CashState(machineCashState);
        var changeCalculationState = new CashState(new HashMap<>());
        Optional<BigDecimal> nextComponent;
        var amountLeftToReturn = amountToReturn;
        do {
            nextComponent = temporaryChangeState.getBiggestLowerThanOrEqualToAmount(amountLeftToReturn);
            amountLeftToReturn = amountLeftToReturn.subtract(nextComponent.orElse(BigDecimal.ZERO));
            nextComponent.ifPresent(c -> {
                temporaryChangeState.removeOne(c);
                changeCalculationState.addOne(c);
            });
        } while (nextComponent.isPresent() && amountLeftToReturn.compareTo(BigDecimal.ZERO) > 0);

        while (changeCalculationState.getFullAmount().compareTo(amountToReturn) < 0) {
            nextComponent = temporaryChangeState.getLowest();
            nextComponent.ifPresent(c -> {
                temporaryChangeState.removeOne(c);
                changeCalculationState.addOne(c);
            });
        }
//        TODO: At this moment if in machine there is less than 0.73 after executing proper order (money paid + initial cash < 0.73) order will be rejected
//        Changing product price won't help as it would only decrease amount left in machine, it seems like undesirable requirement
        if (changeCalculationState.getFullAmount().compareTo(amountToReturn) >= 0 && temporaryChangeState.getFullAmount().compareTo(AMOUNT_TO_BE_LEFT_IN_MACHINE) >= 0) {
            return getChange(changeCalculationState, amountToReturn);
        } else {
            throw new CashInMachineTooLowException();
        }
    }


    private CashState getChange(CashState cashState, BigDecimal amountToReturn) {
        var changeState = new CashState(new HashMap<>());
        var calculationState = new CashState(cashState);
        BigDecimal nextComponent;
        while (changeState.getFullAmount().compareTo(amountToReturn) < 0) {
            nextComponent = calculationState.getBiggest().get();
            calculationState.removeOne(nextComponent);
            changeState.addOne(nextComponent);
        }
        return changeState;
    }
}
