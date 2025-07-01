package com.ixiaodao.template;


import com.alibaba.fastjson.JSON;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.ixiaodao.search.image.model.CoordBean;
import com.ixiaodao.search.image.utils.FindImgUtils;
import com.ixiaodao.search.image.utils.ToolsUtils;
import org.apache.commons.io.FileUtils;

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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 1
 * @author jinwenbiao
 * @since 2025/4/4 21:21
 */
public class BaoTou {
    private static final JFrame jFrame = new JFrame("登录");
    private static final JLabel versionLabel = new JLabel("1010 爆头个数：");
    private static final JLabel countLabel = new JLabel("0");
    private static final JButton clearButton = new JButton("清零");
    private static final JCheckBox CHECK_BOX = new JCheckBox("神行");

    private static final JLabel delayLabel = new JLabel("延时");
    private static final JTextField DELAY = new JTextField("50", 5);

    private static final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(1), new ThreadPoolExecutor.AbortPolicy());

    private static final Robot robot;

    public static BufferedImage btImg; // 窗口
    public static BufferedImage sdImg; // 窗口

    private static boolean flag = false;

    private static int count = 0;
    private static int delay = 50;

    private static final int left = KeyEvent.BUTTON1_MASK;
    private static final int S = KeyEvent.VK_S;
    private static final int E = KeyEvent.VK_E;
    private static final int W = KeyEvent.VK_W;
    private static final int _1 = KeyEvent.VK_1;
    private static final int _3 = KeyEvent.VK_3;
    private static final int x;
    private static final int y;
    private static final int width;
    private static final int height;

    private static final int sdWidth;
    private static final int sdHeight;
    private static final int sdX;
    private static final int sdY;

    private static final int wDelay;
    private static final int sDelay;
    private static final int eDelay;

    private static final boolean find;

    private static final NativeKeyListener nativeKeyListener = new NativeKeyListener() {
        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
            if (nativeEvent.getKeyCode() == 56) {
                flag = false;
                return;
            }
            if (nativeEvent.getKeyCode() == 3675) {
                if (flag) {
                    return;
                }
                executorService.submit(() -> {
                    flag = true;
                    boolean selected = CHECK_BOX.isSelected();
                    while (flag) {
                        if (selected) {
                            robot.keyPress(KeyEvent.VK_E);
                            ToolsUtils.sleep(25);
                            robot.keyRelease(KeyEvent.VK_E);

                            if (find) {
                                ToolsUtils.sleep(eDelay);

                                for (int i = 0; i < 100; i++) {
                                    CoordBean coordBean = FindImgUtils.searchImg(sdX, sdY, sdWidth, sdHeight, sdImg, 31);
                                    if (coordBean != null) {
                                        break;
                                    }
                                    ToolsUtils.sleep(1);
                                }
                            } else {
                                ToolsUtils.sleep(eDelay);
                            }

                            robot.keyPress(KeyEvent.VK_3);
                            ToolsUtils.sleep(22);
                            robot.keyPress(KeyEvent.VK_E);
                            ToolsUtils.sleep(22);

                            robot.mousePress(KeyEvent.BUTTON1_MASK);

                            robot.keyRelease(KeyEvent.VK_3);
                            ToolsUtils.sleep(22);
                            robot.keyRelease(KeyEvent.VK_E);
                            ToolsUtils.sleep(22);

                            for (int i = 0; i < 4; i++) {
                                robot.keyPress(W);
                                ToolsUtils.sleep(wDelay);
                                robot.keyRelease(W);
                                ToolsUtils.sleep(wDelay);

                                robot.keyPress(S);
                                ToolsUtils.sleep(sDelay);
                                robot.keyRelease(S);
                                ToolsUtils.sleep(sDelay);
                            }


                            robot.mouseRelease(KeyEvent.BUTTON1_MASK);
                        } else {
                            robot.keyPress(KeyEvent.VK_E);
                            ToolsUtils.sleep(22);
                            robot.keyRelease(KeyEvent.VK_E);

                            if (find) {
                                ToolsUtils.sleep(eDelay);
                                for (int i = 0; i < 100; i++) {
                                    CoordBean coordBean = FindImgUtils.searchImg(sdX, sdY, sdWidth, sdHeight, sdImg, 31);
                                    if (coordBean != null) {
                                        break;
                                    }
                                    ToolsUtils.sleep(1);
                                }
                            } else {
                                ToolsUtils.sleep(eDelay);
                            }

                            robot.keyPress(KeyEvent.VK_3);
                            ToolsUtils.sleep(22);
                            robot.keyPress(KeyEvent.VK_E);
                            ToolsUtils.sleep(22);

                            robot.mousePress(KeyEvent.BUTTON1_MASK);

                            robot.keyRelease(KeyEvent.VK_3);
                            ToolsUtils.sleep(22);
                            robot.keyRelease(KeyEvent.VK_E);
                            ToolsUtils.sleep(22);

                            ToolsUtils.sleep(890);
                            robot.mouseRelease(KeyEvent.BUTTON1_MASK);
                        }

                        ToolsUtils.sleep(1);
                        CoordBean coordBean = FindImgUtils.searchImg(x, y, width, height, btImg, 31);
                        if (coordBean != null) {
                            flag = false;
                            ToolsUtils.beep();
                            count++;
                            countLabel.setText(String.valueOf(count));
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

            String path = BaoTou.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath();
            path = URLDecoder.decode(path, StandardCharsets.UTF_8.name());
            String jarDir = new File(path).getParent();
            System.out.println("jarDir=" + jarDir);
            String s = FileUtils.readFileToString(new File(jarDir + File.separator + "config.txt"), StandardCharsets.UTF_8);
            s = s.replaceAll("//.*", "");

            LinkedHashMap<String, String> map = JSON.parseObject(s, LinkedHashMap.class);
            String btStart = map.get("btStart");
            String btEnd = map.get("btEnd");
            String sdStart = map.get("sdStart");
            String sdEnd = map.get("sdEnd");
            find = "是".equals(map.get("识图"));

            String[] btStartArr = btStart.replace("，", "").split(",");
            String[] btEndArr = btEnd.replace("，", "").split(",");

            x = Integer.parseInt(btStartArr[0]);
            y = Integer.parseInt(btStartArr[1]);
            width = Integer.parseInt(btEndArr[0]) - x;
            height = Integer.parseInt(btEndArr[1]) - y;

            String[] sdStartArr = sdStart.replace("，", "").split(",");
            String[] sdEndArr = sdEnd.replace("，", "").split(",");
            sdX = Integer.parseInt(sdStartArr[0]);
            sdY = Integer.parseInt(sdStartArr[1]);
            sdWidth = Integer.parseInt(sdEndArr[0]) - sdX;
            sdHeight = Integer.parseInt(sdEndArr[1]) - sdY;

            wDelay = Integer.parseInt(map.get("wDelay"));
            sDelay = Integer.parseInt(map.get("sDelay"));
            eDelay = Integer.parseInt(map.get("eDelay"));

            File btFile = new File(jarDir + File.separator + "爆头.png");
            File sdFile = new File(jarDir + File.separator + "sd.png");
            if (!btFile.exists() || !sdFile.exists()) {
                throw new RuntimeException("图片没找到");
            }
            btImg = ImageIO.read(btFile);
            sdImg = ImageIO.read(sdFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void init() {
        jFrame.setBounds(100, 670, 560, 320);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.add(versionLabel);
        panel.add(countLabel);
        panel.add(clearButton);
        panel.add(CHECK_BOX);
        panel.add(delayLabel);
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

        clearButton.addActionListener(e -> {
            count = 0;
            countLabel.setText("0");
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
