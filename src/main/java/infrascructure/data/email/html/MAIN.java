package infrascructure.data.email.html;

import java.io.IOException;

/**
 * Created by Roman on 08.02.14.
 */
public class MAIN
{
    public static void main(String arg[]) throws IOException {
        String source = new DateSource("/Users/shredinger/Documents/DEVELOPMENT/Projects/SHARED/IntelligentSearch/src/main/resources/nasa_emails/'Bathurst Inlet' Rock on Curiosity's Sol 54, Context View4801.txt").getSource();
        NasaEmailParser parseHtmlPage = new NasaEmailParser();
        System.out.print(parseHtmlPage.parse(source));
    }
}
