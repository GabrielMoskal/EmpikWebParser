package parser;

import java.util.Map;

/**
 * Created by Gabriel on 14.03.2017.
 */
public interface EmpikParserInterface {
    void connect(String url);
    Map<String, String> parseTitlesToConreteSubcategoriesLinks();
    Map<String, String> parseTitlesToConcreteItemsUrls();
    Map<String, String> parseConcreteItemLabelsToDetailsInformation();
    String parseConcreteItemDescription();
    String parseConcreteItemImageUrl();
}
