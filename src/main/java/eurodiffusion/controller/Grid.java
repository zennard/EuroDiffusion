package eurodiffusion.controller;

import eurodiffusion.model.City;
import eurodiffusion.model.Country;
import eurodiffusion.model.Result;
import eurodiffusion.model.ResultPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

public class Grid {

    public static int PLANE_WIDTH = 10;
    public static int PLANE_HEIGHT = 10;

    private List<Country> countries;
    private City[][] cities = new City[PLANE_WIDTH + 2][PLANE_HEIGHT + 2];

    public Grid(List<Country> countries) {
        this.countries = countries;
        countries.forEach(this::addCountryToGrid);
        updateNeighbours();
    }

    public Result simulateDiffusion() {
        Result result = new Result();
        int day = 0;

        var countriesToComplete = new ArrayList<>(List.copyOf(countries));
        while (!countriesToComplete.isEmpty()) {
            List<Country> completedCountries = new ArrayList<>();
            for (var country : countriesToComplete) {
                if (country.isCompleted(countries.size())) {
                    result.addResultPart(ResultPart.builder()
                            .country(country)
                            .daysToComplete(day)
                            .build());
                    completedCountries.add(country);
                }
            }

            for (var completedCountry : completedCountries) {
                countriesToComplete.remove(completedCountry);
            }

            if (!countriesToComplete.isEmpty()) {
                simulateDay();
                day++;
            }
        }

        return result;
    }

    private void simulateDay() {
        Arrays.stream(cities)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .forEach(City::transportRepresentativePortionToNeighbours);
        Arrays.stream(cities)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .forEach(City::addCacheMoneyToTotalBalance);
    }

    private void addCountryToGrid(Country country) {
        int xl = country.getLocation().getLowerLeft().getX();
        int yl = country.getLocation().getLowerLeft().getY();
        int xr = country.getLocation().getHigherRight().getX();
        int yr = country.getLocation().getHigherRight().getY();

        for (int x = xl; x <= xr; x++) {
            for (int y = yl; y <= yr; y++) {
                City city = new City(country);
                country.getCities().add(city);
                cities[x][y] = city;
            }
        }
    }

    private void updateNeighbours() {
        for (int x = 1; x < PLANE_WIDTH + 1; x++) {
            for (int y = 1; y < PLANE_HEIGHT + 1; y++) {
                City city = cities[x][y];
                if (nonNull(city)) {
                    city.addNeighbour(cities[x][y + 1]);
                    city.addNeighbour(cities[x][y - 1]);
                    city.addNeighbour(cities[x + 1][y]);
                    city.addNeighbour(cities[x - 1][y]);
                }
            }
        }
    }

}
