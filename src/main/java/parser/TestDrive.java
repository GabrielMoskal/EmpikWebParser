package parser;

import java.util.List;
import java.util.Map;

public class TestDrive {
    public static void main(String[] args) {
        Long test = null;
        EmpikParser parser = new EmpikParser("http://m.empik.com/ebooki/kategorie");
        Map<String, String> links = parser.parseLinksToConcreteSubcategories();
        int count = 0;
        for (String link : links.keySet()) {
            System.out.println(link);
            /*
            parser.connect(link);
            List<String> concreteBookUrls = parser.parseLinksToConcreteItems();
            for (String concreteUrl : concreteBookUrls) {
                //System.out.println(concreteUrl);
                parser.connect(concreteUrl);
                String itemDescription = parser.parseConcreteItemDescription();
                Map<String, String> booksInfo = parser.parseConcreteItemInformation();
                //System.out.println(booksInfo);
                System.out.println(link);
                //System.out.println(itemDescription);
            }
            */
        }
    }
}

