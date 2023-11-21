package com.example.a2100990_0927;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static int r, cx, cy, layoutWidth, layoutHeight, dx, dy;
    static int rx, ry, w, up = 1;
    MyGraphView myGraphView;
    LinearLayout linearLayout;
    static TextView textView, textView2;
    static Button btn1, btn2;
    SensorManager sensorManager;
    Sensor accelerometerSensor;
    static Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        myGraphView = new MyGraphView(this);
        //setContentView(myGraphView);
        linearLayout = (LinearLayout) findViewById(R.id.ViewLayout);
        linearLayout.addView(myGraphView);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        Random random = new Random();

        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                up = 1;
                r = (int) (layoutHeight * 0.02);
                w = r * 2;
                rx = random.nextInt(layoutWidth - 2 * w) + w;
                ry = random.nextInt(layoutHeight - 2 * w) + w;
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Random random = new Random();
        layoutWidth = linearLayout.getWidth();
        layoutHeight = linearLayout.getHeight();
        cx = (int) (layoutWidth / 2);
        cy = (int) (layoutHeight / 2);
        r = (int) (layoutHeight * 0.02);

        w = r * 2;
        rx = random.nextInt(layoutWidth - 2 * w) + w;
        ry = random.nextInt(layoutHeight - 2 * w) + w;

        myGraphView.invalidate();
        //textView.setText("width = "+layoutWidth+"height = "+layoutheight);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private static class MyGraphView extends View {
        public MyGraphView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.MAGENTA);
            Paint paint1 = new Paint();
            paint1.setColor(Color.BLUE);
            Random random = new Random();

            canvas.drawRect(rx - w, ry - w, rx + w, ry + w, paint1);

            canvas.drawCircle(cx, cy, r, paint);
            chronometer.start();

            long current = SystemClock.elapsedRealtime() - chronometer.getBase();
            int time = (int) (current / 1000);
            int sec = time % 60;

            textView2.setText(up + "단계");

            if(up<=4) {
                if (sec <= 15) {
                    if (cx - r > rx - w && cx + r < rx + w && cy - r > ry - w && cy + r < ry + w) {
                        //textView.setText("들어옴");
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        if (up < 8) {
                            textView.setText("안들어옴");
                            up += 1;
                            w -= 5;
                            rx = random.nextInt(layoutWidth - 2 * w) + w;
                            ry = random.nextInt(layoutHeight - 2 * w) + w;
                        } else if (up == 8) {
                            textView.setText("게임이 끝났습니다.");
                        }
                    } else {
                        textView.setText("안들어옴");
                    }
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    rx = random.nextInt(layoutWidth - 2 * w) + w;
                    ry = random.nextInt(layoutHeight - 2 * w) + w;
                }
            } else {
                if (sec <= 8) {
                    if (cx - r > rx - w && cx + r < rx + w && cy - r > ry - w && cy + r < ry + w) {
                        //textView.setText("들어옴");
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        if (up < 8) {
                            textView.setText("안들어옴");
                            up += 1;
                            w -= 5;
                            rx = random.nextInt(layoutWidth - 2 * w) + w;
                            ry = random.nextInt(layoutHeight - 2 * w) + w;
                        } else if (up == 8) {
                            textView.setText("게임이 끝났습니다.");
                        }
                    } else {
                        textView.setText("안들어옴");
                    }
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    rx = random.nextInt(layoutWidth - 2 * w) + w;
                    ry = random.nextInt(layoutHeight - 2 * w) + w;
                }
            }
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rx = random.nextInt(layoutWidth - 2 * w) + w;
                    ry = random.nextInt(layoutHeight - 2 * w) + w;
                }
            });
        }
    }

    final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //textView.setText("x축 : "+event.values[0]+"\ny축 : "+event.values[1]+"\nz축 : "+event.values[2]);
                if (up == 8) {
                    if (cx - r > rx - w && cx + r < rx + w && cy - r > ry - w && cy + r < ry + w) {
                        dx = 0;
                        dy = 0;
                    } else {
                        dx = -((int) event.values[0] * (up + 1));
                        dy = +((int) event.values[1] * (up + 1));
                    }
                } else {
                    dx = -((int) event.values[0] * (up + 1));
                    dy = +((int) event.values[1] * (up + 1));
                }

                if (cx - r + dx > 0 && cx + r + dx < layoutWidth) {
                    cx = cx + dx;
                }
                if (cy - r + dy > 0 && cy + r + dy < layoutHeight) {
                    cy = cy + dy;
                }
                myGraphView.invalidate();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}