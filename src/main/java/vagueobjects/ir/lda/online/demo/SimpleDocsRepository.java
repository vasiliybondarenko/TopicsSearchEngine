package vagueobjects.ir.lda.online.demo;

import infrascructure.data.launch.DefaultDirectoryReader;
import infrascructure.data.launch.DirectoryReader;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import vagueobjects.ir.lda.online.Config;
import vagueobjects.ir.lda.tokens.Document;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 8/26/13
 * Time: 12:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDocsRepository implements AutoCloseable{

    private FilesQueue files;
    private String[] tittles;
    private BufferedReader titlesReader;

    /**
     *
     */
    public SimpleDocsRepository() {
        init();
    }

    
    public List<Document> getBatchDocs(int batchSize) throws IOException {

        int count = 0;
        try {
            ArrayList<Document> batch = new ArrayList<>(batchSize);
            while (count++ < batchSize) {
                if (!files.hasNext()) {
                    return null;
                }
                String path = files.getNextEntry();
                String data = IOHelper.readFromFile(path);
                String idStr = path.substring(path.lastIndexOf(IOHelper.FILE_SEPARATOR) + 1);
                idStr = idStr.substring(0, idStr.lastIndexOf("."));
                int id = Integer.parseInt(idStr);
                String title = titlesReader.readLine();
                Document doc = new Document(id, title, data);                
                batch.add(doc);
            }
            return batch;
        } finally {
            files.flush();
        }

    }

    public List<String> getCurrentVocabulary() throws IOException {
        long startTime = System.nanoTime();
        String path = Config.getProperty("vocabulary_path");
        List<String> result = IOHelper.readLinesFromFile(path);
        long diff = System.nanoTime() - startTime;
        Trace.trace("[getCurrentVocabulary]: " + diff);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
	titlesReader.close();
	
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
            
            titlesReader = new BufferedReader(new FileReader(path));
            
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
                List<String> files = IOHelper.readLinesFromFile(path);
                this.files.addAll(files);
            }
        }

        private void store() throws IOException {
            PrintWriter writer = new PrintWriter(path);
            try {
                for (String entry : files) {
                    writer.println(entry);
                }
            } finally {
        	writer.close();
	    }
        }

        public void flush() throws IOException {
            store();
        }

    }

    

}
