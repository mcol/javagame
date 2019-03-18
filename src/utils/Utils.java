package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Utils {

    /** Random number generator. */
    private static final Random random = new Random();

    /** Returns a random integer between two values (extremes included). */
    public static int randomInteger(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    /** Returns a random float between two values. */
    public static float randomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    /** Returns a random boolean. */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    /** Returns a random sign. */
    public static int randomSign() {
        return random.nextBoolean() ? 1 : -1;
    }

    /** Reads the content of a file into a string. */
    public static String loadFileAsString(String path) {
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null)
                builder.append(line).append("\n");
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

    /** Enforce a value to stay between two extremes. */
    public static float clampValue(float x, float min, float max) {
        if (x < min)
            return min;
        if (x > max)
            return max;
        return x;
    }

    /** Clamp in the absolute value. */
    public static float clampAbsValue(float x, float val) {
        return clampValue(x, -val, val);
    }
}
