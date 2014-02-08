package infrascructure.data;


import infrascructure.data.email.html.DataSource;
import infrascructure.data.parse.NasaParse;


import java.io.IOException;

/**
 * Created by Roman on 08.02.14.
 */
public class TestNasaParse {
    public static void main(String[] args) throws IOException {
        DataSource dataSource
                = new DataSource("C:\\Users\\Roman\\svn_and_git\\Careers at NASA   NASA.htm");
        Resource resource = new Resource(dataSource.getDoc().html());

        NasaParse nasaParse = new NasaParse();
        PlainTextResource plainTextResource = nasaParse.parse(resource);


        System.out.println(plainTextResource.getText());
        System.out.println(plainTextResource.getTittle());

    }
}
