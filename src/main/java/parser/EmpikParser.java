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
     * @return parsed href titles and links to concrete item categories or empty if not found
     */
    public Map<String, String> parseTitlesToConreteSubcategoriesLinks () {
        Elements labels = document.getElementsByClass("categoryFacetList");
        return parseTitlesToUrls(labels);
    }

    /**
     *
     * @param elements with content being searched for urls
     * @return parsed href titles and links found in elements or empty if not found
     */
    private Map<String, String> parseTitlesToUrls(Elements elements) {
        Map<String, String> titlesToUrls = new HashMap<>();
        for (Element element : elements) {
            /* next level of parsing, Element.select returns Elements */
            Elements links = element.select("a[href]");
            Map<String, String> urls = parseTitlesToUrlsFromElements(links);
            titlesToUrls.putAll(urls);
        }
        return titlesToUrls;
    }

    private Map<String, String> parseTitlesToUrlsFromElements(Elements elements) {
        Map<String, String> hrefs = new HashMap<>();
        for (Element element : elements) {
            String url = EMPIK_ROOT_URL + element.attr("href");
            String description = element.text();
            hrefs.put(description, url);
        }
        return hrefs;
    }

    /**
     * Recursive function, which goes to next pages if there is a link to the next item page
     * @return Map with links to all concrete items in selected category
     */
    public Map<String, String> parseTitlesToConcreteItemsUrls() {
        Elements labels = document.getElementsByClass("title");
        Map<String, String> result = parseTitlesToUrls(labels);
        Elements nextPageLink = document.getElementsByClass("next");
        if (!nextPageLink.isEmpty()) {
            // key to next is empty, there is only one key
            String linkToNextPage = parseTitlesToUrls(nextPageLink).get("");
            connect(linkToNextPage);
            result.putAll(parseTitlesToConcreteItemsUrls());
        }
        return result;
    }

    /**
     *
     * @return Map with strings which contains full description of item,
     * map keys are name of item being described on the page,
     * empty if no item found
     */
    public Map<String, String> parseConcreteItemLabelsToDetailsInformation() {
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
        Pair<Elements, Elements> labelsToDetails = new Pair<>();
        labelsToDetails.setObject1(labels);
        labelsToDetails.setObject2(details);
        return labelsToDetails;
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

    public String parseConcreteItemImageUrl() {
        Elements imageClasses = document.getElementsByClass("productGalleryImage oneImage");
        Element imageClass = imageClasses.first();
        if (imageClass == null) {
            return "";
        }
        Elements imageSource = imageClass.select("img[src]");
        return imageSource.attr("src");
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
