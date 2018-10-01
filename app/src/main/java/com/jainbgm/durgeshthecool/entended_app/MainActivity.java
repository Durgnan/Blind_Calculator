package com.jainbgm.durgeshthecool.entended_app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static com.jainbgm.durgeshthecool.entended_app.calculator.evaluate;
import static com.jainbgm.durgeshthecool.entended_app.calculator.filterOut;
public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextView txtSpeechInput;
    private TextToSpeech tts;
    private EditText txtText;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN))
        {
            Log.w("INFO","Its CEO bitch");
            speakOut("Speak the Expression");
            promptSpeechInput();
        }
        else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP))
        {
            Log.w("INFO","Its CEO bc");
            speakOut("What You Want Us to Do");
            promptSpeechInput();
        }

        return true;
    }
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void micListener(View view)
    {
        promptSpeechInput();
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try {

                        String res=  filterOut(result.toString());

                        Log.w("Exp: " , res);
                         speakOut(String.valueOf(evaluate(result.toString())));

                        //speakOut(result.toString());
                        txtSpeechInput.setText(result.get(0));
                    } catch(Exception e){

                    }
                }
                break;
            }

        }
    }

// TTS
@Override
public void onDestroy() {
    // Don't forget to shutdown tts!
    if (tts != null) {
        tts.stop();
        tts.shutdown();
    }
    super.onDestroy();
}

   // @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //btnSpeak.setEnabled(true);
                speakOut("Hey Dude Wassup. Tell us what do you want us to do");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut(String text) {


        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}

