package parser;

import java.io.IOException;
import java.util.List;

public class TestDrive {
    public static void main(String[] args) throws IOException {
        EmpikParser parser = new EmpikParser("http://www.empik.com/elektronika");
        List<String> links = parser.parseLinksToConcreteSubcategories();
        for (String link : links) {
            parser.connect(link);
            List<String> concreteBookUrls = parser.parseLinksToConcreteItems();
            if (concreteBookUrls == null) {
                continue;
            }

            for (String concreteUrl : concreteBookUrls) {
                parser.connect(concreteUrl);
                List<Pair<String, String>> booksInfo = parser.parseConcreteItemInformation();
                if (booksInfo == null) {
                    continue;
                }
                for (Pair<String, String> bookInfo : booksInfo) {
                    System.out.println(bookInfo);
                }
            }
        }
    }
}

