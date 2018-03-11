package mas;

import java.io.Serializable;

/**
 *
 * @author alexey
 */
public class Corridor  implements Serializable{
    private int researchState; //0 - не исследован, 1 -робот едет его исследовать, 2 - исследуется, 3 исследован
    private int robotID = -1; //Номер робота, исследовавшего коридор
    private int[] startCoordinates = new int[2];

    private int startN; //Вершина, к которой примыкает коридор в начале
    private int finishN; //Вершина, к которой примыкает коридор в конце

    private double angle;
    private double length;

    //Конструктор
    public Corridor(int[] startCoord, int[] finishCoord, int start, int finish) {
        startCoordinates = startCoord;
        setFinishCoord(finishCoord[0], finishCoord[1]);
        startN = start;
        finishN = finish;
    }

    public Corridor(int[] startCoord, double Angle, double Length, int start, int finish) {
        startCoordinates = startCoord;
        angle = Angle;
        length = Length;
        startN = start;
        finishN = finish;
    }

    //Задание новых координат конца - вычисление новых угла и длины
    public final void setFinishCoord(int X, int Y) {
        //Координаты вектора-коридора
        int y = Y - startCoordinates[1];
        int x = X - startCoordinates[0];
        angle = Math.atan2(x, y);   //Получение угла коридора
        angle = Math.toDegrees(angle); //Преобразование в градусы
        if (angle < 0) {   //Угол измерятеся в пределах от 0 до 360
            angle += 360;
        }
        int y2 = y * y;
        int x2 = x * x;
        length = Math.sqrt(x2 + y2);
    }

    //Получение координат начала

    public int getXstart() {
        return startCoordinates[0];
    }

    public int getYstart() {
        return startCoordinates[1];
    }

    //Получение координат конца
    public int getXfinish() {
        double xFin = startCoordinates[0] + length * Math.sin(Math.toRadians(angle));
        return (int) xFin;
    }

    public int getYfinish() {
        double yFin = startCoordinates[1] + length * Math.cos(Math.toRadians(angle));
        return (int) yFin;
    }

    //Получить длину данного коридора
    public double getLength() {
        return length;
    }

    //Получить угол 
    public double getStartAngle() {
        return angle;
    }

    public double getFinishAngle() {
        double fAngle = angle + 180;
        if (fAngle >= 360) {   //Угол измерятеся в пределах от 0 до 359
            fAngle -= 360;
        }
        return fAngle;
    }

    //Определяется, относится данный угол к началу или концу коридора
    public boolean startOrFinish(double angle) throws NoSuchFieldException {
        if (Math.abs(angle - getStartAngle()) <= 3) {
            return true;
        }
        if (Math.abs(angle - getFinishAngle()) <= 3) {
            return false;
        }
        throw new NoSuchFieldException("Angle not found");//Угол вообще не относится к коридору
    }

    //Установить, исследован ли коридор 0 - не исследован, 1 -робот едет его исследовать, 2 - исследуется, 3 исследован
    public void setResearchState(int i) {
        if (i < 0 || i > 3) {
            return;
        }
        researchState = i;
    }

    public int getStartPoint() {
        return startN;
    }

    public int getFinishPoint() {
        return finishN;
    }
//Узнать, исследован ли коридор 0 - не исследован, 1 - исследуется, 2 - исследован

    public int getResearchState() {
        return researchState;
    }

    //Задать номер вершины, к которой примыкает коридор
    public void setFinishPoint(int point) {
        finishN = point;
    }

    public void setStartPoint(int point) {
        startN = point;
    }

    public void setRobotID(int id) {
        robotID = id;
    }

    public int getRobotID() {
        return robotID;
    }
}
