package cn.netkiller.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Thread thread;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(440);
            }
        };
        button.setOnClickListener(onClickListener);


        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Morse morse = new Morse();
//                morse.playSound();
                morse.play();
//                morse.synthesize();

            }
        });


    }


    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    private final int duration = 3;    // seconds
    private final int sampleRate = 44100;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private final double freqOfTone = 440;     // hz
    private final byte generatedSnd[] = new byte[2 * numSamples];
    Handler handler = new Handler();

    @Override
    protected void onResume() {
        super.onResume();
        // Use a new tread as this can take a while final Thread
//        thread = new Thread(new Runnable() {
//            public void run() {
//                genTone();
//                handler.post(new Runnable() {
//                    public void run() {
//                        playSound();
//                    }
//                });
//            }
//        });
//        thread.start();
    }

    void genTone() {
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
        }
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    void playSound() {
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, numSamples, AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }


    public static class SinWave {
        /**
         * 正弦波的高度
         **/
        public static final int HEIGHT = 127;
        /**
         * 2PI
         **/
        public static final double TWOPI = 2 * 3.1415;

        /**
         * 生成正弦波
         *
         * @param wave
         * @param waveLen 每段正弦波的长度
         * @param length  总长度
         * @return
         */
        public static byte[] sin(byte[] wave, int waveLen, int length) {
            for (int i = 0; i < length; i++) {
                wave[i] = (byte) (HEIGHT * (1 - Math.sin(TWOPI * ((i % waveLen) * 1.00 / waveLen))));
            }
            return wave;
        }
    }

    /**
     * 设置频率
     *
     * @param rate
     */
    public void start(int rate) {
        if (rate > 0) {
            int Hz = rate;
            int waveLen = 44100 / Hz;
            int length = waveLen * Hz;
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                    AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_8BIT, length, AudioTrack.MODE_STREAM);
            //生成正弦波
            byte[] wave = new byte[2 * length];
            wave = SinWave.sin(wave, waveLen, length);
            if (audioTrack != null) {
                audioTrack.write(wave, 0, length);
                audioTrack.play();
            }
        } else {
            return;
        }
    }


}
