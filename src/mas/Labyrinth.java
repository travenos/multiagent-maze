package mas;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author alexey
 */
public class Labyrinth implements Serializable{

    private final ArrayList<Corridor> corridors = new <Corridor>ArrayList();
    private int[] startPos = new int[2];

    public int size() {
        return corridors.size();
    }

    public boolean isEmpty() {
        return corridors.isEmpty();
    }

    public void setStartPos(int x, int y) {
        startPos[0] = x;
        startPos[1] = y;
    }

    public int[] getStartPos() {
        return startPos;
    }

    //Есть ли связь с другими коридорами
    public boolean hasConnection(int[] coord) {
        int i;
        int distX, distY;
        int rad = 7;
        distX = startPos[0] - coord[0];
        distY = startPos[1] - coord[1];
        if (distX * distX + distY * distY <= rad * rad) {
            coord[0] = startPos[0];
            coord[1] = startPos[1];
            return true;
        }
        for (i = 0; i <= size() - 1; i++) {
            distX = getCorridor(i).getXfinish() - coord[0];
            distY = getCorridor(i).getYfinish() - coord[1];
            if (distX * distX + distY * distY <= rad * rad) {
                coord[0] = getCorridor(i).getXfinish();
                coord[1] = getCorridor(i).getYfinish();
                return true;
            }
        }

        return false;
    }

    //Добавить коридор
    public void add(int xStart, int yStart, int xFinish, int yFinish) {
        //Создание связей между коридорами
        int[] startCoord = new int[2];
        startCoord[0] = xStart;
        startCoord[1] = yStart;
        int[] finishCoord = new int[2];
        finishCoord[0] = xFinish;
        finishCoord[1] = yFinish;
        int N = intercectNumber();
        int start = N;
        int finish = N;
        for (int i = 0; i <= size() - 1; i++) {
            int x = getCorridor(i).getXstart();
            int y = getCorridor(i).getYstart();
            if (Math.abs(x - xStart) <= 3 && Math.abs(y - yStart) <= 3) {
                start = getCorridor(i).getStartPoint();
            }
            if (Math.abs(x - xFinish) <= 3 && Math.abs(y - yFinish) <= 3) {
                finish = getCorridor(i).getStartPoint();
            }
            x = getCorridor(i).getXfinish();
            y = getCorridor(i).getYfinish();
            if (Math.abs(x - xStart) <= 3 && Math.abs(y - yStart) <= 3) {
                start = getCorridor(i).getFinishPoint();
            }
            if (Math.abs(x - xFinish) <= 3 && Math.abs(y - yFinish) <= 3) {
                finish = getCorridor(i).getFinishPoint();
            }
        }
        if (start == finish && finish == N) {
            finish++;
        }
        Corridor cor = new Corridor(startCoord, finishCoord, start, finish);
        corridors.add(cor);
    }

    public void add(int xStart, int yStart, double Angle, double Length) {
        //Создание связей между коридорами
        int[] startCoord = new int[2];
        startCoord[0] = xStart;
        startCoord[1] = yStart;
        int xFinish, yFinish;
        xFinish = xStart + (int) (Length * Math.sin(Math.toRadians(Angle)));
        yFinish = yStart + (int) (Length * Math.cos(Math.toRadians(Angle)));
        int N = intercectNumber();
        int start = N;
        int finish = N;
        for (int i = 0; i <= size() - 1; i++) {
            int x = getCorridor(i).getXstart();
            int y = getCorridor(i).getYstart();
            if (Math.abs(x - xStart) <= 3 && Math.abs(y - yStart) <= 3) {
                start = getCorridor(i).getStartPoint();
            }
            if (Math.abs(x - xFinish) <= 3 && Math.abs(y - yFinish) <= 3 && Length > 4) {
                finish = getCorridor(i).getStartPoint();
            }
            x = getCorridor(i).getXfinish();
            y = getCorridor(i).getYfinish();
            if (Math.abs(x - xStart) <= 3 && Math.abs(y - yStart) <= 3 && getCorridor(i).getLength() > 4) {
                start = getCorridor(i).getFinishPoint();
            }
            if (Math.abs(x - xFinish) <= 3 && Math.abs(y - yFinish) <= 3 && Length > 4) {
                finish = getCorridor(i).getFinishPoint();
            }
        }
        if (start == finish && finish == N) {
            finish++;
        }
        Corridor cor = new Corridor(startCoord, Angle, Length, start, finish);
        corridors.add(cor);
    }

    //Удалить последний коридор    
    public void removeLast() {
        int N = size() - 1;
        if (N >= 0) {
            if (N == 0) {
                startPos = new int[2];
            }
            corridors.remove((int) N);
        }
    }

    public int getFinishPoint(int x, int y, double angle) { //Координаты только для старта
        for (int i = 0; i <= size() - 1; i++) {
            if (Math.abs(getCorridor(i).getXstart() - x) <= 3 && Math.abs(getCorridor(i).getYstart() - y) <= 3) {
                if (Math.abs(getCorridor(i).getStartAngle() - angle) <= 3) {
                    return getCorridor(i).getFinishPoint();
                }
            }
        }
        return -1;
    }

    public void removeCorridor(int start, int finish) { //Координаты только для старта
        int i = connectsN(start, finish);
        if (i >= 0) {
            remove(i);
        }
    }

    private void remove(int i) {
        int finish = getCorridor(i).getFinishPoint();
        if (getCorridor(i).getResearchState() == 3) { //Исследованные коридоры уже удалить не выйдет
            return;
        }
        Corridor cor;
        for (int j = 0; j <= size() - 1; j++) {
            cor = getCorridor(j);
            // if (startcounter == 1) {
            if (cor.getStartPoint() > finish) {
                cor.setStartPoint(cor.getStartPoint() - 1);
            }
            if (cor.getFinishPoint() > finish) {
                cor.setFinishPoint(cor.getFinishPoint() - 1);
            }
        }
        //}
        corridors.remove(i);
    }

    public void removePoint(int p) {
        Corridor cor;
        for (int j = 0; j <= size() - 1; j++) {
            cor = getCorridor(j);
            if (cor.getStartPoint() > p) {
                cor.setStartPoint(cor.getStartPoint() - 1);
            }
            if (cor.getFinishPoint() > p) {
                cor.setFinishPoint(cor.getFinishPoint() - 1);
            }
        }
    }

    //Количество точек начала-окончания коридоров
    public int intercectNumber() {
        int max = -1;
        for (int i = 0; i <= size() - 1; i++) {
            if (getCorridor(i).getStartPoint() > max) {
                max = getCorridor(i).getStartPoint();
            }
            if (getCorridor(i).getFinishPoint() > max) {
                max = getCorridor(i).getFinishPoint();
            }
        }
        max++;
        return max;
    }

    //Получить i-й коридор
    public Corridor getCorridor(int i) {
        return corridors.get(i);
    }

    public int[] getStartCoord(int i) {
        int[] coord = new int[2];
        coord[0] = getCorridor(i).getXstart();
        coord[1] = getCorridor(i).getYstart();
        return coord;
    }

    public int[] getFinishCoord(int i) {
        int[] coord = new int[2];
        coord[0] = getCorridor(i).getXfinish();
        coord[1] = getCorridor(i).getYfinish();
        return coord;
    }

    //Получить корридор, примыкающего к точке (x,y) под углом angle
    public Corridor getCorridor(int x, int y, double angle) {
        Corridor result = null;
        Corridor cor;
        for (int i = 0; i <= size() - 1; i++) {
            cor = getCorridor(i);
            if (Math.abs(cor.getXstart() - x) <= 3 && Math.abs(cor.getYstart() - y) <= 3) {
                if (Math.abs(cor.getStartAngle() - angle) <= 3) {
                    result = cor;
                    break;
                }
            }
            if (Math.abs(cor.getXfinish() - x) <= 3 && Math.abs(cor.getYfinish() - y) <= 3 && cor.getLength() > 4) {
                if (Math.abs(cor.getFinishAngle() - angle) <= 3) {
                    result = cor;
                    break;
                }
            }
        }
        return result;
    }

    //Получить углы всех корридоров, примыкающих к вершине
    public ArrayList<Double> getAngles(int x, int y) {
        ArrayList<Double> Angles = new <Double>ArrayList();
        for (int i = 0; i <= size() - 1; i++) {
            if (Math.abs(getCorridor(i).getXstart() - x) <= 3 && Math.abs(getCorridor(i).getYstart() - y) <= 3) {
                Angles.add(getCorridor(i).getStartAngle());
            }
            if (Math.abs(getCorridor(i).getXfinish() - x) <= 3 && Math.abs(getCorridor(i).getYfinish() - y) <= 3) {
                Angles.add(getCorridor(i).getFinishAngle());
            }
        }
        return Angles;
    }

    //Поиск наикратчайшего пути при помощи алгоритма Дейкстры
    public RobotTask findShortestWay(int from, int to) {
        double[][] graph = createGraph();
        int N = graph.length;
        Waybill bill = new Waybill();
        if (from < 0 || from > N || to < 0 || to > N) {  //Проверка корректности аргументов
            return null;
        }

        if (from == to) {
            RobotTask task = new RobotTask();
            return task;
        }

        int curV = from; //Вершина, в которой мы находимся в данный момент
        double curDist;
        bill.add(curV, 0, -1);

        boolean flag = false;
        for (int j = 0; j <= N - 1; j++) {
            curDist = bill.getDistance(curV);
            for (int i = 0; i <= N - 1; i++) {
                if (Double.isFinite(graph[curV][i])) { //Если к вершине путь существует
                    double length = curDist + graph[curV][i];
                    if (bill.findVertex(i) == -1) { //Вершины в списке нет, её нужно добавить
                        bill.add(i, length, curV);
                    } else { //Вершина уже есть
                        if (length < bill.getDistance(i)) { //Если есть более короткий путь
                            bill.changeDist(i, length);
                            ArrayList<Integer> way = bill.getWay(curV);
                            way.add(curV);
                            bill.changeWay(i, way);
                        }
                    }
                }
            }
            bill.setChecked(curV); //Помечаем вершину
            curV = bill.findMinUnchecked(); //Выбираем новую вершину с минимальным весом
            if (curV == to) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            System.out.print("ERROR!!! ");
            System.out.print(from);
            System.out.print(" ");
            System.out.println(to);
        }
        return createTask(bill, curV);
    }

    //Получить коридор, имеющий начало в start и конец в finish
    public Corridor connects(int start, int finish) {
        int i = connectsN(start, finish);
        if (i < 0) {
            return null; //Подходящий коридор не найден
        }
        return getCorridor(i);
    }

    private int connectsN(int start, int finish) {
        Corridor cor;
        for (int i = 0; i <= size() - 1; i++) {
            cor = getCorridor(i);
            if (cor.getStartPoint() == start && cor.getFinishPoint() == finish) {
                return i;
            }
        }
        return -1; //Подходящий коридор не найден
    }

    //Представление графа в виде матрицы смежности. Вершины - перекрёстки, дуги - коридоры
    private double[][] createGraph() { //Потом сделать private
        int N = intercectNumber();
        double[][] matrix = new double[N][N]; //Матрица смежности
        Corridor cor;
        for (int i = 0; i <= N - 1; i++) {
            for (int j = 0; j <= N - 1; j++) {
                cor = connects(i, j);
                if (cor != null) {
                    matrix[i][j] = cor.getLength();
                } else {
                    cor = connects(j, i);
                    if (cor != null) {
                        matrix[i][j] = cor.getLength();
                    } else {
                        matrix[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        }
        return matrix;
    }

    //Создать задание для робота
    private RobotTask createTask(Waybill bill, int v) {
        RobotTask task = new RobotTask();
        ArrayList<Integer> arr = (ArrayList<Integer>) bill.getWay(v).clone();
        arr.add(v);
        Corridor cor;
        boolean direction;
        for (int i = 1; i <= arr.size() - 2; i++) {

            cor = connects(arr.get(i), arr.get(i + 1));
            direction = true;
            if (cor == null) {
                cor = connects(arr.get(i + 1), arr.get(i));
                direction = false;
            }
            task.addTask(cor, direction);

        }
        return task;
    }

    //Получить номер вершины с координатами x, y
    public int getPoint(int x, int y) {
        for (int i = 0; i <= size() - 1; i++) {
            if (Math.abs(getCorridor(i).getXstart() - x) <= 3 && Math.abs(getCorridor(i).getYstart() - y) <= 3) {
                return getCorridor(i).getStartPoint();
            }
            if (Math.abs(getCorridor(i).getXfinish() - x) <= 3 && Math.abs(getCorridor(i).getYfinish() - y) <= 3) {
                return getCorridor(i).getFinishPoint();
            }
        }
        return -1;
    }
}
