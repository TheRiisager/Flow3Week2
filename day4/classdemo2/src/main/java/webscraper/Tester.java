package webscraper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import javax.swing.text.html.HTML;

public class Tester {

    public static List<TagCounter> runSequental() {
        List<TagCounter> urls = new ArrayList();
        urls.add(new TagCounter("https://www.fck.dk"));
        urls.add(new TagCounter("https://www.google.com"));
        urls.add(new TagCounter("https://politiken.dk"));
        urls.add(new TagCounter("https://cphbusiness.dk"));
        for (TagCounter tc : urls) {
            tc.doWork();
        }
        return urls;
    }

    public static List<TagDTO> runParrallel()  throws Exception {
        List<TagCounterCallable> urls = new ArrayList();
        List<Future<TagDTO>> futureList = new ArrayList<>();
        List<TagDTO> result = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();

        urls.add(new TagCounterCallable("https://www.fck.dk"));
        urls.add(new TagCounterCallable("https://www.google.com"));
        urls.add(new TagCounterCallable("https://politiken.dk"));
        urls.add(new TagCounterCallable("https://cphbusiness.dk"));

        for(TagCounterCallable tcc : urls) {
            Future<TagDTO> future = executor.submit(tcc);
            futureList.add(future);
        }

        for (Future<TagDTO> future : futureList) {
            result.add(future.get());
        }


        return result;
    }

    public static void main(String[] args) throws Exception {
        long timeSequental;
        long start = System.nanoTime();

        List<TagCounter> fetchedData = new Tester().runSequental();
        long end = System.nanoTime();
        timeSequental = end - start;
        System.out.println("Time Sequential: " + ((timeSequental) / 1_000_000) + " ms.");

        for (TagCounter tc : fetchedData) {
            System.out.println("Title: " + tc.getTitle());
            System.out.println("Div's: " + tc.getDivCount());
            System.out.println("Body's: " + tc.getBodyCount());
            System.out.println("----------------------------------");
        }


        
        start = System.nanoTime();

        List<TagDTO> parallelRes = runParrallel();
        long timeParallel = System.nanoTime() - start;
        System.out.println("Time Parallel: " + ((timeParallel) / 1_000_000) + " ms.");
        System.out.println("Paralle was " + timeSequental / timeParallel + " times faster");

        for (TagDTO tag : parallelRes) {
            System.out.println(tag);
        }
       

    }
}
