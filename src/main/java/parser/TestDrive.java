package parser;

import java.io.IOException;
import java.util.List;

public class TestDrive {
    public static void main(String[] args) throws IOException {
        EmpikParser parser = new EmpikParser("http://m.empik.com/ebooki/kategorie");
        List<String> links = parser.parseLinksToConcreteSubcategories();
        for (String link : links) {
            parser.connect(link);
            List<String> concreteBookUrls = parser.parseLinksToConcreteItems();
            for (String concreteUrl : concreteBookUrls) {
                System.out.println(concreteUrl);
                parser.connect(concreteUrl);
                String itemDescription = parser.parseConcreteItemDescription();
                List<Pair<String, String>> booksInfo = parser.parseConcreteItemInformation();
                if (booksInfo == null) {
                    continue;
                }
                for (Pair<String, String> bookInfo : booksInfo) {
                    System.out.println(bookInfo);
                }
                System.out.println(itemDescription);
            }
        }
    }
}

