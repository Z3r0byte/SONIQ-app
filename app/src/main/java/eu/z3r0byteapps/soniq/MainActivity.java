package eu.z3r0byteapps.soniq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import me.tankery.app.dynamicsinewave.DynamicSineWaveView;

public class MainActivity extends AppCompatActivity {

    DynamicSineWaveView dynamicSineWaveView;
    ToggleButton listenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenButton = findViewById(R.id.listen);
        listenButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    dynamicSineWaveView.startAnimation();

                    dynamicSineWaveView.setVisibility(View.VISIBLE);
                } else {
                    dynamicSineWaveView.stopAnimation();
                    dynamicSineWaveView.setVisibility(View.INVISIBLE);
                }
            }
        });


        dynamicSineWaveView = findViewById(R.id.sine_view);

        dynamicSineWaveView.addWave(0.5f, 0.5f, 0, 0, 0); // Fist wave is for the shape of other waves.
        dynamicSineWaveView.addWave(0.5f, 2f, 0.5f, getResources().getColor(android.R.color.holo_red_dark), 5f);
        dynamicSineWaveView.addWave(0.3f, 4f, 0.7f, getResources().getColor(android.R.color.holo_blue_dark), 5f);
    }
}
