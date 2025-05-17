package com.ixiaodao.template;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.ixiaodao.search.image.model.CoordBean;
import com.ixiaodao.search.image.utils.FindImgUtils;
import com.ixiaodao.search.image.utils.ToolsUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1
 * @author jinwenbiao
 * @since 2025/4/4 21:21
 */
public class BaoTou {
    private static final JFrame jFrame = new JFrame("登录");
    private static final JLabel jLabel3 = new JLabel("1010-H------爆头个数：");
    private static final JLabel jLabel = new JLabel("0");
    private static final JButton clearButton = new JButton("清零");
    private static final JCheckBox CHECK_BOX = new JCheckBox("神行");

    private static final JLabel jLabel2 = new JLabel("延时");
    private static final JTextField DELAY = new JTextField("50", 5);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final Robot robot;

    public static BufferedImage read;

    private static boolean flag = false;

    private static int count = 0;
    private static int delay = 50;

    private static final int left = KeyEvent.BUTTON1_MASK;
    private static final int S = KeyEvent.VK_S;
    private static final int E = KeyEvent.VK_E;
    private static final int W = KeyEvent.VK_W;
    private static final int _1 = KeyEvent.VK_1;
    private static final int _3 = KeyEvent.VK_3;


    private static final NativeKeyListener nativeKeyListener = new NativeKeyListener() {
        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
            if (nativeEvent.getKeyCode() == 65) {
                flag = false;
                return;
            }
            if (nativeEvent.getKeyCode() == 66) {
                executorService.submit(() -> {
                    flag = true;
                    boolean selected = CHECK_BOX.isSelected();
                    while (flag) {
                        if (selected) {
                            robot.mousePress(left);
                            robot.delay(50);
                            robot.mouseRelease(left);

                            robot.keyPress(S);
                            robot.delay(55);
                            robot.keyRelease(S);

                            robot.keyPress(E);

                            robot.delay(delay);

                            robot.keyPress(_3);
                            robot.delay(30);
                            robot.keyRelease(_3);

                            robot.keyRelease(E);

                            robot.keyPress(E);
                            robot.delay(30);
                            robot.keyRelease(E);


                            robot.keyPress(W);
                            robot.delay(58);
                            robot.keyRelease(W);
                            robot.delay(1);

                            robot.delay(1);

                            robot.mousePress(left);
                            robot.keyPress(KeyEvent.VK_SHIFT);
                            robot.delay(11);
                            for (int i = 0; i < 4; i++) {
                                robot.keyPress(KeyEvent.VK_W);
                                robot.delay(55);
                                robot.keyRelease(KeyEvent.VK_W);
                                robot.delay(1);

                                robot.keyPress(KeyEvent.VK_S);
                                robot.delay(55);
                                robot.keyRelease(KeyEvent.VK_S);
                                robot.delay(1);

                                robot.keyPress(KeyEvent.VK_W);
                                robot.delay(55);
                                robot.keyRelease(KeyEvent.VK_W);
                                robot.delay(1);

                                robot.keyPress(KeyEvent.VK_S);
                                robot.delay(55);
                                robot.keyRelease(KeyEvent.VK_S);
                                robot.delay(1);
                            }
                            robot.keyRelease(KeyEvent.VK_SHIFT);
                            robot.delay(1);
                            robot.keyRelease(left);
                        } else {
                            robot.mousePress(KeyEvent.BUTTON1_MASK);
                            robot.keyPress(KeyEvent.VK_E);
                            robot.delay(30);
                            robot.keyRelease(KeyEvent.VK_E);

                            robot.mouseRelease(KeyEvent.BUTTON1_MASK);

                            robot.delay(delay);  // 打开商店延迟

                            robot.mousePress(KeyEvent.BUTTON1_MASK);

                            robot.keyPress(KeyEvent.VK_3);
                            robot.delay(30);
                            robot.keyPress(KeyEvent.VK_E);
                            robot.delay(30);
                            robot.mouseRelease(KeyEvent.BUTTON1_MASK);
                            robot.delay(30);

                            robot.keyRelease(KeyEvent.VK_3);
                            robot.delay(30);
                            robot.keyRelease(KeyEvent.VK_E);
                            robot.delay(30);

                            robot.mousePress(KeyEvent.BUTTON1_MASK);
                            robot.delay(770);
                            robot.mouseRelease(KeyEvent.BUTTON1_MASK);
                        }

                        robot.delay(1);
                        CoordBean coordBean = FindImgUtils.searchImg(14, 540, 70, 65, read, 31);
                        if (coordBean != null) {
                            ToolsUtils.beep();
                            count++;
                            jLabel.setText(String.valueOf(count));
                            break;
                        }
                    }
                });
            }
        }
    };

    static {
        try {
            robot = new Robot();

            File file = new File("爆头.png");
            if (file.exists()) {
                read = ImageIO.read(file);
            }
            file = new File("D:\\nzhan\\爆头.png");
            if (file.exists()) {
                read = ImageIO.read(file);
            }
            if (read == null) {
                throw new RuntimeException("111111111111111111");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void init() {
        jFrame.setBounds(100, 670, 560, 320);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT,20,20));  //水平和竖直间隙
        panel.add(jLabel3);
        panel.add(jLabel);
        panel.add(clearButton);
        panel.add(CHECK_BOX);
        panel.add(jLabel2);
        panel.add(DELAY);


        DELAY.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                delay = Integer.parseInt(DELAY.getText());
            }
        });

        clearButton.addActionListener(e->{
            count = 0;
            jLabel.setText("0");
        });


        jFrame.setContentPane(panel);
        jFrame.setVisible(true);

        jFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                GlobalScreen.addNativeKeyListener(nativeKeyListener);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                GlobalScreen.removeNativeKeyListener(nativeKeyListener);
                ToolsUtils.sleep(1);
                jFrame.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    public static void main(String[] argso) throws NativeHookException {
        init();
        GlobalScreen.registerNativeHook();
    }
}
