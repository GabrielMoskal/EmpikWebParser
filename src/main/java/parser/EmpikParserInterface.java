package parser;

import java.util.List;
import java.util.Map;

/**
 * Created by Gabriel on 14.03.2017.
 */
public interface EmpikParserInterface {
    void connect(String url);
    List<String> parseLinksToConcreteSubcategories();
    List<String> parseLinksToConcreteItems();
    Map<String, String> parseConcreteItemInformation();
    String parseConcreteItemDescription();
}
