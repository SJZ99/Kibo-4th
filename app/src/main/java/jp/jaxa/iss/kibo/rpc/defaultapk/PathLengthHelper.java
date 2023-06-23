package jp.jaxa.iss.kibo.rpc.defaultapk;

public class PathLengthHelper {
    static final int SIZE = 8;
    static float[][] time = new float[8][8];

    static {
        time[0][1] = 40.5f;
        time[1][2] = 41.5f;
    }

    public static float getTime(int from, int to) {
        if(from > to) {
            from ^= to ^= from;
        }

        return time[from][to];
    }
}
