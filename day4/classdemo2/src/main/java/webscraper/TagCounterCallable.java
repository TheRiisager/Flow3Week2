package webscraper;

import java.util.concurrent.Callable;

public class TagCounterCallable implements Callable<TagDTO> {

    String url;

    public TagCounterCallable(String url) {
        this.url = url;
    }

    @Override
    public TagDTO call() throws Exception {
        TagCounter tc = new TagCounter(url);
        tc.doWork();
        return new TagDTO(tc);
    }
}
