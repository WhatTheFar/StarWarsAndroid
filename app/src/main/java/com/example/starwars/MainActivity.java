//Jakpat Mingmongkolmitr 5931217221
package com.example.starwars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private StarWarsView starWarsView;
    private Button pauseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();
    }

    public void initInstances() {
        TextView textview = (TextView) findViewById(R.id.text_view);
        starWarsView = (StarWarsView) findViewById(R.id.star_wars);
        Button slowBtn = (Button) findViewById(R.id.slow_btn);
        Button fastBtn = (Button) findViewById(R.id.fast_btn);
        pauseBtn = (Button) findViewById(R.id.pause_btn);
        Button restartBtn = (Button) findViewById(R.id.reset_btn);

        textview.setText("Jakpat Mingmongkolmitr 5931217221");

        slowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starWarsView.increaseDelay();
            }
        });

        fastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starWarsView.decreaseDelay();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                if (starWarsView.isAnimationStop()) {
                    starWarsView.startAnimate();
                    btn.setText("Pause");
                } else {
                    starWarsView.stopAnimate();
                    btn.setText("Resume");
                }
            }
        });

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starWarsView.restartAnimation();
                pauseBtn.setText("Pause");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf("Activity", "onResume");
        if (pauseBtn.getText() == "Resume") {
            starWarsView.stopAnimate();
            return;
        }
        starWarsView.startAnimate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.wtf("Activity", "onPause");
        starWarsView.stopAnimate();

    }
}
