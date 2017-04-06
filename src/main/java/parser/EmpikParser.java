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

    public EmpikParser() {
        this.document = null;
    }

    public EmpikParser(String url) {
        connect(url);
    }

    public EmpikParser(Document document) {
        setDocument(document);
    }

    /**
     * Connects parser to concrete page, passed by argument
     * @param url to concrete page
     *
     * @throws RuntimeException if IOException occurs, can't recover with given auto-generated links
     */
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

    /**
     *
     * @return parsed links and description to concrete item categories or empty if not found
     */
    public Map<String, String> parseLinksToConcreteSubcategories() {
        Elements labels = document.getElementsByClass("categoryFacetList");
        return parseLinks(labels);
    }

    /**
     *
     * @param elements which
     * @return Map of Strings which contains parsed links or empty if not found
     */
    private Map<String, String> parseLinks(Elements elements) {
        Map<String, String> concreteUrls = new HashMap<>();
        for (Element element : elements) {
            Elements links = element.select("a[href]");
            Map<String, String> urls = parseUrlsAndLabelsFromElements(links);
            concreteUrls.putAll(urls);
        }
        return concreteUrls;
    }

    private Map<String, String> parseUrlsAndLabelsFromElements(Elements elements) {
        Map<String, String> hrefs = new HashMap<>();
        for (Element element : elements) {
            String url = EMPIK_ROOT_URL + element.attr("href");
            String description = element.text();
            hrefs.put(description, url);
        }
        return hrefs;
    }

    /**
     * Recursive function, which goes to next pages by checking, if there is link to the next item page
     * @return Map with links to all concrete items in selected category
     */
    public Map<String, String> parseLinksToConcreteItems() {
        Elements labels = document.getElementsByClass("title");
        Map<String, String> result = parseLinks(labels);
        Elements nextPageLinkElements = document.getElementsByClass("next");
        if (nextPageLinkElements.isEmpty() == false) {
            // key to next is empty, there is only one key
            String linkToNextPage = parseLinks(nextPageLinkElements).get("");
            connect(linkToNextPage);
            result.putAll(parseLinksToConcreteItems());
        }
        return result;
    }

    /**
     *
     * @return Map with strings which contains full description of item,
     * map keys are name of item being described on the page,
     * empty if no item found
     */
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

    /**
     *
     * @return String with description of concrete item from link which this is connected to
     */
    public String parseConcreteItemDescription() {
        Elements description = document.getElementsByClass("productDescriptionText");
        return preserveLineBreaks(description);
    }

    private String preserveLineBreaks(Elements elements) {
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("br").append("\\\\n");
        document.select("p").prepend("\\n\\n");
        String result = elements.text().replaceAll("\\\\n", "\n");
        return Jsoup.clean(result, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
}
