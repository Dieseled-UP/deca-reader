package services;

import connect.Query;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
     */
    private void getReducedList(Stream<String> stream) {

        List<String> list = stream
                .filter(line -> line.contains(":RR:0:"))
                .collect(Collectors.toList());

        getAnchorReads(list.subList(Math.max(list.size() - 5, 0), (list.size() - 2)));

        /*getAnchorReads(stream
                .filter(line -> line.contains(":RR:0:"))
                .collect(Collectors.toList()));*/
    }

    /**
     * Method to split the read String and subtract the range reads
     * @param list List containing the 3 reads
     */
    private void getAnchorReads(List<String> list) {

        ArrayList<String> query = new ArrayList<>();

        for (String read : list) {

            String[] range = read.split(":");

            query.add(range[6]);
            System.out.println("Range for Anchor: " + range[4] + ", Distance: " + range[6]);
            count++;

            if (count == 1) {

                count = 0;
                addDecimalPoint(query);
                query.clear();
            }
        }

        System.out.print("\n");
    }

    private void addDecimalPoint(ArrayList<String> list) {

        int num = 0;

        for (String read : list) {

            list.set(num, new StringBuffer(read).insert(read.length() - 3, ".").toString());
            num++;
        }

        stringToDouble(list);
    }

    private void stringToDouble(ArrayList<String> list) {

        double []temp = new double[1];
        int digit = 0;

        for (String read : list) {

            temp[digit] = Double.parseDouble(read);
            digit++;
        }

        writeOutResults(temp);
    }

    /**
     * Method to subtract the three distances and send to database
     * @param results String array containing distance reads
     */
    private void writeOutResults(double[] results) {

//        System.out.println(results[0] + ", " + results[1] + ", " + results[2]);
        System.out.println(results[0]);
        Query.postReads(results[0], 0, 0);
    }
}
