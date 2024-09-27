package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    private Map<String, Map<String, String>> translations;
    private Map<String, String> threelettertoenglishmap;
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        try {
            String jsonString = Files.readString(
                    Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);
            translations = new HashMap<>();
            threelettertoenglishmap = new HashMap<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                String countryEn = countryObject.getString("en").toLowerCase();
                String alpha3 = countryObject.getString("alpha3").toLowerCase();
                Map<String, String> countryTranslations = new HashMap<>();
                for (String key : countryObject.keySet()) {
                    if (!"id".equals(key) && !"alpha2".equals(key) && !"alpha3".equals(key)) {
                        countryTranslations.put(key, countryObject.getString(key));
                    }
                }
                // countryTranslations map to translations English name as key
                translations.put(countryEn, countryTranslations);
                // alpha3 code to English name mapping for later lookups
                threelettertoenglishmap.put(alpha3, countryEn);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String countryCode) {
        String country = threelettertoenglishmap.get(countryCode);
        if (country != null && translations.containsKey(country)) {
            return new ArrayList<>(translations.get(country).keySet());
        }
        System.out.println(country);
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(threelettertoenglishmap.keySet());
    }

    @Override
    public String translate(String countryCode, String language) {
        String country = threelettertoenglishmap.get(countryCode.toLowerCase());
        if (country != null && translations.containsKey(country)) {
            return translations.get(country).get(language);
        }
        return null;
    }
}
