package mas;

import java.util.ArrayList;

/**
 *
 * @author alexey
 */
public class SimRobot {

    private final Robot robot; //сам робот
    //private int realCorNumber = 0; //номер коридора в реальном мире
    private Corridor realCor = null;
    private int place = 0; //Координата робота в коридоре
    private int[] coord = new int[2]; //координаты на полотне
    private boolean direction = true; //Направление движения робота в коридоре
    //true - от start к finish, false - наоборот
    private boolean canMove = false; //Есть ли у робота возможность продолжать движение в этом коридоре
    private final Labyrinth labyrinth;
    SimRobot[] allRobots;
    
    private int counter=0;

    public SimRobot(Labyrinth realWorld, Labyrinth knownWorld, int ID, SimRobot[] collective) {
        coord[0] = realWorld.getStartPos()[0];
        coord[1] = realWorld.getStartPos()[1];
        robot = new Robot(this, knownWorld, ID, coord[0], coord[1]);
        labyrinth = realWorld;
        allRobots = collective;
    }

    //Передать указатель на робота
    public Robot getRobot() {
        return robot;
    }

    public void changeCorridor(int x, int y, double angle) {
        Corridor oldCor = realCor;
        try {
            realCor = labyrinth.getCorridor(x, y, angle);
            direction = realCor.startOrFinish(angle);
            place = 0; //Координата робота в коридоре        
            canMove = true;
        } catch (NullPointerException ex) {
            System.out.print("Null poiter: id=");
            System.out.println(robot.getID());
            canMove = false;
            realCor = oldCor;
        }
        catch (NoSuchFieldException e) {
            System.out.println(e.getMessage());
            canMove = false;
            realCor = oldCor;
        }
    }

    //Вычислить новое положение робота
    public void calcNextPos() {
        place++;
        counter++;
        double length = realCor.getLength();
        int[] finishCoord = new int[2];
        int[] startCoord = new int[2];
        if (direction) {
            startCoord[0] = realCor.getXstart();
            startCoord[1] = realCor.getYstart();
            finishCoord[0] = realCor.getXfinish();
            finishCoord[1] = realCor.getYfinish();
        } else {
            startCoord[0] = realCor.getXfinish();
            startCoord[1] = realCor.getYfinish();
            finishCoord[0] = realCor.getXstart();
            finishCoord[1] = realCor.getYstart();
        }
        if (place >= length) {
            coord = finishCoord;
            canMove = false;
        } else {
            coord[0] = (int) (place / length * (finishCoord[0] - startCoord[0]) + startCoord[0]);
            coord[1] = (int) (place / length * (finishCoord[1] - startCoord[1]) + startCoord[1]);
        }
    }

    //Координата робота по оси X
    public int getX() {
        return coord[0];
    }

    //Координата робота по оси Y
    public int getY() {
        return coord[1];
    }

    public boolean canContinue() {
        return canMove;
    }
    
    public int getCounter(){
        return counter;
    }

    //Углы корридоров на данном перекрёстке
    public ArrayList<Double> availableAngles() {
        return labyrinth.getAngles(coord[0], coord[1]);
    }

    //Отправить сообщение всем роботам
    public void sendMessage(String msg) {
        for (int i = 0; i <= allRobots.length - 1; i++) {
            if (allRobots[i].getRobot().getID() != robot.getID()) {
                allRobots[i].getRobot().reciveInfo(msg);
            }
        }
    }

    //Отправить сообщение конкретному роботу

    public void sendMessage(String msg, int id) {
        for (int i = 0; i <= allRobots.length - 1; i++) {
            if (allRobots[i].getRobot().getID() == id) {
                allRobots[i].getRobot().reciveInfo(msg);
            }
        }
    }
}
