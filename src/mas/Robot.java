package mas;

import java.util.ArrayList;

/**
 *
 * @author alexey
 */
public class Robot {
    private final int[] coord = new int[2]; //Координаты робота в пространстве

    private RobotTask task = new RobotTask(); //Задача, выполняемая роботом

    private final SimRobot head;
    private final Labyrinth labyrinth;

    private final int id;
    private boolean canAsk = true;

    private int pos = 0; //Номер перекрёстка, на котором находится робот

    private final ArrayList<RobotTask> perspectiveTasks = new <RobotTask>ArrayList();
    private boolean lastCanKeep; //Подходит ли последнее перспективное задание

    public Robot(SimRobot host, Labyrinth knownWorld, int ID, int x, int y) {
        head = host;
        labyrinth = knownWorld;
        id = ID;
        coord[0] = x;
        coord[1] = y;
    }

    private void getTask() { //Получить задание из доступных
        perspectiveTasks.clear();
        Corridor cor;
        sendMsg("do AskMode false");
        for (int i = 0; i <= labyrinth.size() - 1; i++) {
            cor = labyrinth.getCorridor(i);
            if (cor.getResearchState() == 0) {
                perspectiveTasks.add(labyrinth.findShortestWay(pos, cor.getStartPoint()));
                lastCanKeep = true;
                askEveryone("DistanceTo " + String.valueOf(cor.getStartPoint()));

                //Когда будет тут по сети - сделать ожидание ответа в течение неск. мс
                if (!lastCanKeep) { //Если нашёлся робот, у которого данное задание ближе
                    perspectiveTasks.remove(perspectiveTasks.size() - 1);
                } else {
                    perspectiveTasks.get(perspectiveTasks.size() - 1).addTask(cor, true);
                }
            }
        }
        //Выбрать ближайшее из подходящих заданий
        if (perspectiveTasks.isEmpty()) {
            sendMsg("do AskMode true");
            return;
        }
        double min = perspectiveTasks.get(0).getSumLength();
        int minIndex = 0;
        for (int i = 1; i <= perspectiveTasks.size() - 1; i++) {
            if (perspectiveTasks.get(i).getSumLength() < min) {
                min = perspectiveTasks.get(i).getSumLength();
                minIndex = i;
            }
        }
        task = perspectiveTasks.get(minIndex);
        task.getCorridor(task.size() - 1).setResearchState(1);
        task.getCorridor(task.size() - 1).setRobotID(id);
        head.changeCorridor(task.getX(), task.getY(), task.getAngle());
        sendMsg("do AskMode true");
    }

    //3
    private void createTasks() { //Если обнаружены новые коридоры, создаём задания
        if (!task.isEmpty()) {//Проверка, прибыли ли в уже известную вершину
            for (int i = 0; i <= labyrinth.size() - 1; i++) {
                int[] fC = labyrinth.getFinishCoord(i);
                int[] sC = labyrinth.getStartCoord(i);
                if (Math.abs(fC[0] - coord[0]) <= 3 && Math.abs(fC[1] - coord[1]) <= 3) {
                    int foundPoint = labyrinth.getCorridor(i).getFinishPoint();
                    if (foundPoint != task.getCorridor().getFinishPoint()) {
                        gotToKnown(foundPoint);
                        return;
                    }
                }
                if (Math.abs(sC[0] - coord[0]) <= 3 && Math.abs(sC[1] - coord[1]) <= 3) {
                    int foundPoint = labyrinth.getCorridor(i).getStartPoint();
                    if (foundPoint != task.getCorridor().getStartPoint()) {
                        gotToKnown(foundPoint);
                        return;
                    }
                }
            }
        }
        //Прибыли в новую вершину
        ArrayList<Double> angles = head.availableAngles();
        for (int i = 0; i <= angles.size() - 1; i++) {
            if (labyrinth.getCorridor(coord[0], coord[1], angles.get(i)) == null) {
                //Коридор необходимо добавить
                labyrinth.add(coord[0], coord[1], angles.get(i), 4);
                labyrinth.getCorridor(coord[0], coord[1], angles.get(i)).setResearchState(0);
            }
        }
        task.clear();
    }

    private void move() {    //Движение робота
        head.calcNextPos();
        coord[0] = head.getX();
        coord[1] = head.getY();
        if (task.getCorridor().getResearchState() == 1) {
            task.getCorridor().setResearchState(2);
        }
        if (task.getCorridor().getResearchState() == 2) { //Если коридор исследуется
            task.getCorridor().setFinishCoord(coord[0], coord[1]); //Задать новое предполагаемое место конца
        }
        //isMoving=head.canContinue();
        if (!head.canContinue()) {
            if (task.getDirection()) {
                pos = task.getCorridor().getFinishPoint();
            } else {
                pos = task.getCorridor().getStartPoint();
            }
            if (task.size() > 1) { //Если робот просто едет дальше
                //Нужно поехать в следующий коридор                
                task.delTask();
                head.changeCorridor(task.getX(), task.getY(), task.getAngle());
            } else { //Завершено исследование коридора
                if (task.getCorridor().getResearchState() != 3) {
                    task.getCorridor().setResearchState(3);
                    createTasks();
                } else {
                    task.clear();
                }
                //ТУТ ЧТО-ТО нужно сделать!!!
            }
        }
    }

    public void go() {//Действия робота
        if (labyrinth.isEmpty()) {
            labyrinth.setStartPos(coord[0], coord[1]);
            createTasks();
        }
        if (task.isEmpty()) {
            getTask();
        }
        if (!task.isEmpty()) {
            move();
        }
    }

    //Прибыли в уже известную вершину
    private void gotToKnown(int foundPoint) {
        System.out.print("Got To Known point. ID=");
        System.out.println(id);
        int falsePoint1 = pos;
        int falsePoint2 = labyrinth.getFinishPoint(coord[0], coord[1], task.getCorridor().getFinishAngle());
        task.getCorridor().setFinishPoint(foundPoint);      
        //Доложить другим роботам о снатии задания
        String str;//str = "do RemoveTask " + String.valueOf(coord[0]) + " " + String.valueOf(coord[1]) + " " + String.valueOf(task.getCorridor().getFinishAngle());
        str = "do RemoveTask " + String.valueOf(foundPoint) + " " + String.valueOf(falsePoint1);
        str = str + " " + String.valueOf(falsePoint2) + " " + String.valueOf(task.getCorridor().getStartPoint());
        sendMsg(str);
        if (foundPoint > falsePoint1) {
            pos = foundPoint - 1;
        } else {
            pos = foundPoint;
        }
        labyrinth.removeCorridor(foundPoint, falsePoint2);
        if (falsePoint1 > falsePoint2) {
            falsePoint1--;
        }
        labyrinth.removePoint(falsePoint1);
        task.clear();
    }

    private void askEveryone(String ask) {
        if (!canAsk) {
            return;
        }
        String str = "ask ";
        str += String.valueOf(id);
        str += " ";
        str += ask;
        System.out.println(str);
        head.sendMessage(str);
    }

    private void sendMsg(String msg, int id) {
        head.sendMessage(msg, id);
    }

    private void sendMsg(String msg) {
        head.sendMessage(msg);
    }

    public void reciveInfo(String answer) {
        if (answer.startsWith("ans")) {
            getAnswer(answer);
            return;
        }
        if (answer.startsWith("do")) {
            doAction(answer);
            return;
        }
        if (answer.startsWith("ask")) {
            makeAnswer(answer);
            //return;
        }
    }

    //Получить вопрос от другого робота и ответить на него
    private void makeAnswer(String ask) {
        String[] parts;
        parts = ask.split(" ");
        int askerID;
        askerID = Integer.parseInt(parts[1]);
        if (parts[2].intern().equals("DistanceTo")) {
            if (!task.isEmpty()) {
                return;
            }
            int to = Integer.parseInt(parts[3]);
            double L = labyrinth.findShortestWay(pos, to).getSumLength();
            String str = "ans ";
            str += String.valueOf(askerID);
            str += " DistanceIs ";
            str += String.valueOf(L);
            sendMsg(str, askerID);
        }
    }

//Получить от другого робота ответ на вопрос
    private void getAnswer(String ans) {
        String[] parts;
        System.out.println(ans);
        parts = ans.split(" ");
        int answID;
        answID = Integer.parseInt(parts[1]);
        if (id != answID) {
            return;
        }
        if (parts[2].intern().equals("DistanceIs")) {
            double L = Double.parseDouble(parts[3]);
            if (!perspectiveTasks.isEmpty()) {
                if (L < perspectiveTasks.get(perspectiveTasks.size() - 1).getSumLength()) {
                    lastCanKeep = false;
                }
            }
        }
    }

    //Выполнить запрашиваемое другим роботом действие
    private void doAction(String act) {
        String[] parts;
        parts = act.split(" ");
        if (parts[1].intern().equals("AskMode")) {
            if (parts[2].intern().equals("true")) {
                canAsk = true;
            } else if (parts[2].intern().equals("false")) {
                canAsk = false;
            }
            return;
        } else if (parts[1].intern().equals("RemoveTask")) {
            int foundPoint = Integer.parseInt(parts[2]);
            int falsePoint1 = Integer.parseInt(parts[3]);
            int falsePoint2 = Integer.parseInt(parts[4]);
            int normPoint = Integer.parseInt(parts[5]);
            if (!task.isEmpty()) {
                Corridor cor = labyrinth.connects(foundPoint, falsePoint2);
                if (task.getCorridor(task.size() - 1).equals(cor)) {
                    if (task.size() == 1) {
                        Corridor cor2 = labyrinth.connects(normPoint, foundPoint);
                        task.clear();
                        task.addTask(cor2, false);
                        /*if (foundPoint > falsePoint1) {
                            pos=foundPoint-1;
                        } else {
                            pos = foundPoint;
                        }
                        return;*/
                    } else {
                        Corridor cor2 = task.getCorridor();
                        boolean direct = task.getDirection();
                        task.clear();
                        task.addTask(cor2, direct);
                    }
                }
            }
            if (pos > falsePoint2) {
                if (pos > falsePoint1) {
                    pos = pos - 2;
                } else {
                    pos--;
                }
                return;
            }
            if (pos > falsePoint1) {
                pos--;
            }
        }
    }

    public int getID() {
        return id;
    }

    public int getPos() {
        return pos;
    }
}
