import jdk.jfr.Description;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        /**
         * Parse the HTML document from URL specified in the variable "mainURL"
         * and download images to the directory in the path specified in the variable "path" ,
         * saving the original image name from the URL.
         * If directory not exist, creat directory.
         * */
        String mainURL = "https://ru.hexlet.io/";
        String path = "C:\\img\\hexlet\\";
        try {
            Document page = Jsoup.connect(mainURL).get();
            Elements imgs = page.select("img");
            AtomicInteger counter = new AtomicInteger(0);
            imgs.forEach(img -> {
                String src = img.attr("abs:src");
                if (src.contains(".jpg") || src.contains(".png") ||
                        src.contains(".jpeg") || src.contains(".gif") ||
                        src.contains(".bmp") || src.contains(".svg")) {
                    File file = new File(path);
                    file.mkdirs();

                    Path dest = Paths.get(path.concat(src.substring(src.lastIndexOf("/") + 1)));
                    System.out.println(dest);
                    /**
                     * Download files with using Files.copy()
                     */
                    /*try (InputStream source = new URL(src).openStream()) {
                        System.out.println(source.toString());
                        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }*/
                    /**
                    * Download file with using Channels
                    * */
                    try (ReadableByteChannel rbc = Channels.newChannel(new URL(src).openStream());
                         FileOutputStream fos = new FileOutputStream(dest.toString());
                         FileChannel fc = fos.getChannel()
                    ) {
                        fc.transferFrom(rbc, 0, Long.MAX_VALUE);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    counter.incrementAndGet();
                }

            });
            System.out.println("Скачано " + counter + " файлов.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
