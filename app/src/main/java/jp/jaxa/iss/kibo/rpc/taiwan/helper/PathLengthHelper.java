package jp.jaxa.iss.kibo.rpc.taiwan.helper;

public class PathLengthHelper {
    static final int SIZE = 9;
    static float[][] time = new float[SIZE][SIZE];

    static {
        time[0][1] = 40.5f;
        time[1][2] = 41.5f;
        time[1][3] = 50.8f;
        time[1][4] = 43.2f;
        time[1][5] = 34.3f;
        time[1][6] = 25.8f;
        time[1][8] = 55.5f;

        time[0][2] = 20.4f;
        time[2][3] = 44.1f;
        time[2][4] = 39.1f;
        time[2][5] = 30.5f;
        time[2][6] = 24.75f;
        time[2][8] = 50.7f;

        time[0][3] = 61f;
        time[3][1] = 48f;
        time[3][4] = 41f;
        time[3][5] = 37.2f;
        time[3][6] = 41.6f;
        time[3][8] = 23.1f;

        time[0][4] = 61f;
        time[4][3] = 44.6f;
        time[4][5] = 28.4f;
        time[4][6] = 38.3f;
        time[4][8] = 19.4f;

        time[0][5] = 34.8f;
        time[5][3] = 44.1f;
        time[5][6] = 39.5f;
        time[5][8] = 24f;

        time[0][6] = 28.5f;
        time[6][8] = 32.5f;

        time[7][1] = 28.8f;
        time[7][2] = 35.52f;
        time[7][3] = 47.5f;
        time[7][4] = 50.9f;
        time[7][5] = 37.82f;
        time[7][6] = 18.18f;
        time[7][8] = 40.9f;
    }

    public static float getTime(int from, int to) {
        if(time[from][to] > 10) {
            return time[from][to] * 1000;
        } else {
            return time[to][from] * 1000;
        }
    }
}
