package infrastructure.data.integration;

import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.integration.DirectoryDocumentMetaDataReader;
import infrascructure.data.util.IOHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.Iterator;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/18/14
 * Time: 1:59 PM
 * Project: IntelligentSearch
 */
public class DirectoryDocumentMetaDataReaderTest {

    private final String batchesDirectory = "src/main/resources/batches";
    private final String absoluteBatchesDirectoryPath = new File(batchesDirectory).getAbsolutePath();
    private final String titlesPath = "titles.txt";
    private final String titlesPathOfInvalidTitles = "wrong_titles.txt";

    private final String fullTitlePath = absoluteBatchesDirectoryPath + File.separatorChar + titlesPath;
    private final String fullTitlesPathOfWrongTitles = absoluteBatchesDirectoryPath + File.separatorChar + titlesPathOfInvalidTitles;
    private final String fullDoc1Path = absoluteBatchesDirectoryPath + File.separatorChar + "1.txt";
    private final String fullDoc2Path = absoluteBatchesDirectoryPath + File.separatorChar + "2.txt";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        File batchesDir = new File(batchesDirectory);
        batchesDir.mkdir();
        IOHelper.saveToFile(fullTitlePath, "Load\nReload\n");
        IOHelper.saveToFile(fullTitlesPathOfWrongTitles, "T1");
        IOHelper.saveToFile(fullDoc1Path, "A1");
        IOHelper.saveToFile(fullDoc2Path, "A2");
    }


    @After
    public void removeTestFiles() throws Exception {
        new File(fullTitlePath).delete();
        new File(fullTitlesPathOfWrongTitles).delete();
        new File(fullDoc1Path).delete();
        new File(fullDoc2Path).delete();
        new File(batchesDirectory).delete();
    }

    @Test
    public void readerShouldReadAllSpecifiedDocs() throws Exception {
        DirectoryDocumentMetaDataReader reader = new DirectoryDocumentMetaDataReader(batchesDirectory, titlesPath);

        assertThat(reader.readDocumentMetaData()).hasSize(2);
    }

    @Test
    public void readerShouldReadDocsWithTitles() throws Exception {
        DirectoryDocumentMetaDataReader reader = new DirectoryDocumentMetaDataReader(batchesDirectory, titlesPath);

        assertThat(reader.readDocumentMetaData()).containsOnly(
                new DocumentMetaData(1, "Load", fullDoc1Path, false),
                new DocumentMetaData(2, "Reload", fullDoc2Path, false)
        );
    }

    @Test
    public void readerShouldThrowExceptionIfTitlesFileIsWrong() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Titles file " + fullTitlesPathOfWrongTitles + " does not contain all titles");

        DirectoryDocumentMetaDataReader reader = new DirectoryDocumentMetaDataReader(batchesDirectory, titlesPathOfInvalidTitles);

        Iterator<DocumentMetaData> docsIterator = reader.readDocumentMetaData();
        docsIterator.next();
        docsIterator.next();
    }
}
