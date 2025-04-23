package com.ltalk.controller;

public class VoiceServerSocketController {

    public static VoiceServerSocketController voiceServerController;

    public static VoiceServerSocketController getVoiceServerController() {
        if (voiceServerController == null) {
            voiceServerController = new VoiceServerSocketController();
            try{
                voiceServerController.connect();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return voiceServerController;
    }

    public void connect(){

    }
}
