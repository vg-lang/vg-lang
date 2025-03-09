package components;


import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class MyGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Timer windowTimer;
    private List<Timer> activeTimers = new ArrayList<>();


    public MyGUI(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setFocusable(true);
        requestFocusInWindow();

        setWindowIcon("vg transparent.png");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                killAllTimers();
            }
        });

    }

    public void killAllTimers() {
        for (Timer timer : activeTimers) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
        }
        activeTimers.clear();
    }
    public void setWindowBackgroundImage(String imagePath) {
        try {

            ImageIcon backgroundIcon = new ImageIcon(imagePath);
            JLabel backgroundLabel = new JLabel(backgroundIcon);


            backgroundLabel.setBounds(0, 0, this.getWidth(), this.getHeight());


            this.setContentPane(backgroundLabel);


            this.setLayout(null);


            this.repaint();
            this.revalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeComponent(JComponent component) {
        remove(component);
        repaint();
    }

    public void setOnKeyPress(KeyListener listener) {
        addKeyListener(listener);
    }

    public void setOnKeyReleased(KeyListener listener) {
        addKeyListener(listener);
    }

    public void setOnKeyTyped(KeyListener listener) {
        addKeyListener(listener);
    }

    private Icon getCustomIcon(String iconName) {
        String assetsPath = System.getProperty("user.dir") + "\\assets\\" + iconName;
        try {
            ImageIcon imgIcon = new ImageIcon(assetsPath);
            if (imgIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = imgIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } else {
                System.out.println("Error loading custom icon: " + iconName);
            }
        } catch (Exception e) {
            System.out.println("Error loading custom icon: " + e.getMessage());
        }
        return null;
    }


    public void showInfoPopup(String message, String title) {
        Icon icon = getCustomIcon("info.png");
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }


    public void showWarningPopup(String message, String title) {
        Icon icon = getCustomIcon("warning.png");
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE, icon);
    }

    public void showErrorPopup(String message, String title) {
        Icon icon = getCustomIcon("error.png");
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE, icon);
    }


    public int showConfirmPopup(String message,String option, String title) {
        Icon icon = null;
        if (option.equals("YES_NO_OPTION")) {
            icon = getCustomIcon("confirm.png");
            return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        } else if (option.equals("YES_NO_CANCEL_OPTION")) {

            icon = getCustomIcon("confirm.png");
            return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        }
        else if(option.equals("OK_CANCEL_OPTION")){
            icon = getCustomIcon("confirm.png");
            return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        }
        else {
            icon = getCustomIcon("confirm.png");
            return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        }

    }

    public void launch() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public static Border createBorder(Color color, int thickness) {
        return new LineBorder(color, thickness);
    }

    public void setWindowBackgroundColor(Color color) {
        getContentPane().setBackground(color);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void dispose(Window w){
        w.dispose();
    }
    public void setWindowIcon(String iconName) {
        String appPath = System.getProperty("user.dir");
        String assetsPath = appPath + "/assets/"+iconName;

        try {
            ImageIcon icon = new ImageIcon(assetsPath);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                setIconImage(icon.getImage());
            } else {
                System.out.println("Error loading window icon: " + iconName);
            }
        } catch (Exception e) {
            System.out.println("Error setting window icon: " + e.getMessage());
        }
    }

    public void setWindowResizable(boolean resizable) {
        setResizable(resizable);
    }

    public void addComponentToWindow(Object window, Object component) {
        if (!(window instanceof MyGUI)) {
            throw new RuntimeException("Invalid window object.");
        }
        if (!(component instanceof JComponent)) {
            throw new RuntimeException("Invalid component object.");
        }

        JFrame frame = (JFrame) window;
        JComponent comp = (JComponent) component;

        frame.getContentPane().add(comp);
        System.out.println("Component added to window: " + comp.getClass().getName());


        frame.revalidate();
        frame.repaint();

    }

    public int getWindowComponentsCount() {
        return this.getWindowComponents().length;
    }

    public JComponent[] getWindowComponents() {
        Component[] components = this.getContentPane().getComponents();
        List<JComponent> jComponents = new ArrayList<>();
        for (Component comp : components) {
            if (comp instanceof JComponent) {
                jComponents.add((JComponent) comp);
            }
        }
        return jComponents.toArray(new JComponent[0]);
    }
    public JComponent getWindowComponentAt(int index) {
        JComponent[] components = this.getWindowComponents();
        if (index < 0 || index >= components.length) {
            throw new IndexOutOfBoundsException("Component index out of range.");
        }
        return components[index];
    }

    public static class MyTextField extends JTextField {
        private static final long serialVersionUID = 1L;

        public MyTextField(int x, int y, int width, int height) {
            super();
            setBounds(x, y, width, height);
            setColumns(10);
            setFont(new Font("Arial", Font.PLAIN, 14));
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals("Enter text here...")) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText("Enter text here...");
                        setForeground(Color.GRAY);
                    }
                }
            });

            setText("Enter text here...");
            setForeground(Color.GRAY);
        }

        public void setPlaceholder(String placeholder) {
            setText(placeholder);
            setForeground(Color.GRAY);
        }


        @Override
        public String getText() {
            return super.getText();
        }

        public void setFontFamily(String fontFamily) {
            setFont(new Font(fontFamily, getFont().getStyle(), getFont().getSize()));
        }

        public void setFontStyle(int style) {
            setFont(new Font(getFont().getName(), style, getFont().getSize()));
        }
    }
    public static class MyLabel extends JLabel {
        private static final long serialVersionUID = 1L;
        private Font customFont = null;

        public MyLabel(String text, int x, int y, int width, int height) {
            super(text);
            setBounds(x, y, width, height);
            setOpaque(false);
            setFont(new Font("Arial", Font.PLAIN, 14));
            setForeground(Color.BLACK);
        }

        public void setTextColor(Color color) {
            setForeground(color);
        }

        public void setLabelBorder(Color color, int thickness) {
            setBorder(createBorder(color, thickness));
        }

        public void setBackgroundColor(Color color) {
            setBackground(color);
        }

        @Override
        public void setText(String newText) {
            super.setText(newText);
        }
        @Override
        public String getText(){

            return super.getText();
        }
        public void setFontFamily(String fontFamily) {
            Font currentFont = getFont();
            Font newFont;

            try {

                File fontFile = new File("C:\\Users\\hodif\\Desktop\\Interpreter\\projects\\folderfortest\\" + fontFamily + ".ttf");
                if (fontFile.exists()) {

                    newFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                    newFont = newFont.deriveFont(currentFont.getStyle(), currentFont.getSize());
                    customFont = newFont;
                } else {

                    newFont = new Font(fontFamily, currentFont.getStyle(), currentFont.getSize());
                    customFont = newFont;
                    System.out.println("Font doesn't exist, using system font");
                }
                setFont(newFont);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();

                setFont(currentFont);
                System.out.println("Loading of font failed");
            }
        }


        public void setFontSize(int size) {
            Font currentFont = getFont();
            if (customFont != null) {

                setFont(customFont.deriveFont(currentFont.getStyle(), size));
            } else {

                setFont(new Font(currentFont.getFamily(), currentFont.getStyle(), size));
            }
        }



    }
    public static class MyPane extends JPanel {
        private static final long serialVersionUID = 1L;
        private JLabel backgroundLabel;


        public MyPane(int x, int y, int width, int height) {
            super();
            setBounds(x, y, width, height);
            setLayout(null);
            setBackground(Color.WHITE);
        }


        public void setPaneColor(Color color) {
            setBackground(color);
        }
        public void setPaneBackgroundImage(String imagePath) {
            try {
                if (backgroundLabel != null) {
                    this.remove(backgroundLabel);
                }


                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledImage = icon.getImage().getScaledInstance(
                        this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
                backgroundLabel = new JLabel(new ImageIcon(scaledImage));


                backgroundLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
                this.add(backgroundLabel);
                this.repaint();
            } catch (Exception e) {
                throw new RuntimeException("Failed to set pane background image: " + e.getMessage());
            }
        }
        public void setPaneBorder(Color color, int thickness) {
            setBorder(createBorder(color, thickness));
        }


    }
    public static class MyAnimator {
        private Timer timer;
        private JComponent component;
        private int targetX, targetY;
        private int deltaX, deltaY;
        private int duration;
        private long startTime;
        private Timer animationTimer;
        private MyImage sprite;
        private int[] frames;
        private int currentFrameIndex = 0;
        private int tileWidth, tileHeight, cols;

        public MyAnimator(JComponent component, int targetX, int targetY, int duration) {
            this.component = component;
            this.targetX = targetX;
            this.targetY = targetY;
            this.duration = duration;
        }

        public MyAnimator(MyImage sprite, int tileWidth, int tileHeight, int cols, int[] frames, int duration) {
            this.sprite = sprite;
            this.tileWidth = tileWidth;
            this.tileHeight = tileHeight;
            this.cols = cols;
            this.frames = frames;

            animationTimer = new Timer(duration / frames.length, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int frame = frames[currentFrameIndex];
                    int row = frame / cols;
                    int col = frame % cols;

                    sprite.setTile(row, col, tileWidth, tileHeight);
                    currentFrameIndex = (currentFrameIndex + 1) % frames.length;
                }
            });
        }

        public void start() {
            if (animationTimer != null) {
                animationTimer.start();
            } else {
                int startX = component.getX();
                int startY = component.getY();
                deltaX = targetX - startX;
                deltaY = targetY - startY;
                startTime = System.currentTimeMillis();

                timer = new Timer(16, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        long elapsed = System.currentTimeMillis() - startTime;
                        double progress = (double) elapsed / duration;

                        if (progress >= 1.0) {
                            progress = 1.0;
                            timer.stop();
                        }

                        int newX = startX + (int) (deltaX * progress);
                        int newY = startY + (int) (deltaY * progress);
                        component.setLocation(newX, newY);
                    }
                });
                timer.start();
            }
        }

        public void stop() {
            if (animationTimer != null) {
                animationTimer.stop();
            } else {
                timer.stop();
            }
        }
    }
    public static class MyImage extends JLabel {
        private static final long serialVersionUID = 1L;
        private String imagePath;
        private BufferedImage spritesheet;

        public MyImage(String imagePath, int x, int y, int width, int height) {
            super();
            this.imagePath = imagePath;
            setBounds(x, y, width, height);
            loadImage();
        }



        public void setImage(Object imageSource) {
            try {
                if (imageSource == null) {
                    throw new IllegalArgumentException("Image source cannot be null.");
                }

                if (imageSource instanceof String) {

                    this.imagePath = (String) imageSource;
                    loadImage();
                } else if (imageSource instanceof BufferedImage) {

                    BufferedImage bufferedImage = (BufferedImage) imageSource;
                    ImageIcon icon = new ImageIcon(bufferedImage);
                    setIcon(icon);
                } else {
                    throw new IllegalArgumentException(
                            "Unsupported image source type: " + imageSource.getClass().getName()
                    );
                }
            } catch (Exception e) {
                System.err.println("Error setting image: " + e.getMessage());
                e.printStackTrace();
            }
        }



        public void resizeImage(int width, int height) {
            setSize(width, height);
            loadImage();
        }


        public void moveImage(int x, int y) {
            setLocation(x, y);
        }


        private void loadImage() {
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                if (isGIF(imagePath)) {
                    setIcon(icon);
                } else {
                    Image img = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(img));
                }
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
            }
        }


        private boolean isGIF(String imagePath) {
            return imagePath.toLowerCase().endsWith(".gif");
        }

        public ImageIcon getTile(int row, int col, int tileWidth, int tileHeight) {
            if (spritesheet == null) {
                throw new RuntimeException("Spritesheet not loaded");
            }
            int x = col * tileWidth;
            int y = row * tileHeight;
            BufferedImage tile = spritesheet.getSubimage(x, y, tileWidth, tileHeight);
            return new ImageIcon(tile);
        }

        public void setTile(int row, int col, int tileWidth, int tileHeight) {
            ImageIcon tileIcon = getTile(row, col, tileWidth, tileHeight);
            setIcon(tileIcon);
        }
    }
    public static class MyTextArea extends JTextArea {
        private static final long serialVersionUID = 1L;

        public MyTextArea(int x, int y, int width, int height) {
            super();
            setBounds(x, y, width, height);
            setLineWrap(true);
            setWrapStyleWord(true);
            setFont(new Font("Arial", Font.PLAIN, 14));
        }

        public void setFontFamily(String fontFamily) {
            setFont(new Font(fontFamily, getFont().getStyle(), getFont().getSize()));
        }

        public void setFontStyle(int style) {
            setFont(new Font(getFont().getName(), style, getFont().getSize()));
        }
    }
    public class LineNumberingTextArea extends JTextArea {


        public LineNumberingTextArea()
        {
            TextArea textArea = new TextArea();
            setBackground(Color.LIGHT_GRAY);
            setEditable(true);
        }

        public void updateLineNumbers(LineNumberingTextArea textArea)
        {
            String lineNumbersText = getLineNumbersText(textArea);
            setText(lineNumbersText);
        }

        private String getLineNumbersText(LineNumberingTextArea textArea)
        {
            int caretPosition = textArea.getDocument().getLength();
            Element root = textArea.getDocument().getDefaultRootElement();
            StringBuilder lineNumbersTextBuilder = new StringBuilder();
            lineNumbersTextBuilder.append("1").append(System.lineSeparator());

            for (int elementIndex = 2; elementIndex < root.getElementIndex(caretPosition) + 2; elementIndex++)
            {
                lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
            }

            return lineNumbersTextBuilder.toString();
        }
    }
    public static class MyColorPicker extends JPanel {
        private static final long serialVersionUID = 1L;

        public MyColorPicker(int x, int y, int width, int height) {
            setBounds(x, y, width, height);
            setLayout(new BorderLayout());

            JLabel label = new JLabel("Pick a color:");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            JButton pickColorButton = new JButton("Choose Color");
            JLabel colorDisplay = new JLabel();
            colorDisplay.setOpaque(true);
            colorDisplay.setBackground(Color.WHITE);
            colorDisplay.setPreferredSize(new Dimension(width, 50));

            add(label, BorderLayout.NORTH);
            add(pickColorButton, BorderLayout.CENTER);
            add(colorDisplay, BorderLayout.SOUTH);

            pickColorButton.addActionListener(e -> {
                Color selectedColor = JColorChooser.showDialog(this, "Choose a Color", colorDisplay.getBackground());
                if (selectedColor != null) {
                    colorDisplay.setBackground(selectedColor);
                    setBackground(selectedColor);
                }
            });
        }
    }
    public static class MyProgressIndicator extends JProgressBar {
        private static final long serialVersionUID = 1L;

        public MyProgressIndicator(int x, int y, int width, int height) {
            setBounds(x, y, width, height);
            setMinimum(0);
            setMaximum(100);
        }

        public void setProgress(int value) {
            setValue(value);
        }
    }
    public static class MyDatePicker extends JPanel {
        private static final long serialVersionUID = 1L;
        private JSpinner dateSpinner;

        public MyDatePicker(int x, int y, int width, int height) {
            setBounds(x, y, width, height);
            setLayout(new BorderLayout());
            SpinnerDateModel dateModel = new SpinnerDateModel();
            dateSpinner = new JSpinner(dateModel);
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
            dateSpinner.setEditor(dateEditor);
            add(dateSpinner, BorderLayout.CENTER);
        }

        public Date getSelectedDate() {
            return (Date) dateSpinner.getValue();
        }
    }
    public static class MyButton extends JButton {
        private static final long serialVersionUID = 1L;

        public MyButton(String text, int x, int y, int width, int height) {
            super(text);
            setBounds(x, y, width, height);
            setFocusPainted(false);
        }
        public void setButtonColor(Color color) {
            setBackground(color);
            setOpaque(true);
            setBorderPainted(false);
        }
        public void setButtonBorder(Color color, int thickness) {
            setBorder(createBorder(color, thickness));
        }

        public void setBorderPainted(int painted) {

            boolean shouldPaint = painted != 0;
            super.setBorderPainted(shouldPaint);
        }

        }


        public void setFontFamily(String fontFamily) {
            setFont(new Font(fontFamily, getFont().getStyle(), getFont().getSize()));
        }

        public void setFontStyle(int style) {
            setFont(new Font(getFont().getName(), style, getFont().getSize()));
        }
    }

