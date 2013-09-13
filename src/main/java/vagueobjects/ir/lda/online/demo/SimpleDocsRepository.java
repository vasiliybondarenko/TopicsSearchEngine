package vagueobjects.ir.lda.online.demo;

import infrascructure.data.launch.DefaultDirectoryReader;
import infrascructure.data.launch.DirectoryReader;
import infrascructure.data.launch.DocsRepository;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import vagueobjects.ir.lda.online.Config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 8/26/13
 * Time: 12:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDocsRepository extends DocsRepository {

    private FilesQueue files;
    private String[] tittles;

    /**
     *
     */
    public SimpleDocsRepository() {
        init();
    }

    /* (non-Javadoc)
     * @see vagueobjects.ir.lda.online.DocsRepository#getBatchDocs()
     */
    @Override
    public List<String> getBatchDocs(int batchSize) throws IOException {

        int count = 0;
        try {
            ArrayList<String> batch = new ArrayList<>(batchSize);
            while (count++ < batchSize) {
                if (!files.hasNext()) {
                    return null;
                }
                String path = files.getNextEntry();
                String data = IOHelper.readFromoFile(path);
                batch.add(data);
            }
            return batch;
        } finally {
            files.flush();
        }

    }

    /* (non-Javadoc)
     * @see vagueobjects.ir.lda.online.DocsRepository#getCurrentVocabulary()
     */
    @Override
    public List<String> getCurrentVocabulary() throws IOException {
        long startTime = System.nanoTime();
        String path = Config.getProperty("vocabulary_path");
        List<String> result = IOHelper.readLinesFromoFile(path);
        long diff = System.nanoTime() - startTime;
        Trace.trace("[getCurrentVocabulary]: " + diff);
        return result;
    }

    public void init() {

        try {
            String path = Config.getProperty("queue_docs");
            files = FilesQueue.createFilesQueue(null, path);
            if (!files.hasNext()) {
                DirectoryReader directoryReader = new DefaultDirectoryReader(Config.getProperty("batches_dir"));
                List<String> allFiles = directoryReader.getFiles();
                Trace.trace("Files: " + allFiles.size());
                files = FilesQueue.createFilesQueue(allFiles, path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class FilesQueue {
        private Queue<String> files;
        private String path;

        public static FilesQueue createFilesQueue(List<String> files, String path) throws IOException {
            return new FilesQueue(files, path);
        }

        public String getNextEntry() {
            return files.poll();
        }

        public boolean hasNext() {
            return !files.isEmpty();
        }

        /**
         * @throws IOException
         */
        protected FilesQueue(Collection<String> files, String path) throws IOException {
            this.path = path;
            this.files = new LinkedList<>();
            restore();
            if (files != null) {
                this.files.addAll(files);
            }
        }

        private void restore() throws IOException {
            File f = new File(path);
            if (f.exists()) {
                List<String> files = IOHelper.readLinesFromoFile(path);
                this.files.addAll(files);
            }
        }

        private void store() throws IOException {
            try (PrintWriter writer = new PrintWriter(path)) {
                for (String entry : files) {
                    writer.println(entry);
                }
            }
        }

        public void flush() throws IOException {
            store();
        }

    }

}
