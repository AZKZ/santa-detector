package azkz.camera;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Camera {

    private VideoCapture videoCapture;

    public Camera(VideoCapture videoCapture) {
        this.videoCapture = videoCapture;
    }

    /**
     * @param destDirStr 画像保存先ディレクトリ
     * @return 撮影された画像ファイルの絶対パス
     */
    public String takePicture(String destDirStr) {
        // 引数からFileオブジェクトをインスタンス化
        File destDir = new File(destDirStr);

        // 指定したディレクトリが存在しない場合 or ディレクトリではない場合
        if (!destDir.exists() || !destDir.isDirectory()) {
            throw new IllegalArgumentException("指定したディレクトリが存在しない、もしくはディレクトリではありません。");
        }

        // マトリックスのインスタンスを生成
        Mat mat = new Mat();

        // 撮影
        videoCapture.read(mat);

        // ファイル名 (2020年1月1日 13:15:30に撮影した場合 -> 20200101-131530.jpg)
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".jpg";
        String imageFilPath = destDir.getAbsolutePath() + "/" + fileName;
        // ファイルとして出力
        opencv_imgcodecs.imwrite(imageFilPath, mat);

        return imageFilPath;
    }
}
