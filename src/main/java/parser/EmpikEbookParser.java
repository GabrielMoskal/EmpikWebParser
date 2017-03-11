package parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmpikEbookParser {
    private Document document;

    public EmpikEbookParser() {
        this.document = null;
    }

    public EmpikEbookParser(String url) throws IOException {
        setDocumentFromUrl(url);
        preserveLineBreaks();
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setDocumentFromUrl(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        this.document = connection.get();
    }

    private void preserveLineBreaks() {
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        //String s = document.html().replaceAll("\\\\n", "\n");
        //return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    public List<Pair<String, String>> parseBookInfo() throws IOException {
        Element productMainInfo = document.getElementById("tabs");
        Pair<Elements, Elements> pairOfLabelsAndDetails = makePairOfLabelsAndDetails(productMainInfo);
        if (pairOfLabelsAndDetails == null) {
            return null;
        }
        List<Pair<String, String>> bookInfo = makeBookInfo(pairOfLabelsAndDetails);
        return bookInfo;
    }

    private Pair<Elements, Elements> makePairOfLabelsAndDetails(Element productMainInfo) {
        if (productMainInfo == null) {
            return null;
        }
        Elements labels = productMainInfo.getElementsByClass("productDetailsLabel");
        Elements details = productMainInfo.getElementsByClass("productDetailsValue");
        Pair<Elements, Elements> pairOfLabelsAndDetails = new Pair<Elements, Elements>();
        pairOfLabelsAndDetails.setObject1(labels);
        pairOfLabelsAndDetails.setObject2(details);
        return pairOfLabelsAndDetails;
    }

    private List<Pair<String, String>> makeBookInfo(Pair<Elements, Elements> pairOfLabelsAndDetails) {
        List<Pair<String, String>> bookInfo = new ArrayList<Pair<String, String>>();
        fillBookInfoWithProperLabelsAndDetails(bookInfo, pairOfLabelsAndDetails);
        return bookInfo;
    }

    private void fillBookInfoWithProperLabelsAndDetails(List<Pair<String, String>> bookInfo, Pair<Elements, Elements> pairOfLabelsAndDetails) {
        Elements labels = pairOfLabelsAndDetails.getObject1();
        Elements details = pairOfLabelsAndDetails.getObject2();
        int loopFinish = labels.size();
        for (int i = 0; i < loopFinish; i++) {
            Pair<String, String> labelAndDetail = getLabelAndDetail(labels, details, i);
            bookInfo.add(labelAndDetail);
        }
    }

    private Pair<String, String> getLabelAndDetail(Elements labels, Elements details, int index) {
        Pair<String, String> result = new Pair<String, String>();
        result.setObject1(labels.get(index).text());
        result.setObject2(details.get(index).text());
        return result;
    }

    public String parseBookDescription() {
        Element productMainInfo = document.getElementById("tabs");
        Elements description = productMainInfo.getElementsByClass("contentPacketText longDescription");
        String result = description.text().replaceAll("\\\\n", "\n");
        Jsoup.clean(result, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        return result;
    }

    public List<String> parseLinksToConcreteBookUrls() {
        String rootUrl = "http://www.empik.com";
        Elements labels = document.getElementsByClass("productBox-450Title");
        List<String> concreteUrls = new ArrayList<String>();
        for (Element element : labels) {
            Elements links = element.select("a[href]");
            for (Element link : links) {
                String child = rootUrl + link.attr("href");
                concreteUrls.add(child);
            }
        }
        return concreteUrls;
    }

    public List<String> parseLinksToConcreteCategories() {
        String rootUrl = "http://www.empik.com";
        Elements labels = document.getElementsByClass("menuCategories");
        List<String> concreteUrls = new ArrayList<String>();
        for (Element element : labels) {
            Elements links = element.select("a[href]");
            for (Element link : links) {
                String child = rootUrl + link.attr("href");
                concreteUrls.add(child);
            }
        }
        return concreteUrls;
    }
}
