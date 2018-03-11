package mas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFileChooser;
import java.awt.Canvas;
import javax.swing.JOptionPane;

/**
 *
 * @author alexey
 */
public class mainFrame extends javax.swing.JFrame {

    /**
     * Creates new form mainFrame
     */
    private boolean pressed = false;
    private int xStart = 0, yStart = 0;
    private final BufferedImage canvasImg;
    private final BufferedImage oldImg;
    private BufferedImage imag;
    private Labyrinth labyrinth = new Labyrinth();
    private Timer ourTimer = null;

    private int minRobots; //Минимальное количество роботов
    private int maxRobots; //Максимальное количество роботов
    private double[] times = null; //Время исследования лабиринта
    private int N; //Номер текущего шага

    private ChartWindow chartWindow = null;
    private boolean mode = false; //false - одиночный заезд
    
    private final int sleepTime = 60; //Задержка перед отрисовкой графиков

    public mainFrame() {
        initComponents();

        jTextField2.setVisible(false);
        jLabel2.setVisible(false);

        canvasImg = new BufferedImage(canvas1.getWidth(), canvas1.getHeight(), BufferedImage.TYPE_INT_RGB);
        canvasImg.createGraphics().setPaint(Color.WHITE);
        canvasImg.getGraphics().fillRect(0, 0, canvas1.getWidth(), canvas1.getHeight());
        imag = new BufferedImage(canvas1.getWidth(), canvas1.getHeight(), BufferedImage.TYPE_INT_RGB);
        imag.getGraphics().drawImage(canvasImg, 0, 0, this);
        oldImg = new BufferedImage(canvas1.getWidth(), canvas1.getHeight(), BufferedImage.TYPE_INT_RGB);
        oldImg.getGraphics().drawImage(canvasImg, 0, 0, this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvas2 = new java.awt.Canvas();
        startButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        canvas1 = new java.awt.Canvas();
        clearButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Лабиринт");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        startButton.setText("Старт");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Отмена действия");
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Стоп");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        jTextField1.setText("4");

        jLabel1.setText("Число роботов");

        canvas1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                canvas1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                canvas1MouseReleased(evt);
            }
        });
        canvas1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                canvas1MouseDragged(evt);
            }
        });

        clearButton.setText("Очистить");
        clearButton.setName(""); // NOI18N
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Сохранить");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        openButton.setText("Открыть");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        jTextField2.setText("5");

        jLabel2.setText("до");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Одно исследование", "Серия исследований" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 579, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(32, 32, 32)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(clearButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addGap(18, 18, 18)
                        .addComponent(openButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addGap(18, 18, 18)
                        .addComponent(stopButton))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(canvas2, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(41, 41, 41)
                    .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(486, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(canvas2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(cancelButton)
                    .addComponent(stopButton)
                    .addComponent(clearButton)
                    .addComponent(saveButton)
                    .addComponent(openButton)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jLabel1.getAccessibleContext().setAccessibleName("Число роботов");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Перемещение мыши
    private void canvas1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canvas1MouseDragged
        if (pressed) {
            imag.getGraphics().drawImage(canvasImg, 0, 0, this);
            Graphics2D g = (Graphics2D) imag.getGraphics();
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(3.0f));
            g.drawLine(xStart, yStart, evt.getX(), evt.getY());
            canvas1.getGraphics().drawImage(imag, 0, 0, this);
        }
        //g.drawLine();
    }//GEN-LAST:event_canvas1MouseDragged

    //Нажатие мышкой на полотно
    private void canvas1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canvas1MousePressed
        if (stopButton.isEnabled()) { //Нельзя рисовать коридор, когда едут роботы
            return;
        }
        int[] coord = new int[2];
        coord[0] = evt.getX();
        coord[1] = evt.getY();
        if (labyrinth.hasConnection(coord) || labyrinth.isEmpty()) {
            xStart = coord[0];
            yStart = coord[1];
            oldImg.getGraphics().drawImage(canvasImg, 0, 0, this);
            pressed = true;
            if (labyrinth.isEmpty()) {
                labyrinth.setStartPos(coord[0], coord[1]);
                drawStart();
            }
        }
    }//GEN-LAST:event_canvas1MousePressed

    //Отпустить мышку
    private void canvas1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_canvas1MouseReleased
        if (pressed) {
            pressed = false;
            int[] coord = new int[2];
            coord[0] = evt.getX();
            coord[1] = evt.getY();
            double length;
            int x = coord[0] - xStart;
            int y = coord[1] - yStart;
            length = Math.sqrt(x * x + y * y);
            if (length < 8) { //Не добавлять слишком короткие коридоры
                imag.getGraphics().drawImage(canvasImg, 0, 0, this);
                canvas1.getGraphics().drawImage(imag, 0, 0, this);
                return;
            }
            Graphics2D g = (Graphics2D) imag.getGraphics();
            g.setColor(Color.BLACK);
            if (labyrinth.hasConnection(coord)) {
                imag.getGraphics().drawImage(canvasImg, 0, 0, this);
                g.setStroke(new BasicStroke(3.0f));
                g.drawLine(xStart, yStart, coord[0], coord[1]);
            } else {
                g.setStroke(new BasicStroke(5.0f));
                g.drawOval(coord[0] - 4, coord[1] - 4, 8, 8);
            }
            canvas1.getGraphics().drawImage(imag, 0, 0, this);
            canvasImg.getGraphics().drawImage(imag, 0, 0, this);

            labyrinth.add(xStart, yStart, coord[0], coord[1]);

            cancelButton.setEnabled(true);

            System.out.print(coord[0]);
            System.out.print(" ");
            System.out.print(coord[1]);
            System.out.println();
            System.out.print(labyrinth.getCorridor(labyrinth.size() - 1).getXfinish());
            System.out.print(" ");
            System.out.println(labyrinth.getCorridor(labyrinth.size() - 1).getYfinish());
            System.out.println();
        }
    }//GEN-LAST:event_canvas1MouseReleased

    //Нарисовать стартовую позицию
    private void drawStart() {
        Graphics2D g = (Graphics2D) canvasImg.getGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5.0f));
        g.drawOval(xStart - 4, yStart - 4, 8, 8);
        canvas1.getGraphics().drawImage(canvasImg, 0, 0, this);
    }

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        //Начать движение роботов
        N = 0;
        try {
            minRobots = Integer.parseInt(jTextField1.getText());
            if (mode) {
                maxRobots = Integer.parseInt(jTextField2.getText());
                times = new double[maxRobots - minRobots + 1];
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString());
            return;
        }
        startRobots();
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        openButton.setEnabled(false);
        cancelButton.setEnabled(false);
        jComboBox1.setEnabled(false);
    }//GEN-LAST:event_startButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        canvas1.getGraphics().drawImage(oldImg, 0, 0, this);
        canvasImg.getGraphics().drawImage(oldImg, 0, 0, this);
        labyrinth.removeLast();
        cancelButton.setEnabled(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        if (ourTimer != null) {
            stopRobots();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            openButton.setEnabled(true);           
            jComboBox1.setEnabled(true); 
        }
    }//GEN-LAST:event_stopButtonActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    //Начать всё с начала
    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        if (ourTimer != null) {
            stopButtonActionPerformed(null);
        }
        canvasImg.createGraphics().setPaint(Color.WHITE);
        canvasImg.getGraphics().fillRect(0, 0, canvas1.getWidth(), canvas1.getHeight());
        imag = new BufferedImage(canvas1.getWidth(), canvas1.getHeight(), BufferedImage.TYPE_INT_RGB);
        imag.getGraphics().drawImage(canvasImg, 0, 0, this);
        oldImg.getGraphics().drawImage(canvasImg, 0, 0, this);
        labyrinth = new Labyrinth();
        canvas1.getGraphics().drawImage(canvasImg, 0, 0, this);
        canvas2.getGraphics().clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
    }//GEN-LAST:event_clearButtonActionPerformed

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        JFileChooser openDial = new JFileChooser();
        if (openDial.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = openDial.getSelectedFile();
            try {
                ObjectInputStream fout = new ObjectInputStream(new FileInputStream(file));
                labyrinth = (Labyrinth) fout.readObject();
                drawLabyrinth();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_openButtonActionPerformed

    private void drawLabyrinth() {
        canvasImg.createGraphics().setPaint(Color.WHITE);
        canvasImg.getGraphics().fillRect(0, 0, canvas1.getWidth(), canvas1.getHeight());
        Graphics2D g = (Graphics2D) canvasImg.getGraphics();

        //Рисуем стартовую позицию
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(5.0f));
        int x0 = labyrinth.getStartPos()[0];
        int y0 = labyrinth.getStartPos()[1];
        g.drawOval(x0 - 4, y0 - 4, 8, 8);

        //Рисуем коридоры
        int xs, ys, xf, yf;
        Corridor cor;
        for (int i = 0; i <= labyrinth.size() - 1; i++) {
            cor = labyrinth.getCorridor(i);
            xs = cor.getXstart();
            ys = cor.getYstart();
            xf = cor.getXfinish();
            yf = cor.getYfinish();
            g.setStroke(new BasicStroke(3.0f));
            g.setColor(Color.BLACK);
            g.drawLine(xs, ys, xf, yf);
            if (Math.abs(x0 - xf) > 3 || Math.abs(y0 - yf) > 3) {
                g.setStroke(new BasicStroke(5.0f));
                g.drawOval(xf - 4, yf - 4, 8, 8);
            }
        }
        canvas1.getGraphics().drawImage(canvasImg, 0, 0, this);

    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        JFileChooser saveDial = new JFileChooser();
        if (saveDial.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = saveDial.getSelectedFile();
            try {
                ObjectOutputStream fin = new ObjectOutputStream(new FileOutputStream(file));
                fin.writeObject(labyrinth);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getSelectedIndex() == 0) {
            mode = false;
            jLabel1.setText("Число роботов");
        } else {
            mode = true;
            jLabel1.setText("Число роботов от");
        }
        jTextField2.setVisible(mode);
        jLabel2.setVisible(mode);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    public Canvas getCanvas(int i) {
        switch (i) {
            case 1:
                return canvas1;
            case 2:
                return canvas2;
        }
        return null;
    }

    public void nextRobot(double time, double [] dist) { //Переход к запуску следующего коллектива
        if (mode) {
            times[N] = time;
            N++;
            if (minRobots + N > maxRobots) {
                stopButtonActionPerformed(null);
                drawTimeGraph();
                return;
            }
            stopRobots();
            startRobots();
        }else{
            stopButtonActionPerformed(null);  
            drawDistGraph(dist);
        }
    }

    //Нарисовать диаграмму времени прохождения различным составом
    private void drawTimeGraph() {
        if (chartWindow == null) {
            chartWindow = new ChartWindow(true);
        }else{
            chartWindow.setMode(true);
        }
        chartWindow.setVisible(true);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(this, e.toString());
        }
        chartWindow.draw(minRobots, times);
    }
    
    //Нарисовать диаграмму пройденного пути каждым роботом
    private void drawDistGraph(double [] dist) {
        if (chartWindow == null) {
            chartWindow = new ChartWindow(false);
        }else{
            chartWindow.setMode(false);
        }
        chartWindow.setVisible(true);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(this, e.toString());
        }
        chartWindow.draw(dist);
    }

    private void stopRobots() {
        if (ourTimer != null) {
            ourTimer.cancel();
            ourTimer.purge();
            ourTimer = null;
        }
    }

    private void startRobots() {
        ourTimer = new Timer();
        TimerTask toDo;
        toDo = new Step(minRobots + N, labyrinth, this, canvasImg);
        ourTimer.schedule(toDo, 40, 40);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private java.awt.Canvas canvas1;
    private java.awt.Canvas canvas2;
    private javax.swing.JButton clearButton;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton openButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
}
