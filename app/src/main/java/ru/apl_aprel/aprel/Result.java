package ru.apl_aprel.aprel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Result extends AppCompatActivity {
    /* Menu */
    /* Menu END */

    private int getSumOfStrChars(String str) {
        int strSum = 0;

        for (int i = 0; i < str.length(); i++) {
            strSum += Character.getNumericValue(str.charAt(i));
        }

        return strSum;
    }

    private ArrayList fillChart(int day, int month, int year, boolean replaceZeros) {

        String dayMonthStr = String.format("%02d", day) + String.format("%02d",month);
        String yearStr = String.format("%04d", year);

        if (replaceZeros) {
            dayMonthStr = dayMonthStr.replace("0", "1");
            yearStr = yearStr.replace("0", "1");
        }

        int dayMonthInt = Integer.parseInt(dayMonthStr, 10);
        year = Integer.parseInt(yearStr, 10);

        String sumStr = String.valueOf(dayMonthInt * year);

        ArrayList<Integer> graphNums = new ArrayList<>();

        for (int i = sumStr.length() - 1; i >= 0; i--) {
           graphNums.add(Character.getNumericValue(sumStr.charAt(i)));
        }

        if (graphNums.size() < 7) {
            graphNums.add(6, 0);
        }

        Collections.reverse(graphNums);

        return graphNums;
    }


    private PointF intersectionPoint(
            int x1,int y1,int x2,int y2,
            int x3, int y3, int x4,int y4
    ) {
        // if the lines intersect, the result contains the x and y of the intersection (treating the lines as infinite) and booleans for whether line segment 1 or line segment 2 contain the point
        float denominator, a, b, numerator1, numerator2, x, y;
        boolean onLine1 = false;
        boolean onLine2 = false;
        PointF intrsctn = new PointF();

        if(y2 == y4) {
            intrsctn.set((float) x2, (float) y2);
            return intrsctn;
        }

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

    private ArrayList findChartIntersect(ArrayList<Integer> will, ArrayList<Integer> faith, ArrayList<Integer> years) {
        ArrayList<PointF> crosses = new ArrayList<>();

        Integer birthWill = will.get(0);
        Integer birthFaith = faith.get(0);
        Integer twelveWill = will.get(1);
        Integer twelveFaith = faith.get(1);

        // Ищем точку рождения, если пересекаются, пишем в массив точек
        if(birthWill.equals(birthFaith) && !twelveWill.equals(twelveFaith)) {
            PointF firstPoint = new PointF();
            firstPoint.set(0, (float) birthFaith);
            crosses.add(firstPoint);
        }

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
        final boolean correct_form = getIntent().getBooleanExtra("correct_form", false);
        final boolean fromSaved = getIntent().getBooleanExtra("from", false);

        // from NewCalc
        if (correct_form) {
                setTitle(result_name + " " + result_surname);
        }

        // from Saved
        if (fromSaved) {
            setTitle("[сохр.] " + result_name + " " + result_surname);
        }

        // По нажатию "сохранить" переходим
        final Intent savedIntent = new Intent(this, AprelTabLayout.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Включаем тулбар

        Toolbar result_toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(result_toolbar);
        ActionBar result_ab = getSupportActionBar();
        result_ab.setDisplayHomeAsUpEnabled(true);

        // Дату в строку

        String dateToStr =
                String.format("%02d", result_day) +
                String.format("%02d", result_month) +
                String.format("%04d", result_year);

        String dateToFormatStr =
                String.format("%02d", result_day) + "." +
                String.format("%02d", result_month) + "." +
                String.format("%04d", result_year);


        // Складываем числа между собой (Сумма A)

        int magicSumA = getSumOfStrChars(dateToStr);

        int magicConst;
        if (result_year < 1999) {
            magicConst = -2;
        } else {
            magicConst = 19;
        }


        // Складываем числа между собой (Сумма B)

        int magicSumB = getSumOfStrChars(String.valueOf(magicSumA));


        // Число C

        int magicSumC;
            magicSumC = magicSumA + magicConst;


        // Число D

        int magicSumD = getSumOfStrChars(String.valueOf(magicSumC));

        // Строим матрицу!
        String magicFinalStr =
                dateToStr +
                magicSumA +
                magicSumB +
                magicSumC +
                magicSumD +
                Math.abs(magicConst);

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

        // Итогговая цифра
        final TextView table_final_digit = (TextView) findViewById(R.id.table_final_digit);
        table_final_digit.setText(String.valueOf(magicFinalDigit));


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

        // Мужские цифры
        final TextView table_man_digit = (TextView) findViewById(R.id.table_man_digit);
        table_man_digit.setText(String.valueOf(manDigit));

        // Мженские цифры
        final TextView table_woman_digit = (TextView) findViewById(R.id.table_woman_digit);
        table_woman_digit.setText(String.valueOf(womanDigit));


        // Пишем для какой даты расчет в заголовок перед графиком
        TextView dateTitle = (TextView) findViewById(R.id.dateTitle);
        dateTitle.append(dateToFormatStr);

        // График

        ArrayList<Integer> faithChartVal = fillChart(result_day, result_month, result_year, false);
        ArrayList<Integer> willChartVal = fillChart(result_day, result_month, result_year, true);
        ArrayList<Integer> yearsForChart = new ArrayList<>();
        for (int i=0; i<=72; i+=12){
            yearsForChart.add(i);
        }

        ArrayList<PointF> crossPoints = findChartIntersect(willChartVal, faithChartVal, yearsForChart);

        TextView intrsctnTableTitle = (TextView) findViewById(R.id.intersectionsTableTitle);
        intrsctnTableTitle.setText("Точки пересечения");

        // Таблица с пересечениями
        TableLayout intrsctnTable = (TableLayout) findViewById(R.id.intersectionsTable);

        // Добаляем заголовок
        TableRow intrsctnHeader = (TableRow) getLayoutInflater().inflate(R.layout.tpl_intersection_row_header, intrsctnTable, false);
        intrsctnTable.addView(intrsctnHeader);

        // Наполняем таблицу
        for(int i = 0; i < crossPoints.size(); i++) {
            if (crossPoints.get(i) != null) {

                PointF point = crossPoints.get(i);

                TableRow intrsctnRow = (TableRow) getLayoutInflater().inflate(R.layout.tpl_intersection_row, intrsctnTable, false);
                TextView date = (TextView) intrsctnRow.findViewById(R.id.insctnDate);
                TextView age = (TextView) intrsctnRow.findViewById(R.id.insctnAge);

                String birthDate = result_day + "." + result_month + "." + result_year;  // Start date
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(sdf.parse(birthDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int yearsToAdd = (int) point.x;
                float daysToAdd = (point.x - yearsToAdd) * 365; // Получаем десятичную дробь и сичтаем сколько это в днях

                calendar.add(Calendar.YEAR, yearsToAdd);
                calendar.add(Calendar.DATE, (int) daysToAdd);

                String outputDate = sdf.format(calendar.getTime());
                date.setText(outputDate);

                age.setText(String.valueOf(yearsToAdd));
                intrsctnTable.addView(intrsctnRow);
            }
        }

        //TextView testTextArea = (TextView) findViewById(R.id.testTextArea);
//          testTextArea.append("\n Пересечение: " + crossPoints.size());
 //       testTextArea.append("\n Точечки: " + Arrays.toString(crossPoints.toArray()));
//          testTextArea.append("\n Годы: " + Arrays.toString(yearsForChart.toArray()));

//        testTextArea.append("\n Судьба: " + Arrays.toString(faithChartVal.toArray()));
//        testTextArea.append("\n Воля: " + Arrays.toString(willChartVal.toArray()));

        LineChart chart = (LineChart) findViewById(R.id.chartGraph);

        List<Entry> faithPoints = new ArrayList<>();
        List<Entry> willPoints = new ArrayList<>();
        List<Entry> intersectPoints = new ArrayList<>();
        List<Entry> todayPoints = new ArrayList<>();

        // Готовим волю и судьюу
        for(int i=0; i < yearsForChart.size(); i++) {
            faithPoints.add(new Entry(yearsForChart.get(i), faithChartVal.get(i)));
            willPoints.add(new Entry(yearsForChart.get(i), willChartVal.get(i)));
        }

        // Готовим пересечения
        for (int i = 0; i < crossPoints.size(); i++) {
            if (crossPoints.get(i) != null) {
                PointF point = crossPoints.get(i);
                intersectPoints.add(new Entry(point.x, point.y));
            }
        }

        // Готовим линию сегодня
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        float todayX = currYear - result_year;
        todayPoints.add(new Entry(todayX, 0f));
        todayPoints.add(new Entry(todayX, 9f));

        LineDataSet faithDataSet = new LineDataSet(faithPoints, "Судьба"); // add entries to dataset
        faithDataSet.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorGreen));
        faithDataSet.setDrawValues(false);
        faithDataSet.setCircleColor(ContextCompat.getColor(getBaseContext(), R.color.colorGreen));
        faithDataSet.setCircleColorHole(R.color.colorPrimaryDark);
        faithDataSet.setLineWidth(3f);

        LineDataSet willDataSet = new LineDataSet(willPoints, "Воля"); // add entries to dataset
        willDataSet.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorYellow));
        willDataSet.setDrawValues(false);
        willDataSet.setCircleColor(ContextCompat.getColor(getBaseContext(), R.color.colorYellow));
        willDataSet.setCircleColorHole(R.color.colorPrimaryDark);
        willDataSet.setLineWidth(3f);

        LineDataSet intersectDataSet = new LineDataSet(intersectPoints, "Пересечения"); // add entries to dataset
        intersectDataSet.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorInrsctPoint));
        intersectDataSet.setDrawValues(false);
        intersectDataSet.setCircleColor(ContextCompat.getColor(getBaseContext(), R.color.colorInrsctPoint));
        intersectDataSet.setCircleColorHole(R.color.colorInrsctPoint);
        intersectDataSet.setCircleRadius(6f);
        intersectDataSet.setCircleHoleRadius(3f);
        intersectDataSet.setLineWidth(0f);
        intersectDataSet.enableDashedLine(0f, 400f, 0f);

        LineDataSet todayDataSet = new LineDataSet(todayPoints, "Сегодня"); // add entries to dataset
        todayDataSet.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorTodayLine));
        todayDataSet.setDrawValues(false);
        todayDataSet.setDrawCircles(false);
        todayDataSet.setLineWidth(3f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(faithDataSet);
        dataSets.add(willDataSet);
        dataSets.add(intersectDataSet);
        dataSets.add(todayDataSet);

        LineData FinalChart = new LineData(dataSets);

        chart.setData(FinalChart);
        chart.setDescription("График судьбы и воли");
        chart.setDescriptionTextSize(14f);
        chart.setDescriptionColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary200));
        chart.setTouchEnabled(false);
        //chart.animateY(2000, Easing.EasingOption.EaseOutCirc);
        //chart.animateXY(1000, 1000);

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
        bttmAxis.setTextSize(12f);
        bttmAxis.setGranularity(12f);
        bttmAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bttmAxis.setAvoidFirstLastClipping(true);

        // Легенда

        Legend l = chart.getLegend();
        l.setEnabled(true); // set the size of the legend forms/shapes
        l.setFormSize(8f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(12f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(2f); // set the space between the legend entries on the y-axis
        l.setWordWrapEnabled(true);

        chart.invalidate();

        // Сохраняем данные

        Button result_save = (Button) findViewById(R.id.result_save);

        if (!correct_form || fromSaved) {
            result_save.setEnabled(false);
            result_save.setBackgroundResource(R.drawable.disabled_button);
            result_save.setTextColor(getResources().getColor(R.color.colorPrimary400));
        } else {
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

                String searchQuery = "SELECT * FROM " + DbHelper.TABLE_NAME;
                Cursor cursor = db.rawQuery(searchQuery, null);

                savedIntent.putExtra("tabToOpen", 1);
                startActivity(savedIntent);
                cursor.close();
                db.close();
                }
            });
        }
    }
}
