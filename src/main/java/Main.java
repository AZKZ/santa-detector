import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        // Webカメラを指定
        VideoCapture videoCapture = new VideoCapture(0);

        // マトリックスのインスタンスを生成
        Mat mat = new Mat();

        // 撮影
        videoCapture.read(mat);

        // ファイルとして出力
        opencv_imgcodecs.imwrite("image.jpg",mat);
    }
}