package jp.jaxa.iss.kibo.rpc.taiwan.helper;

public class PathLengthHelper {
    static final int SIZE = 9;
    static float[][] time = new float[SIZE][SIZE];

    static {
        time[0][1] = 44f;
        time[1][2] = 29.2f;
        time[1][3] = 35.3f;
        time[1][4] = 43f;
        time[1][5] = 34.3f;
        time[1][6] = 25.8f;
        time[1][8] = 55.5f;

        time[0][2] = 23.3f;
        time[2][3] = 44.4f;
        time[2][4] = 34.9f;
        time[2][5] = 30.5f;
        time[2][6] = 24.75f;
        time[2][8] = 50.7f;

        time[0][3] = 51.3f;
        time[3][1] = 42f;
        time[3][2] = 41.4f;
        time[3][4] = 25.2f;
        time[3][5] = 37.2f;
        time[3][6] = 41.6f;
        time[3][8] = 23.1f;

        time[0][4] = 56.2f;
        time[4][5] = 28.4f;
        time[4][6] = 38.3f;
        time[4][8] = 18.3f;

        time[0][5] = 34.8f;
        time[5][3] = 44.1f;
        time[5][6] = 39.5f;
        time[5][8] = 24f;

        time[0][6] = 28.5f;
        time[6][8] = 32.5f;

        time[0][7] = 24.4f;
        time[7][1] = 26.3f;
        time[7][2] = 35.52f;
        time[7][3] = 37.44f;
        time[7][4] = 35.9f;
        time[7][5] = 24.6f;
        time[7][6] = 17.8f;
        time[7][8] = 43.3f;
    }

    public static float getTime(int from, int to) {
        if(from >= SIZE || to >= SIZE || from < 0 || to < 0) {
            return 300 * 1000;
        }

        if(time[from][to] > 10) {
            return time[from][to] * 1000;
        } else {
            return time[to][from] * 1000;
        }
    }
}
