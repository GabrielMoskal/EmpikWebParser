package parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class EmpikParser implements EmpikParserInterface {
    private static final String EMPIK_ROOT_URL = "http://www.empik.com";
    private Document document;

    public EmpikParser() {
        this.document = null;
    }

    public EmpikParser(String url) throws IOException {
        connectToGivenUrl(url);
    }

    public void connectToGivenUrl(String url) throws IOException {
        if (!url.startsWith(EMPIK_ROOT_URL)) {
            throw new MalformedURLException("setDocumentFromUrl(..) didn't get proper URL");
        }
        Connection connection = Jsoup.connect(url);
        this.document = connection.get();
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<String> parseLinksToConcreteSubcategories() {
        Elements labels = document.getElementsByClass("menuCategories");
        List<String> concreteUrls = parseTagsFromElements(labels);
        return concreteUrls;
    }

    private List<String> parseTagsFromElements(Elements elements) {
        if (elements == null) {
            return null;
        }
        List<String> concreteUrls = new ArrayList<String>();
        for (Element element : elements) {
            Elements links = element.select("a[href]");
            List<String> urls = parseUrlsFromElements(links);
            concreteUrls.addAll(urls);
        }
        return concreteUrls;
    }

    private List<String> parseUrlsFromElements(Elements elements) {
        if (elements == null) {
            return null;
        }
        List<String> urls = new ArrayList<String>();
        for (Element element : elements) {
            String child = EMPIK_ROOT_URL + element.attr("href");
            urls.add(child);
        }
        return urls;
    }

    public List<String> parseLinksToConcreteItems() {
        Elements labels = document.getElementsByClass("productBox-450Title");
        List<String> concreteUrls = parseTagsFromElements(labels);
        return concreteUrls;
    }

    // to test and propably it should be public
    public void preserveLineBreaks() {
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        //String s = document.html().replaceAll("\\\\n", "\n");
        //return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    public List<Pair<String, String>> parseConcreteItemInformation() {
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

}