package parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class EmpikParser implements EmpikParserInterface {
    private static final String EMPIK_ROOT_URL = "http://m.empik.com";
    private Document document;

    public EmpikParser(String url) {
        connect(url);
    }

    public EmpikParser(Document document) {
        setDocument(document);
    }

    public void connect(String url) {
        Connection connection = Jsoup.connect(url);
        try {
            this.document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<String> parseLinksToConcreteSubcategories() {
        Elements labels = document.getElementsByClass("categoryFacetList");
        return parseLinks(labels);
    }

    private List<String> parseLinks(Elements elements) {
        List<String> concreteUrls = new ArrayList<>();
        for (Element element : elements) {
            Elements links = element.select("a[href]");
            List<String> urls = parseUrlsFromElements(links);
            concreteUrls.addAll(urls);
        }
        return concreteUrls;
    }

    private List<String> parseUrlsFromElements(Elements elements) {
        List<String> urls = new ArrayList<>();
        for (Element element : elements) {
            String child = EMPIK_ROOT_URL + element.attr("href");
            urls.add(child);
        }
        return urls;
    }

    public List<String> parseLinksToConcreteItems() {
        Elements labels = document.getElementsByClass("title");
        return parseLinks(labels);
    }

    public Map<String, String> parseConcreteItemInformation() {
        Element productMainInfo = document.getElementById("layoutContent");
        if (productMainInfo == null) {
            return Collections.emptyMap();
        }
        Pair<Elements, Elements> labelsAndDetails = makePairOfLabelsAndDetails(productMainInfo);
        return makeBookInfo(labelsAndDetails);
    }

    private Pair<Elements, Elements> makePairOfLabelsAndDetails(Element productMainInfo) {
        Elements labels = productMainInfo.getElementsByClass("productDetailsLabel");
        Elements details = productMainInfo.getElementsByClass("productDetailsValue");
        Pair<Elements, Elements> labelsAndDetails = new Pair<>();
        labelsAndDetails.setObject1(labels);
        labelsAndDetails.setObject2(details);
        return labelsAndDetails;
    }

    private Map<String, String> makeBookInfo(Pair<Elements, Elements> pairOfLabelsAndDetails) {
        Map<String, String> bookInfo = new HashMap<>();
        fillBookInfoWithProperLabelsAndDetails(bookInfo, pairOfLabelsAndDetails);
        return bookInfo;
    }

    private void fillBookInfoWithProperLabelsAndDetails(Map<String, String> itemInfo, Pair<Elements, Elements> pairOfLabelsAndDetails) {
        Elements labels = pairOfLabelsAndDetails.getObject1();
        Elements details = pairOfLabelsAndDetails.getObject2();
        int loopFinish = labels.size();
        for (int i = 0; i < loopFinish; i++) {
            Pair<String, String> labelAndDetail = getLabelAndDetail(labels, details, i);
            itemInfo.put(labelAndDetail.getObject1(), labelAndDetail.getObject2());
        }
    }

    private Pair<String, String> getLabelAndDetail(Elements labels, Elements details, int index) {
        Pair<String, String> result = new Pair<>();
        result.setObject1(labels.get(index).text());
        result.setObject2(details.get(index).text());
        return result;
    }

    public String parseConcreteItemDescription() {
        Elements description = document.getElementsByClass("productDescriptionText");
        return preserveLineBreaks(description);
    }

    private String preserveLineBreaks(Elements elements) {
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        String result = elements.text().replaceAll("\\\\n", "\n");
        return Jsoup.clean(result, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
}
