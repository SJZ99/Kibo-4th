package com.example.testalgorithm.obj;

import com.example.testalgorithm.PathFinding;

/**
 * Created by Jian Zhe Su on 7/11/2023.
 */

public class WayPoint implements Comparable<WayPoint> {
    // point id
    int id = 0;

    // time of whole path
    float time = 0;

    // points that can get when finish this path
    int points = 0;

    // bitmask to represent visited points, e.g., 0000 0001 mean point 0 have been assigned to this path
    short visited = 0;

    // previous way point
    WayPoint from = null;

    public WayPoint(int id, float time, int accumulatedPoints, short preVisited) {
        this.id = id;
        this.time = time;
        this.visited = preVisited;

        setVisited(id);

        switch (id) {
            case 1:
            case 5:
            case 6: {
                points = 30;
                break;
            }
            case 2:
            case 4:
            case 7: {
                points = 20;
                break;
            }
            case 3 : {
                points = 40;
                break;
            }
        }
        points += accumulatedPoints;
    }

    public int getId() {
        return id;
    }

    public float getTime() {
        return time;
    }

    public int getPoints() {
        return points;
    }

    public WayPoint getFrom() {
        return from;
    }

    public short getVisited() {
        return visited;
    }

    public boolean isVisited(int point) {
        return ((1 << point) & visited) > 0;
    }

    public int numberOfVisitedPoints() {
        return Integer.bitCount(visited);
    }

    public void setVisited(int point) {
        visited |= (1 << point);
    }

    public void setUnvisited(int point) {
        visited -= (1 << point);
    }

    /**
         * Compare by whether have qr code or not, points the path can get, and time the path need
         * @param wayPoint another way point
         * @return positive for this greater than other
         */
    @Override
    public int compareTo(WayPoint wayPoint) {

        int critical1 = 0, critical2 = 0;
        if(this.isVisited(7)) {
            critical1++;
        }
        if(this.isVisited(8)) {
            critical1++;
        }
        if(wayPoint.isVisited(7)) {
            critical2++;
        }
        if(wayPoint.isVisited(8)) {
            critical2++;
        }


        if(critical1 == critical2) {
            if(this.points == wayPoint.points) {
                // less time, higher priority
                PathFinding.log("time: (curr, new) " + this.time + " " + wayPoint.time);
                return (int) (wayPoint.time - this.time);
            } else {
                // higher points, higher priority
                return this.points - wayPoint.points;
            }
        } else {
            // if contain qr code, the path should have higher priority
            return critical1 - critical2;
        }
    }
}
