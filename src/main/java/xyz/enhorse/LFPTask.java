package xyz.enhorse;

import java.util.Calendar;

/*
 * Calculator
 * Created by PAK on 10.12.13.
 */
class LFPTask {
    private double p_width = 0;
    private double p_height = 0;
    private double p_distance_cringle = 0;
    private String p_gluing = "";
    private double p_price_print = 0;
    private double p_price_cringle = 0;
    private double p_price_gluing = 0;
    private String p_cringle = "";
    private double p_materialMaxWidth = 0;
    private double p_field;

    private long cringle_amount = 0;
    private double gluing_amount = 0;
    public long cost_print = 0;
    public long cost_cringle = 0;
    public long cost_gluing = 0;
    public long cost = 0;

    public LFPTask(
            double width,
            double height,
            double distance_cringle,
            String cringle,
            String gluing,
            double price_print,
            double price_cringle,
            double price_gluing,
            double materialMaxWidth,
            double field) {
        p_width = width;
        p_height = height;
        p_distance_cringle = distance_cringle;
        p_gluing = gluing;
        p_price_cringle = price_cringle;
        p_price_gluing = price_gluing;
        p_price_print = price_print;
        p_cringle = cringle;
        p_materialMaxWidth = materialMaxWidth;
        p_field = field;
        Calculate();
    }

    public String CostAsString() {
        Num n = new Num();
        String[][] currency = {{"рубль", "рубля", "рублей"}, {"копейка", "копейки", "копеек"}};
        return n.toString(cost, currency);
    }

    public String Description() {
        StringBuilder s = new StringBuilder();
        s.append("========================\n")
        .append("Ширина: ").append(Double.toString(p_width)).append(" м\n")
        .append("Высота: ").append(Double.toString(p_height)).append(" м\n")
        .append("Люверсы: ").append(Long.toString(cringle_amount))
                .append(" шт. через ").append(Double.toString(p_distance_cringle))
                .append("м, по сторонам: ").append(DecodePerimeter(p_cringle)).append("\n")
        .append("Проклейка/склейка: ").append(Math.rint(100.0 * gluing_amount) / 100.0)
                .append(" п.м. по сторонам: ").append(DecodePerimeter(p_gluing)).append("\n")
        .append("========================\n \n")
        .append("СТОИМОСТЬ\n")
        .append("========================\n")
        .append("Печать: ").append(Double.toString(cost_print)).append("р.\n")
        .append("Люверсы: ").append(Double.toString(cost_cringle)).append("р.\n")
        .append("Проклейка/склейка: ").append(Double.toString(cost_gluing)).append("р.\n")
        .append("Итого: ").append(Double.toString(cost)).append("р.").append(" (").append(CostAsString()).append(")\n")
        .append("========================\n \n");

        Calendar c = Calendar.getInstance();
        s.append("Дата расчета: ")
                .append(c.get(Calendar.DAY_OF_MONTH)).append(".")
                .append(c.get(Calendar.MONTH)).append(".")
                .append(c.get(Calendar.YEAR));
        return s.toString();
    }

    String DecodePerimeter(String strPerimeter) {
        String s = "";
        if (strPerimeter.contains("T")) s += "сверху, ";
        if (strPerimeter.contains("B")) s += "снизу, ";
        if (strPerimeter.contains("L")) s += "слева, ";
        if (strPerimeter.contains("R")) s += "справа, ";
        if (s.isEmpty()) s = "не выбрано";
        if (s.endsWith(", ")) s = s.substring(0, s.length() - 2);
        return s;
    }

    void Calculate() {
        double square = p_width * p_height;
        cost_print = Math.round(p_price_print * square);

        cringle_amount = 0;
        if (p_distance_cringle > 0) {
            if (p_cringle.contains("T")) cringle_amount += Math.round(p_width / p_distance_cringle);
            if (p_cringle.contains("B")) cringle_amount += Math.round(p_width / p_distance_cringle);
            if (p_cringle.contains("L")) cringle_amount += Math.round(p_height / p_distance_cringle);
            if (p_cringle.contains("R")) cringle_amount += Math.round(p_height / p_distance_cringle);
            if ((p_cringle.contains("R")) && (p_cringle.contains("T"))) cringle_amount--;
            if ((p_cringle.contains("R")) && (p_cringle.contains("B"))) cringle_amount--;
            if ((p_cringle.contains("L")) && (p_cringle.contains("T"))) cringle_amount--;
            if ((p_cringle.contains("L")) && (p_cringle.contains("B"))) cringle_amount--;
        }
        cost_cringle = Math.round(cringle_amount * p_price_cringle);

        gluing_amount = 0;
        if (p_gluing.contains("T")) gluing_amount += p_width;
        if (p_gluing.contains("B")) gluing_amount += p_width;
        if (p_gluing.contains("L")) gluing_amount += p_height;
        if (p_gluing.contains("R")) gluing_amount += p_height;
        gluing_amount += NeedCut(p_width, p_height, p_materialMaxWidth, p_field);
        cost_gluing = Math.round(gluing_amount * p_price_gluing);

        cost = cost_print + cost_cringle + cost_gluing;
    }

    public double NeedCut(double width, double height, double maxMaterialWidth, double field) {
        if ((maxMaterialWidth - width + field * 2) > 0 || (maxMaterialWidth - height + field * 2) > 0) {
            return 0;
        } else {
            double needGluing;
            double mWidth = maxMaterialWidth - field * 2 - 0.05;
            if ((width / mWidth) > (height / mWidth)) {
                needGluing = ((int) (height / mWidth)) * width;
            } else {
                needGluing = ((int) (width / mWidth)) * height;
            }
            return needGluing;
        }
    }
}
