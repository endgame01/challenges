package pt.com.visual.nuts.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.com.visual.nuts.model.Country;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VisualNutsAnalysis {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public int countCountries(String countriesJson) {
        return getCountryList(countriesJson).size();
    }

    public String findGermanSpeakingCountryWithMostOfficialLanguages(String countriesJson) {
        return getCountryList(countriesJson).stream()
                .filter(country -> country.getLanguages().contains("de"))
                .max(Comparator.comparing(country -> country.getLanguages().size()))
                .map(Country::getCountry)
                .orElse(null);
    }

    public Map<String, Long> countCountriesSpokenLanguages(String countriesJson) {
        return getCountryList(countriesJson).stream()
                .collect(Collectors.groupingBy(Country::getCountry, Collectors.summingLong(c -> c.getLanguages().size())));
    }

    public String findCountryWithMostOfficialLanguages(String countriesJson) {
        return getCountryList(countriesJson).stream()
                .max(Comparator.comparing(country -> country.getLanguages().size()))
                .map(Country::getCountry)
                .orElse(null);
    }

    public String findMostCommonSpokenLanguage(String countriesJson) {
        return getCountryList(countriesJson).stream()
                .map(Country::getLanguages)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

    }

    private List<Country> getCountryList(String countriesJson) {
        try {
            return objectMapper.readValue(countriesJson, new TypeReference<List<Country>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
