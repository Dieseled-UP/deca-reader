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

    private Path dir = Paths.get("C:\\Users\\denis\\Documents\\decawave_trek\\TREK1000\\DecaRangeRTLS-PC\\Logs\\");  // specify your directory
    private int count = 0;

    @Override
    public void run() {

        File newFile = getLatestFileFromDir(dir.toString());

        if (newFile != null) {

            try (Stream<String> stream = Files.lines(Paths.get(newFile.toString()))) {

                writeOutResults(getAnchorReads(getReducedList(stream)));

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
    private List<String> getReducedList(Stream<String> stream) {

        List<String> list = stream.collect(Collectors.toList());

        return list.subList(Math.max(list.size() - 5, 0), (list.size() - 2));
    }

    /**
     * Method to split the read String and subtract the range reads
     * @param list List containing the 3 reads
     * @return array with the three distances
     */
    private String[] getAnchorReads(List<String> list) {

        String []query = new String[3];

        for (String read : list) {

            String[] range = read.split(":");

            query[count] = range[6];
            System.out.println("Range for Anchor: " + range[4] + ", Distance: " + range[6]);
            count++;
        }

        System.out.print("\n");

        return query;
    }

    /**
     * Method to subtract the three distances and send to database
     * @param results String array containing distance reads
     */
    private void writeOutResults(String[] results) {

        Query.postReads(results[0], results[1], results[2]);
    }
}
