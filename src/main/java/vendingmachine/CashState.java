package vendingmachine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CashState {

    private final Map<Cash, Integer> cashState;

    public CashState() {
        this.cashState = new HashMap<>();
    }

    public CashState(Map<Cash, Integer> cashState) {
        this.cashState = new HashMap<>(cashState);
    }

    public CashState(CashState copy) {
        this.cashState = new HashMap<>(copy.getCashState());
    }

    public Map<Cash, Integer> getCashState() {
        return cashState;
    }

    public BigDecimal getFullAmount() {
        return cashState.entrySet()
                .stream()
                .map(e -> e.getKey().getAmount().multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public void add(CashState addedCash) {
        addedCash.getCashState().forEach((k, v) -> this.cashState.merge(k, v, Integer::sum));
    }

    public void add(Cash cash, Integer quantity) {
        this.cashState.merge(cash, quantity, Integer::sum);
    }

    public void addOne(BigDecimal cash) {
        this.cashState.merge(Cash.of(cash), 1, Integer::sum);
    }

    public void remove(CashState addedCash) {
        addedCash.getCashState().forEach((k, v) -> this.cashState.merge(k, v, (oldValue, valueToSubtract) -> {
            var diff = oldValue - valueToSubtract;
            if(diff < 0) {
                throw new IllegalStateException();
            }
            return diff;
        }));
    }

    public void removeOne(BigDecimal cash) {
        this.cashState.merge(Cash.of(cash), 0, (oldValue, valueToSubtract) -> {
            if (oldValue == 0) {
                return 0;
            } else {
                return oldValue - 1;
            }
        });
    }

    public void clear() {
        this.cashState.clear();
    }

    public Optional<BigDecimal> getBiggestLowerThanOrEqualToAmount(BigDecimal amount) {
        return this.cashState.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .filter(e -> e.getValue() > 0)
                .filter(e -> e.getKey().getAmount().compareTo(amount) <= 0)
                .map(Map.Entry::getKey)
                .map(Cash::getAmount)
                .findFirst();
    }

    public Optional<BigDecimal> getLowest() {
        return this.cashState.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .map(Cash::getAmount)
                .findFirst();
    }

    public Optional<BigDecimal> getBiggest() {
        return this.cashState.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .map(Cash::getAmount)
                .findFirst();
    }
}
