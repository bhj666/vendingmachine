import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vendingmachine.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static java.math.BigDecimal.*;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static vendingmachine.Cash.*;

@RunWith(Parameterized.class)
public class VendingMachineTest {

    Map<Cash, Integer> initialState;
    Map<Cash, Integer> moneyPaid;
    Boolean result;
    BigDecimal expectedEndMachineValue;
    BigDecimal productPrice;

    public VendingMachineTest(Map<Cash, Integer> initialState, Map<Cash, Integer> moneyPaid, BigDecimal productPrice, Boolean result, BigDecimal expectedEndMachineValue) {
        this.initialState = initialState;
        this.moneyPaid = moneyPaid;
        this.result = result;
        this.expectedEndMachineValue = expectedEndMachineValue;
        this.productPrice = productPrice;
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{
                {emptyMap(),
                        emptyMap(),
                        valueOf(2),
                        false,
                        ZERO},
                {Map.of(ONE_CENT, 5),
                        Map.of(TWENTY_CENTS, 5),
                        valueOf(1),
                        true,
                        valueOf(1.05)},
                {Map.of(ONE_CENT, 5),
                        Map.of(TWENTY_CENTS, 5),
                        valueOf(1),
                        true,
                        valueOf(1.05)},
                {Map.of(ONE_CENT, 5),
                        Map.of(TWO_CENTS, 5),
                        valueOf(0.1),
                        false,
                        valueOf(0.05)},
                {Map.of(ONE_CENT, 5),
                        Map.of(TWENTY_CENTS, 7),
                        valueOf(1),
                        true,
                        valueOf(1.05)}
        });
    }

    @Test
    public void testBasicScenarios() {
        var machine = prepareMachine(initialState);
        machine.addMoney(new CashState(moneyPaid));
        var transactionResult = machine.buyProduct(productPrice);
        assertEquals(transactionResult, result);
        assertEquals(expectedEndMachineValue, machine.getMachineState().getFullAmount());
    }

    private VendingMachine prepareMachine(Map<Cash, Integer> initialState) {
        return new VendingMachine(new CashState(initialState), new VendingTransactionValidator(), new VendingMachineChangeCalculator());
    }
}
