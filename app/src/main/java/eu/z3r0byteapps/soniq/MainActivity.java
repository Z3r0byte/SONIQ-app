package eu.z3r0byteapps.soniq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import me.tankery.app.dynamicsinewave.DynamicSineWaveView;

public class MainActivity extends AppCompatActivity {

    DynamicSineWaveView dynamicSineWaveView;
    CircularProgressButton listenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenButton = findViewById(R.id.listen);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicSineWaveView.startAnimation();
                dynamicSineWaveView.setVisibility(View.VISIBLE);
                listenButton.startAnimation();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamicSineWaveView.setVisibility(View.INVISIBLE);
                                listenButton.revertAnimation(new OnAnimationEndListener() {
                                    @Override
                                    public void onAnimationEnd() {
                                        dynamicSineWaveView.stopAnimation();
                                    }
                                });
                            }
                        });
                    }
                }).start();
            }
        });


        dynamicSineWaveView = findViewById(R.id.sine_view);

        dynamicSineWaveView.addWave(0.5f, 4.0f, 0, 0, 0); // Fist wave is for the shape of other waves.
        dynamicSineWaveView.addWave(0.5f, 1.1f, 0.5f, getResources().getColor(R.color.colorPrimary), 5f);
        dynamicSineWaveView.addWave(0.3f, 2f, 0.7f, getResources().getColor(R.color.colorAccent), 5f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        listenButton.dispose();
    }
}
