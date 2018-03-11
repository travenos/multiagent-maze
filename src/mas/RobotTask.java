package mas;

import java.util.ArrayList;

/**
 *
 * @author alexey
 */
public class RobotTask {

    private double L = 0;//Суммарная длина коридоров
    private ArrayList<Corridor> corridors = new <Corridor>ArrayList();//Список коридоров, которые необходимо пройти
    private ArrayList<Boolean> directions = new <Boolean>ArrayList();//Список коридоров, которые необходимо пройти


    //Очистить задание
    public void clear() {
        L = 0;
        corridors = new <Corridor>ArrayList();
        directions = new <Boolean>ArrayList();
    }
    
    public int size(){
        return corridors.size();
    }

    //Имеется ли задание?
    public boolean isEmpty() {
        return corridors.isEmpty();
    }

    //Коридор, проходимый в данный момент
    public Corridor getCorridor() {
        return getCorridor(0);
    }

    //Коридор, который будеть проходиться i-м
    public Corridor getCorridor(int i) {
        return corridors.get(i);
    }
    
    //Направление прохождения i-го коридора
    public boolean getDirection() {
        return getDirection(0);
    }
    
    public boolean getDirection(int i) {
        return directions.get(i);
    }

    //Методы ниже - получить координаты и углы входа в коридор
    public int getX() {
        return getX(0);
    }

    public int getX(int i) {
        if (getDirection(i)){            
            return getCorridor(i).getXstart();
        }else{
            return getCorridor(i).getXfinish();
        }
    }

    public int getY() {
        return getY(0);
    }

    public int getY(int i) {
        if (getDirection(i)){            
            return getCorridor(i).getYstart();
        }else{
            return getCorridor(i).getYfinish();
        }
    }

    public double getAngle() {
        return getAngle(0);
    }

    public double getAngle(int i) {
        if (getDirection(i)){            
            return getCorridor(i).getStartAngle();
        }else{
            return getCorridor(i).getFinishAngle();
        }
    }
    
    public double getSumLength(){
        return L;
    }

    //Добавить новый коридор
    public void addTask(Corridor cor, boolean direction) {
        corridors.add(cor);
        directions.add(direction);
        L += cor.getLength();
    }

    //Удалить пройденный коридор
    public void delTask() {
        L -= getCorridor().getLength();
        directions.remove((int) 0);
        corridors.remove((int) 0);
    }

}
