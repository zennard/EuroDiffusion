package eurodiffusion.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static eurodiffusion.util.Util.safeStream;

@Data
public class Country {

    public static final int MAX_NAME_LENGTH = 25;
    public static final int MAX_SIZE = 10;
    public static final int MIN_SIZE = 1;
    public static final String NAME_INCORRECT_LENGTH_EXCEPTION_PATTERN = "Country name should not contain more than %s characters";
    public static final String INVALID_COUNTRY_COORDINATES_VALUES_EXCEPTION_PATTERN = "Invalid country coordinates values, coordinate must be between %s and %s";

    private String name;
    private Location location;
    private List<City> cities;

    public Country(String name, int xl, int yl, int xh, int yh) {
        boolean isNameLengthInvalid = name.length() > MAX_NAME_LENGTH || name.length() < 1;
        boolean areCoordinatesValuesInvalid = List.of(xl, yl, xh, yh).stream()
                .anyMatch(coord -> coord < MIN_SIZE || coord > MAX_SIZE);
        boolean isCoordinatesRangeInvalid = xl > xh || yl > yh;


        if (isNameLengthInvalid) {
            throw new IllegalArgumentException(String.format(NAME_INCORRECT_LENGTH_EXCEPTION_PATTERN, MAX_NAME_LENGTH));
        }
        if (areCoordinatesValuesInvalid) {
            throw new IllegalArgumentException(String.format(INVALID_COUNTRY_COORDINATES_VALUES_EXCEPTION_PATTERN,
                    MIN_SIZE, MAX_SIZE));
        }
        if (isCoordinatesRangeInvalid) {
            throw new IllegalArgumentException("Invalid country coordinates range, it is necessary that xl <= xh and yl <= yh");
        }

        this.location = Location.builder()
                .lowerLeft(Coordinate.builder().x(xl).y(yl).build())
                .higherRight(Coordinate.builder().x(xh).y(yh).build())
                .build();
        this.name = name;
        this.cities = new ArrayList<>();
    }

    public boolean isCompleted(int currenciesExpected) {
        return safeStream(cities).allMatch(c -> c.isCompleted(currenciesExpected));
    }

    public String getCurrencyName() {
        return this.name;
    }

}
