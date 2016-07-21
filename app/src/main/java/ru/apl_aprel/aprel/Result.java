package ru.apl_aprel.aprel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Arrays;
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
    }
}
