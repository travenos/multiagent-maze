package mas;

import java.util.ArrayList;

/**
 *
 * @author alexey
 */
public class Waybill {

    private final ArrayList<Integer> vertex = new <Integer>ArrayList();//Номер вершины
    private final ArrayList<Double> distance = new <Double>ArrayList();//Кратчайшее расстояние
    private final ArrayList<Boolean> checked = new <Boolean>ArrayList();//Помечена ли вершина
    private final ArrayList<ArrayList<Integer>> waypoints = new <ArrayList<Integer>>ArrayList();//Путь к вершине

    public int size() {
        return vertex.size();
    }

    //Добавить исследуемую вершину
    public void add(int vert, double dist, int gotFrom) {
        ArrayList way;
        way = getWay(gotFrom);
        if (way == null) {
            way = new <Integer>ArrayList();
        } else {
            way = (ArrayList<Integer>) way.clone();
        }
        way.add(gotFrom);
        waypoints.add(way);
        vertex.add(vert);
        distance.add(dist);
        checked.add(false);
    }

    //Получить путь к вершине v
    public ArrayList<Integer> getWay(int v) {
        int i = findVertex(v);
        if (i >= 0) {
            return (ArrayList<Integer>)waypoints.get(i).clone();
        } else {
            return null;
        }
    }

    //Получить расстояние до вершины v
    public double getDistance(int v) {
        int i = findVertex(v);
        if (i >= 0) {
            return distance.get(i);
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    //Помечена ли вершина v
    public boolean isChecked(int v) {
        int i = findVertex(v);
        if (i >= 0) {
            return checked.get(i);
        } else {
            return false;
        }
    }

    //Найти индекс вершины с номером v
    public int findVertex(int v) {
        for (int i = 0; i <= size() - 1; i++) {
            if (vertex.get(i) == v) {
                return i;
            }
        }
        return -1;
    }

    //Изменить дистанцию для вершины с индексом i
    public void changeDist(int v, double dist) {
        int i=findVertex(v);
        if (i<0)
            return;
        distance.set(i, dist);
    }

    //Изменить путь к вершине с индексом i
    public void changeWay(int v, ArrayList<Integer> way) {
        int i=findVertex(v);
        if (i<0)
            return;
        waypoints.set(i, way);
    }

    //Пометить i-ю вершину
    public void setChecked(int v) {
        int i=findVertex(v);
        if (i<0)
            return;
        checked.set(i, true);
    }
    
    //Поиск неотмеченной вершины с минимальным расстоянием
    public int findMinUnchecked(){
        double min=Double.POSITIVE_INFINITY;
        int minIndex=size()-1;
        for (int i=0;i<=size()-1;i++){
            if (!checked.get(i)){
                min=distance.get(i);
                minIndex=i;
                break;
            }
        }
        for (int i=minIndex+1;i<=size()-1;i++){
            if(!checked.get(i) && distance.get(i)<min){
                min=distance.get(i);
                minIndex=i;
            }
        }
        return vertex.get(minIndex);
    }
}
