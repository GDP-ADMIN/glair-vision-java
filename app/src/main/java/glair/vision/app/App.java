package glair.vision.app;

import glair.vision.Vision;
import glair.vision.logger.LoggerConfig;
import glair.vision.model.VisionSettings;
import glair.vision.model.param.KtpParam;
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

            Vision vision = new Vision(visionSettings, new LoggerConfig(LoggerConfig.DEBUG));
            //        vision.printLoggerConfig();

            String response = "";

            try {
                KtpParam param = new KtpParam(env.getKtp());
//                KtpParam param = new KtpParam(env.getKtp(), true);
                response = vision
                    .ocr()
                    .ktp(param);
            } catch (Exception e) {
                response = e.getMessage();
            }

            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("File config.properties is not found.");
        }
    }
}
