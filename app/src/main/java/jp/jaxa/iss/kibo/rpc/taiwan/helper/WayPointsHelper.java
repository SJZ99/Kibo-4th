package jp.jaxa.iss.kibo.rpc.taiwan.helper;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

public class WayPointsHelper {
    private static final int SIZE = 9;
    private static ArrayList<Point>[][] wayPoint = new ArrayList[SIZE][SIZE];
    private static Map<Integer, Quaternion> targetRotation = new HashMap<>();
    private static Point[] points = new Point[SIZE];

    static {
        points[0] = new Point(10.315, -9.806, 4.293);
        points[1] = new Point(11.27 + 0.105, -9.92 + 0.17, 5.29 + 0.05);
        points[2] = new Point(10.612 - 0.045, -9.07 + 0.092, 4.48 + 0.15);
        points[3] = new Point(10.71, -7.7 - 0.068, 4.48 + 0.21);

        points[4] = new Point(10.51, -6.9, 5.1804 + 0.03); // y = -6.718 + 0.1

        points[5] = new Point(11.114 - 0.07, -7.97 + 0.05, 5.33);
        points[6] = new Point(11.355, -8.993 - 0.053, 4.78 + 0.16);
        points[7] = new Point(11.369 - 0.5, -8.9, 4.9); // -0.5, -0
        points[8] = new Point(11.143, -6.71, 4.96);

        wayPointInit();
        targetRotationInit();
        addWayPoint();
    }

    private static void wayPointInit() {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = i + 1; j < SIZE; ++j) {
                wayPoint[i][j] = new ArrayList();
            }
        }
    }

    private static void targetRotationInit() {
        targetRotation.put(1, new Quaternion(-0.529f, 0.469f, -0.529f, 0.469f));
        targetRotation.put(2, new Quaternion(-0.5f, 0.5f, 0.5f, 0.5f));
        targetRotation.put(3, new Quaternion(0, 0.707f, 0, 0.707f));
        targetRotation.put(4, new Quaternion(0, 0, -0.977f, -0.212f));
        targetRotation.put(5, new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f));
        targetRotation.put(6, new Quaternion(0, 0, 0, 1));
        targetRotation.put(7, new Quaternion(0.0f, 0.707f, 0.0f, 0.707f));
        targetRotation.put(8, new Quaternion(0, 0, -0.707f, 0.707f));
    }

    private static void addWayPoint() {
        //-----------------------------------------Point 1---------------------------------------------
        // 0 <-> 7
        wayPoint[0][7].add(points[0]);
        wayPoint[0][7].add(points[7]);

        // 0 <-> 1
        wayPoint[0][1].add(points[0]);
        wayPoint[0][1].add(new Point(10.55, -9.9 + 0.1, 5.25));
        wayPoint[0][1].add(points[1]);

        // 1 <-> 2
        wayPoint[1][2].add(points[1]);
        wayPoint[1][2].add(points[2]);

        // 1 <-> 3
        wayPoint[1][3].add(points[1]);
        wayPoint[1][3].add(points[3]);

        // 1 <-> 4
        wayPoint[1][4].add(points[1]);
        wayPoint[1][4].add(points[4]);

        // 1 <-> 5
        wayPoint[1][5].add(points[1]);
        wayPoint[1][5].add(points[5]);

        // 1 <-> 6
        wayPoint[1][6].add(points[1]);
        wayPoint[1][6].add(points[6]);

        // 1 <-> 7
        wayPoint[1][7].add(points[1]);
        wayPoint[1][7].add(points[7]);

        // 1 <-> 8
        wayPoint[1][8].add(points[1]);
        wayPoint[1][8].add(new Point(11.14, -8.20, 4.83));
        wayPoint[1][8].add(new Point(11.143, -6.95, 4.96));

        //-----------------------------------------Point 2---------------------------------------------
        // 0 <-> 2
        wayPoint[0][2].add(points[0]);
        wayPoint[0][2].add(points[2]);

        // 2 <-> 3
        wayPoint[2][3].add(points[2]);
        wayPoint[2][3].add(new Point(10.6 , -8.38,5));
        wayPoint[2][3].add(points[3]);

        // 2 <-> 4
        wayPoint[2][4].add(points[2]);
        wayPoint[2][4].add(points[4]);

        // 2 <-> 5
        wayPoint[2][5].add(points[2]);
        wayPoint[2][5].add(points[5]);

        // 2 <-> 6
        wayPoint[2][6].add(points[2]);
        wayPoint[2][6].add(points[6]);

        // 2 <-> 7
        wayPoint[2][7].add(points[2]);
        wayPoint[2][7].add(points[2]); // scan qr code at p2

        // 2 <-> 8
        wayPoint[2][8].add(points[2]);
        wayPoint[2][8].add(new Point(10.9, -8.20, 4.83));
        wayPoint[2][8].add(new Point(11.143, -6.95, 4.96));


        //-----------------------------------------Point 3---------------------------------------------
        // 0 <-> 3
        wayPoint[0][3].add(points[0]);
        wayPoint[0][3].add(new Point(10.66 , -8.3,5.06));
        wayPoint[0][3].add(points[3]);

        // 3 <-> 4
        wayPoint[3][4].add(points[3]);
        wayPoint[3][4].add(points[4]);

        // 3 <-> 5
        wayPoint[3][5].add(points[3]);
        wayPoint[3][5].add(new Point(10.9, -7.58,  5.25));
        wayPoint[3][5].add(points[5]);

        // 3 <-> 6
        wayPoint[3][6].add(points[3]);
        wayPoint[3][6].add(new Point(10.6974195342895,-8.101652195976,  4.78));
        wayPoint[3][6].add(points[6]);

        // 3 <-> 7
        wayPoint[3][7].add(points[3]);
        wayPoint[3][7].add(new Point(10.66 , -8.3,5.06));
        wayPoint[3][7].add(points[7]);

        // 3 <-> 8
        wayPoint[3][8].add(points[3]);
        wayPoint[3][8].add(new Point(11.143, -6.95, 4.96)); // shorter

        //-----------------------------------------Point 4---------------------------------------------
        // 0 <-> 4
        wayPoint[0][4].add(points[0]);
        wayPoint[0][4].add(new Point(10.5,-8.35, 4.9));
        wayPoint[0][4].add(points[4]);

        // 4 <-> 5
        wayPoint[4][5].add(points[4]);
        wayPoint[4][5].add(points[5]);

        // 4 <-> 6
        wayPoint[4][6].add(points[4]);
        wayPoint[4][6].add(points[6]);

        // 4 <-> 7
        wayPoint[4][7].add(points[4]);
        wayPoint[4][7].add(points[7]);

        // 4 <-> 8
        wayPoint[4][8].add(points[4]);
        wayPoint[4][8].add(new Point(11.1, -6.7607, 5.1254));

        //-----------------------------------------Point 5---------------------------------------------
        // 0 <-> 5
        wayPoint[0][5].add(points[0]);
        wayPoint[0][5].add(points[5]);

        // 5 <-> 6
        wayPoint[5][6].add(points[5]);
        wayPoint[5][6].add(new Point(11.20,-8.33,5.5));
        wayPoint[5][6].add(points[6]);

        // 5 <-> 7
        wayPoint[5][7].add(points[5]);
        wayPoint[5][7].add(points[7]);

        // 5 <-> 8
        wayPoint[5][8].add(points[5]);
        wayPoint[5][8].add(new Point(11.143, -6.96, 4.96));

        //-----------------------------------------Point 6---------------------------------------------
        // 0 <-> 6
        wayPoint[0][6].add(points[0]);
        wayPoint[0][6].add(points[6]);

        // 6 <-> 7
        wayPoint[6][7].add(points[6]);
        wayPoint[6][7].add(points[7]);

        // 6 <-> 8
        wayPoint[6][8].add(points[6]);
        wayPoint[6][8].add(new Point(11.143, -6.96, 4.96));

        //-----------------------------------------Point 7---------------------------------------------
        // 7 <-> 8
        wayPoint[7][8].add(points[7]);
        wayPoint[7][8].add(new Point(10.9, -8.20, 4.83));
        wayPoint[7][8].add(new Point(11.143, -6.96, 4.96));
    }


    public static Quaternion getTargetRotation(int targetPoint) {
        return targetRotation.get(targetPoint);
    }

    public static ArrayList<Point> getWayPoint(int from, int to) {
        // will not happen
        if(from < 0 || to < 0 || from >= SIZE || to >= SIZE || from >= to) {
            return new ArrayList<>();
        }
        return wayPoint[from][to];
    }

    public static Point getPoint(int p) {
        if(p < 0 || p >= SIZE) {
            return null;
        }
        return points[p];
    }
}
