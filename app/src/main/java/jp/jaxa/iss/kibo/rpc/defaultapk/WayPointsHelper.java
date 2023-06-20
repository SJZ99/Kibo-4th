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
    private static ArrayList<Point>[][] wayPoint = new ArrayList[7][7];
    private static Map<Integer, Quaternion> targetRotaion = new HashMap<>();

    static {
        wayPointInit();
        targetRotationInit();
        addWayPoint();
    }

    private static void wayPointInit() {
        for (int i = 0; i < 7; ++i) {
            for (int j = i + 1; j < 7; ++j) {
                wayPoint[i][j] = new ArrayList();
            }
        }
    }

    private static void targetRotationInit() {
        targetRotaion.put(0, new Quaternion(0, 0, -0.707f, 0.707f));
        targetRotaion.put(1, new Quaternion(0.5f, 0.5f, -0.5f, 0.5f));
        targetRotaion.put(2, new Quaternion(0, 0.707f, 0, 0.707f));
        targetRotaion.put(3, new Quaternion(0, 0, -1, 0));
        targetRotaion.put(4, new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f));
        targetRotaion.put(5, new Quaternion(0, 0, 0, 1));
        targetRotaion.put(6, new Quaternion(0, 0.707f, 0, 0.707f));
    }

    private static void addWayPoint() {
        // 1 <-> 2
        wayPoint[0][1].add(new Point(11.15, -9.78, 4.65));
        wayPoint[0][1].add(new Point(10.612 - 0.08, -9.15 - 0.115, 4.48 - 0.075));

        // 1 <-> 3
        wayPoint[0][2].add(new Point(11.08, -9.69, 4.75));
        wayPoint[0][2].add(new Point(10.71, -8.25, 4.75));
        wayPoint[0][2].add(new Point(10.71, -7.7 - 0.068, 4.48));

        // 1 <-> 4
        wayPoint[0][3].add(new Point(11.08, -9.69, 4.75));
        //wayPoint[0][3].add(new Point(10.71, -8.25, 4.75));
        wayPoint[0][3].add(new Point(10.51, -6.718 + 0.1, 5.1804 + 0.03));

        // 1 <-> 5
        wayPoint[0][4].add(new Point(11.114 - 0.07, -7.97 - 0.053, 5.33 - 0.1));

        // 1 <-> 6
        wayPoint[0][5].add(new Point(11.355, -8.993, 4.78));
    }

    public static Quaternion getTargetRotation(int targetPoint) {
        return targetRotaion.get(targetPoint);
    }

    public static ArrayList<Point> getWayPoint(int from, int to) {
        return wayPoint[from][to];
    }
}
