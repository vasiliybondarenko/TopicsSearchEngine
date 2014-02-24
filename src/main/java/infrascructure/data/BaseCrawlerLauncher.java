package infrascructure.data;

import infrascructure.data.parse.PlainDocsRepository;
import infrascructure.data.readers.ResourcesRepository;
import infrascructure.data.util.IOHelper;
import infrascructure.data.util.Trace;
import infrascructure.data.vocabulary.BaseVocabularyBuilder;
import infrascructure.data.vocabulary.Vocabulary;
import infrascructure.data.vocabulary.Word;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: shredinger
 * Date: 12/15/13
 * Time: 10:33 PM
 * Project: IntelligentSearch
 */
public abstract class BaseCrawlerLauncher implements CrawlTaskExecutor{
    @Autowired
    protected Config config;

    @Autowired
    private ResourcesRepository reader;

    @Autowired
    private PlainDocsRepository docsRepo;

    @Autowired
    private BaseVocabularyBuilder vocabularyBuilder;

    @Override
    public void start() throws Exception {
        process();
    }

    public void process(){
        try {
            doCrawling(reader);
            Future<Throwable> parseResult = doParsing(docsRepo);
            afterCrawlingCompleted(parseResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void afterCrawlingCompleted(Future<Throwable> parseResult) throws Exception;

    protected void doCrawling(final ResourcesRepository reader) {
        Executors.newCachedThreadPool().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Trace.trace("reading docs ..");
                    reader.readAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    protected Future<Throwable> doParsing(final PlainDocsRepository docsRepo) {
        return Executors.newCachedThreadPool().submit(new Callable<Throwable>() {

            @Override
            public Throwable call() {
                try {
                    Trace.trace("parsing ..");
                    docsRepo.readAll();
                    Trace.trace("parsed ..");
                } catch (IOException e) {
                    return e;
                }
                return null;

            }
        });
    }

    protected void buildVocabulary(Future<Throwable> parseResult) throws InterruptedException, ExecutionException, IOException {
        Boolean parse = Boolean.parseBoolean(config.getProperty(Config.PARSE_DOCS_NOW, "true"));
        if(parse){
            Throwable parseException = parseResult.get();
            if(parseException == null){
                Trace.trace("Waiting for building vocabulary...");
                Vocabulary vocabulary = vocabularyBuilder.buildVocabulary();
                Trace.trace("Building vocabulary - done");
                saveVocabulary(vocabulary);
            }else{
                Trace.trace("Parsing failed: ");
                parseException.printStackTrace();
            }

        }
    }

    protected void saveVocabulary(Vocabulary v) throws IOException {

        String vocabularyPath = config.getProperty(Config.VOCABULARY_PATH);
        String wordCountsPath = config.getProperty(Config.WORDCOUNTS_PATH);
        Set<String> words = v.getWords().keySet();
        List<Word> wordCounts = v.getWordCounts();

        Queue<Word> sortedWordCounts = new PriorityQueue<>();
        for (Word w : wordCounts) {
            sortedWordCounts.add(w);
        }
        IOHelper.writeLinesToFile(vocabularyPath, wordCounts.stream().map(
                (w) -> w.getStemmedWord() + ": " + w.getOriginalWords().stream().reduce((x, y) -> x + " " + y).get()).iterator());

//        IOHelper.writeLinesToFile(vocabularyPath, words);
        try (PrintWriter pw = new PrintWriter(wordCountsPath)) {
            while (!sortedWordCounts.isEmpty()) {
                Word w = sortedWordCounts.poll();
                String line = w.toString();
                pw.println(line);
            }
        }
    }
}
