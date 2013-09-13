package infrastructure;

import infrascructure.data.stripping.EnglishSuffixStripper;
import infrascructure.data.stripping.Stemmer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 8/29/13
 * Time: 8:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnglishSuffixStripperTest {
    private Stemmer stemmer;

    @BeforeClass
    private void setUp(){
        stemmer = new EnglishSuffixStripper();
    }

    @Test
    public void testNounsStemming() {
        Assert.assertEquals(stemmer.getCanonicalForm("caresses"), "caress");
        Assert.assertEquals(stemmer.getCanonicalForm("ponies"), "poni");
        Assert.assertEquals(stemmer.getCanonicalForm("ties"), "ti");
        Assert.assertEquals(stemmer.getCanonicalForm("caress"), "caress");
        Assert.assertEquals(stemmer.getCanonicalForm("cats"), "cat");
    }

    @Test
    public void testVerbStemming1b() {
        Assert.assertEquals(stemmer.getCanonicalForm("feed"), "feed");
        Assert.assertEquals(stemmer.getCanonicalForm("agreed"), "agree");
        Assert.assertEquals(stemmer.getCanonicalForm("plastered"), "plaster");
        Assert.assertEquals(stemmer.getCanonicalForm("motoring"), "motor");
        Assert.assertEquals(stemmer.getCanonicalForm("sing"), "sing");
        //Assert.assertEquals(stemmer.getCanonicalForm("expired"), "expire");


        Assert.assertEquals(stemmer.getCanonicalForm("conflated"), "conflate");
        Assert.assertEquals(stemmer.getCanonicalForm("troubled"), "trouble");
        Assert.assertEquals(stemmer.getCanonicalForm("sized"), "size");
        Assert.assertEquals(stemmer.getCanonicalForm("hopping"), "hop");
        Assert.assertEquals(stemmer.getCanonicalForm("tanned"), "tan");
        Assert.assertEquals(stemmer.getCanonicalForm("falling"), "fall");
        Assert.assertEquals(stemmer.getCanonicalForm("hissing"), "hiss");
        Assert.assertEquals(stemmer.getCanonicalForm("fizzed"), "fizz");
        Assert.assertEquals(stemmer.getCanonicalForm("failing"), "fail");
        Assert.assertEquals(stemmer.getCanonicalForm("filing"), "file");

    }

    //should be removed after isConsonant will private
    @Test
    public void consonantTest(){
        Assert.assertTrue(((EnglishSuffixStripper)stemmer).isConsonant("metallica", 0));
        Assert.assertTrue(((EnglishSuffixStripper)stemmer).isConsonant("metallica", 2));
        Assert.assertFalse(((EnglishSuffixStripper)stemmer).isConsonant("Mystery", 1));
        Assert.assertFalse(((EnglishSuffixStripper)stemmer).isConsonant("Mystery", 6));
        Assert.assertFalse(((EnglishSuffixStripper)stemmer).isConsonant("Hi", 1));
    }

    //should be removed after getMeasure will private
    @Test
    public void measureTest(){
        Assert.assertEquals(0, ((EnglishSuffixStripper)stemmer).getMeasure("TR"));
        Assert.assertEquals(0, ((EnglishSuffixStripper)stemmer).getMeasure("EE"));
        Assert.assertEquals(0, ((EnglishSuffixStripper)stemmer).getMeasure("TREE"));
        Assert.assertEquals(0, ((EnglishSuffixStripper)stemmer).getMeasure("Y"));
        Assert.assertEquals(0, ((EnglishSuffixStripper)stemmer).getMeasure("BY"));

        Assert.assertEquals(1, ((EnglishSuffixStripper)stemmer).getMeasure("TROUBLE"));
        Assert.assertEquals(1, ((EnglishSuffixStripper)stemmer).getMeasure("OATS"));
        Assert.assertEquals(1, ((EnglishSuffixStripper)stemmer).getMeasure("TREES"));

        Assert.assertEquals(2, ((EnglishSuffixStripper)stemmer).getMeasure("TROUBLES"));
        Assert.assertEquals(2, ((EnglishSuffixStripper)stemmer).getMeasure("PRIVATE"));
        Assert.assertEquals(2, ((EnglishSuffixStripper)stemmer).getMeasure("OATEN"));
        Assert.assertEquals(2, ((EnglishSuffixStripper)stemmer).getMeasure("ORRERY"));

    }

    @Test(dataProvider = "test1cDataProvider")
    public void test1c(String expected, String input){
        Assert.assertEquals(stemmer.getCanonicalForm(input), expected);
    }

    @Test(dataProvider = "testStep2DataProvider")
    public void testStep2(String expected, String input){
        Assert.assertEquals(stemmer.getCanonicalForm(input), expected);
    }

    @Test(dataProvider = "testStep3DataProvider")
    public void testStep3(String expected, String input){
        Assert.assertEquals(stemmer.getCanonicalForm(input), expected);
    }

    @Test(dataProvider = "testStep4DataProvider")
    public void testStep4(String expected, String input){
        Assert.assertEquals(stemmer.getCanonicalForm(input), expected);
    }

    @Test(dataProvider = "testStep5DataProvider")
    public void testStep5(String expected, String input){
        Assert.assertEquals(stemmer.getCanonicalForm(input), expected);
    }

    @DataProvider
    public Object[][] test1cDataProvider(){
        return new Object[][]{
                {"happi", "happy"},
                {"sky", "sky"}
        };
    }

    @DataProvider
    private Object[][] testStep3DataProvider(){
        return new Object[][]{
                {"triplic", "triplicate"},
                {"form", "formative"},
                {"formal", "formalize"},
                {"electric", "electriciti"},
                {"electric", "electrical"},
                {"good", "goodness"}
        };
    }

    @DataProvider
    public Object[][] testStep2DataProvider(){
        return new Object[][]{
                {"relate", "relational"},
                {"condition", "conditional"},
                //{"rational", "rational"},
                {"ration", "rational"},

                {"valence", "valenci"},
                {"hesitance", "hesitanci"},
                {"digitize", "digitizer"},
                {"conformable", "conformabli"},
                {"radical", "radicalli"},
                {"different", "differentli"},
                {"vile", "vileli"},
                {"vietnamize", "vietnamization"},
                {"predicate", "predication"},
                {"operate", "operator"},
                {"feudal", "feudalism"},
                {"decisive", "decisiveness"},
                {"hopeful", "hopefulness"},
                {"callous", "callousness"},
                {"formal", "formaliti"},
                {"sensitive", "sensitiviti"},
                {"sensible", "sensibiliti"},
        };
    }

    @DataProvider
    public Object[][] testStep4DataProvider(){
        return new Object[][]{
                {"reviv", "revival"},
                {"allow", "allowance"},
                {"infer", "inference"},
                {"airlin", "airliner"},
                {"gyroscop", "gyroscopic"},
                {"adjust", "adjustable"},
                {"defens", "defensible"},
                {"irrit", "irritant"},
                {"replac", "replacement"},
                {"adjust", "adjustment"},
                {"depend", "dependent"},
                {"adopt", "adoption"},
                {"homolog", "homologou"},
                {"commun", "communism"},
                {"activ", "activate"},
                {"angular", "angulariti"},
                {"homolog", "homologous"},
                {"effect", "effective"},
                {"bowdler", "bowdlerize"}
        };
    }

    @DataProvider
    public Object[][] testStep5DataProvider(){
        return new Object[][]{
                {"probat", "probate"},
                {"rate", "rate"},
                {"ceas", "cease"},
                {"control", "controll"},
                {"roll", "roll"}
        };
    }


}
