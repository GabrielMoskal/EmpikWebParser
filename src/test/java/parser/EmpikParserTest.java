package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmpikParserTest {

    @Test
    public void parseBookInfoTest() {
        List<Pair<String, String>> expectedResult = new ArrayList<Pair<String, String>>();
        expectedResult.add(new Pair<String, String>("Tytuł:", "W kręgach władzy. Tom 1. Wotum nieufności"));
        expectedResult.add(new Pair<String, String>("Autor:", "Mróz Remigiusz"));
        expectedResult.add(new Pair<String, String>("Wydawnictwo:", "Filia"));
        expectedResult.add(new Pair<String, String>("Rok wydania:", "2017"));
        expectedResult.add(new Pair<String, String>("Język wydania:", "polski"));
        expectedResult.add(new Pair<String, String>("Ilość stron:", "473"));
        expectedResult.add(new Pair<String, String>("Format:", "MOBI UWAGA! Ebook chroniony przez watermark. więcej ›"));
        expectedResult.add(new Pair<String, String>("Liczba urządzeń:", "bez ograniczeń"));
        expectedResult.add(new Pair<String, String>("Drukowanie:", "bez ograniczeń"));
        expectedResult.add(new Pair<String, String>("Kopiowanie:", "bez ograniczeń"));
        expectedResult.add(new Pair<String, String>("Indeks:", "20837181"));

        String url = "http://m.empik.com/w-kregach-wladzy-tom-1-wotum-nieufnosci-mroz-remigiusz,p1135880608,ebooki-i-mp3-p";
        List<Pair<String, String>> result = null;
        try {
            EmpikParser parser = new EmpikParser(url);
            result = parser.parseConcreteItemInformation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expectedResult, result);
    }

    @Test
    public void parseBookDescriptionTest() {
        String expectedResult = "\n\nPierwsza polska seria political fiction! \n\n" +
                "Marszałek sejmu, Daria Seyda, budzi się w pokoju hotelowym, nie pamiętając, jak się w nim znalazła ani co się z nią działo przez ostatnich dziesięć godzin. Jest przekonana, że stała się ofiarą manipulacji, ale nie wie, kto ani dlaczego może za nią stać. Tymczasem Patryk Hauer, wschodząca gwiazda prawicy, podczas prac komisji śledczej odkrywa polityczny spisek sięgający najistotniejszych osób w kręgach władzy. \n\n" +
                "Seyda i Hauer znajdują się po przeciwnych stronach sceny politycznej. Dzieli ich wszystko, ale połączy jedna sprawa…";

        String url = "http://m.empik.com/w-kregach-wladzy-tom-1-wotum-nieufnosci-mroz-remigiusz,p1135880608,ebooki-i-mp3-p";
        String result = null;
        try {
            EmpikParser parser = new EmpikParser(url);
            result = parser.parseConcreteItemDescription();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expectedResult, result);
    }

    @Test
    public void parseLinksToConcreteBookUrls() throws IOException {
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add("http://m.empik.com/inwigilacja-mroz-remigiusz,p1137827825,ebooki-i-mp3-p");
        expectedResult.add("http://m.empik.com/kasacja-mroz-remigiusz,p1106211525,ebooki-i-mp3-p");

        String filePath = new File("").getAbsolutePath();
        filePath = filePath + "\\src\\test\\java\\parser\\PageWithLinksToConcreteEbooks.html";
        File input = new File(filePath);

        EmpikParser parser = new EmpikParser();
        Document doc = Jsoup.parse(input, "UTF-8", "");
        parser.setDocument(doc);
        List<String> result = parser.parseLinksToConcreteItems();

        assertEquals(expectedResult, result);
    }
}
