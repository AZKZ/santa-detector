package azkz.main;

import azkz.camera.Camera;
import azkz.imagerecognition.GoogleVisionApi;
import azkz.messenger.SlackMessenger;
import com.google.cloud.vision.v1.EntityAnnotation;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            // 使用するカメラデバイスを指定し、Cameraクラスをインスタンス化する。
            VideoCapture videoCapture = new VideoCapture(Integer.parseInt(System.getenv("VIDEO_CAPTURE_INDEX")));
            Camera camera = new Camera(videoCapture);

            // 写真を撮影し、保存された画像ファイルの絶対パスを取得する。
            String imageFilePath = camera.takePicture(System.getenv("IMAGE_SAVE_DIR"));

            // 絶対パスからFileオブジェクトを作成する。
            File imageFile = new File(imageFilePath);

            // Google Vision APIで画像のラベル検出を行う。
            List<EntityAnnotation> labels = GoogleVisionApi.detectLabels(imageFile);

            // [santa claus]が含まれているかチェックする。
            boolean containsSanta = labels.stream().anyMatch(entityAnnotation ->
                    entityAnnotation.getDescription().toLowerCase().contains("santa claus"));

            // 含まれていたらSlackに画像ファイルをアップロードする。
            if (containsSanta) {
                System.out.println("サンタいた");
                SlackMessenger messenger = new SlackMessenger(System.getenv("SLACK_BOT_TOKEN"));
                messenger.uploadFile(Arrays.asList(System.getenv("SLACK_CHANNEL")), "サンタが出たぞ！！！", imageFile);
            } else {
                System.out.println("サンタいなかった。。。");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}