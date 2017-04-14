package parser;

import java.util.Map;

public class TestDrive {
    public static void main(String[] args) {

        EmpikParser parser = new EmpikParser("http://m.empik.com/nowosci/ebooki");

        Map<String, String> concreteBookUrls = parser.parseTitlesToConcreteItemsUrls();
        for (String concreteUrl : concreteBookUrls.values()) {
            parser.connect(concreteUrl);
            Map<String, String> labelsToDetails = parser.parseConcreteItemLabelsToDetailsInformation();
            for (String label : labelsToDetails.keySet()) {
                String index = labelsToDetails.get("Indeks:");
                System.out.println(index);
            }

            //System.out.println(concreteUrl);
        }
    }
}

