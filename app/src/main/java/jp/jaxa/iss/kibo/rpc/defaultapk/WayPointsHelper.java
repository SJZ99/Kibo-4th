package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

/**
 * Created by Jian Zhe Su on 6/20/2023.
 */

public class WayPointsHelper {
    private static final int SIZE = 8;
    private static ArrayList<Point>[][] wayPoint = new ArrayList[SIZE][SIZE];
    private static Map<Integer, Quaternion> targetRotation = new HashMap<>();
    private static Point[] points = new Point[8];

    static {
        points[0] = new Point(9.815, -9.806, 4.293);
        points[1] = new Point(11.27 - 0.06, -9.92 - 0.05, 5.29 + 0.185);
        points[2] = new Point(10.612 - 0.155, -9.07 - 0.125, 4.48 - 0.075);
        points[3] = new Point(10.71, -7.7 - 0.068, 4.48);
        points[4] = new Point(10.51, -6.718 + 0.1, 5.1804 + 0.03);
        points[5] = new Point(11.114 - 0.07, -7.97 + 0.05, 5.33);
        points[6] = new Point(11.355, -8.993 - 0.039, 4.78 + 0.16);
        points[7] = new Point(11.369, -8.55, 4.48);

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
        targetRotation.put(1, new Quaternion(0, 0, -0.707f, 0.707f));
        targetRotation.put(2, new Quaternion(0.5f, 0.5f, -0.5f, 0.5f));
        targetRotation.put(3, new Quaternion(0, 0.707f, 0, 0.707f));
        targetRotation.put(4, new Quaternion(0, 0, -1, 0));
        targetRotation.put(5, new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f));
        targetRotation.put(6, new Quaternion(0, 0, 0, 1));
        targetRotation.put(7, new Quaternion(0, 0.707f, 0, 0.707f));
    }

    private static void addWayPoint() {
        //-----------------------------------------Point 1---------------------------------------------
        // 0 <-> 1
        wayPoint[0][1].add(points[0]);
        wayPoint[0][1].add(new Point(10.6, -9.9, 4.9));
        wayPoint[0][1].add(points[1]);

        // 1 <-> 2
        wayPoint[1][2].add(points[1]);
        wayPoint[1][2].add(new Point(10.99, -9.45, 5.40));
        wayPoint[1][2].add(points[2]);

        // 1 <-> 3
        wayPoint[1][3].add(points[1]);
        wayPoint[1][3].add(new Point(10.88, -9.25, 5.40));
        wayPoint[1][3].add(new Point(10.75, -8.1, 4.7));
        wayPoint[1][3].add(points[3]);

        // 1 <-> 4
        wayPoint[1][4].add(points[1]);
        wayPoint[1][4].add(new Point(10.85, -9.45, 5.38));
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


        //-----------------------------------------Point 3---------------------------------------------
        // 3 <-> 4
        wayPoint[3][4].add(new Point(10.51, -6.7185, 5.1804));

        //3 <-> 5
        wayPoint[3][5].add(new Point(11.114, -7.9756, 5.3393));

        //-----------------------------------------Point 6---------------------------------------------
        // 0 <-> 6
        wayPoint[0][6].add(points[0]);
        wayPoint[0][6].add(points[6]);

        // 6 <-> 7
        wayPoint[6][7].add(points[6]);
        wayPoint[6][7].add(points[7]);
    }


    public static Quaternion getTargetRotation(int targetPoint) {
        return targetRotation.get(targetPoint);
    }

    public static ArrayList<Point> getWayPoint(int from, int to) {
        return wayPoint[from][to];
    }
}
