package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;


public class YourService extends KiboRpcService {
    static final boolean isDebug = true;
    static final int LOOP_MAX = 5;
    static int currPoint = 0;
    static boolean hasScanned = false;
    static String message = "";

    /*************** move ***************/
    private boolean moveTo(Point p, Quaternion q) {
        int loopCounter = 0;
        Result result = null;
        do {
            result = api.moveTo(p, q, isDebug);
            ++loopCounter;
        } while(result != null && !result.hasSucceeded() && loopCounter < LOOP_MAX);
        return result != null && result.hasSucceeded();
    }

    private void move(int from, int to) {
        // same point
        if(from == to) return;

        // reverse or not
        boolean isReversed = false;
        Quaternion rotation = WayPointsHelper.getTargetRotation(to);
        if(from > to) {
            int temp = from;
            from = to;
            to = temp;
            isReversed = true;
        }

        ArrayList<Point> points = WayPointsHelper.getWayPoint(from, to);
        boolean isSuccess = false;
        if(isReversed) {
            for(int i = points.size() - 2; i >= 0; --i) {
                isSuccess = moveTo(points.get(i), rotation);
            }
        } else {
            for(int i = 1; i < points.size(); ++i) {
                isSuccess = moveTo(points.get(i), rotation);
            }
        }

        if(isSuccess) {
            currPoint = isReversed ? from : to;
            if(currPoint != 7 && currPoint != 8) {
                api.laserControl(true);
                api.takeTargetSnapshot(currPoint);
            }
        }
    }
    /*************** move end ***************/

    private void qrCodeMission() {
//        long qrTime = api.getTimeRemaining().get(1);
        move(currPoint, 7);
//        qrTime -= api.getTimeRemaining().get(1);
//        Log.i("TTime", "" + qrTime);


        Bitmap bitmap = api.getBitmapNavCam();
        message = QrCodeHelper.scan(bitmap);
//        log("Qrcode 1 finished");
        // if failed, try again with consuming more time
        if(message.equals("")) {
            moveTo(WayPointsHelper.getPoint(7), WayPointsHelper.getTargetRotation(7));
            message = QrCodeHelper.deepScan(bitmap);
//            log("Qrcode 2 finished");
        }
//        log("Qrcode finished with result: " + message);
    }

    private void processString() {
        if(message.equals("JEM")) {
            message = "STAY_AT_JEM";
        } else if(message.equals("COLUMBUS")) {
            message ="GO_TO_COLUMBUS";
        } else if(message.equals("RACK1")) {
            message = "CHECK_RACK_1";
        } else if(message.equals("ASTROBEE")) {
            message = "I_AM_HERE";
        } else if(message.equals("INTBALL")) {
            message = "LOOKING_FORWARD_TO_SEE_YOU";
        } else {
            message = "NO_PROBLEM";
        }
    }

    public void log(String message) {
        Log.i("Main", message);
    }

    @Override
    protected void runPlan1(){
        api.startMission();

        while(api.getTimeRemaining().get(1) > 19000 + PathLengthHelper.getTime(currPoint, 8)) {
//            log("while loop");
            List<Integer> activatedTargets = api.getActiveTargets();

//            log("list size: " + activatedTargets.size());

            // single activated target
            if(activatedTargets.size() == 1) {
                int targetPoint = activatedTargets.get(0);
//                log("Single point: " + targetPoint);
                float time = PathLengthHelper.getTime(currPoint, targetPoint);
                float toGoal = time + PathLengthHelper.getTime(targetPoint, 8);
                float totalRemaining = api.getTimeRemaining().get(1), roundRemaining = api.getTimeRemaining().get(0);

//                log("time, remaining: " + time + ", " + roundRemaining);
//                log("toGoal, remaining: " + toGoal + ", " + totalRemaining);

                if (time < roundRemaining && toGoal < totalRemaining) {
//                    log("time check ok, go to: " + targetPoint);
                    move(currPoint, targetPoint);
                } else if(time > roundRemaining && roundRemaining > totalRemaining) {
                    break;
                } else if(toGoal > totalRemaining) {
                    break;
                } else {
                    // stand
                }

            } else { // two targets
//                log("Two points");
                int target1 = activatedTargets.get(0), target2 = activatedTargets.get(1);
                float time1 = PathLengthHelper.getTime(currPoint, target1), time2 = PathLengthHelper.getTime(currPoint, target2);
                float toGoal1 = time1 + PathLengthHelper.getTime(target1, 8), toGoal2 = time2 + PathLengthHelper.getTime(target2, 8);
                long remaining = api.getTimeRemaining().get(1), roundTime = api.getTimeRemaining().get(0);

                // if both targets can be deactivated within the total time, try to deactivate both targets
                // else try to deactivate one target or break
                if(toGoal1 < remaining && toGoal2 < remaining) {
                    // try to go to two points
                    boolean isPoint1 = true;
                    float bestTimeRound;
                    float bestTimeTotal;

                    if(time1 + PathLengthHelper.getTime(target1, target2) + PathLengthHelper.getTime(target2, 8) >
                        time2 + PathLengthHelper.getTime(target2, target1) + PathLengthHelper.getTime(target1, 8)
                    ) {
                        isPoint1 = false;
                        bestTimeRound = time2 + PathLengthHelper.getTime(target2, target1);
                        bestTimeTotal = bestTimeRound + PathLengthHelper.getTime(target1, 8);
                    } else {
                        isPoint1 = true;
                        bestTimeRound = time1 + PathLengthHelper.getTime(target1, target2);
                        bestTimeTotal = bestTimeRound + PathLengthHelper.getTime(target2, 8);
                    }

                    // both two targets can be deactivated
                    if(bestTimeTotal < remaining) {
                        // if still a lot time (180 sec, for deactivate 3 target and p1 -> goal), ignore the time to goal,
                        // else it's close to end, following the minimum time to goal
                        if(remaining > 180000) {
                            if(time1 + PathLengthHelper.getTime(target1, target2) > time2 + PathLengthHelper.getTime(target2, target1)) {
                                isPoint1 = false;
                                bestTimeRound = time2 + PathLengthHelper.getTime(target2, target1);
                            } else {
                                isPoint1 = true;
                                bestTimeRound = time1 + PathLengthHelper.getTime(target1, target2);
                            }
                        }

                        // both round time and total time are enough
                        if(bestTimeRound < roundTime) {
//                            log("Choose both two points");
                            if(isPoint1) {
                                move(currPoint, target1);
                                move(currPoint, target2);
                            } else {
                                move(currPoint, target2);
                                move(currPoint, target1);
                            }
                        } else {

                            // only one target can be deactivated within the round time (or zero, then do nothing)
                            if(time1 > time2 && time2 < roundTime) {
//                                log("Two points, but choose one: " + target2);
                                move(currPoint, target2);
                            } else if(time1 < time2 && time1 < roundTime) {
//                                log("Two points, but choose one: " + target1);
                                move(currPoint, target1);
                            }
                        }

                    } else if(toGoal1 < remaining) {
                        if(time1 < roundTime) {
//                            log("Two points, but don't have enough time, only choose one: " + target1);
                            move(currPoint, target1);
                        }
                    } else if(toGoal2 < remaining) {
                        if(time2 < roundTime) {
//                            log("Two points, but don't have enough time, only choose one: " + target2);
                            move(currPoint, target2);
                        }
                    } else if(roundTime >= remaining) {
                        break;
                    }
                } else if(toGoal1 < remaining) {
                    if(time1 < roundTime) {
//                        log("Two points, but don't have enough time, only choose one: " + target1);
                        move(currPoint, target1);
                    }
                } else if(toGoal2 < remaining) {
                    if(time2 < roundTime) {
//                        log("Two points, but don't have enough time, only choose one: " + target2);
                        move(currPoint, target2);
                    }
                } else if(roundTime >= remaining) {
                    break;
                }
            }
        }

        if(PathLengthHelper.getTime(currPoint, 7) + PathLengthHelper.getTime(7, 8) < api.getTimeRemaining().get(1) + 5000) {
            qrCodeMission();
        }


//        log("Start go to goal when remaining: " + api.getTimeRemaining().get(1));
        api.notifyGoingToGoal();
        processString();

        if(api.getTimeRemaining().get(1) < PathLengthHelper.getTime(currPoint, 8)) {
            api.reportMissionCompletion(message);
            return;
        }

        move(currPoint, 8);
        api.reportMissionCompletion(message);
    }
}

