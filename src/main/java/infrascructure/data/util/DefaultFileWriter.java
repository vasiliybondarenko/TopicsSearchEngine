package infrascructure.data.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 11/23/13
 * Time: 10:37 PM
 * Project: IntelligentSearch
 */
public class DefaultFileWriter implements CloseableWriter{
    private PrintWriter writer;

    public DefaultFileWriter(String path) throws FileNotFoundException {
        this.writer = new PrintWriter(path);
    }

    @Override
    public void appendLine(String line) {
        writer.println(line);
    }

    @Override
    public void close() {
        writer.close();
    }
}