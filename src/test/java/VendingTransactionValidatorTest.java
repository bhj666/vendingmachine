import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import vendingmachine.exception.AmountNotSufficientException;
import vendingmachine.VendingTransactionValidator;

import static java.math.BigDecimal.*;

@RunWith(JUnit4.class)
public class VendingTransactionValidatorTest {

    private final VendingTransactionValidator validator = new VendingTransactionValidator();

    @Test
    public void testHappyPath() throws AmountNotSufficientException {
        validator.validate(valueOf(20), valueOf(10));
    }

    @Test
    public void testFreeProduct() throws AmountNotSufficientException {
        validator.validate(valueOf(0), valueOf(0));
    }

    @Test(expected = AmountNotSufficientException.class)
    public void testAmountLesserThanOrder() throws AmountNotSufficientException {
        validator.validate(valueOf(20), valueOf(100));
    }
}
