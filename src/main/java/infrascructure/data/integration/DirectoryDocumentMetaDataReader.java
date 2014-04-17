package infrascructure.data.integration;

import com.google.common.base.Preconditions;
import infrascructure.data.dao.ResourceMetaDataRepository;
import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.dom.ResourceMetaData;
import infrascructure.data.dom.Tag;
import infrascructure.data.dom.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    @Autowired
    @Qualifier(value = "plainDocMetaDataRepository")
    private ResourceMetaDataRepository repository;

    private Stream<Path> pathStream;

    public DirectoryDocumentMetaDataReader(String batchesDirectory) {
        this.batchesDirectory = batchesDirectory;
    }

    @Override
    public Iterator<DocumentMetaData> readDocumentMetaData() throws IOException {

        pathStream = Files.walk(Paths.get(batchesDirectory), 1, FileVisitOption.FOLLOW_LINKS);

        return pathStream.filter(isDocFile(FILE_PATTERN)).map(
                (path) -> {
                    String pathStr = path.toAbsolutePath().toString();
                    String idStr = pathStr.substring(pathStr.lastIndexOf(File.separator) + 1, pathStr.lastIndexOf(".txt"));
                    Integer id = Integer.valueOf(idStr);

                    ResourceMetaData resourceMetaData = repository.findById(id);
                    Tag titleTag = resourceMetaData.getTag(Tags.TITLE);
                    Preconditions.checkArgument(titleTag != null, "Title cannot be null");
                    String title = titleTag.getValue();

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
