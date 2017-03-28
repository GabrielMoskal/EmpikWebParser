package parser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TestDrive {
    public static void main(String[] args) throws IOException {
        EmpikParser parser = new EmpikParser("http://m.empik.com/ebooki/kategorie");
        List<String> links = parser.parseLinksToConcreteSubcategories();
        for (String link : links) {
            parser.connect(link);
            List<String> concreteBookUrls = parser.parseLinksToConcreteItems();
            for (String concreteUrl : concreteBookUrls) {
                //System.out.println(concreteUrl);
                parser.connect(concreteUrl);
                String itemDescription = parser.parseConcreteItemDescription();
                Map<String, String> booksInfo = parser.parseConcreteItemInformation();

                Collection<String> values = booksInfo.keySet();
                for (String bookInfo : values) {
                    System.out.println(bookInfo);
                }
                //System.out.println(itemDescription);
            }
        }
    }
}

