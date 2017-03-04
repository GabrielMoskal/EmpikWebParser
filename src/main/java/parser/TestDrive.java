package parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import parser.Pair;

public class TestDrive {
    public static void main(String[] args) throws IOException {

        String filePath = new File("").getAbsolutePath();
        //System.out.println(filePath);


        /*
        FileReader fReader = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fReader);
        String text = reader.readLine();
        while (text != null) {
            text = text + reader.readLine();
        }
            */

        //filePath = filePath + "\\src\\main\\java\\parser\\PageWithLinksToConcreteEbooks.html";// "\\PageWithLinksToConcreteEbooks.html";
        EmpikEbookParser parser = new EmpikEbookParser("http://www.empik.com/ebooki-i-mp3,350102,s?c=ebooki-i-mp3-ebooki-biografie-i-dokument");
        //File input = new File(filePath);
        //Document doc = Jsoup.parse(input, "UTF-8", "");
        //parser.setDocument(doc);

        List<String> urls = parser.parseLinksToConcreteBookUrls();

        for (String url : urls) {
            System.out.println(url);
            //parser.setDocumentFromUrl(url);
            /*
            String description = parser.parseBookDescription();
            System.out.println(description);
            count++;
            */
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
        String tempUrl = new String("http://www.empik.com/angielski-dla-uczniow-szkol-podstawowych-boguslawska-joanna,p1136241152,ebooki-i-mp3-p");
        EmpikEbookParser empikEbookParser = new EmpikEbookParser(url);
        String description = empikEbookParser.parseBookDescription();
        List<Pair<String, String>> bookInfo = empikEbookParser.parseBookInfo();
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

