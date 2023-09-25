package glair.vision.app;

import glair.vision.Vision;
import glair.vision.model.VisionSettings;
import glair.vision.util.Env;

public class App {
    public static void main(String[] args) {
        try {
            Env env = new Env();
            VisionSettings visionSettings = new VisionSettings.Builder()
                .username(env.getUsername())
                .password(env.getPassword())
                .apiKey(env.getApiKey())
                .build();

            Vision vision = new Vision(visionSettings);
            //        vision.printLoggerConfig();

            String response = "";

            try {
                response = vision
                    .ocr()
                    .ktp(env.getKtp());
            } catch (Exception e) {
                response = e.getMessage();
            }

            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("File config.properties is not found.");
        }
    }
}
