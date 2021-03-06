package com.company;

import org.junit.Test;

import java.util.Random;

public class runMode {

    // 2維陣列表示 x, y  x:[0][], y[1][]
    private static int [][] randomRunCenter;
    private static int [][] randomRunLeft;
    private static int [][] randomRunRight;
    private static int [][] randomRunUp;
    private static int [][] randomRunDown;
    private static int [][][] randomRun;
    
    private static int [][] leftRun;
    private static int [][] rightRun;
    private static int [][] upRun;
    private static int [][] downRun;

    // 所有類型權重 {原地不動    正堂權重    額外權重}
    static {
         randomRunCenter = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1},
                 {0,0,   1,1,0,-1,-1,-1,0,1}};
         randomRunRight = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1,   1,1,1,1},
                 {0,0,   1,1,0,-1,-1,-1,0,1,   1,0,-1,0}};
         randomRunLeft = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1,   -1,-1,-1,-1},
                 {0,0,   1,1,0,-1,-1,-1,0,1,   -1,0,1,0}};
         randomRunUp = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1,   0,1,-1,0},
                 {0,0,   1,1,0,-1,-1,-1,0,1,   1,1,1,1}};
         randomRunDown = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1,   1,0,-1,0},
                 {0,0,   1,1,0,-1,-1,-1,0,1,   -1,-1,-1,-1}};
         randomRun = new int[][][] {
                 randomRunCenter, randomRunLeft, randomRunRight, randomRunUp, randomRunDown
         };
         rightRun = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1,   1,1,1, 1,1,1, 1,1,1, 1,1,1},
                 {0,0,   1,1,0,-1,-1,-1,0,1,   1,0,-1, 1,0,-1, 1,0,-1, 1,0,-1}};
         leftRun = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1,   -1,-1,-1, -1,-1,-1, -1,-1,-1, -1,-1,-1},
                 {0,0,   1,1,0,-1,-1,-1,0,1,   -1,0,1, -1,0,1, -1,0,1, -1,0,1}};
         upRun = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1,   0,1,-1, 0,1,-1, 0,1,-1, 0,1,-1},
                 {0,0,   1,1,0,-1,-1,-1,0,1,   1,1,1, 1,1,1, 1,1,1, 1,1,1}};
         downRun = new int[][] {
                 {0,0,   0,1,1,1,0,-1,-1,-1,   1,0,-1, 1,0,-1, 1,0,-1, 1,0,-1},
                 {0,0,   1,1,0,-1,-1,-1,0,1,   -1,-1,-1, -1,-1,-1, -1,-1,-1, -1,-1,-1}};
    }

    private int bound;
    private pair<Integer, Integer> XY;
    private Random random = new Random();

    public runMode (int bound){
        this.bound = bound;
    }

    @Test
    public void run(){
        mytest();
    }

    public  void mytest(){
        System.out.println(randomRun);
    }

    private void setXY(pair<Integer, Integer> XY){
        this.XY = XY;
    }
    private pair<Integer, Integer> getXY(){
        return this.XY;
    }

    public String chooseRANDOM(){
        return "RANDOM";
    }

    public String chooseLeft(){
        return "LEFT";
    }

    public String chooseRight(){
        return "RIGHT";
    }

    public String chooseUp(){
        return "UP";
    }

    public String chooseDown(){
        return "DOWN";
    }

    public String stop(){ return "STOP"; }

    public String chooseRunType(pair<Integer, Integer>start, pair<Integer, Integer>target){

        if (target.getFirst() < 0 && target.getSecond() < 0)
            return chooseRANDOM();

        if (target.getFirst() == 0 && target.getSecond() == 0)
            return stop();

        int differentX = abs(start.getFirst() - target.getFirst());
        int differentY = abs(start.getSecond() - target.getSecond());

        if (differentX >= differentY){
            // 目標點 在 現在點 左邊
            if (start.getFirst() > target.getFirst())
                return chooseLeft();

            // 目標點 在 現在點 右邊
            if (start.getFirst() < target.getFirst())
                return chooseRight();
        }
        else {
            // 目標點 在 現在點 上面
            if (start.getSecond() < target.getSecond())
                return chooseUp();

            // 目標點 在 現在點 下面
            if (start.getSecond() > target.getSecond())
                return chooseDown();
        }

        return "-------Error happened in ChooseRunType Function-------";
    }

    public int abs(int a){
        //如大於0則直接返回，小於0則再乘個負數將其轉成正數返回
        return a > 0 ? a : -a;
    }

    public pair<Integer, Integer> runSteps(int runStep, pair<Integer, Integer> XY, pair<Integer, Integer> target){

        String runType = chooseRunType(XY, target);
        setXY(XY);

        if (runType.equals("RANDOM")){
            for (int i = 0; i < runStep; i++) {
                randomRunOneStep();
                if (isArrivalTarget(XY, target)){
                    break;
                }
            }
        }
        if (runType.equals("LEFT")){
            for (int i = 0; i < runStep; i++) {
                leftRunOneStep();
                if (isArrivalTarget(XY, target)){
                    break;
                }
            }
        }
        if (runType.equals("RIGHT")){
            for (int i = 0; i < runStep; i++) {
                rightRunOneStep();
                if (isArrivalTarget(XY, target)){
                    break;
                }
            }
        }
        if (runType.equals("UP")){
            for (int i = 0; i < runStep; i++) {
                upRunOneStep();
                if (isArrivalTarget(XY, target)){
                    break;
                }
            }
        }
        if (runType.equals("DOWN")){
            for (int i = 0; i < runStep; i++) {
                downRunOneStep();
                if (isArrivalTarget(XY, target)){
                    break;
                }
            }
        }
        if (runType.equals("STOP")){
            // do nothing
        }

        return getXY();
    }

    // 判斷是否到達目的地，如果到達，則回傳true
    private boolean isArrivalTarget(pair<Integer, Integer> nowXY, pair<Integer, Integer> target){

        if (target.getFirst() == nowXY.getFirst() && target.getSecond() == nowXY.getSecond())
            return true;

        return false;
    }

    // 從 randomRun 中隨機取一個權重，去走一步
    private void randomRunOneStep(){

        int r = random.nextInt(randomRun.length);
        int range = randomRun[r][0].length;
        int nextX = XY.getFirst() + randomRun[r][0][random.nextInt(range)];
        int nextY = XY.getSecond() + randomRun[r][1][random.nextInt(range)];

        while (nextX < 0 || nextX > this.bound)
            nextX = XY.getFirst() + randomRun[r][0][random.nextInt(range)];

        while (nextY < 0 || nextY > this.bound)
            nextY = XY.getSecond() + randomRun[r][1][random.nextInt(range)];

        XY.setFirst(nextX);
        XY.setSecond(nextY);

        return;
    }

    // 從 leftRun 中隨機取一個權重，去走一步
    private void leftRunOneStep(){

        int range = leftRun[0].length;
        int nextX = XY.getFirst() + leftRun[0][random.nextInt(range)];
        int nextY = XY.getSecond() + leftRun[1][random.nextInt(range)];

        while (nextX < 0 || nextX > this.bound)
            nextX = XY.getFirst() + leftRun[0][random.nextInt(range)];
        while (nextY < 0 || nextY > this.bound)
            nextY = XY.getSecond() + leftRun[1][random.nextInt(range)];

        XY.setFirst(nextX);
        XY.setSecond(nextY);

        return;
    }

    // 從 rightRun 中隨機取一個權重，去走一步
    private void rightRunOneStep(){

        int range = rightRun[0].length;
        int nextX = XY.getFirst() + rightRun[0][random.nextInt(range)];
        int nextY = XY.getSecond() + rightRun[1][random.nextInt(range)];

        while (nextX < 0 || nextX > this.bound)
            nextX = XY.getFirst() + rightRun[0][random.nextInt(range)];
        while (nextY < 0 || nextY > this.bound)
            nextY = XY.getSecond() + rightRun[1][random.nextInt(range)];

        XY.setFirst(nextX);
        XY.setSecond(nextY);

        return;
    }

    // 從 upRun 中隨機取一個權重，去走一步
    private void upRunOneStep(){

        int range = upRun[0].length;
        int nextX = XY.getFirst() + upRun[0][random.nextInt(range)];
        int nextY = XY.getSecond() + upRun[1][random.nextInt(range)];

        while (nextX < 0 || nextX > this.bound)
            nextX = XY.getFirst() + upRun[0][random.nextInt(range)];
        while (nextY < 0 || nextY > this.bound)
            nextY = XY.getSecond() + upRun[1][random.nextInt(range)];

        XY.setFirst(nextX);
        XY.setSecond(nextY);

        return;
    }

    // 從 downRun 中隨機取一個權重，去走一步
    private void downRunOneStep(){

        int range = downRun[0].length;
        int nextX = XY.getFirst() + downRun[0][random.nextInt(range)];
        int nextY = XY.getSecond() + downRun[1][random.nextInt(range)];

        while (nextX < 0 || nextX > this.bound)
            nextX = XY.getFirst() + downRun[0][random.nextInt(range)];
        while (nextY < 0 || nextY > this.bound)
            nextY = XY.getSecond() + downRun[1][random.nextInt(range)];

        XY.setFirst(nextX);
        XY.setSecond(nextY);

        return;
    }

}


