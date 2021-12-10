package pt.com.visual.nuts.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.com.visual.nuts.util.Json;

import java.util.Map;

public class VisualNutsAnalysisTest {

    VisualNutsAnalysis visualNutsAnalysis = new VisualNutsAnalysis();

    @Test
    void mustCountCountries() {
        Assertions.assertEquals(6, visualNutsAnalysis.countCountries(Json.COUNTRY_LANGUAGES));
    }

    @Test
    void mustReturnGermanSpeakingCountryWithMostSpokenLanguages() {
        Assertions.assertEquals("BE", visualNutsAnalysis.findGermanSpeakingCountryWithMostOfficialLanguages(Json.COUNTRY_LANGUAGES));
    }

    @Test
    void mustCountCountriesSpokenLanguages() {
        Map<String, Long> countriesByLanguages = visualNutsAnalysis.countCountriesSpokenLanguages(Json.COUNTRY_LANGUAGES);
        Assertions.assertEquals(1L, countriesByLanguages.get("US"));
        Assertions.assertEquals(3L, countriesByLanguages.get("BE"));
        Assertions.assertEquals(2L, countriesByLanguages.get("NL"));
        Assertions.assertEquals(1L, countriesByLanguages.get("DE"));
        Assertions.assertEquals(1L, countriesByLanguages.get("ES"));
        Assertions.assertEquals(5L, countriesByLanguages.get("MK"));
    }

    @Test
    void mustFindCountryWithMostSpokenLanguages() {
        Assertions.assertEquals("MK", visualNutsAnalysis.findCountryWithMostOfficialLanguages(Json.COUNTRY_LANGUAGES));
    }

    @Test
    void mustFindMostCommonSpokenLanguage() {
        Assertions.assertEquals("de", visualNutsAnalysis.findMostCommonSpokenLanguage(Json.COUNTRY_LANGUAGES));
    }
}
