package infrascructure.data.integration;

import com.google.common.base.Preconditions;
import infrascructure.data.dom.DocumentMetaData;
import infrascructure.data.util.IOHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
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

    private CloseableStream<Path> pathStream;
    private CloseableStream<String> titlesStream;

    public DirectoryDocumentMetaDataReader(String batchesDirectory, String titlesFileName) {
        this.batchesDirectory = batchesDirectory;
        this.titlesFileName = titlesFileName;
    }

    @Override
    public Iterator<DocumentMetaData> readDocumentMetaData() throws IOException {
        final String titlesPath = new File(batchesDirectory).getAbsolutePath() + File.separator + titlesFileName;
        titlesStream = IOHelper.readLinesFromFileLazy(titlesPath);
        Iterator<String> titlesIterator = titlesStream.iterator();
        pathStream = Files.walk(Paths.get(batchesDirectory), 1, FileVisitOption.FOLLOW_LINKS);

        return pathStream.filter(isDocFile(FILE_PATTERN)).map(
                (path) -> {
                    String pathStr = path.toAbsolutePath().toString();
                    String idStr = pathStr.substring(pathStr.lastIndexOf(File.separator) + 1, pathStr.lastIndexOf(".txt"));

                    Preconditions.checkArgument(titlesIterator.hasNext(), "Titles file " + titlesPath + " does not contain all titles");
                    String  title = titlesIterator.next();
                    return new DocumentMetaData(Integer.valueOf(idStr), title, pathStr, false);

                }).iterator();
    }

    protected Predicate<? super Path> isDocFile(String pattern) {
        return (path) -> !Files.isDirectory(path) && path.toAbsolutePath().toString().matches(pattern);
    }

    @Override
    public void close() throws Exception {
        close(pathStream);
        close(titlesStream);
    }

    private void close(AutoCloseable resource) throws Exception {
        if(resource != null){
            resource.close();
        }
    }
}
