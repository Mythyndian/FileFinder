import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;


public class App {
    public static void main(String[] args) throws Exception {
        findFiles("png", "https://github.com/");
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
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.getElementsByAttribute("href");
        for (Element link : links) {
            String linkHref = link.attr("href");
            //if (linkHref.endsWith("." + extension)) {
                // Save to file with a file name as url of page were it's found
                String filename = "page" + "_" + extension + "s";
                writeResults(filename, linkHref);
            //}
        }
    }

    public static void writeResults(String filename, String data) {
        String currentDirectory = System.getProperty("user.dir");
        String lineSeparator = System.getProperty("line.separator");
        try {
            File dump = new File( currentDirectory + File.separator + "collectedData" + File.separator + filename + ".txt");
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
                System.out.println("Entry successfully added!");

            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
