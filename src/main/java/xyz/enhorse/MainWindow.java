package xyz.enhorse;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Main application window
 * Created by PAK on 11.12.13.
 */

class MainWindow extends JFrame {
    private static final int[][] PRICES_PRINT = {
            {360, 300, 270},
            {360, 320, 300},
            {360, 320, 300},
            {185, 175, 165},
            {300, 260, 230},
            {620, 600, 550},
            {600, 550, 500},
            {440, 390, 330}};
    private static final  int[][] PRICES_POSTPRINT = {
            {20, 150},
            {15, 120},
            {10, 100}};
    private static final String[] MEDIA = {
            "Банер «Европа»",
            "Глянцевая пленка",
            "Матовая пленка",
            "Бумага (блюбэк)",
            "Банер «Китай»",
            "Транслюсцентный банер",
            "Транслюсцентная плёнка",
            "Банерная сетка"};
    private static final String[] MEDIA_ABBR = {
            "банер",
            "глсамоклей",
            "матсамоклей",
            "блюбэк",
            "корея",
            "трансбанер",
            "транспленка",
            "сетка"};

    private final HUDTextField lblName;
    private final HUDNumberField lblWidth;
    private final HUDNumberField lblHeight;
    private final HUDNumberField lblCringleDist;
    private final HUDNumberField lblPricePrint;
    private final HUDNumberField lblPriceCringle;
    private final HUDNumberField lblPriceGluing;
    private final HUDNumberField lblPriceDiscount;
    private final HUDSidesPanel fspCringle;
    private final HUDSidesPanel fspGluing;
    private final HUDNumberField lblResultCost;
    private final HUDNumberField lblResultPrint;
    private final HUDNumberField lblResultCringle;
    private final HUDNumberField lblResultGluing;
    private final HUDNumberField lblResultDiscount;
    private final HUDNumberField lblResultFinalCost;
    private final JComboBox cbMaterial;
    private final JComboBox cbPrices;
    private final JButton btnSaveFile;
    private LFPTask baner;
    private int mtype = 0;
    private int ptype = 0;

    public MainWindow() {
        super("");

        JPanel pnlSize = new JPanel();
        pnlSize.setLayout(new GridLayout(5, 1));
        pnlSize.setBorder(BorderFactory.createTitledBorder("ПАРАМЕТРЫ"));

        lblName = new HUDTextField("Заказ: ", "", "Неизвестный");
        pnlSize.add(lblName);
        lblName.defaultValue = "Неизвестный";

        cbMaterial = new JComboBox<>(MEDIA);
        cbMaterial.setSelectedIndex(mtype);
        pnlSize.add(cbMaterial);
        lblWidth = new HUDNumberField("Ширина: ", "м", "0");
        pnlSize.add(lblWidth);
        lblWidth.defaultValue = "";
        lblHeight = new HUDNumberField("Высота: ", "м", "0");
        pnlSize.add(lblHeight);
        lblHeight.defaultValue = "";
        lblCringleDist = new HUDNumberField("Шаг люверса: ", "cм", "0");
        lblCringleDist.defaultValue = "";
        pnlSize.add(lblCringleDist);


        JPanel pnlPrice = new JPanel();
        pnlPrice.setLayout(new GridLayout(5, 1));
        pnlPrice.setBorder(BorderFactory.createTitledBorder("ЦЕНЫ"));
        String[] pricesList = {"Стандарт", "Объем", "РА"};
        cbPrices = new JComboBox<>(pricesList);
        cbPrices.setSelectedIndex(ptype);
        pnlPrice.add(cbPrices);
        lblPricePrint = new HUDNumberField("Печать: ", "р.", Integer.toString(PRICES_PRINT[mtype][ptype]));
        pnlPrice.add(lblPricePrint);
        lblPriceCringle = new HUDNumberField("Люверс: ", "р.", Integer.toString(PRICES_POSTPRINT[ptype][0]));
        pnlPrice.add(lblPriceCringle);
        lblPriceGluing = new HUDNumberField("Проклейка: ", "р.", Integer.toString(PRICES_POSTPRINT[ptype][1]));
        pnlPrice.add(lblPriceGluing);
        lblPriceDiscount = new HUDNumberField("Скидка: ", "%", "0");
        pnlPrice.add(lblPriceDiscount);

        JPanel pnlParameters = new JPanel();
        pnlParameters.setLayout(new GridLayout(1, 2));
        pnlParameters.add(pnlSize);
        pnlParameters.add(pnlPrice);

        JPanel pnlPostPrint = new JPanel();
        pnlPostPrint.setLayout(new GridLayout(1, 2));
        pnlPostPrint.setBorder(BorderFactory.createTitledBorder("ПОСТПЕЧАТНАЯ ОБРАБОТКА"));
        fspCringle = new HUDSidesPanel("Люверсы");
        fspGluing = new HUDSidesPanel("Проклейка");
        pnlPostPrint.add(fspCringle);
        pnlPostPrint.add(fspGluing);

        JPanel pnlCalc = new JPanel();
        pnlCalc.setLayout(new FlowLayout());
        JButton btnCalc = new JButton("Рассчитать");
        pnlCalc.add(btnCalc);
        JButton btnReset = new JButton("Сбросить");
        pnlCalc.add(btnReset);
        btnSaveFile = new JButton("Сохранить в файл");
        btnSaveFile.setEnabled(false);
        pnlCalc.add(btnSaveFile);

        JPanel pnlResult = new JPanel();
        pnlResult.setBorder(BorderFactory.createTitledBorder("СТОИМОСТЬ"));
        pnlResult.setLayout(new GridLayout(3, 2));
        lblResultCost = new HUDNumberField("Итого: ", "р.", "0");
        lblResultCost.field.setFocusable(false);
        pnlResult.add(lblResultCost);
        lblResultPrint = new HUDNumberField("Печать: ", "р.", "0");
        lblResultPrint.field.setFocusable(false);
        pnlResult.add(lblResultPrint);
        lblResultDiscount = new HUDNumberField("Скидка: ", "р.", "0");
        lblResultDiscount.field.setFocusable(false);
        pnlResult.add(lblResultDiscount);
        lblResultCringle = new HUDNumberField("Люверсы: ", "р.", "0");
        lblResultCringle.field.setFocusable(false);
        pnlResult.add(lblResultCringle);
        lblResultFinalCost = new HUDNumberField("К оплате: ", "р.", "0");
        lblResultFinalCost.field.setFocusable(false);
        lblResultFinalCost.field.setBackground(Color.BLUE);
        lblResultFinalCost.field.setForeground(Color.WHITE);
        lblResultFinalCost.field.setFont(lblResultFinalCost.field.getFont().deriveFont(Font.BOLD));
        pnlResult.add(lblResultFinalCost);
        lblResultGluing = new HUDNumberField("Проклейка: ", "р.", "0");
        lblResultGluing.field.setFocusable(false);
        pnlResult.add(lblResultGluing);

        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.add(pnlParameters);
        pnlMain.add(pnlPostPrint);
        pnlMain.add(pnlCalc);
        pnlMain.add(pnlResult);


        cbPrices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ptype = cbPrices.getSelectedIndex();
                setPrices();
            }
        });

        cbMaterial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mtype = cbMaterial.getSelectedIndex();
                setPrices();
            }
        });


        class btnResetListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                clearParameters();
                clearResults();
            }
        }
        btnReset.addActionListener(new btnResetListener());


        class btnSaveFileListener implements ActionListener {
            private JFileChooser jfcDialog;

            public void actionPerformed(ActionEvent e) {
                jfcDialog = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Файл расчета (*.tsk)", "tsk");
                jfcDialog.setSelectedFile(new File(lblName.field.getText()
                        + "-" + lblWidth.field.getText()
                        + "x" + lblHeight.field.getText()
                        + "_" + MEDIA_ABBR[cbMaterial.getSelectedIndex()]
                        + ".tsk"));
                jfcDialog.setFileFilter(filter);
                jfcDialog.setDialogType(JFileChooser.SAVE_DIALOG);
                jfcDialog.setCurrentDirectory(new File("."));
                jfcDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfcDialog.setMultiSelectionEnabled(false);
                if (jfcDialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String fname = jfcDialog.getSelectedFile().getAbsolutePath();
                    try {
                        WriteToFile(fname, lblName.field.getText() + "\n \n"
                                + cbMaterial.getSelectedItem().toString() + "\n"
                                + baner.Description());
                    } catch (FileNotFoundException | UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        btnSaveFile.addActionListener(new btnSaveFileListener());

        class btnCalcListener implements ActionListener {
            private String perimeterCringle = "";
            private String perimeterGluing = "";
            private double width;
            private double height;
            private double cringle_dist;
            private double price_print;
            private double price_cringle;
            private double price_gluing;
            private double price_discount;

            public void actionPerformed(ActionEvent e) {

                NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);

                Number number = null;
                try {
                    number = format.parse(lblWidth.field.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                width = number != null ?
                        number.doubleValue()
                        : 0;

                try {
                    number = format.parse(lblHeight.field.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                height = number != null ?
                        number.doubleValue()
                        : 0;

                try {
                    number = format.parse(lblCringleDist.field.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                cringle_dist = number != null ?
                        number.doubleValue()  * 0.01
                        : 0;

                try {
                    number = format.parse(lblPricePrint.field.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                price_print = number != null ?
                        number.doubleValue()
                        : 0;

                try {
                    number = format.parse(lblPriceCringle.field.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                price_cringle = number != null ?
                        number.doubleValue()
                        : 0;

                try {
                    number = format.parse(lblPriceGluing.field.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                price_gluing = number != null ?
                        number.doubleValue()
                        : 0;

                try {
                    number = format.parse(lblPriceDiscount.field.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                price_discount = number != null ?
                        number.doubleValue()
                        : 0;

                if (width > 0 && height > 0) {
                    perimeterCringle = "";
                    if (fspCringle.left) perimeterCringle += "L";
                    if (fspCringle.right) perimeterCringle += "R";
                    if (fspCringle.top) perimeterCringle += "T";
                    if (fspCringle.bottom) perimeterCringle += "B";
                    perimeterGluing = "";
                    if (fspGluing.left) perimeterGluing += "L";
                    if (fspGluing.right) perimeterGluing += "R";
                    if (fspGluing.top) perimeterGluing += "T";
                    if (fspGluing.bottom) perimeterGluing += "B";

                    baner = new LFPTask(width, height, cringle_dist, perimeterCringle, perimeterGluing, price_print, price_cringle, price_gluing, 3.2, 0.05);

                    lblResultCost.defaultValue = Integer.toString(Math.round(baner.cost));
                    lblResultCost.field.setText(lblResultCost.defaultValue);
                    lblResultPrint.defaultValue = Integer.toString(Math.round(baner.cost_print));
                    lblResultPrint.field.setText(lblResultPrint.defaultValue);
                    lblResultCringle.defaultValue = Integer.toString(Math.round(baner.cost_cringle));
                    lblResultCringle.field.setText(lblResultCringle.defaultValue);
                    lblResultGluing.defaultValue = Integer.toString(Math.round(baner.cost_gluing));
                    lblResultGluing.field.setText(lblResultGluing.defaultValue);
                    lblResultDiscount.defaultValue = Double.toString(Math.round(baner.cost * price_discount * 0.01));
                    lblResultDiscount.field.setText(lblResultDiscount.defaultValue);
                    lblResultFinalCost.defaultValue = Double.toString(Math.round(baner.cost - (baner.cost * price_discount * 0.01)));
                    lblResultFinalCost.field.setText(lblResultFinalCost.defaultValue);
                    btnSaveFile.setEnabled(true);
                }
            }
        }

        btnCalc.addActionListener(new btnCalcListener());
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frMain = new JFrame("Расчет стоимости широкоформатной печати");

        frMain.setContentPane(pnlMain);
        frMain.setSize(300, 400);
        frMain.setResizable(false);
        frMain.setLocationRelativeTo(null);
        frMain.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frMain.getSize().width;
        int h = frMain.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frMain.setLocation(x, y);
        frMain.setVisible(true);
        lblWidth.field.requestFocus();
        frMain.pack();
    }

    void setPrices() {
        lblPricePrint.field.setText(Integer.toString(PRICES_PRINT[mtype][ptype]));

        lblPricePrint.defaultValue = Double.toString(PRICES_PRINT[mtype][ptype]);
        lblPriceGluing.defaultValue = Double.toString(PRICES_PRINT[mtype][ptype]);
        lblPriceCringle.defaultValue = Double.toString(PRICES_PRINT[mtype][ptype]);


        if (mtype == 0 || mtype == 4 || mtype == 5 || mtype == 7) {
            fspCringle.setEnabled(true);
            fspGluing.setEnabled(true);
            lblPriceCringle.field.setText(Integer.toString(PRICES_POSTPRINT[ptype][0]));
            lblPriceGluing.field.setText(Integer.toString(PRICES_POSTPRINT[ptype][1]));
            lblCringleDist.field.setEnabled(true);
            lblPriceGluing.field.setEnabled(true);
            lblPriceCringle.field.setEnabled(true);
        } else {
            fspGluing.setSelected(false);
            fspCringle.setSelected(false);
            fspCringle.setEnabled(false);
            fspGluing.setEnabled(false);
            lblPriceCringle.field.setText("0");
            lblPriceGluing.field.setText("0");
            lblCringleDist.field.setText("0");
            lblPriceCringle.field.setEnabled(false);
            lblPriceGluing.field.setEnabled(false);
            lblCringleDist.field.setEnabled(false);

        }
    }

    void clearResults() {
        lblResultCost.field.setText("0");
        lblResultPrint.field.setText("0");
        lblResultDiscount.field.setText("0");
        lblResultCringle.field.setText("0");
        lblResultFinalCost.field.setText("0");
        lblResultGluing.field.setText("0");
        btnSaveFile.setEnabled(false);
    }

    void clearParameters() {
        lblWidth.field.setText("0");
        lblHeight.field.setText("0");
        lblCringleDist.field.setText("0");
        fspCringle.setSelected(false);
        fspGluing.setSelected(false);
        btnSaveFile.setEnabled(false);
    }

    void WriteToFile(String filename, String text) throws FileNotFoundException, UnsupportedEncodingException {
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
        try {
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

