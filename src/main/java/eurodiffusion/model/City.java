package eurodiffusion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Data
@AllArgsConstructor
public class City {

    public static final int REPRESENTATIVE_PORTION_DIVIDER = 1000;
    public static final int STARTING_COINS_CURRENCY = 1_000_000;

    @ToString.Exclude
    private Country country;
    @ToString.Exclude
    private List<City> neighbours;
    private Map<String, Integer> currencyToTotalAmount;
    private Map<String, Integer> currencyToCacheAmount;

    public City(Country country) {
        this.country = country;
        this.neighbours = new ArrayList<>();
        this.currencyToTotalAmount = new HashMap<>();
        this.currencyToCacheAmount = new HashMap<>();
        this.currencyToTotalAmount.put(this.country.getCurrencyName(), STARTING_COINS_CURRENCY);
    }

    public boolean isCompleted(int currenciesExpected) {
        return currencyToTotalAmount.keySet().size() == currenciesExpected;
    }

    public void addNeighbour(City city) {
        if (nonNull(neighbours) && nonNull(city)) {
            neighbours.add(city);
        }
    }

    private void addMoney(Map<String, Integer> currencyToMoney, String currency, int money) {
        currencyToMoney.merge(currency, money, Integer::sum);
    }

    private void subtractMoney(Map<String, Integer> currencyToMoney, String currency, int money) {
        if (currencyToMoney.containsKey(currency)) {
            currencyToMoney.merge(currency, money, (m1, m2) -> m1 - m2);
        }
    }

    public void addMoneyToCache(String currency, int money) {
        addMoney(currencyToCacheAmount, currency, money);
    }

    private void addMoneyToTotal(String currency, int money) {
        addMoney(currencyToTotalAmount, currency, money);
    }

    public void addCacheMoneyToTotalBalance() {
        currencyToCacheAmount.forEach(this::addMoneyToTotal);
        currencyToCacheAmount.clear();
    }

    public void transportRepresentativePortionToNeighbours() {
        Map<String, Integer> currencyToRepresentativePortion = currencyToTotalAmount.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), this::calculateRepresentativePortion));

        for (City neighbour : neighbours) {
            for (String currency : currencyToTotalAmount.keySet()) {
                int representativePortion = currencyToRepresentativePortion.get(currency);
                if (representativePortion > 0) {
                    neighbour.addMoneyToCache(currency, representativePortion);
                    subtractMoney(currencyToTotalAmount, currency, representativePortion);
                }
            }
        }
    }

    private Integer calculateRepresentativePortion(String currency) {
        return currencyToTotalAmount.getOrDefault(currency, 0) / REPRESENTATIVE_PORTION_DIVIDER;
    }

}
