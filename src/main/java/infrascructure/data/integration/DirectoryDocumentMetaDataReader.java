package infrascructure.data.integration;

import infrascructure.data.dao.ResourceMetaDataRepository;
import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.dom.Tags;
import infrascructure.data.serialize.FileResourceSerializer;
import infrascructure.data.util.IOHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.CloseableStream;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 1/18/14
 * Time: 1:46 PM
 * Project: IntelligentSearch
 */
public class DirectoryDocumentMetaDataReader implements DocumentMetaDataReader, AutoCloseable {

    final String FILE_PATTERN = ".*[\\d+]+\\.txt";

    private final String batchesDirectory;
    private final String titlesFileName;

    @Autowired
    @Qualifier(value = "plainDocMetaDataRepository")
    private ResourceMetaDataRepository repository;

    private CloseableStream<Path> pathStream;

    public DirectoryDocumentMetaDataReader(String batchesDirectory, String titlesFileName) {
        this.batchesDirectory = batchesDirectory;
        this.titlesFileName = titlesFileName;
    }

    @Override
    public Iterator<DocumentMetaData> readDocumentMetaData() throws IOException {
        final String titlesPath = new File(batchesDirectory).getAbsolutePath() + File.separator + titlesFileName;

        List<String> titlesLines = IOHelper.readLinesFromFile(titlesPath);
        Map<Integer,String> tittles = FileResourceSerializer.parseTittles(titlesLines);

        pathStream = Files.walk(Paths.get(batchesDirectory), 1, FileVisitOption.FOLLOW_LINKS);

        return pathStream.filter(isDocFile(FILE_PATTERN)).map(
                (path) -> {
                    String pathStr = path.toAbsolutePath().toString();
                    String idStr = pathStr.substring(pathStr.lastIndexOf(File.separator) + 1, pathStr.lastIndexOf(".txt"));
                    Integer id = Integer.valueOf(idStr);

                    ResourceMetaData resourceMetaData = repository.findById(id);
                    String title = resourceMetaData.getTag(Tags.TITLE).getValue();

                    return new DocumentMetaData(id, title, pathStr, false);

                }).sorted((d1, d2) -> (int) (new File(d1.getFilePath()).length() - new File(d2.getFilePath()).length()))
                .iterator();
    }

    protected Predicate<? super Path> isDocFile(String pattern) {
        return (path) -> !Files.isDirectory(path) && path.toAbsolutePath().toString().matches(pattern);
    }

    @Override
    public void close() throws Exception {
        close(pathStream);
    }

    private void close(AutoCloseable resource) throws Exception {
        if(resource != null){
            resource.close();
        }
    }
}
