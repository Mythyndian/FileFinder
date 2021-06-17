import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class App {
    public static void main(String[] args) throws Exception {
        collectData("https://github.com/", "png");

    }

    public static boolean isUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    // Method for finding files with certain extension
    public static void findFiles(String extension, String url) throws IOException {
        String clearUrl;
        if (isUrl(url)) {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.getElementsByAttribute("href");
            for (Element link : links) {
                String linkHref = link.attr("href");
                if (linkHref.endsWith("." + extension)) {
                    clearUrl = url.substring(8);
                    clearUrl = clearUrl.replaceAll("\\W", ".");
                    System.out.println(clearUrl);
                    String filename = clearUrl + "_" + extension + "s";
                    writeResults(filename, linkHref);
                }
            }

        } else
            System.out.println("Incorrect URL.");
    }

    public static void writeResults(String filename, String data) {
        String currentDirectory = System.getProperty("user.dir");
        String lineSeparator = System.getProperty("line.separator");
        try {
            File dump = new File(currentDirectory + File.separator + "collectedData" + File.separator + filename + ".txt");
            if (dump.createNewFile()) {
                System.out.println("Creating file: " + dump.getName() + "...");
                FileWriter fw = new FileWriter(dump);
                fw.write(data + lineSeparator);
                fw.flush();
                fw.close();
            } else {
                FileWriter fw = new FileWriter(dump, true);
                fw.write(data + lineSeparator);
                fw.flush();
                fw.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static List<String> findLinks(String url) throws IOException {
        String clearUrl;
        String linkHref;
        List subpages = new ArrayList();
        if (isUrl(url)) {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.getElementsByTag("a");
            for (Element link : links) {
                linkHref = link.attr("href");
                if (linkHref.startsWith("/") & linkHref != "/") {
                    subpages.add(linkHref);
                }

            }
        }
        return subpages;
    }

    public static void collectData(String startUrl, String extension) throws IOException {
        List pages = findLinks(startUrl);
        for (Object page : pages) {
//            System.out.println(startUrl + page.toString().substring(1));
            findFiles(extension, startUrl + page.toString().substring(1));
        }


    }
}
