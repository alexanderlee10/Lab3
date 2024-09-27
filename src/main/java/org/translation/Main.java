package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        while (true) {
            String country = promptForCountry(translator);
            String t = "quit";
            if (t.equals(country)) {
                break;
            }
            // TODO Task: Once you switch promptForCountry so that it returns the country
            String language = promptForLanguage(translator, countryCodeConverter.fromCountry(country));
            if (t.equals(language)) {
                break;
            }
            System.out.println(country + " in " + language + " is " + translator.translate(
                    countryCodeConverter.fromCountry(country),
                    languageCodeConverter.fromLanguage(language)));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (t.equals(textTyped)) {
                break;
            }
        }
    }

    private static String promptForCountry(Translator translator) {
        List<String> countryCodes = translator.getCountries();
        List<String> countryNames = new ArrayList<>();
        // We ThreeLetterToEnglishMap
        for (String code : countryCodes) {
            String countryName = translator.translate(code, "en");
            if (countryName != null) {
                countryNames.add(countryName);
            }
        }
        Collections.sort(countryNames);
        for (String country : countryNames) {
            System.out.println(country);
        }
        System.out.println("select a country from above:");
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    private static String promptForLanguage(Translator translator, String country) {
        List<String> languageCodes = translator.getCountryLanguages(country);
        List<String> languageNames = new ArrayList<>();
        LanguageCodeConverter languageConverter = new LanguageCodeConverter();
        for (String code : languageCodes) {
            languageNames.add(languageConverter.fromLanguageCode(code));
        }
        Collections.sort(languageNames);
        for (String language : languageNames) {
            System.out.println(language);
        }
        System.out.println("Select a language from above:");
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

}
