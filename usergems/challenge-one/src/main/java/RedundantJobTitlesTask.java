package com.usergems.challenge;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"ArraysAsListWithZeroOrOneArgument", "SlowListContainsAll"})
public class RedundantJobTitlesTask {
    /* Find all redundant entries in the haystack array where an entry in the needles array covers all or part of a haystack item.
     * E.g.: The entry "Software Engineer" in the haystack is redundant because the needles array includes an entry "Engineer".
     * "Hardware VP Engineering" is redundant because of "VP Engineering"
     * "Hardware VP" is NOT considered redundant for "VP Engineering" because only the word "VP" matches and only full matches of "VP Engineering" count
     * "Director" is not considered redundant for "cto" because only full word matches count
     */
    private List<String> findRedundantKeywords(List<String> needles, List<String> haystack) {
        return haystack.stream()
                .filter(entry -> isRedundant(needles, entry))
                .collect(Collectors.toList());
    }

    private boolean isRedundant(List<String> needles, String entry) {
        return needles.stream().anyMatch(needle -> isMatch(entry, needle));
    }

    private boolean isMatch(String entry, String match ) {
        return entry.contains(match) && getWordsList(entry).containsAll(getWordsList(match));
    }

    private List<String> getWordsList(String entry) {
        return Arrays.asList(entry.split(" "));
    }

    public static void main(String[] args) {
        new RedundantJobTitlesTask().test();
    }

    private void test() {
        List<Map<String, List<String>>> tests = Arrays.asList(
                Map.of(
                        "needles", Arrays.asList("Software", "cto", "VP Engineering"),
                        "haystack", Arrays.asList("Software Engineer", "Director", "Hardware VP Engineering"),
                        "output", Arrays.asList("Software Engineer", "Hardware VP Engineering")
                ),
                Map.of(
                        "needles", Arrays.asList("Software", "cto", "VP Engineering"),
                        "haystack", Arrays.asList("Software Engineer", "Director", "Hardware VP"),
                        "output", Arrays.asList("Software Engineer")
                )
        );

        for (Map<String, List<String>> test : tests) {
            List<String> actualOutput = findRedundantKeywords(test.get("needles"), test.get("haystack"));
            List<String> expectedOutput = test.get("output");
            if (actualOutput.containsAll(expectedOutput) && expectedOutput.containsAll(actualOutput)) {
                System.out.println("Tests success");
            } else {
                System.err.println("Test failed");
            }
        }
    }
}
