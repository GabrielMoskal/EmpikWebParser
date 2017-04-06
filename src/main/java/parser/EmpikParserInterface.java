package parser;

import java.util.Map;

/**
 * Created by Gabriel on 14.03.2017.
 */
public interface EmpikParserInterface {
    void connect(String url);
    Map<String, String> parseLinksToConcreteSubcategories();
    Map<String, String> parseLinksToConcreteItems();
    Map<String, String> parseConcreteItemInformation();
    String parseConcreteItemDescription();
    String parseConcreteItemImageUrl();
}
