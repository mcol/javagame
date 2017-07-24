package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Utils {

    /** Random number generator. */
    private static final Random random = new Random();

    /** Returns a random integer between two values. */
    public static int randomInteger(int min, int max) {
        return min + random.nextInt(max - min);
    }

    /** Returns a random float between two values. */
    public static float randomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    /** Returns a random boolean. */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    /** Reads the content of a file into a string. */
    public static String loadFileAsString(String path) {
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null)
                builder.append(line + "\n");
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch(NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float clampValue(float x, int min, int max) {
        if (x < min)
            return min;
        if (x > max)
            return max;
        return x;
    }
}
