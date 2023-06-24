package jp.jaxa.iss.kibo.rpc.defaultapk;

public class PathLengthHelper {
    static final int SIZE = 8;
    static float[][] time = new float[8][8];

    static {
        time[0][1] = 40.5f;
        time[1][2] = 41.5f;
        time[1][3] = 50.8f;
        time[1][4] = 43.2f;
        time[1][5] = 34.3f;
        time[1][6] = 25.8f;
        time[1][8] = 57.3f;

        time[2][3] = 44.1f;
        time[2][4] = 39.1f;
        time[2][5] = 30.5f;
        time[2][6] = 24.75f;
        time[2][8] = 50.7f;

        time[3][1] = 48f;
        time[3][4] = 41f;
        time[3][5] = 37.2f;
        time[3][6] = 41.6f;
        time[3][8] = 25;

        time[4][3] = 44.6f;
        time[4][5] = 28.4f;
        time[4][6] = 38.3f;
        time[4][8] = 19.4f;

        time[5][3] = 44.1f;
        time[5][6] = 39.5f;
        time[5][8] = 25.2f;
    }

    public static float getTime(int from, int to) {
        if(time[from][to] > 10) {
            return time[from][to];
        } else {
            return time[to][from];
        }
    }
}
