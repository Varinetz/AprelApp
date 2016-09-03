package ru.apl_aprel.aprel;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Result extends AppCompatActivity {

    public int getSumOfStrChars(String str) {
        int[] strArr = new int[str.length()];
        int strSum = 0;

        for (int i = 0; i < str.length(); i++) {
            strSum += Character.getNumericValue(str.charAt(i));
        }

        return strSum;
    }

    public ArrayList fillChart(int day, int month, int year, boolean replaceZeros) {

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


    public PointF intersectionPoint(
            int x1,int y1,int x2,int y2,
            int x3, int y3, int x4,int y4
    ) {
        // if the lines intersect, the result contains the x and y of the intersection (treating the lines as infinite) and booleans for whether line segment 1 or line segment 2 contain the point
        float denominator, a, b, numerator1, numerator2, x, y;
        boolean onLine1 = false;
        boolean onLine2 = false;
        PointF intrsctn = new PointF();

        denominator = ((y4 - y3) * (x2 - x1)) - ((x4 - x3) * (y2 - y1));
        if (denominator == 0) {
            return null;
        }
        a = y1 - y3;
        b = x1 - x3;
        numerator1 = ((x4 - x3) * a) - ((y4 - y3) * b);
        numerator2 = ((x2 - x1) * a) - ((y2 - y1) * b);
        a = numerator1 / denominator;
        b = numerator2 / denominator;

        // if we cast these lines infinitely in both directions, they intersect here:
        x = x1 + (a * (x2 - x1));
        y = y1 + (a * (y2 - y1));
        intrsctn.set(x, y); //result x,y
/*
        // it is worth noting that this should be the same as:
        x = x3 + (b * (x4 - x3));
        y = x3 + (b * (y4 - y3));
        */
        // if line1 is a segment and line2 is infinite, they intersect if:
        if (a > 0 && a < 1) {
            onLine1 = true;
        }
        // if line2 is a segment and line1 is infinite, they intersect if:
        if (b > 0 && b < 1) {
            onLine2 = true;
        }
        // if line1 and line2 are segments, they intersect if both of the above are true

        if (!onLine1 && !onLine2) {
            return null;
        }
        return intrsctn;
    }

    public ArrayList findChartCross(ArrayList<Integer> will, ArrayList<Integer> faith, ArrayList<Integer> years) {
        ArrayList<PointF> crosses = new ArrayList<>();


        for (int i = 0; i < years.size() - 1; i++) {
            int startX = years.get(i); //P1x, G1x
            int endX = years.get(i+1); //P2x, G2x

            int willStartY = will.get(i); //P1y
            int willEndY = will.get(i+1); //P2y

            int faithStartY = faith.get(i); //G1y
            int faithEndY = faith.get(i+1); //G2y

            PointF intersectionP = intersectionPoint(startX, faithStartY, endX, faithEndY, startX, willStartY, endX, willEndY);
            crosses.add(intersectionP);
        }

        return crosses;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String result_name = getIntent().getStringExtra("result_name");
        final String result_surname = getIntent().getStringExtra("result_surname");
        final String result_occupation = getIntent().getStringExtra("result_occupation");
        final int result_day = getIntent().getIntExtra("result_day", 0);
        final int result_month = getIntent().getIntExtra("result_month", 0);
        final int result_year = getIntent().getIntExtra("result_year", 0);

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

        ArrayList<Integer> faithChartVal = fillChart(result_day, result_month, result_year, false);
        ArrayList<Integer> willChartVal = fillChart(result_day, result_month, result_year, true);
        ArrayList<Integer> yearsForChart = new ArrayList<>();
        for (int i=0; i<=72; i+=12){
            yearsForChart.add(i);
        }

        ArrayList crossPoints = findChartCross(willChartVal, faithChartVal, yearsForChart);

        testTextArea.append("\n Пересечение: " + crossPoints.size());
        testTextArea.append("\n Точечки: " + Arrays.toString(crossPoints.toArray()));
        testTextArea.append("\n Годы: " + Arrays.toString(yearsForChart.toArray()));

        ArrayList<ArrayList<Integer>> bothGraph = new ArrayList<ArrayList<Integer>>();
        bothGraph.add(faithChartVal);
        bothGraph.add(willChartVal);

        testTextArea.append("\n Судьба: " + Arrays.toString(faithChartVal.toArray()));
        testTextArea.append("\n Воля: " + Arrays.toString(willChartVal.toArray()));

        LineChart chart = (LineChart) findViewById(R.id.chartGraph);

        List<Entry> faithPoints = new ArrayList<>();
        List<Entry> willPoints = new ArrayList<>();

        for(int i=0; i < yearsForChart.size(); i++) {
            faithPoints.add(new Entry(yearsForChart.get(i), faithChartVal.get(i)));
            willPoints.add(new Entry(yearsForChart.get(i), willChartVal.get(i)));
        }




        LineDataSet faithDataSet = new LineDataSet(faithPoints, "Судьба"); // add entries to dataset
        faithDataSet.setColor(Color.YELLOW);
        faithDataSet.setValueTextColor(Color.WHITE);
        faithDataSet.setDrawValues(false);
        faithDataSet.setCircleColor(Color.YELLOW);
        faithDataSet.setCircleColorHole(R.color.colorPrimaryDark);
        faithDataSet.setLineWidth(3f);

        LineDataSet willDataSet = new LineDataSet(willPoints, "Воля"); // add entries to dataset
        willDataSet.setColor(Color.GREEN);
        willDataSet.setValueTextColor(Color.WHITE);
        willDataSet.setDrawValues(false);
        willDataSet.setCircleColor(Color.GREEN);
        willDataSet.setCircleColorHole(R.color.colorPrimaryDark);
        willDataSet.setLineWidth(3f);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(faithDataSet);
        dataSets.add(willDataSet);

        LineData FinalChart = new LineData(dataSets);

        chart.setData(FinalChart);
        chart.setDescription("График судьбы и воли");
        chart.setDescriptionTextSize(14f);
        chart.setDescriptionColor(Color.WHITE);
        chart.setTouchEnabled(false);
        chart.animateXY(100, 100);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(14f);
        leftAxis.setGranularity(1f);
        //leftAxis.setLabelCount(6, true); // force 6 labels

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setTextSize(14f);
        rightAxis.setGranularity(1f);

        XAxis bttmAxis = chart.getXAxis();
        bttmAxis.setTextColor(Color.WHITE);
        bttmAxis.setTextSize(14f);
        bttmAxis.setGranularity(12f);
        bttmAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bttmAxis.setAvoidFirstLastClipping(true);

        // Легенда

        Legend l = chart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.LINE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

        chart.invalidate();

        // Сохраняем данные

        FloatingActionButton result_save = (FloatingActionButton) findViewById(R.id.result_save);

        result_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DbHelper dbOpenHelper = new DbHelper(Result.this);
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DbHelper.NAME, result_name);
                cv.put(DbHelper.SURNAME, result_surname);
                cv.put(DbHelper.OCCUPATION, result_occupation);
                cv.put(DbHelper.DAY, result_day);
                cv.put(DbHelper.MONTH, result_month);
                cv.put(DbHelper.YEAR, result_year);

                db.insert(DbHelper.TABLE_NAME,null,cv);
                db.close();
            }
        });
    }
}
