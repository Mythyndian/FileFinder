import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;


public class App {
    public static void main(String[] args) throws Exception {
        findFiles("png", "https://github.com/");
        findLinks("https://github.com/");

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
        int counter = 0;
        if (isUrl(url)) {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.getElementsByAttribute("href");
            for (Element link : links) {
                String linkHref = link.attr("href");
                if (linkHref.endsWith("." + extension)) {
                    // TODO link find method
                    if (url.contains("https://")) {
                        clearUrl = url.replace("/", "");
                        clearUrl = clearUrl.replace("https:", "");
                    } else {
                        clearUrl = url.replace("/", "");
                        clearUrl = clearUrl.replace("http:", "");
                    }

                    String filename = clearUrl + "_" + extension + "s";
                    writeResults(filename, linkHref);
                }
            }

        } else
            System.out.println("Incorect URL.");
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

    public static void findLinks(String url) throws IOException {
        String clearUrl;
        String linkHref;
        if (isUrl(url)) {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.getElementsByTag("a");
            for (Element link : links) {
                linkHref = link.attr("href");
                if (url.contains("https://")) {
                    clearUrl = url.replace("/", "");
                    clearUrl = clearUrl.replace("https:", "");
                } else {
                    clearUrl = url.replace("/", "");
                    clearUrl = clearUrl.replace("http:", "");
                }

                String filename = clearUrl + "_" + "links";
                writeResults(filename, linkHref);
            }
        }
    }
}
