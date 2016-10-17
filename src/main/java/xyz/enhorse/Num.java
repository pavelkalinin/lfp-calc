package xyz.enhorse;

public class Num {
    public final static int DG_POWER = 6;
    private final static String[][] POWERS = new String[][]{
            {"0", "", "", ""}, // 1
            {"1", "тысяча ", "тысячи ", "тысяч "}, // 2
            {"0", "миллион ", "миллиона ", "миллионов "}, // 3
            {"0", "миллиард ", "миллиарда ", "миллиардов "}, // 4
            {"0", "триллион ", "триллиона ", "триллионов "}, // 5
            {"0", "квадриллион ", "квадриллиона ", "квадриллионов "}, // 6
            {"0", "квинтиллион ", "квинтиллиона ", "квинтиллионов "} // 7
    };
    private final static String[][] NUMBERS = new String[][]{
            {"", "", "десять ", "", ""},
            {"один ", "одна ", "одиннадцать ", "десять ", "сто "},
            {"два ", "две ", "двенадцать ", "двадцать ", "двести "},
            {"три ", "три ", "тринадцать ", "тридцать ", "триста "},
            {"четыре ", "четыре ", "четырнадцать ", "сорок ", "четыреста "},
            {"пять ", "пять ", "пятнадцать ", "пятьдесят ", "пятьсот "},
            {"шесть ", "шесть ", "шестнадцать ", "шестьдесят ", "шестьсот "},
            {"семь ", "семь ", "семнадцать ", "семьдесят ", "семьсот "},
            {"восемь ", "восемь ", "восемнадцать ", "восемьдесят ", "восемьсот "},
            {"девять ", "девять ", "девятнадцать ", "девяносто ", "девятьсот "}
    };

    String toString(int sum) {
        StringBuilder result = new StringBuilder();
        long divisor;
        int psum = sum;

        int one = 1;
        int four = 2;
        int many = 3;

        int hun = 4;
        int dec = 3;
        int dec2 = 2;

        if (sum == 0) {
            return "ноль ";
        }
        if (sum < 0) {
            result.append("минус ");
            psum = -psum;
        }

        int i, mny;

        for (i = 0, divisor = 1; i < DG_POWER; i++) {
            divisor *= 1000;
        }

        for (i = DG_POWER - 1; i >= 0; i--) {
            divisor /= 1000;
            mny = (int) (psum / divisor);
            psum %= divisor;
            if (mny == 0) {
                if (i > 0) {
                    continue;
                }
                result.append(POWERS[i][one]);
            } else {
                if (mny >= 100) {
                    result.append(NUMBERS[mny / 100][hun]);
                    mny %= 100;
                }
                if (mny >= 20) {
                    result.append(NUMBERS[mny / 10][dec]);
                    mny %= 10;
                }
                if (mny >= 10) {
                    result.append(NUMBERS[mny - 10][dec2]);
                } else {
                    if (mny >= 1) {
                        result.append(NUMBERS[mny]["0".equals(POWERS[i][0]) ? 0 : 1]);
                    }
                }
                switch (mny) {
                    case 1:
                        result.append(POWERS[i][one]);
                        break;
                    case 2:
                    case 3:
                    case 4:
                        result.append(POWERS[i][four]);
                        break;
                    default:
                        result.append(POWERS[i][many]);
                        break;
                }
            }
        }
        return result.toString().trim();
    }

    String toString(double num, String[][] curs) {
        return toString((int) num) + " " + declOfNum((int) num, curs[0]) + " "
                + toString((int) (num * 100 - ((int) num) * 100)) + declOfNum((int) (num * 100 - ((int) num) * 100), curs[1]);
    }

    String declOfNum(Integer number, String[] titles) {
        Integer[] cases = new Integer[6];
        cases[0] = 2;
        cases[1] = 0;
        cases[2] = 1;
        cases[3] = 1;
        cases[4] = 1;
        cases[5] = 2;
        String result = "";

        Integer position = 0;
        if (number % 100 > 4 && number % 100 < 20) {
            position = 2;

        } else {
            position = cases[Math.min(number % 10, 5)];
        }
        result += titles[position];

        return result;
    }
}