package com.easynetwork.weather.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;


import java.util.HashMap;
import java.util.Locale;

/**
 * 文字转语音类
 * Created by chenruizhi on 2016/7/8.
 */
public class TextSpeakControl {

    private static final String TAG = "TextSpeakControl";

    public static final String XIAOYAN = "XiaoYan";
    public static final String XIAOFENG = "XiaoFeng";

    private Context context;
    private TextToSpeech tts;
    private String speakStr = "";

    public TextSpeakControl(final Context context) {
        this.context = context;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    ttsReady = true;
                    int result = tts.setLanguage(Locale.CHINA);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    }
                }
            }
        }, "com.iflytek.tts");
    }

    public void speak(String text) {
        Log.e(TAG, "speak: " + text);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        speakStr = text;
    }

    /**
     * 设置语速
     *
     * @param speedRate
     */
    public void setSpeakSpeed(float speedRate) {
        tts.setSpeechRate(speedRate);
    }

    /**
     * 中断阅读
     */
    public void stopSpeak() {
        if (tts != null)
            tts.stop();
    }

    public void closeTts() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }


    /**
     * 设置播音人
     *
     * @param speaker
     */
    public void setVoice(String speaker) {
        String voiceString = "3";
        if (speaker.equals(XIAOYAN)) {
            voiceString = "3";
        } else if (speaker.equals(XIAOFENG)) {
            voiceString = "4";
        }
        try {
            Context targetContext = context.createPackageContext("com.iflytek.tts", Context.CONTEXT_IGNORE_SECURITY);
            Object[] var2 = new Object[]{voiceString, 56, targetContext.getPackageResourcePath()};
            String command = "*role*" + String.format("role=%s;offset=%d;path=%s;", var2);
            tts.speak(command, 0, (HashMap) null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否在播放
     */
    public boolean isSpeak() {
        return tts.isSpeaking();
    }

    private boolean ttsReady = false;

    public boolean isTTSReady() {
        return ttsReady;
    }

    public void speakAfterTTSReady(final String speak){
        if (isTTSReady()){
            speak(speak);
        }else {
            new Thread(){
                @Override
                public void run() {
                    while (!isTTSReady()){
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        speak(speak);
                    }
                }
            }.start();
        }
    }

}
