package eurodiffusion.parser;

import eurodiffusion.model.Country;
import eurodiffusion.model.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static eurodiffusion.util.Util.safeStream;
import static java.util.Objects.isNull;

@Data
@AllArgsConstructor
@Slf4j
public class ProblemFileParser implements Parser<List<Problem>> {

    private String filePath;

    @Override
    public List<Problem> parse() {
        List<Problem> parsedProblems = new ArrayList<>();
        int problemId = 1;
        List<String> lines = parseFile();
        for (int i = 0; i < lines.size(); i++) {
            int countriesNumber = Integer.parseInt(lines.get(i));
            List<String> countriesData = lines.subList(i + 1, i + 1 + countriesNumber);

            List<Country> parsedCountries = parseCountries(countriesData);
            parsedProblems.add(new Problem(problemId, parsedCountries));
            problemId++;
            i += countriesNumber;
        }
        return parsedProblems;
    }

    private List<Country> parseCountries(List<String> countriesData) {
        return safeStream(countriesData)
                .map(this::parseCountry)
                .collect(Collectors.toList());
    }

    private Country parseCountry(String countryData) {
        if (isNull(countryData)) {
            return null;
        }

        String[] countryFields = countryData.split(" ");
        if (countryFields.length != 5) {
            throw getIllegalArgumentException("Not enough fields was provided to define country: " + countryData);
        }

        String name = countryFields[0];
        int xl = Integer.parseInt(countryFields[1]);
        int yl = Integer.parseInt(countryFields[2]);
        int xh = Integer.parseInt(countryFields[3]);
        int yh = Integer.parseInt(countryFields[4]);
        return new Country(name, xl, yl, xh, yh);
    }

    private List<String> parseFile() {
        try (var reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("File was not found", ex);
            throw getIllegalArgumentException("Incorrect file path was provided: " + filePath);
        }
    }

    private IllegalArgumentException getIllegalArgumentException(String message) {
        return new IllegalArgumentException(message);
    }
}
