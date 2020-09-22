import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vendingmachine.Cash;
import vendingmachine.exception.CashInMachineTooLowException;
import vendingmachine.CashState;
import vendingmachine.VendingMachineChangeCalculator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static vendingmachine.Cash.*;

@RunWith(Enclosed.class)
public class VendingMachineChangeCalculatorTest {

    protected final VendingMachineChangeCalculator calculator = new VendingMachineChangeCalculator();

    Map<Cash, Integer> initialState;
    Map<Cash, Integer> expectedChange;
    BigDecimal amountToReturn;

    public VendingMachineChangeCalculatorTest(Map<Cash, Integer> initialState, BigDecimal amountToReturn, Map<Cash, Integer> expectedChange) {
        this.initialState = initialState;
        this.expectedChange = expectedChange;
        this.amountToReturn = amountToReturn;
    }

    @RunWith(Parameterized.class)
    public static class TooLowAmountTest extends VendingMachineChangeCalculatorTest {

        public TooLowAmountTest(Map<Cash, Integer> initialState, BigDecimal amountToReturn, Map<Cash, Integer> expectedChange) {
            super(initialState, amountToReturn, expectedChange);
        }

        @Parameterized.Parameters
        public static Collection tooLowAmountInMachineParams() {
            return Arrays.asList(new Object[][]{
                    {emptyMap(),
                            valueOf(0),
                            emptyMap()},
                    {Map.of(TWENTY_CENTS, 3,
                            ONE_CENT, 14),
                            valueOf(0.03),
                            emptyMap()}
            });
        }

        @Test(expected = CashInMachineTooLowException.class)
        public void validateAmountTooLow() throws CashInMachineTooLowException {
            calculator.calculateChange(new CashState(initialState), amountToReturn);
        }
    }

    @RunWith(Parameterized.class)
    public static class ProperChangeTest extends VendingMachineChangeCalculatorTest {

        public ProperChangeTest(Map<Cash, Integer> initialState, BigDecimal amountToReturn, Map<Cash, Integer> expectedChange) {
            super(initialState, amountToReturn, expectedChange);
        }

        @Parameterized.Parameters
        public static Collection changeReturnedParameters() {
            return Arrays.asList(new Object[][]{
                    {Map.of(TWENTY_CENTS, 3,
                            TEN_CENTS, 2,
                            ONE_DOLLAR, 2),
                            valueOf(0.4),
                            Map.of(TWENTY_CENTS, 2)},
                    {Map.of(TEN_CENTS, 101,
                            ONE_DOLLAR, 2,
                            FIVE_DOLLARS, 2),
                            valueOf(10.11),
                            Map.of(FIVE_DOLLARS, 2,
                                    TEN_CENTS, 2)},
                    {Map.of(TWENTY_CENTS, 12,
                            TWO_CENTS, 12,
                            ONE_DOLLAR, 2),
                            valueOf(3.67),
                            Map.of(TWO_CENTS, 4,
                                    TWENTY_CENTS, 8,
                                    ONE_DOLLAR, 2)},
                    {Map.of(ONE_CENT, 5,
                            TWO_CENTS, 6,
                            FIVE_CENTS, 4,
                            TEN_CENTS, 10,
                            TWENTY_CENTS, 11,
                            FIFTY_CENTS, 11,
                            ONE_DOLLAR, 5,
                            TWO_DOLLARS, 6,
                            FIVE_DOLLARS, 5),
                            valueOf(0.16),
                            Map.of(TEN_CENTS, 1,
                                    FIVE_CENTS, 1,
                                    ONE_CENT, 1)},
                    {Map.of(ONE_CENT, 1,
                            TWO_CENTS, 5,
                            TWENTY_CENTS, 2,
                            FIFTY_CENTS, 2,
                            ONE_DOLLAR, 1,
                            TWO_DOLLARS, 1),
                            valueOf(0.17),
                            Map.of(TWENTY_CENTS, 1)},
                    {Map.of(ONE_CENT, 11,
                            TWO_CENTS, 50,
                            TWENTY_CENTS, 202,
                            FIFTY_CENTS, 23,
                            ONE_DOLLAR, 11,
                            TWO_DOLLARS, 1),
                            valueOf(1.34),
                            Map.of(TWO_CENTS, 7,
                                    TWENTY_CENTS, 1,
                                    ONE_DOLLAR, 1)},
                    {Map.of(TEN_CENTS, 1,
                            TWENTY_CENTS, 4,
                            TWO_DOLLARS, 1),
                            valueOf(0.41),
                            Map.of(TWENTY_CENTS, 2,
                                    TEN_CENTS, 1)
                    }
            });
        }

        @Test
        public void validateProperChangeReturned() throws CashInMachineTooLowException {
            var change = calculator.calculateChange(new CashState(initialState), amountToReturn);
            assertEquals(expectedChange, change.getCashState());
        }
    }

}
