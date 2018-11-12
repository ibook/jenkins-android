package cn.netkiller.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.ArrayList;
import java.util.List;

public class Morse {


    private final int sampleRate = 44100;
    private int numSamples;
    private final double freqOfTone = 440;     // hz
    private byte[] sound = null;
    List<byte[]> code = new ArrayList<byte[]>();


    private byte[] tone(int duration) {
//        int duration = 3;    // seconds
        numSamples = duration * sampleRate;
        byte[] sound = new byte[2 * numSamples];
        double sample[] = new double[numSamples];

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
            sound[idx++] = (byte) (val & 0x00ff);
            sound[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        return sound;
    }

    private byte[] space(int duration) {
//        int duration = 3;    // seconds
        numSamples = duration * sampleRate;
        byte[] sound = new byte[2 * numSamples];
        double sample[] = new double[numSamples];

        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / 1));
        }
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            sound[idx++] = (byte) (val & 0x00ff);
            sound[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        return sound;
    }

    private void encode() {

        code.add(this.tone(1));
        code.add(this.space(1));
        code.add(this.tone(1));
        code.add(this.space(1));
        code.add(this.tone(1));

        code.add(this.space(3));

        code.add(this.tone(2));
        code.add(this.space(1));
        code.add(this.tone(2));
        code.add(this.space(1));
        code.add(this.tone(2));

        code.add(this.space(3));

        code.add(this.tone(1));
        code.add(this.space(1));
        code.add(this.tone(1));
        code.add(this.space(1));
        code.add(this.tone(1));

//        sound = code.toArray( sound);
//        sound = code.toArray(new byte[code.size()]);


//        return code.toArray();
    }


    public void play() {
        this.encode();

        List<Byte> sound = new ArrayList<Byte>();
        for (byte[] c : code) {
            for (byte s : c) {
                sound.add(s);
            }
        }

        byte[] buffer = new byte[sound.size()];
        int i = 0;
        for (byte s : sound) {
            buffer[i] = s;
            i++;
        }

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffer.length, AudioTrack.MODE_STATIC);
        audioTrack.write(buffer, 0, buffer.length);
        audioTrack.play();
        code = null;
    }

    private byte[] genTone(int duration) {
//        int duration = 3;    // seconds
        numSamples = duration * sampleRate;
        byte[] sound = new byte[2 * numSamples];
        double sample[] = new double[numSamples];

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
            sound[idx++] = (byte) (val & 0x00ff);
            sound[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        return sound;
    }

    void playSound() {
        byte[] sound = this.genTone(1);
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, numSamples, AudioTrack.MODE_STATIC);
        audioTrack.write(sound, 0, sound.length);
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


        // fill out the array
//    for (int i = 0; i < numOfSamples; ++i) {
//            double valueSum = 0;
//
//            for (int j = 0; j < soundData.length; j++) {
//                valueSum += Math.sin(2 * Math.PI * i / (SAMPLE_RATE / soundData[j][0]));
//            }
//
//            sample[i] = valueSum / soundData.length;
//        }


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

    public void synthesize() {

        final double scale = 1;
        final int duration = 1; // Seconds
        final int sampleRate = 22050; // Hz (maximum frequency is 7902.13Hz (B8))
        final int numSamples = duration * sampleRate;
        final double samples[] = new double[numSamples];
        final short buffer[] = new short[numSamples];


        double frequency = 440.0;
        double note = scale * frequency;
        for (int i = 0; i < numSamples; ++i) {
            samples[i] = Math.sin(2 * Math.PI * i / (sampleRate / note)); // Sine wave
            buffer[i] = (short) (samples[i] * Short.MAX_VALUE);  // Higher amplitude increases volume
        }
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, buffer.length, AudioTrack.MODE_STATIC);
        audioTrack.write(buffer, 0, buffer.length);
        audioTrack.play();
    }


}
