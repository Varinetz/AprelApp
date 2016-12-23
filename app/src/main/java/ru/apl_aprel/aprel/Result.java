package ru.apl_aprel.aprel;

import android.support.v7.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
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


    private StringBuffer HTTPrequest(String urlString, HashMap<String, String> params) {
        // TODO Auto-generated method stub
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuffer chaine = new StringBuffer("");
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder();

            for(Map.Entry<String, String> param : params.entrySet()) {
                String key = param.getKey();
                String value = param.getValue();

                builder.appendQueryParameter(key, value);
            }


            String query = builder.build().getEncodedQuery();

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }
        }
        catch (IOException e) {
            // Writing exception to log
            e.printStackTrace();
        }
        return chaine;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
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

        final String dateToStr =
                String.format("%02d", result_day) +
                String.format("%02d", result_month) +
                String.format("%04d", result_year);

        final String dateToFormatStr =
                String.format("%02d", result_day) + "." +
                String.format("%02d", result_month) + "." +
                String.format("%04d", result_year);


        // Складываем числа между собой (Сумма A)

        int magicSumA = getSumOfStrChars(dateToStr);

        int magicConst;
        if (result_year <= 1999) {
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

        final String[] matrix = new String[10];
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

        final String magicFinalDigitStr = String.valueOf(magicFinalDigit);

        // Итогговая цифра
        final TextView table_final_digit = (TextView) findViewById(R.id.table_final_digit);
        table_final_digit.setText(magicFinalDigitStr);


        // Мужские и женские цифры
        final int manDigit =
                matrix[2].length() +
                matrix[4].length() +
                matrix[6].length() +
                matrix[8].length();
        final int womanDigit =
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
        final ArrayList<Integer> yearsForChart = new ArrayList<>();
        for (int i=0; i<=72; i+=12){
            yearsForChart.add(i);
        }

        ArrayList<PointF> crossPoints = findChartIntersect(willChartVal, faithChartVal, yearsForChart);
        final ArrayList<String> crossPointsString = new ArrayList<>();

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

                // Добавляем пару дата/возраст в массив
                crossPointsString.add(outputDate + "-" + String.valueOf(yearsToAdd));
            }
        }

        //TextView testTextArea = (TextView) findViewById(R.id.testTextArea);
//          testTextArea.append("\n Пересечение: " + crossPoints.size());
 //       testTextArea.append("\n Точечки: " + Arrays.toString(crossPoints.toArray()));
//          testTextArea.append("\n Годы: " + Arrays.toString(yearsForChart.toArray()));

//        testTextArea.append("\n Судьба: " + Arrays.toString(faithChartVal.toArray()));
//        testTextArea.append("\n Воля: " + Arrays.toString(willChartVal.toArray()));

        LineChart chart = (LineChart) findViewById(R.id.chartGraph);
        LineChart chartHidden = (LineChart) findViewById(R.id.chartGraphHidden);

        List<Entry> faithPoints = new ArrayList<>();
        List<Entry> willPoints = new ArrayList<>();
        List<Entry> intersectPoints = new ArrayList<>();
        List<Entry> todayPoints = new ArrayList<>();

        // Готовим волю и судьбу
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

        ValueFormatter intersectionsValueFormatter = new ValueFormatter() {

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                return String.valueOf((int) entry.getX());
            }
        };

        LineDataSet intersectDataSet = new LineDataSet(intersectPoints, "Пересечения"); // add entries to dataset
        intersectDataSet.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorInrsctPoint));
        intersectDataSet.setValueTextColor(Color.WHITE);
        intersectDataSet.setValueTextSize(16f);
        //intersectDataSet.setDrawValues(false);
        intersectDataSet.setCircleColor(ContextCompat.getColor(getBaseContext(), R.color.colorInrsctPoint));
        intersectDataSet.setCircleColorHole(R.color.colorInrsctPoint);
        intersectDataSet.setCircleRadius(6f);
        intersectDataSet.setCircleHoleRadius(3f);
        intersectDataSet.setLineWidth(0f);
        intersectDataSet.enableDashedLine(0f, 400f, 0f);
        intersectDataSet.setValueFormatter(intersectionsValueFormatter);


        // Дублируем пересечения для скрытого графика
        LineDataSet intersectDataSetHidden = new LineDataSet(intersectPoints, "Пересечения"); // add entries to dataset
        intersectDataSetHidden.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorInrsctPoint));
        intersectDataSetHidden.setValueTextColor(Color.BLACK);
        intersectDataSetHidden.setValueTextSize(16f);
        // intersectDataSetHidden.setDrawValues(false);
        intersectDataSetHidden.setCircleColor(ContextCompat.getColor(getBaseContext(), R.color.colorInrsctPoint));
        intersectDataSetHidden.setCircleColorHole(R.color.colorInrsctPoint);
        intersectDataSetHidden.setCircleRadius(6f);
        intersectDataSetHidden.setCircleHoleRadius(3f);
        intersectDataSetHidden.setLineWidth(0f);
        intersectDataSetHidden.enableDashedLine(0f, 400f, 0f);
        intersectDataSetHidden.setValueFormatter(intersectionsValueFormatter);

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

        // Дублируем данные для скрытого графика
        List<ILineDataSet> dataSetsHidden = new ArrayList<>();
        dataSetsHidden.add(faithDataSet);
        dataSetsHidden.add(willDataSet);
        dataSetsHidden.add(intersectDataSetHidden);
        dataSetsHidden.add(todayDataSet);

        LineData FinalChart = new LineData(dataSets);

        LineData FinalChartHidden = new LineData(dataSetsHidden);

        chart.setExtraOffsets(0, 45, 0, 15);
        chart.setData(FinalChart);
        chart.setDescription("");
        chart.setTouchEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12f);
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMaxValue(9);
        leftAxis.setAxisMinValue(0);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setLabelCount(10, true); // force 6 labels

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);

        AxisValueFormatter xAxisFormatter = new AxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value != 0) {
                    return String.valueOf((int) value);
                } else {
                    return "";
                }
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        };

        XAxis bttmAxis = chart.getXAxis();
        bttmAxis.setTextColor(Color.WHITE);
        bttmAxis.setTextSize(12f);
        bttmAxis.setGranularity(12f);
        bttmAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bttmAxis.setAvoidFirstLastClipping(true);
        bttmAxis.setValueFormatter(xAxisFormatter);

        // Легенда
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.invalidate();



        // Hidden chart
        chartHidden.setData(FinalChartHidden);
        chartHidden.setDescription("");
        chartHidden.setExtraOffsets(0, 45, 0, 15);
        chartHidden.setTouchEnabled(false);

        YAxis leftAxisHidden = chartHidden.getAxisLeft();
        leftAxisHidden.setTextColor(Color.BLACK);
        leftAxisHidden.setTextSize(12f);
        leftAxisHidden.setGranularity(1f);
        leftAxisHidden.setAxisMaxValue(9);
        leftAxisHidden.setAxisMinValue(0);
        leftAxisHidden.setGranularityEnabled(true);
        leftAxisHidden.setLabelCount(10, true); // force 6 labels

        YAxis rightAxisHidden = chartHidden.getAxisRight();
        rightAxisHidden.setEnabled(true);
        rightAxisHidden.setDrawLabels(false);
        rightAxisHidden.setDrawGridLines(false);

        XAxis bttmAxisHidden = chartHidden.getXAxis();
        bttmAxisHidden.setTextColor(Color.BLACK);
        bttmAxisHidden.setTextSize(12f);
        bttmAxisHidden.setGranularity(12f);
        bttmAxisHidden.setPosition(XAxis.XAxisPosition.BOTTOM);
        bttmAxisHidden.setAvoidFirstLastClipping(true);
        bttmAxisHidden.setValueFormatter(xAxisFormatter);

        Legend lHidden = chartHidden.getLegend();
        lHidden.setEnabled(false);

        chartHidden.invalidate();


        // Отключаем кнопку отправки пока функция не готова
        Button result_send = (Button) findViewById(R.id.result_send);
//        result_send.setEnabled(false);
//        result_send.setBackgroundResource(R.drawable.disabled_button);
//        result_send.setTextColor(getResources().getColor(R.color.colorPrimary400));
        result_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LineChart chart = (LineChart) findViewById(R.id.chartGraphHidden);
                Bitmap chartImg = chart.getChartBitmap();
                final String chartImgToSend = encodeToBase64(chartImg, Bitmap.CompressFormat.JPEG, 90);
//                Шлем POST запрос

                final Context ctx = getApplicationContext();
                final String urlString = "http://apl-aprel.ru/aprel_app_mailer.php";
                //Toast.makeText(ctx, chartImgToSend, Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(Result.this);
                builder.setTitle("Отправка отчета")
                .setMessage("Введите адрес Email")
                .setIcon(R.drawable.ic_send_white_24dp);

// Set up the input
                final EditText input = new EditText(ctx);
                LinearLayout layout = new LinearLayout(ctx);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(40, 10, 40, 10);
                layout.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT));
                layout.addView(input);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(layout);

// Set up the buttons
                builder.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();

                        HashMap<String, String> requestParams = new HashMap<>();
                        requestParams.put("email", m_Text);
                        requestParams.put("date", dateToFormatStr);
                        requestParams.put("img", chartImgToSend);
                        requestParams.put("final_digit", magicFinalDigitStr);
                        requestParams.put("man_digit", String.valueOf(manDigit));
                        requestParams.put("woman_digit", String.valueOf(womanDigit));

                        for(int i = 0; i < crossPointsString.size(); i++) {
                            requestParams.put("intersections[" + i + "]", crossPointsString.get(i));
                        }

                        for(int i = 0; i < matrix.length; i++) {
                            requestParams.put("matrix[" + i + "]", matrix[i]);
                        }

                        Toast.makeText(ctx, HTTPrequest(urlString, requestParams), Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


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
