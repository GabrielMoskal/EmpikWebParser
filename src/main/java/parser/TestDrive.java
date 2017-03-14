package parser;

import java.io.IOException;
import java.util.List;

public class TestDrive {
    public static void main(String[] args) throws IOException {

        EmpikParser parser = new EmpikParser("http://www.empik.com/elektronika");

        List<String> links = parser.parseLinksToConcreteSubcategories();
        for (String link : links) {
            //System.out.println(link);
            parser.connectToGivenUrl(link);
            List<String> concreteBookUrls = parser.parseLinksToConcreteItems();
            if (concreteBookUrls == null) {
                continue;
            }

            for (String concreteUrl : concreteBookUrls) {
                parser.connectToGivenUrl(concreteUrl);
                List<Pair<String, String>> booksInfo = parser.parseConcreteItemInformation();
                if (booksInfo == null) {
                    continue;
                }
                for (Pair<String, String> bookInfo : booksInfo) {
                    System.out.println(bookInfo);
                }
            }

        }

        /*
        EmpikParser parser = new EmpikParser("http://www.empik.com/ebooki");
        List<String> links = parser.parseLinksToConcreteCategories();
        int count = 0;
        for (String link : links) {
            parser.setDocumentFromUrl(link);
            List<String> concreteBookUrls = parser.parseLinksToConcreteBookUrls();
            for (String concreteBookUrl : concreteBookUrls) {
                parser.setDocumentFromUrl(concreteBookUrl);
                List<Pair<String, String>> booksInfo = parser.parseBookInfo();
                if (booksInfo == null) {
                    continue;
                }
                count++;
                System.out.println(count);
                //for (Pair<String, String> bookInfo : booksInfo) {
                    //System.out.println(bookInfo);
                //}
            }
            //String desc = parser.parseBookDescription();


            //System.out.println(link);
        }

        /*
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        Elements labels = document.getElementsByClass("productBox-450Title");

        int count = 0;

        for (Element element : labels) {
            Elements links = element.select("a[href]");
            for (Element link : links) {
                //System.out.println(link.attr("abs:href"));
                String child = link.attr("abs:href");
                TestDrive.parseConcreteEbook(child);
                count++;
            }
        }
        System.out.println(count);

        //Elements content = body.getElementsByClass("contentPacketText longDescription");

        //Elements description = content.select("div[class=layoutContent]");

        /*
        for (Element title : content) {
            System.out.println(title);
        }
        /*
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
        */
    }
    public static void parseConcreteEbook(String url) throws IOException {
        String tempUrl = "http://www.empik.com/angielski-dla-uczniow-szkol-podstawowych-boguslawska-joanna,p1136241152,ebooki-i-mp3-p";
        EmpikParser empikParser = new EmpikParser(url);
        String description = empikParser.parseBookDescription();
        List<Pair<String, String>> bookInfo = empikParser.parseConcreteItemInformation();
        System.out.println(description);
        for (Pair<String, String> pair : bookInfo) {
            System.out.println(pair);
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}

