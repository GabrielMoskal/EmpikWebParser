package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EmpikParserTest {

    @Test
    public void parseBookInfoTest() {
        Map<String,String> expectedResult = new HashMap<>();
        expectedResult.put("Tytuł:", "W kręgach władzy. Tom 1. Wotum nieufności");
        expectedResult.put("Autor:", "Mróz Remigiusz");
        expectedResult.put("Wydawnictwo:", "Filia");
        expectedResult.put("Rok wydania:", "2017");
        expectedResult.put("Język wydania:", "polski");
        expectedResult.put("Ilość stron:", "473");
        expectedResult.put("Format:", "MOBI UWAGA! Ebook chroniony przez watermark. więcej ›");
        expectedResult.put("Liczba urządzeń:", "bez ograniczeń");
        expectedResult.put("Drukowanie:", "bez ograniczeń");
        expectedResult.put("Kopiowanie:", "bez ograniczeń");
        expectedResult.put("Indeks:", "20837181");

        String url = "http://m.empik.com/w-kregach-wladzy-tom-1-wotum-nieufnosci-mroz-remigiusz,p1135880608,ebooki-i-mp3-p";
        EmpikParser parser = new EmpikParser(url);
        Map<String, String> result = parser.parseConcreteItemLabelsToDetailsInformation();

        assertEquals(expectedResult, result);
    }

    @Test
    public void parseBookDescriptionTest() {
        String expectedResult = "\n\nPierwsza polska seria political fiction! \n\n" +
                "Marszałek sejmu, Daria Seyda, budzi się w pokoju hotelowym, nie pamiętając, jak się w nim znalazła ani co się z nią działo przez ostatnich dziesięć godzin. Jest przekonana, że stała się ofiarą manipulacji, ale nie wie, kto ani dlaczego może za nią stać. Tymczasem Patryk Hauer, wschodząca gwiazda prawicy, podczas prac komisji śledczej odkrywa polityczny spisek sięgający najistotniejszych osób w kręgach władzy. \n\n" +
                "Seyda i Hauer znajdują się po przeciwnych stronach sceny politycznej. Dzieli ich wszystko, ale połączy jedna sprawa…";

        String url = "http://m.empik.com/w-kregach-wladzy-tom-1-wotum-nieufnosci-mroz-remigiusz,p1135880608,ebooki-i-mp3-p";
        EmpikParser parser = new EmpikParser(url);
        String result = parser.parseConcreteItemDescription();

        assertEquals(expectedResult, result);
    }

    @Test
    public void parseLinksToConcreteBookUrls() throws IOException {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("http://m.empik.com/inwigilacja-mroz-remigiusz,p1137827825,ebooki-i-mp3-p");
        expectedResult.add("http://m.empik.com/kasacja-mroz-remigiusz,p1106211525,ebooki-i-mp3-p");

        String filePath = new File("").getAbsolutePath();
        filePath = filePath + "\\src\\test\\java\\parser\\PageWithLinksToConcreteEbooks.html";
        File input = new File(filePath);

        Document doc = Jsoup.parse(input, "UTF-8", "");
        EmpikParser parser = new EmpikParser(doc);
        Map<String, String> result = parser.parseTitlesToConcreteItemsUrls();

        assertArrayEquals(expectedResult.toArray(), result.values().toArray());
    }
}
