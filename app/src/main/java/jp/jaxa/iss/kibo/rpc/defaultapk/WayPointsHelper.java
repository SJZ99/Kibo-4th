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

    static {
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
        // 0 <-> 1
        wayPoint[0][1].add(new Point(10.6, -9.9, 4.9));
        wayPoint[0][1].add(new Point(11.27 - 0.06, -9.92 - 0.05, 5.29 + 0.185));

        // 1 <-> 2
        wayPoint[1][2].add(new Point(11.15, -9.78, 4.65));
        wayPoint[1][2].add(new Point(10.612 - 0.155, -9.07 - 0.125, 4.48 - 0.075));

        // 1 <-> 3
        wayPoint[1][3].add(new Point(11.08, -9.69, 4.75));
        wayPoint[1][3].add(new Point(10.71, -8.25, 4.75));
        wayPoint[1][3].add(new Point(10.71, -7.7 - 0.068, 4.48));

        // 1 <-> 4
        wayPoint[1][4].add(new Point(11.08, -9.69, 4.75));
        wayPoint[1][4].add(new Point(10.51, -6.718 + 0.1, 5.1804 + 0.03));

        // 1 <-> 5
        wayPoint[1][5].add(new Point(11.114 - 0.07, -7.97 + 0.05, 5.33));

        // 1 <-> 6
        wayPoint[1][6].add(new Point(11.355, -8.993, 4.78));


        //----------------------------------------------------Judy go go go! ---------------------------------------------

        // 3<->4
        wayPoint[3][4].add(new Point(10.626533168828,-7.1936467429033,5.1804));
        wayPoint[3][4].add(new Point(10.51, -6.718 + 0.1, 5.1804 + 0.03));


        //3-5

        wayPoint[3][5].add(new Point(10.71, -7.7 - 0.068,  5.33));
        wayPoint[3][5].add(new Point(11.114 - 0.07, -7.97 + 0.05, 5.33));

        //3-6
       // wayPoint[3][6].add(new Point(10.7462457251825,-7.7407861997722,  4.78));
        wayPoint[3][6].add(new Point(10.6974195342895,-8.101652195976,  4.78));
        wayPoint[3][6].add(new Point(11.355, -8.993, 4.78));


        //3-7

        wayPoint[3][7].add(new Point(10.6974195342895,-8.101652195976,  4.78));
        wayPoint[3][7].add(new Point(11.3054897644974,-8.4952459219104, 4.78));
        wayPoint[3][7].add(new Point(11.355, -8.993, 4.78));



        //4-5
        wayPoint[4][5].add(new Point( 10.53, -6.76, 5.2));
        wayPoint[4][5].add(new Point(10.92,-7.57,5.3393));
        wayPoint[4][5].add(new Point(11.114, -7.9756, 5.3393));
    }


    public static Quaternion getTargetRotation(int targetPoint) {
        return targetRotation.get(targetPoint);
    }

    public static ArrayList<Point> getWayPoint(int from, int to) {
        return wayPoint[from][to];
    }
}
