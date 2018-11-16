package eu.z3r0byteapps.soniq;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.canelmas.let.AskPermission;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import eu.z3r0byteapps.soniq.Containers.Search;
import eu.z3r0byteapps.soniq.Containers.SearchResult;
import eu.z3r0byteapps.soniq.Networking.HttpGet;
import eu.z3r0byteapps.soniq.Networking.HttpPost;
import eu.z3r0byteapps.soniq.Util.ConfigUtil;
import me.tankery.app.dynamicsinewave.DynamicSineWaveView;

public class MainActivity extends AppCompatActivity implements RuntimePermissionListener {
    private static final String TAG = "MainActivity";

    DynamicSineWaveView dynamicSineWaveView;
    CircularProgressButton listenButton;
    CardView resultCard;
    TextView titel;
    TextView artist;
    TextInputLayout apiEditTextLayout;
    EditText apiEditText;

    AudioRecord audioRecord;
    Boolean recorderInitialized = false;
    Boolean resultFound = false;

    Search search = new Search();

    String backendUrl = "";

    short[] recordedAudio;
    int requestsSubmitted = 0;
    int requestsFinished = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConfigUtil configUtil = new ConfigUtil(this);
        backendUrl = configUtil.getString("api_url", "");

        resultCard = findViewById(R.id.result);
        titel = findViewById(R.id.titel);
        artist = findViewById(R.id.artiest);
        apiEditTextLayout = findViewById(R.id.api_url_layout);
        apiEditText = apiEditTextLayout.getEditText();

        resultCard.setVisibility(View.INVISIBLE);

        listenButton = findViewById(R.id.listen);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultCard.setVisibility(View.INVISIBLE);
                dynamicSineWaveView.startAnimation();
                dynamicSineWaveView.setVisibility(View.VISIBLE);
                listenButton.startAnimation();
                initializeRecording();
                if (recorderInitialized) {
                    startListening();
                } else {
                    dynamicSineWaveView.setVisibility(View.INVISIBLE);
                    listenButton.revertAnimation(new OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {
                            dynamicSineWaveView.stopAnimation();
                        }
                    });
                    Log.e(TAG, "onClick: Error while initializing AudioRecorder");
                    Toast.makeText(MainActivity.this, R.string.fout_initialiseren_microfoon, Toast.LENGTH_SHORT).show();
                }
            }
        });

        apiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                if (!input.startsWith("http://") && !input.startsWith("https://"))
                    input = "http://" + input;
                configUtil.setString("api_url", input);
                backendUrl = input;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        apiEditText.setText(backendUrl);


        dynamicSineWaveView = findViewById(R.id.sine_view);

        dynamicSineWaveView.addWave(0.5f, 4.0f, 0, 0, 0);
        dynamicSineWaveView.addWave(0.5f, 1.1f, 0.5f, getResources().getColor(R.color.colorPrimary), 5f);
        dynamicSineWaveView.addWave(0.3f, 2f, 0.7f, getResources().getColor(R.color.colorAccent), 5f);
    }


    private void startListening() {
        requestsSubmitted = 0;
        requestsFinished = 0;
        resultFound = false;
        search.setSearchId("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    search = new Gson().fromJson(HttpGet.get(backendUrl + "/search/new"), Search.class);
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResult();
                        }
                    });
                    return;
                }
                if (search == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResult();
                        }
                    });
                    return;
                }

                audioRecord.startRecording();
                int readSize = 88200;
                for (int i = 0; i < 5; i++) {
                    recordedAudio = new short[readSize];
                    if (resultFound) break;
                    audioRecord.read(recordedAudio, 0, readSize);
                    if (resultFound) break;
                    matchAudio(recordedAudio);
                }
                audioRecord.release();
            }
        }).start();
    }

    private void showResult() {
        if (search == null) {
            titel.setText(getString(R.string.fout));
            artist.setText(R.string.geen_verbinding);
            resultCard.setVisibility(View.VISIBLE);
        } else if (search.getSearchResult() == null) {
            titel.setText(getString(R.string.fout));
            artist.setText(getString(R.string.fout_matchen));
            resultCard.setVisibility(View.VISIBLE);
        } else {
            if (search.getSearchResult().getSuccess()) {
                titel.setText(search.getSearchResult().getSong().getTitle());
                artist.setText(search.getSearchResult().getSong().getArtist());
                resultCard.setVisibility(View.VISIBLE);
            } else {
                titel.setText(getString(R.string.geen_match));
                artist.setText(String.format(getString(R.string.beste_gok), search.getSearchResult().getSong().getTitle(), search.getSearchResult().getSong().getArtist()));
                resultCard.setVisibility(View.VISIBLE);
            }
        }
        dynamicSineWaveView.setVisibility(View.INVISIBLE);
        listenButton.revertAnimation(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                dynamicSineWaveView.stopAnimation();
            }
        });
    }

    private void matchAudio(short[] recordedAudio) {
        requestsSubmitted++;
        final short[] soundFragment = recordedAudio;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String searchId = search.getSearchId();
                String jsonAudio = "{ \"data\": " + new Gson().toJson(soundFragment) + "}";
                try {
                    SearchResult result = new Gson().fromJson(HttpPost.post(backendUrl + "/search/data", jsonAudio, search), SearchResult.class);
                    if (resultFound || !searchId.equals(search.getSearchId()))
                        return; //controleren of resultaat nog actueel en nuttig is
                    if (result == null) {
                        resultFound = true;
                        throw new IOException();
                    }

                    if (search.getSearchResult() == null || result.getConfidence() > search.getSearchResult().getConfidence()) {
                        search.setSearchResult(result);
                        if (result.getConfidence() > 5) {
                            resultFound = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showResult();
                                }
                            });
                        }
                    }
                } catch (IOException | IllegalStateException e) {
                    e.printStackTrace();
                } finally {
                    if ((requestsSubmitted == 5 && requestsFinished == 4)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showResult();
                            }
                        });
                    }
                    requestsFinished++;
                }
            }
        }).start();
    }


    @AskPermission(Manifest.permission.RECORD_AUDIO)
    public void initializeRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        recorderInitialized = audioRecord.getRecordingState() == AudioRecord.STATE_INITIALIZED;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        listenButton.dispose();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Let.handle(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onShowPermissionRationale(List<String> permissions, final RuntimePermissionRequest request) {
        request.retry();
    }

    @Override
    public void onPermissionDenied(List<DeniedPermission> deniedPermissionList) {
        Toast.makeText(this, R.string.toegang_microfoon_vereist, Toast.LENGTH_SHORT).show();
    }
}
