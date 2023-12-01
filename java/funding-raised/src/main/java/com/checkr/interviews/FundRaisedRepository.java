package com.checkr.interviews;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FundRaisedRepository {

    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        List<String[]> csvData = readFile();
        for(Map.Entry<String, String> entry : options.entrySet()) {
            switch (entry.getKey()) {
                case "company_name":
                    csvData = filterResult(csvData, 1, entry.getValue());
                    break ;
                case "city":
                    csvData = filterResult(csvData, 4, entry.getValue());
                    break ;
                case "state":
                    csvData = filterResult(csvData, 5, entry.getValue());
                    break ;
                case "round":
                    csvData = filterResult(csvData, 9, entry.getValue());
                    break ;
            }
        }
        List<Map<String, String>> output = new ArrayList<>();

        for (String[] csvDatum : csvData) {
            output.add(fillMap.apply(csvDatum, new HashMap<>()));
        }

        return output;
    }

    public static Map<String, String> findBy(Map<String, String> options) throws IOException {
        List<String[]> csvData = readFile();
        Map<String, String> mapped = new HashMap<> ();

        for (String[] csvDatum : csvData) {
            if ((options.containsKey("company_name") && csvDatum[1].equals(options.get("company_name"))) ||
                    (options.containsKey("city") && csvDatum[4].equals(options.get("city"))) ||
                    (options.containsKey("state") && csvDatum[5].equals(options.get("state"))) ||
                    (options.containsKey("round") && csvDatum[9].equals(options.get("round")))) {

                fillMap.apply(csvDatum, mapped);
            } else {
                throw new RuntimeException("No such entry");
            }
        }
        return mapped;
    }

    static BiFunction<String[], Map<String, String>, Map<String, String>> fillMap = (csvData, mapped) -> {
        mapped.put("permalink", csvData[0]);
        mapped.put("company_name", csvData[1]);
        mapped.put("number_employees", csvData[2]);
        mapped.put("category", csvData[3]);
        mapped.put("city", csvData[4]);
        mapped.put("state", csvData[5]);
        mapped.put("funded_date", csvData[6]);
        mapped.put("raised_amount", csvData[7]);
        mapped.put("raised_currency", csvData[8]);
        mapped.put("round", csvData[9]);
        return mapped;
    };

    private static List<String[]> filterResult(List<String[]> data, Integer column, String value) {
        return data.stream().filter(datum -> datum[column].equals(value)).collect(Collectors.toList());
    }

    private static List<String[]> readFile() {
        try {
            List<String[]> csvData = new ArrayList<>();
            CSVReader reader = new CSVReader(new FileReader("startup_funding.csv"));
            String[] row = null;

            while ((row = reader.readNext()) != null) {
                csvData.add(row);
            }

            reader.close();
            csvData.remove(0);
            return csvData;
        } catch (IOException | CsvValidationException e) {
            System.out.print(e.getMessage());
            System.out.print("error reading file");
        }
        return Collections.emptyList();
    }


}
