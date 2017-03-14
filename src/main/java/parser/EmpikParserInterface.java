package parser;

import java.util.List;

/**
 * Created by Gabriel on 14.03.2017.
 */
public interface EmpikParserInterface {
    //List<String> parseLinksToConcreteCategories();
    List<String> parseLinksToConcreteSubcategories();
    List<String> parseLinksToConcreteItems();
    List<Pair<String, String>> parseConcreteItemInformation();
    //String parseConcreteItemDescription();
}
