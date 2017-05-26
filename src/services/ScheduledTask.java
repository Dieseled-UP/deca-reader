package services;

import connect.Query;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by denis on 4/28/2017.
 * deca-reader
 */
public class ScheduledTask extends TimerTask {

    private Path dir = Paths.get("/home/denis/Documents/logs/");  // specify your directory
    private int count = 0;

    @Override
    public void run() {

        File newFile = getLatestFileFromDir(dir.toString());

        if (newFile != null) {

            try (Stream<String> stream = Files.lines(Paths.get(newFile.toString()))) {

                getReducedList(stream);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /**
     * Method to get the last modified file from a given directory
     * @param dirPath String path to directory
     * @return File last modified
     */
    private File getLatestFileFromDir(String dirPath){

        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }

    /**
     * Method to get the last 3 reads form the log
     * @param stream read from the log file
     * @return List containing the 3 reads
     */
    private void getReducedList(Stream<String> stream) {

        /*List<String> list = stream.collect(Collectors.toList());

        return list.subList(Math.max(list.size() - 5, 0), (list.size() - 2));*/

        getAnchorReads(stream
                .filter(line -> line.contains(":RR:0:"))
                .collect(Collectors.toList()));

        /*return stream
                .filter(line -> line.contains(":RR:0:"))
                .collect(Collectors.toList());*/
    }

    /**
     * Method to split the read String and subtract the range reads
     * @param list List containing the 3 reads
     * @return array with the three distances
     */
    private void getAnchorReads(List<String> list) {

        String []query = new String[3];

        for (String read : list) {

            String[] range = read.split(":");

            query[count] = range[6];
            System.out.println("Range for Anchor: " + range[4] + ", Distance: " + range[6]);
            count++;

            if (count == 3) {
                count = 0;
                writeOutResults(query);
            }
        }

        System.out.print("\n");
    }

    /**
     * Method to subtract the three distances and send to database
     * @param results String array containing distance reads
     */
    private void writeOutResults(String[] results) {

        count = 0;
        Query.postReads(results[0], results[1], results[2]);
    }
}
