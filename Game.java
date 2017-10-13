import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/**
 * An example for javadoc.
 * @author qiyangh
 */
public class Game implements ActionListener {
    private JButton start;
    private JTextArea time;
    private JTextArea report;
    private JButton[] hole;
    private Mhole[] cthole = new Mhole[16];
    private int rptscore = 0;
    private long origin;
 
    public Game() {
    //UI for info
        JFrame frame = new JFrame("Whack-a-mole Game");
        frame.setSize(350, 350);
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel interaction = new JPanel();
        interaction.setPreferredSize(new Dimension(350, 70));
        start = new JButton("Start");
        JLabel timeleft = new JLabel("Time left:");
        time = new JTextArea(String.valueOf(0), 1, 5);
        JLabel score = new JLabel("Score:");
        report = new JTextArea(1, 5);
        report.setText(String.valueOf(rptscore));
        start.addActionListener(this);
        interaction.add(start);
        interaction.add(timeleft);
        interaction.add(time);
        interaction.add(score);
        interaction.add(report);
        //UI for mole
        JPanel mole = new JPanel();
        mole.setPreferredSize(new Dimension(350, 300));
        hole = new JButton[16];
        for (int i = 0; i < 16; i++) {
            hole[i] = new JButton("-");
            hole[i].setBackground(Color.GRAY);
            hole[i].setOpaque(true);
            hole[i].addActionListener(this);
            mole.add(hole[i]);
        }
        pane.add(interaction);
        pane.add(mole);
        frame.setContentPane(pane);
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            for (int i = 0; i < 16; i++) {
                cthole[i] = new Mhole("-", Color.GRAY, hole[i], 20);
                cthole[i].setPriority(4);
                hole[i].setEnabled(true);
            }
            time.setEditable(false);
            report.setEditable(false);
            synchronized (report.getText()) {
                rptscore = 0;
                report.setText(String.valueOf(rptscore));
            synchronized (time.getText()) {
                time.setText(String.valueOf(20));
                Timer onclick = new Timer(20, time, start);
                onclick.setPriority(9);
                origin = System.currentTimeMillis();
                onclick.start();
                for (int i = 0; i < 16; i++) {
                    cthole[i].start();
                }
            }
            }
        } else {
            JButton a = (JButton) e.getSource();
            synchronized (report.getText()) {
            synchronized (a) {
                synchronized (time.getText()) {
                    if ((!(time.getText().equals("0"))) && a.getText().equals("0_0")) {
                        a.setText("ouch!");
                        a.setBackground(Color.RED);
                        rptscore += 1;
                        report.setText(String.valueOf(rptscore));
                    }
                }
            }
            }
        }
    }
    /**
    * class mhole.
    */
    private class Mhole extends Thread {
        private String condition;
        private Color mycolor;
        private JButton button;
        private int sec;
        private Random random = new Random();
        
        Mhole(String a, Color b, JButton c, int d) {
            condition = a;
            mycolor = b;
            button = c;
            sec = d;
        }
        @Override
        public void run() {
            try {
                synchronized (time.getText()) {
                    button.setText(condition);
                    button.setBackground(mycolor);
                        while (!(time.getText().equals("0"))) {
                            int bfsleep = random.nextInt(sec) * 1000;
                            System.out.println("waiting: " + bfsleep);
                            Thread.sleep(bfsleep);
                            if (!(time.getText().equals("0"))) {
                                button.setText("0_0");
                                button.setBackground(Color.GREEN);
                                int upsleep = 1000 * (random.nextInt(4) + 1);
                                System.out.println("lasting" + upsleep);
                                int remain = 1000 * (20 - retsec(origin));
                                if (upsleep > remain) {
                                    upsleep = remain;
                                }
                                Thread.sleep(upsleep);
                            }
                            if (!(time.getText().equals("0"))) {
                                button.setText(condition);
                                button.setBackground(mycolor);
                                int finalsleep = 2000;
                                int fnremain = 1000 * (20 - retsec(origin));
                                if (finalsleep > fnremain) {
                                    finalsleep = fnremain;
                                }
                                Thread.sleep(finalsleep);
                            }
                        }
                    button.setText("-");
                        button.setBackground(Color.GRAY);
                }
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
    }
    /**
    * class timer.
    */
    private class Timer extends Thread {
        private boolean going = true;
        private int sec;
        private JTextArea show;
        private JButton control;
        
        Timer(int tt, JTextArea a, JButton b) {
            sec = tt;
            show = a;
            control = b;
        }
        @Override
        public void run() {
            try {
                synchronized (time.getText()) {
                control.setEnabled(false);
                int timesleep = 1000;
                while (going) {
                    show.setText(String.valueOf(sec));
                        if (sec == 0) {
                            going = false;
                            for (int i = 0; i < 16; i++) {
                                hole[i].setText("-");
                                hole[i].setBackground(Color.GRAY);
                            }
                            Thread.sleep(4000);
                            break;
                        }
                        sec -= 1;
                        Thread.sleep(timesleep);
                }
                }
                    control.setEnabled(true);
                    for (int i = 0; i < 16; i++) {
                        hole[i].setEnabled(true);
                    }
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
            }
    }

    public int retsec(long originl) {
        return (int) ((System.currentTimeMillis() - originl) / 1000);
    }

    public static void main(String[] args) {
        new Game();
    }
}
