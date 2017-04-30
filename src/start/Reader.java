package start;

import services.ScheduledTask;

import java.util.Timer;

/**
 * Created by denis on 4/27/2017.
 * deca-reader
 */
public class Reader {

    public static void main(String []args) {

        Timer time = new Timer(); // Instantiate Timer Object
        ScheduledTask st = new ScheduledTask(); // Instantiate services.ScheduledTask class
        time.schedule(st, 0, 500); // Create Repetitively task for every 1 secs
    }
}
