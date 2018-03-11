package mas;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

/**
 *
 * @author alexey
 */
public class Step extends TimerTask {

    private final int N; //Количество роботов
    private final Labyrinth labyrinth; //Реальная карта лабиринта
    private final Labyrinth knownWorld; //Карта построенная роботами
    private final SimRobot[] robots; //Роботы
    private final BufferedImage image1;
    private final BufferedImage currentImg1;
    private final BufferedImage image2;
    private final Canvas canvas1;
    private final Canvas canvas2;
    private final mainFrame window;
    private final long t0;

    //Конструктор
    public Step(int robotNumber, Labyrinth world, mainFrame parent, BufferedImage img) {
        super();
        window =parent;
        N = robotNumber;
        labyrinth = world;
        knownWorld = new Labyrinth();
        robots = new SimRobot[N];
        image1 = img;
        canvas1 = parent.getCanvas(1);
        canvas2 = parent.getCanvas(2);
        currentImg1 = new BufferedImage(canvas1.getWidth(), canvas1.getHeight(), BufferedImage.TYPE_INT_RGB);
        image2 = new BufferedImage(canvas1.getWidth(), canvas1.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i <= N - 1; i++) {
            robots[i] = new SimRobot(labyrinth, knownWorld, i, robots);
        }
        t0 = System.currentTimeMillis();
    }

    @Override
    //Действие по таймеру
    public void run() {
        currentImg1.getGraphics().drawImage(image1, 0, 0, canvas1);
        for (int i = 0; i <= N - 1; i++) {
            robots[i].getRobot().go();
            drawRobot(i);
        }
        drawMap();
        canvas1.getGraphics().drawImage(currentImg1, 0, 0, canvas1);
        canvas2.getGraphics().drawImage(image2, 0, 0, canvas2);
    }

    private void drawRobot(int i) {
        int x = robots[i].getX();
        int y = robots[i].getY();
        Graphics2D g = (Graphics2D) currentImg1.getGraphics();
        g.setColor(Color.blue);
        g.setStroke(new BasicStroke(3.0f));
        g.drawRect(x - 9, y - 9, 18, 18);
        Font font = new Font("Times New Roman", Font.BOLD, 23);
        g.setColor(Color.BLUE);
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString(String.valueOf(robots[i].getRobot().getID()), x - 5, y + 8);
    }

    private void drawMap() {
        image2.getGraphics().clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
        Graphics2D g = (Graphics2D) image2.getGraphics();
        g.setStroke(new BasicStroke(3.0f));
        int xs, ys, xf, yf;
        Corridor cor;
        boolean hasTasks = false;
        for (int i = 0; i <= knownWorld.size() - 1; i++) {
            cor = knownWorld.getCorridor(i);
            xs = cor.getXstart();
            ys = cor.getYstart();
            xf = cor.getXfinish();
            yf = cor.getYfinish();
            g.setColor(Color.GREEN);
            g.drawLine(xs, ys, xf, yf);
            g.setColor(Color.MAGENTA);
            Font font = new Font("Times New Roman", Font.PLAIN, 18);
            g.setFont(font);
            g.drawString(String.valueOf(cor.getStartPoint()), xs, ys);
            g.drawString(String.valueOf(cor.getFinishPoint()), xf, yf);
            font = new Font("Times New Roman", Font.BOLD, 12);
            g.setFont(font);
            switch (cor.getResearchState()) {
                case 0: //Коридор не исследован
                {
                    g.setColor(Color.red);
                    Double angle = cor.getStartAngle();
                    int x = xs + (int) (20 * Math.sin(Math.toRadians(angle)));
                    int y = ys + (int) (20 * Math.cos(Math.toRadians(angle)));
                    g.drawString("φ=" + String.valueOf(Math.round(angle)), x, y);
                    hasTasks = true;
                    break;
                }
                case 1: //Робот едет исследовать коридор
                {
                    g.setColor(Color.blue);
                    Double angle = cor.getStartAngle();
                    int x = xs + (int) (20 * Math.sin(Math.toRadians(angle)));
                    int y = ys + (int) (20 * Math.cos(Math.toRadians(angle)));
                    g.drawString("φ=" + String.valueOf(Math.round(angle)), x, y);
                    g.drawString("N=" + String.valueOf(cor.getRobotID()), x, y + 12);
                    hasTasks = true;
                    break;
                }
                case 2: //Робот исследует коридор
                {
                    g.setColor(Color.white);
                    Double angle = cor.getStartAngle();
                    int x = (int) ((xs + xf) / 2 + 20 * Math.cos(Math.toRadians(angle)));
                    int y = (int) ((ys + yf) / 2 + 20 * Math.sin(Math.toRadians(angle)));
                    g.drawString("φ=" + String.valueOf(Math.round(angle)), x, y);
                    g.drawString("L=" + String.valueOf(Math.round(cor.getLength())), x, y + 12);
                    g.drawString("N=" + String.valueOf(cor.getRobotID()), x, y + 24);
                    hasTasks = true;
                    break;
                }
                case 3: //Коридор исследован 
                {
                    g.setColor(Color.green);
                    Double angle = cor.getStartAngle();
                    int x = (int) ((xs + xf) / 2 + 20 * Math.cos(Math.toRadians(angle)));
                    int y = (int) ((ys + yf) / 2 + 20 * Math.sin(Math.toRadians(angle)));
                    g.drawString("φ=" + String.valueOf(Math.round(angle)), x, y);
                    g.drawString("L=" + String.valueOf(Math.round(cor.getLength())), x, y + 12);
                    break;
                }
            }
        }
        if (!hasTasks) { //Если исследование завершено
            long t = System.currentTimeMillis();
            double time = (t - t0) / 1000.0;
            double [] dist=new double[robots.length];
            for (int i=0;i<=robots.length-1;i++){
                dist[i]=robots[i].getCounter();
            }
            window.nextRobot(time,dist);
        }
    }
}
