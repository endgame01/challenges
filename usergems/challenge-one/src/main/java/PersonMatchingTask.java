package com.usergems.challenge;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("SlowListContainsAll")
public class PersonMatchingTask {
    /* Given two arrays with personal data of people (name, email, company)
     * we want to find matches of people in the developers array within the candidates array.
     *
     * A match is defined as one of the following:
     *    - Exact match of the email addresses
     *    - The same combination of name and company (both values need to be defined)
     *
     * Assume that each array will contain millions of entries -> performance is important.
     * Hint 1: A nested loop with if statements will not be performant enough.
     */
    private List<MergeEntry> addCandidateIdsToDevelopers(List<Candidate> candidates, List<Developer> developers) {
        Map<String, Candidate> candidatesMap = createCandidatesMap(candidates);

        return developers.stream()
                .map(dev -> new MergeEntry(matchCandidate(candidatesMap, dev), dev))
                .filter(m -> m.candidate != null)
                .collect(Collectors.toList());
    }

    private Map<String, Candidate> createCandidatesMap(List<Candidate> candidates) {
        Map<String, Candidate> emailCandidateMap = candidates.stream()
                .collect(Collectors.toMap(c -> c.email, Function.identity()));
        Map<String, Candidate> nameCompanyCandidateMap = candidates.stream()
                .collect(Collectors.toMap(c -> createNameCompanyHash(c.name, c.company), Function.identity()));


        return Stream.concat(emailCandidateMap.entrySet().stream(), nameCompanyCandidateMap.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1));
    }

    private String createNameCompanyHash(String name, String email) {
        return String.valueOf(Objects.hash(name, email));
    }

    private Candidate matchCandidate(Map<String, Candidate> result, Developer dev) {
        if (result.containsKey(dev.email)) return result.get(dev.email);
        return result.get(createNameCompanyHash(dev.name, dev.company));
    }

    public static void main(String[] args) {
        new PersonMatchingTask().test();
    }

    private void test() {
        Candidate jonC = new Candidate(1, "jon", "jon@acme.com", "acme");
        Candidate janeC = new Candidate(2, "jane", "jane@google.com", "google");
        Candidate peterC = new Candidate(3, "peter", "peter@microsoft.com", "microsoft");
        List<Candidate> candidates = Arrays.asList(jonC, janeC, peterC);

        Developer jonD = new Developer("jonathan", "jon@acme.com", null);
        Developer janeD = new Developer("jane", "jane@gmail.com", "google");
        Developer peterD = new Developer("peter", "peter@netflix.com", "netflix");
        List<Developer> developers = Arrays.asList(jonD, janeD, peterD);

        List<MergeEntry> expectedOutput = Arrays.asList(
                new MergeEntry(jonC, jonD),
                new MergeEntry(janeC, janeD)
        );

        List<MergeEntry> actualOutput = addCandidateIdsToDevelopers(candidates, developers);

        if (actualOutput.containsAll(expectedOutput) && expectedOutput.containsAll(actualOutput)) {
            System.out.println("Tests success");
        } else {
            System.err.println("Test failed");
        }
    }

    private static class MergeEntry {
        Candidate candidate;
        Developer developer;

        public MergeEntry(Candidate candidate, Developer developer) {
            this.candidate = candidate;
            this.developer = developer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MergeEntry that = (MergeEntry) o;
            return Objects.equals(candidate, that.candidate) && Objects.equals(developer, that.developer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(candidate, developer);
        }
    }

    private static class Candidate {
        int id;
        String name;
        String email;
        String company;

        public Candidate(int id, String name, String email, String company) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.company = company;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Candidate candidate = (Candidate) o;
            return id == candidate.id && Objects.equals(name, candidate.name) && Objects.equals(email, candidate.email) && Objects.equals(company, candidate.company);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, email, company);
        }
    }

    private static class Developer {
        String name;
        String email;
        String company;

        public Developer(String name, String email, String company) {
            this.name = name;
            this.email = email;
            this.company = company;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Developer developer = (Developer) o;
            return Objects.equals(name, developer.name) && Objects.equals(email, developer.email) && Objects.equals(company, developer.company);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, email, company);
        }
    }
}
