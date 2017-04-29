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

    @Override
    public void run() {

        Path dir = Paths.get("C:\\Users\\denis\\Documents\\decawave_trek\\TREK1000\\DecaRangeRTLS-PC\\Logs\\");  // specify your directory
        List<String> list;
        String []range;

        File newFile = getLatestFileFromDir(dir.toString());

        if (newFile != null) {

            try (Stream<String> stream = Files.lines(Paths.get(newFile.toString()))) {

                list = stream.collect(Collectors.toList());
                List<String> tail = list.subList(Math.max(list.size() - 5, 0), (list.size() - 2));

                for (String read : tail) {

                    range = read.split(":");

                    System.out.println("Range for Anchor: " + range[4] + ", Distance: " + range[6]);
                }

                System.out.print("\n");

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
}
