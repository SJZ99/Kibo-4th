package jp.jaxa.iss.kibo.rpc.taiwan.helper;


import java.util.ArrayList;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

public class WayPointsHelper {
    private static final int SIZE = 9;
    private static ArrayList<Point>[][] wayPoint = new ArrayList[SIZE][SIZE];
    private static ArrayList<Quaternion>[][] wayRotation = new ArrayList[SIZE][SIZE];

    private static Quaternion[] targetRotation = new Quaternion[SIZE];
    private static Point[] points = new Point[SIZE];

    static {
        points[0] = new Point(10.315000, -9.806000, 4.293000);
        points[1] = new Point(11.270000 + 0.105000, -9.92000 + 0.170000, 5.290000 + 0.050000);
        points[2] = new Point(10.612000 - 0.045000, -9.070000 + 0.10500, 4.480000 + 0.150000);
        points[3] = new Point(10.710000, -7.700000 - 0.068000, 4.480000 + 0.210000);
        points[4] = new Point(10.510000, -6.900000, 5.180400 + 0.030000); // y = -6.718 + 0.1

        points[7] = new Point(11.369000 - 0.400000, -9.000000, 4.900000); // y = -8.9
        points[8] = new Point(11.143000, -6.710000, 4.960000);

        wayPointInit();
        wayRotationInit();
        targetRotationInit();
        addWayPoint();
    }

    private static void wayPointInit() {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = i + 1; j < SIZE; ++j) {
                wayPoint[i][j] = new ArrayList<>();
            }
        }
    }

    private static void wayRotationInit() {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = i + 1; j < SIZE; ++j) {
                wayRotation[i][j] = new ArrayList<>();
            }
        }
    }

    private static void targetRotationInit() {
        targetRotation[0] = new Quaternion(1, 0, 0, 0);
        targetRotation[1] = new Quaternion(-0.523f, 0.475f, -0.523f, 0.475f);
        targetRotation[2] = new Quaternion(-0.5f, 0.5f, 0.5f, 0.5f);
        targetRotation[3] = new Quaternion(0, 0.707f, 0, 0.707f);
        targetRotation[4] = new Quaternion(0, 0, -0.977f, -0.212f);

        targetRotation[7] = new Quaternion(0.0f, 0.707f, 0.0f, 0.707f);
        targetRotation[8] = new Quaternion(0, 0, -0.707f, 0.707f);
    }

    private static void addWayPoint() {
        //-----------------------------------------Point 1---------------------------------------------
        // 0 <-> 7
        add(0, 7, points[0], targetRotation[7]);
        add(0, 7, points[7], targetRotation[7]);

        // 0 <-> 1
        add(0, 1, points[0], targetRotation[0]);
        add(0, 1, new Point(10.550000, -9.800000, 5.250000), targetRotation[1]);
        add(0, 1, points[1], targetRotation[1]);

        // 1 <-> 2
        add(1, 2, points[1], targetRotation[1]);
        add(1, 2, points[2], targetRotation[2]);

        // 1 <-> 3
        add(1, 3, points[1], targetRotation[1]);
        add(1, 3, points[3], targetRotation[3]);

        // 1 <-> 4
        add(1, 4, points[1], targetRotation[1]);
        add(1, 4, points[4], targetRotation[4]);

        // 1 <-> 7
        add(1, 7, points[1], targetRotation[1]);
        add(1, 7, points[7], targetRotation[7]);

        // 1 <-> 8
        add(1, 8, points[1], targetRotation[1]);
        add(1, 8, points[8], targetRotation[8]);
//        wayPoint[1][8].add(new Point(11.1, -7.73, 5.34));
//        wayPoint[1][8].add(new Point(11.143, -6.9, 5.1054));

        //-----------------------------------------Point 2---------------------------------------------
        // 0 <-> 2
        add(0, 2, points[0], targetRotation[0]);
        add(0, 2, points[2], targetRotation[2]);

        // 2 <-> 3
        add(2, 3, points[2], targetRotation[2]);
        add(2, 3, new Point(10.600000, -8.380000,5.000000), targetRotation[3]);
        add(2, 3, points[3], targetRotation[3]);

        // 2 <-> 4
        add(2, 4, points[2], targetRotation[2]);
        add(2, 4, points[4], targetRotation[4]);

        // 2 <-> 7
        add(2, 7, points[2], targetRotation[2]);
        add(2, 7, points[2], targetRotation[7]); // scan qr code at p2

        // 2 <-> 8
        add(2, 8, points[2], targetRotation[2]);
        add(2, 8, new Point(10.900000, -8.350000, 4.880000), targetRotation[8]);
        add(2, 8, new Point(11.143000, -6.950000, 4.960000), targetRotation[8]);

        //-----------------------------------------Point 3---------------------------------------------
        // 0 <-> 3
        add(0, 3, points[0], targetRotation[0]);
        add(0, 3, new Point(10.660000, -8.300000,5.060000), targetRotation[3]);
        add(0, 3, points[3], targetRotation[3]);

        // 3 <-> 4
        add(3, 4, points[3], targetRotation[3]);
        add(3, 4, points[4], targetRotation[4]);

        // 3 <-> 7
        add(3, 7, points[3], targetRotation[3]);
        add(3, 7, new Point(10.660000, -8.300000,5.060000), targetRotation[7]);
        add(3, 7, points[7], targetRotation[7]);

        // 3 <-> 8
        add(3, 8, points[3], targetRotation[3]);
        add(3, 8, new Point(11.143000, -6.950000, 4.960000), targetRotation[8]); // shorter

        //-----------------------------------------Point 4---------------------------------------------
        // 0 <-> 4
        add(0, 4, points[0], targetRotation[0]);
        add(0, 4, new Point(10.500000,-8.350000, 4.900000), targetRotation[4]);
        add(0, 4, points[4], targetRotation[4]);

        // 4 <-> 7
        add(4, 7, points[4], targetRotation[4]);
        add(4, 7, points[7], targetRotation[7]);

        // 4 <-> 8
        add(4, 8, points[4], targetRotation[4]);
        add(4, 8, new Point(11.100000, -6.760700, 5.125400), targetRotation[8]);

        //-----------------------------------------Point 7---------------------------------------------
        // 7 <-> 8
        add(7, 8, points[7], targetRotation[7]);
        add(7, 8, new Point(10.900000, -8.200000, 4.830000), targetRotation[8]);
        add(7, 8, new Point(11.143000, -6.960000, 4.960000), targetRotation[8]);
    }

    private static void add(int from, int to, Point p, Quaternion q) {
        wayPoint[from][to].add(p);
        wayRotation[from][to].add(q);
    }

    public static Quaternion getTargetRotation(int targetPoint) {
        if(targetPoint >= SIZE || targetPoint <= 0) {
            return new Quaternion();
        }
        return targetRotation[targetPoint];
    }

    public static ArrayList<Point> getWayPoint(int from, int to) {
        // will not happen
        if(from < 0 || to < 0 || from >= SIZE || to >= SIZE || from >= to || from == 5 || from == 6 || to == 5 || to == 6) {
            return new ArrayList<>();
        }
        return wayPoint[from][to];
    }

    public static ArrayList<Quaternion> getWayRotation(int from, int to) {
        // will not happen
        if(from < 0 || to < 0 || from >= SIZE || to >= SIZE || from >= to || from == 5 || from == 6 || to == 5 || to == 6) {
            return new ArrayList<>();
        }
        return wayRotation[from][to];
    }

    public static Point getPoint(int p) {
        if(p <= 0 || p >= SIZE) return null;
        return points[p];
    }
}
