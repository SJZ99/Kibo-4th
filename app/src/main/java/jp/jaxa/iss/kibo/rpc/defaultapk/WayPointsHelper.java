package jp.jaxa.iss.kibo.rpc.defaultapk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import java.util.Map;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

/*
 * Created by Jian Zhe Su on 6/20/2023.
 */

public class WayPointsHelper {
    private static final int SIZE = 9;
    private static ArrayList<Point>[][] wayPoint = new ArrayList[SIZE][SIZE];
    private static Map<Integer, Quaternion> targetRotation = new HashMap<>();
    private static Point[] points = new Point[SIZE];

    static {
        points[0] = new Point(9.815, -9.806, 4.293);
        points[1] = new Point(11.27 - 0.06, -9.92 - 0.05, 5.29 + 0.185);
        points[2] = new Point(10.612 - 0.155, -9.07 - 0.125, 4.58);
        points[3] = new Point(10.71, -7.7 - 0.068, 4.48);
        points[4] = new Point(10.51, -6.718 + 0.1, 5.1804 + 0.03);
        points[5] = new Point(11.114 - 0.07, -7.97 + 0.05, 5.33);
        points[6] = new Point(11.355, -8.993 - 0.053, 4.78 + 0.16);
        points[7] = new Point(11.369, -8.55, 4.48);
        points[8] = new Point(11.143, -6.76, 4.96);

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
        targetRotation.put(8, new Quaternion(0, 0, -0.707f, 0.707f));
    }

    private static void addWayPoint() {
        //-----------------------------------------Point 1---------------------------------------------
        // 0 <-> 1
        wayPoint[0][1].add(points[0]);
        wayPoint[0][1].add(new Point(10.6, -9.9, 4.9));
        wayPoint[0][1].add(points[1]);

        // 1 <-> 2
        wayPoint[1][2].add(points[1]);
        wayPoint[1][2].add(new Point(10.99, -9.45, 5.35));
        wayPoint[1][2].add(points[2]);

        // 1 <-> 3
        wayPoint[1][3].add(points[1]);
        wayPoint[1][3].add(new Point(10.88, -9.25, 5.35));
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

        // 1 <-> 8
        wayPoint[1][8].add(points[1]);
        wayPoint[1][8].add(new Point(11.22, -8.20, 4.83));
        wayPoint[1][8].add(points[8]);

        //-----------------------------------------Point 2---------------------------------------------
        // 2 <-> 3
        wayPoint[2][3].add(points[2]);
        wayPoint[2][3].add(new Point(10.66 , -8.38,5));
        wayPoint[2][3].add(points[3]);

        // 2 <-> 4
        wayPoint[2][4].add(points[2]);
        wayPoint[2][4].add(new Point(10.58, -8.32 , 5));
        wayPoint[2][4].add(points[4]);

        // 2 <-> 5
        wayPoint[2][5].add(points[2]);
//        wayPoint[2][5].add(new Point(10.612 - 0.155, -9.07 - 0.125,5.33));
        wayPoint[2][5].add(points[5]);

        // 2 <-> 6
        wayPoint[2][6].add(points[2]);
//        wayPoint[2][6].add(new Point(10.92,-9.30,4.48));
        wayPoint[2][6].add(points[6]);

        // 2 <-> 7
        wayPoint[2][7].add(points[2]);
        wayPoint[2][7].add(new Point(10.612 - 0.155, -9.07 - 0.125, 4.94));
        wayPoint[2][7].add(points[7]);


        //-----------------------------------------Point 3---------------------------------------------

        //0-3
        wayPoint[0][3].add(points[0]);

        wayPoint[0][3].add(new Point(  10.6,-8.5,4.9));
//        wayPoint[0][3].add(new Point(10.411232142903,-9.7023593629268,4.9));
        wayPoint[0][3].add(new Point(10.6006913884673,-7.8709199891385,4.9));
        wayPoint[0][3].add(points[3]);

        // 3 <-> 4
        wayPoint[3][4].add(points[3]);
        wayPoint[3][4].add(new Point(10.626533168828,-7.1936467429033,5.1804));
        wayPoint[3][4].add(points[4]);

        // 3 <-> 5
        wayPoint[3][5].add(points[3]);
        wayPoint[3][5].add(new Point(10.665747528562,-7.6568514959995,  5.33));
        wayPoint[3][5].add(points[5]);

        // 3 <-> 6
        // wayPoint[3][6].add(new Point(10.7462457251825,-7.7407861997722,  4.78));
        wayPoint[3][6].add(points[3]);
        wayPoint[3][6].add(new Point(10.6974195342895,-8.101652195976,  4.78));
        wayPoint[3][6].add(points[6]);

        // 3 <-> 7
        wayPoint[3][7].add(points[3]);
        wayPoint[3][7].add(new Point(10.6974195342895,-8.101652195976,  4.78));
        wayPoint[3][7].add(new Point(11.3054897644974,-8.4952459219104, 4.78));
        wayPoint[3][7].add(new Point(11.355, -8.993, 4.78));
        wayPoint[3][7].add(points[7]);

        // 3 <-> 8
        wayPoint[3][8].add(points[3]);
        wayPoint[3][8].add(points[8]);

        //-----------------------------------------Point 4---------------------------------------------

       // 0-4
        wayPoint[0][4].add(points[0]);
        wayPoint[0][4].add(new Point(10.411232142903,-9.7023593629268, 5.185));
        wayPoint[0][4].add(points[4]);

        // 4 <-> 5
        wayPoint[4][5].add(points[4]);
        wayPoint[4][5].add(new Point( 10.4397212574624,-7.5187431047674, 5.33));
        wayPoint[4][5].add(points[5]);

        //4-6
        wayPoint[4][6].add(points[4]);
        wayPoint[4][6].add(new Point( 10.5385173783457,-8.6727181907114, 4.8));
        wayPoint[4][6].add(points[6]);

        //4-7
        wayPoint[4][7].add(points[4]);
        wayPoint[4][7].add(new Point(10.7630545575701,-8.4938681311779, 4.9));
        wayPoint[4][7].add(points[7]);

        // 4 <-> 8
        wayPoint[4][8].add(points[4]);
        wayPoint[4][8].add(points[8]);

        //-----------------------------------------Point 5---------------------------------------------
        // 5 <-> 6
        wayPoint[5][6].add(points[5]);
        wayPoint[5][6].add(new Point(11.20,-8.33,5));
        wayPoint[5][6].add(points[6]);

        // 5 <-> 7
        wayPoint[5][7].add(points[5]);
        wayPoint[5][7].add(new Point(11.20,-8.33,5));
        wayPoint[5][7].add(points[7]);


        //-----------------------------------------Point 6---------------------------------------------
        // 0 <-> 6
        wayPoint[0][6].add(points[0]);
        wayPoint[0][6].add(points[6]);

        // 6 <-> 7
        wayPoint[6][7].add(points[6]);
        wayPoint[6][7].add(points[7]);

        // 6 <-> 8
        wayPoint[6][8].add(points[6]);
//        wayPoint[6][8].add(new Point(11.22, -8.20, 4.85));
        wayPoint[6][8].add(points[8]);
    }


    public static Quaternion getTargetRotation(int targetPoint) {
        return targetRotation.get(targetPoint);
    }

    public static ArrayList<Point> getWayPoint(int from, int to) {
        return wayPoint[from][to];
    }

    public static Point getPoint(int p) {
        return points[p];
    }

    public static class PointComparator implements Comparator<Point> {
        private Point now;
        public PointComparator(Point now) {
            this.now = now;
        }

        @Override
        public int compare(Point p1, Point p2) {
            double d1 = getDistanceSquare(p1);
            double d2 = getDistanceSquare(p2);
            if(d1 < d2) {
                return 1;
            } else if (d1 > d2) {
                return -1;
            }
            return 0;
        }

        private double getDistanceSquare(Point p) {
            return Math.pow(p.getX() - now.getX(), 2) + Math.pow(p.getY() - now.getY(), 2) + Math.pow(p.getZ() - now.getZ(), 2);
        }
    }
}
