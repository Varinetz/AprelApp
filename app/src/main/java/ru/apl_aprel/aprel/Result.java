package ru.apl_aprel.aprel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

//import im.dacer.androidcharts.LineView;

public class Result extends AppCompatActivity {

    public int getSumOfStrChars(String str) {
        int[] strArr = new int[str.length()];
        int strSum = 0;

        for (int i = 0; i < str.length(); i++) {
            strSum += Character.getNumericValue(str.charAt(i));
        }

        return strSum;
    }

    public ArrayList fillGraph(int day, int month, int year, boolean replaceZeros) {

        String dayMonthStr = String.format("%02d", day) + String.format("%02d",month);

        if (replaceZeros) {
            dayMonthStr = dayMonthStr.replace("0", "1");
        }

        int dayMonthInt = Integer.parseInt(dayMonthStr, 10);

        String sumStr = String.valueOf(dayMonthInt * year);

        ArrayList graphNums = new ArrayList();

        for (int i = sumStr.length() - 1; i >= 0; i--) {
           graphNums.add(Character.getNumericValue(sumStr.charAt(i)));
        }

        if (graphNums.size() < 7) {
            graphNums.add(6, 0);
        }

        Collections.reverse(graphNums);

        return graphNums;
    }

    public int LEAP(int year) {
        int result4 = year / 4;
        int result400 = year / 400;
        int result100 = year / 100;
        if (!(!(result4 - Math.floor(result4) == 0 && result100 - Math.floor(result100) != 0) && result400 - Math.floor(result400) != 0))
        {
            return (1);
        }
        else
        {
            return (0);
        }
    };













    public int Long_dd (int Ldd, int Lmm, int Lgg, double LNddx)
    {
            int Ndd = 1;

            while (Ndd <= LNddx)
            {
                ++Ldd;
                if (!(Ldd != 29 || (Lmm != 2 || LEAP(Lgg) != 0)))
                {
                    Ldd = 1;
                    Lmm = 3;

                } // end if
                if (!(Ldd != 29 || (Lmm != 2 || LEAP(Lgg) != 1)))
                {
                    Ldd = 29;
                    Lmm = 2;

                } // end if
                if (!(Ldd <= 29 || (Lmm != 2 || LEAP(Lgg) != 1)))
                {
                    Ldd = 1;
                    Lmm = 3;

                } // end if
                if (!(Ldd <= 30 || !(Lmm == 4 || (Lmm == 6 || (Lmm == 9 || Lmm == 11)))))
                {
                    Ldd = 1;
                    Lmm = Lmm + 1;

                } // end if
                if (!(Ldd <= 31 || !(Lmm == 1 || (Lmm == 3 || (Lmm == 5 || (Lmm == 7 || (Lmm == 8 || (Lmm == 10 || Lmm == 12))))))))
                {
                    Ldd = 1;
                    Lmm = Lmm + 1;

                } // end if
                if (Lmm > 12)
                {
                    Lgg = Lgg + 1;
                    Lmm = 1;
                    Ldd = 1;
                    continue;
                } // end if

                Ndd++;
            } // end while
            return (Ldd);
    };


    public int Long_mm (int Ldd, int Lmm, int Lgg, double LNddx)
    {
           int Ndd = 1;

            while (Ndd <= LNddx)
            {
                ++Ldd;
                if (!(Ldd != 29 || (Lmm != 2 || LEAP(Lgg) != 0)))
                {
                    Ldd = 1;
                    Lmm = 3;

                } // end if
                if (!(Ldd != 29 || (Lmm != 2 || LEAP(Lgg) != 1)))
                {
                    Ldd = 29;
                    Lmm = 2;

                } // end if
                if (!(Ldd <= 29 || (Lmm != 2 || LEAP(Lgg) != 1)))
                {
                    Ldd = 1;
                    Lmm = 3;

                } // end if
                if (!(Ldd <= 30 || !(Lmm == 4 || (Lmm == 6 || (Lmm == 9 || Lmm == 11)))))
                {
                    Ldd = 1;
                    Lmm = Lmm + 1;

                } // end if
                if (!(Ldd <= 31 || !(Lmm == 1 || (Lmm == 3 || (Lmm == 5 || (Lmm == 7 || (Lmm == 8 || (Lmm == 10 || Lmm == 12))))))))
                {
                    Ldd = 1;
                    Lmm = Lmm + 1;

                } // end if
                if (Lmm > 12)
                {
                    Lgg = Lgg + 1;
                    Lmm = 1;
                    Ldd = 1;
                    continue;
                } // end if

                Ndd++;
            } // end while
            return (Lmm);
    };


    public int Long_gg (int Ldd, int Lmm, int Lgg, double  LNddx)
    {
            int Ndd = 1;

            while (Ndd <= LNddx)
        {
            ++Ldd;
            if (!(Ldd != 29 || (Lmm != 2 || LEAP(Lgg) != 0))) {
                Ldd = 1;
                Lmm = 3;

            } // end if
            if (!(Ldd != 29 || (Lmm != 2 || LEAP(Lgg) != 1))) {
                Ldd = 29;
                Lmm = 2;

            } // end if
            if (!(Ldd <= 29 || (Lmm != 2 || LEAP(Lgg) != 1))) {
                Ldd = 1;
                Lmm = 3;

            } // end if
            if (!(Ldd <= 30 || !(Lmm == 4 || (Lmm == 6 || (Lmm == 9 || Lmm == 11))))) {
                Ldd = 1;
                Lmm = Lmm + 1;

            } // end if
            if (!(Ldd <= 31 || !(Lmm == 1 || (Lmm == 3 || (Lmm == 5 || (Lmm == 7 || (Lmm == 8 || (Lmm == 10 || Lmm == 12)))))))) {
                Ldd = 1;
                Lmm = Lmm + 1;

            } // end if
            if (Lmm > 12) {
                Lgg = Lgg + 1;
                Lmm = 1;
                Ldd = 1;
                continue;
            } // end if

            Ndd++;
        }
        return (Lgg);
    };











    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int result_day = getIntent().getIntExtra("result_day", 0);
        int result_month = getIntent().getIntExtra("result_month", 0);
        int result_year = getIntent().getIntExtra("result_year", 0);
//
//        int result_day = 01;
//        int result_month = 12;
//        int result_year = 1970;
        String result_name = getIntent().getStringExtra("result_name");
        TextView testTextArea;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Дату в строку

        String dateToStr =
                String.format("%02d", result_day) +
                String.format("%02d", result_month) +
                String.format("%04d", result_year);


        testTextArea = (TextView) findViewById(R.id.textViewResult);
        testTextArea.setText(dateToStr + ", " + dateToStr.length());

        // Разбиваем числа на отдельные цифры

        int[] magicNums = new int[8];

        for (int i = 0; i < dateToStr.length(); i++) {
            magicNums[i] = Character.getNumericValue(dateToStr.charAt(i));
        }

        // Складываем числа между собой (Сумма A)

        int magicSumA = getSumOfStrChars(dateToStr);
        testTextArea.append("\n" + magicSumA);

        int magicConst = 0;
        if (result_year < 1999) {
            magicConst = -2;
        } else {
            magicConst = 19;
        }


        // Складываем числа между собой (Сумма B)

        int magicSumB = getSumOfStrChars(String.valueOf(magicSumA));
        testTextArea.append("\n" + magicSumB);


        // Число C

        int magicSumC = 0;
            magicSumC = magicSumA + magicConst;
        testTextArea.append("\n" + magicSumC);


        // Число D

        int magicSumD = getSumOfStrChars(String.valueOf(magicSumC));
        testTextArea.append("\n" + magicSumD);

        // Строим матрицу!
        String magicFinalStr =
                dateToStr +
                magicSumA +
                magicSumB +
                magicSumC +
                magicSumD +
                Math.abs(magicConst);

            testTextArea.append("\n" + magicFinalStr + ", " + magicFinalStr.length());

        String[] matrix = new String[10];
        Arrays.fill(matrix, "");

        for(int i = 0; i < magicFinalStr.length(); i++) {
            char curChar =  magicFinalStr.charAt(i);
            switch (curChar) {
                case '0':
                    matrix[0] += curChar;
                    break;
                case '1':
                    matrix[1] += curChar;
                    break;
                case '2':
                    matrix[2] += curChar;
                    break;
                case '3':
                    matrix[3] += curChar;
                    break;
                case '4':
                    matrix[4] += curChar;
                    break;
                case '5':
                    matrix[5] += curChar;
                    break;
                case '6':
                    matrix[6] += curChar;
                    break;
                case '7':
                    matrix[7] += curChar;
                    break;
                case '8':
                    matrix[8] += curChar;
                    break;
                case '9':
                    matrix[9] += curChar;
                    break;
            }
        }

        for (int i = 1; i < matrix.length; i++) {
            String tableCellId = "textViewMatrix" + i;
            int resID = getResources().getIdentifier(tableCellId, "id", "ru.apl_aprel.aprel");
            TextView tableCell = ((TextView) findViewById(resID));

            if("".equals(matrix[i]))
                tableCell.setText("—");
            else
                tableCell.setText(matrix[i]);
        }


        // Итоговая цифра

        int magicFinalDigit = getSumOfStrChars(dateToStr);
        while(magicFinalDigit >= 10) {
            magicFinalDigit = getSumOfStrChars(String.valueOf(magicFinalDigit));
        }
        testTextArea.append("\n Итоговая цифра: " + magicFinalDigit);


        // Мужские и женские цифры
        int manDigit =
                matrix[2].length() +
                matrix[4].length() +
                matrix[6].length() +
                matrix[8].length();
        int womanDigit =
                matrix[1].length() +
                matrix[3].length() +
                matrix[5].length() +
                matrix[7].length() +
                matrix[9].length();
        testTextArea.append(
                "\n Мужские цифры: " + manDigit +
                "\n Женские цифры: " + womanDigit
        );


        // График

        ArrayList faithGraph = fillGraph(result_day, result_month, result_year, false);
        ArrayList willGraph = fillGraph(result_day, result_month, result_year, true);

        ArrayList<ArrayList<Integer>> bothGraph = new ArrayList<ArrayList<Integer>>();
        bothGraph.add(faithGraph);
        bothGraph.add(willGraph);

        testTextArea.append("\n Судьба: " + Arrays.toString(faithGraph.toArray()));
        testTextArea.append("\n Воля: " + Arrays.toString(willGraph.toArray()));

        LineChart chart = (LineChart) findViewById(R.id.chartGraph);

//        final LineView lineView = (LineView) findViewById(R.id.line_viewGraph);
//
//        //must*
//        ArrayList<String> test = new ArrayList<String>();
//        for (int i=0; i<=72; i+=12){
//            test.add(String.valueOf(i));
//        }
//        lineView.setBottomTextList(test);
//        lineView.setDataList(bothGraph);
//        lineView.setDrawDotLine(false);
//        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
//
//        lineView.setDataList(bothGraph);
        
        
// [Action in Frame 4]
        int DD12 = result_year + 12;
        int DD24 = result_year + 24;
        int DD36 = result_year + 36;
        int DD48 = result_year + 48;
        int DD60 = result_year + 60;
        int DD72 = result_year + 72;
        int SNdd12 = 0;
        int SNdd24 = 0;
        int SNdd36 = 0;
        int SNdd48 = 0;
        int SNdd60 = 0;
        int SNdd72 = 0;
        int igg = result_year;

        while (igg < DD12)
        {
            SNdd12 = SNdd12 + (365 + LEAP(igg));
            igg++;
        } // end while
        igg = result_year;

        while (igg < DD24)
        {
            SNdd24 = SNdd24 + (365 + LEAP(igg));
            igg++;
        } // end while
        igg = result_year;

        while (igg < DD36)
        {
            SNdd36 = SNdd36 + (365 + LEAP(igg));
            igg++;
        } // end while
        igg = result_year;

        while (igg < DD48)
        {
            SNdd48 = SNdd48 + (365 + LEAP(igg));
            igg++;
        } // end while
        igg = result_year;

        while (igg < DD60)
        {
            SNdd60 = SNdd60 + (365 + LEAP(igg));
            igg++;
        } // end while
        igg = result_year;

        while (igg < DD72)
        {
            SNdd72 = SNdd72 + (365 + LEAP(igg));
            igg++;
        } // end while

// [Action in Frame 5]

        String ddggstring = String.valueOf(faithGraph);
        String invddggstring = String.valueOf(willGraph);
        Calendar now = Calendar.getInstance();
        int NowDate = now.get(Calendar.DATE);
        int NowMonth = now.get(Calendar.MONTH) + 1;
        int NowYear = now.get(Calendar.YEAR);
        int age = 0;
        int gg = result_year;


        if (result_month < NowMonth)
        {
            age = NowYear - gg;

        } // end if
        if (result_month > NowMonth)
        {
            age = NowYear - result_year;

        } // end if
        if (!(result_day >= NowDate || result_month != NowMonth))
        {
            age = NowYear - gg;

        } // end if
        if (!(result_day != NowDate || result_month != NowMonth))
        {
            age = NowYear - gg;

        } // end if
        if (!(result_day <= NowDate || result_month != NowMonth))
        {
            age = NowYear - gg;

        } // end if

        int X1 = 0;
        int X2 = 12;
        int X3 = 24;
        int X4 = 36;
        int X5 = 48;
        int X6 = 60;
        int X7 = 72;

        int Yr1 = -10 * Character.getNumericValue(ddggstring.charAt(0));
        int Yr2 = -10 * Character.getNumericValue(ddggstring.charAt(1));
        int Yr3 = -10 * Character.getNumericValue(ddggstring.charAt(2));
        int Yr4 = -10 * Character.getNumericValue(ddggstring.charAt(3));
        int Yr5 = -10 * Character.getNumericValue(ddggstring.charAt(4));
        int Yr6 = -10 * Character.getNumericValue(ddggstring.charAt(5));
        int Yr7 = -10 * Character.getNumericValue(ddggstring.charAt(6));
        int Yr = 3561;


        if (!(age < X1 || age > X2))
        {
            Yr = (Yr2 - Yr1) / (X2 - X1) * age + (Yr1 - (Yr2 - Yr1) / (X2 - X1) * X1);

        } // end if
        if (!(age < X2 || age > X3))
        {
            Yr = (Yr3 - Yr2) / (X3 - X2) * age + (Yr2 - (Yr3 - Yr2) / (X3 - X2) * X2);

        } // end if
        if (!(age < X3 || age > X4))
        {
            Yr = (Yr4 - Yr3) / (X4 - X3) * age + (Yr3 - (Yr4 - Yr3) / (X4 - X3) * X3);

        } // end if
        if (!(age < X4 || age > X5))
        {
            Yr = (Yr5 - Yr4) / (X5 - X4) * age + (Yr4 - (Yr5 - Yr4) / (X5 - X4) * X4);

        } // end if
        if (!(age < X5 || age > X6))
        {
            Yr = (Yr6 - Yr5) / (X6 - X5) * age + (Yr5 - (Yr6 - Yr5) / (X6 - X5) * X5);

        } // end if
        if (!(age < X6 || age > X7))
        {
            Yr = (Yr7 - Yr6) / (X7 - X6) * age + (Yr6 - (Yr7 - Yr6) / (X7 - X6) * X6);

        } // end if
        int Yr_out = Yr / -10;

        int Yb1 = -10 * Character.getNumericValue(invddggstring.charAt(0));
        int Yb2 = -10 * Character.getNumericValue(invddggstring.charAt(1));
        int Yb3 = -10 * Character.getNumericValue(invddggstring.charAt(2));
        int Yb4 = -10 * Character.getNumericValue(invddggstring.charAt(3));
        int Yb5 = -10 * Character.getNumericValue(invddggstring.charAt(4));
        int Yb6 = -10 * Character.getNumericValue(invddggstring.charAt(5));
        int Yb7 = -10 * Character.getNumericValue(invddggstring.charAt(6));

        X1 = 0;
        X2 = SNdd12;
        X3 = SNdd24;
        X4 = SNdd36;
        X5 = SNdd48;
        X6 = SNdd60;
        X7 = SNdd72;

        String XX12 = result_day + "." + result_month + "." + (result_year + 12);
        String XX24 = result_day + "." + result_month + "." + (result_year + 24);
        String XX36 = result_day + "." + result_month + "." + (result_year + 36);
        String XX48 = result_day + "." + result_month + "." + (result_year + 48);
        String XX60 = result_day + "." + result_month + "." + (result_year + 60);
        String XX72 = result_day + "." + result_month + "." + (result_year + 72);
        int Z0012 = (Yr2 - Yr1) / (X2 - X1) - (Yb2 - Yb1) / (X2 - X1);


        int X0012 = 0;
        int X1224 = 0;
        int X2436 = 0;
        int X3648 = 0;
        int X4860 = 0;
        int X6072 = 0;

        if (Z0012 != 0)
        {
            X0012 = (Yb1 - X1 * ((Yb2 - Yb1) / (X2 - X1)) - (Yr1 - X1 * ((Yr2 - Yr1) / (X2 - X1)))) / Z0012;

        } // end if
        int Z1224 = (Yr3 - Yr2) / (X3 - X2) - (Yb3 - Yb2) / (X3 - X2);
        if (Z1224 != 0)
        {
            X1224 = (Yb2 - X2 * ((Yb3 - Yb2) / (X3 - X2)) - (Yr2 - X2 * ((Yr3 - Yr2) / (X3 - X2)))) / ((Yr3 - Yr2) / (X3 - X2) - (Yb3 - Yb2) / (X3 - X2));

        } // end if
        int Z2436 = (Yr4 - Yr3) / (X4 - X3) - (Yb4 - Yb3) / (X4 - X3);
        if (Z2436 != 0)
        {
            X2436 = (Yb3 - X3 * ((Yb4 - Yb3) / (X4 - X3)) - (Yr3 - X3 * ((Yr4 - Yr3) / (X4 - X3)))) / ((Yr4 - Yr3) / (X4 - X3) - (Yb4 - Yb3) / (X4 - X3));

        } // end if
        int Z3648 = (Yr5 - Yr4) / (X5 - X4) - (Yb5 - Yb4) / (X5 - X4);
        if (Z3648 != 0)
        {
            X3648 = (Yb4 - X4 * ((Yb5 - Yb4) / (X5 - X4)) - (Yr4 - X4 * ((Yr5 - Yr4) / (X5 - X4)))) / ((Yr5 - Yr4) / (X5 - X4) - (Yb5 - Yb4) / (X5 - X4));

        } // end if
        int Z4860 = (Yr6 - Yr5) / (X6 - X5) - (Yb6 - Yb5) / (X6 - X5);




        X4860 = (Yb5 - X5 * ((Yb6 - Yb5) / (X6 - X5)) - (Yr5 - X5 * ((Yr6 - Yr5) / (X6 - X5)))) / ((Yr6 - Yr5) / (X6 - X5) - (Yb6 - Yb5) / (X6 - X5));





        int Z6072 = (Yr7 - Yr6) / (X7 - X6) - (Yb7 - Yb6) / (X7 - X6);
        if (Z6072 != 0)
        {
            X6072 = (Yb6 - X6 * ((Yb7 - Yb6) / (X7 - X6)) - (Yr6 - X6 * ((Yr7 - Yr6) / (X7 - X6)))) / ((Yr7 - Yr6) / (X7 - X6) - (Yb7 - Yb6) / (X7 - X6));

        } // end if
        double X0012_out = Math.floor(X0012);
        double X1224_out = Math.floor(X1224);
        double X2436_out = Math.floor(X2436);
        double X3648_out = Math.floor(X3648);
        double X4860_out = Math.floor(X4860);
        double X6072_out = Math.floor(X6072);
        String T0012 = String.valueOf(X0012_out);
        String T1224 = String.valueOf(X1224_out);
        String T2436 = String.valueOf(X2436_out);
        String T3648 = String.valueOf(X3648_out);
        String T4860 = String.valueOf(X4860_out);
        String T6072 = String.valueOf(X6072_out);
        if (Z0012 != 0)
        {
            T0012 = Long_dd(result_day, result_month, result_year, X0012_out) + "." + Long_mm(result_day, result_month, result_year, X0012_out) + "." + Long_gg(result_day, result_month, result_year, X0012_out);

        } // end if
        if (Z1224 != 0)
        {
            T1224 = Long_dd(result_day, result_month, result_year, X1224_out) + "." + Long_mm(result_day, result_month, result_year, X1224_out) + "." + Long_gg(result_day, result_month, result_year, X1224_out);

        } // end if
        if (Z2436 != 0)
        {
            T2436 = Long_dd(result_day, result_month, result_year, X2436_out) + "." + Long_mm(result_day, result_month, result_year, X2436_out) + "." + Long_gg(result_day, result_month, result_year, X2436_out);

        } // end if
        if (Z3648 != 0)
        {
            T3648 = Long_dd(result_day, result_month, result_year, X3648_out) + "." + Long_mm(result_day, result_month, result_year, X3648_out) + "." + Long_gg(result_day, result_month, result_year, X3648_out);

        } // end if
        if (Z4860 != 0)
        {
            T4860 = Long_dd(result_day, result_month, result_year, X4860_out) + "." + Long_mm(result_day, result_month, result_year, X4860_out) + "." + Long_gg(result_day, result_month, result_year, X4860_out);

        } // end if
        if (Z6072 != 0)
        {
            T6072 = Long_dd(result_day, result_month, result_year, X6072_out) + "." + Long_mm(result_day, result_month, result_year, X6072_out) + "." + Long_gg(result_day, result_month, result_year, X6072_out);

        } // end if
        if (!(X0012_out >= X1 && (X0012_out <= X2 && Z0012 != 0)))
        {
            T0012 = "";

        } // end if
        if (!(X1224_out >= X2 && (X1224_out <= X3 && Z1224 != 0)))
        {
            T1224 = "";

        } // end if
        if (!(X2436_out >= X3 && (X2436_out <= X4 && Z2436 != 0)))
        {
            T2436 = "";

        } // end if
        if (!(X3648_out >= X4 && (X3648_out <= X5 && Z3648 != 0)))
        {
            T3648 = "";

        } // end if
        if (!(X4860_out >= X5 && (X4860_out <= X6 && Z4860 != 0)))
        {
            T4860 = "";

        } // end if
        if (!(X6072_out >= X6 && (X6072_out <= X7 && Z6072 != 0)))
        {
            T6072 = "";

        } // end if
        if (X0012 == X1224)
        {
            T1224 = "";

        } // end if
        if (X1224 == X2436)
        {
            T2436 = "";

        } // end if
        if (X2436 == X3648)
        {
            T3648 = "";

        } // end if
        if (X3648 == X4860)
        {
            T4860 = "";

        } // end if
        if (X4860 == X6072)
        {
            T6072 = "";

        } // end if
        if (T0012 == T1224)
        {
            T1224 = "";

        } // end if
        if (T1224 == T2436)
        {
            T2436 = "";

        } // end if
        if (T2436 == T3648)
        {
            T3648 = "";

        } // end if
        if (T3648 == T4860)
        {
            T4860 = "";

        } // end if
        if (T4860 == T6072)
        {
            T6072 = "";

        } // end if

        // Показываем пересечения
    }
}
