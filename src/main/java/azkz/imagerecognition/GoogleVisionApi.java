package azkz.imagerecognition;

import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * GoogleのVisionAPIを使うクラス
 */
public class GoogleVisionApi {

    /**
     * 画像のラベル検出をする。
     * 処理内容はほぼガイドの通り。
     *
     * @param imageFile 画像ファイル
     * @return VisionAPIのラベル検出の結果を返す。
     * @throws IOException 画像ファイル読み込みエラー時の例外。
     * @throws GoogleVisionApiException VisionAPIのレスポンスがエラーの場合の例外。
     * @see <a href="https://cloud.google.com/vision/docs/labels#java">ラベル検出のガイド</a>
     */
    public static List<EntityAnnotation> detectLabels(File imageFile) throws IOException, GoogleVisionApiException {
        // 画像解析に使うクライアントを初期化する。
        // try-with-resourcesで処理終了後にクライアントを自動でcloseする。
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // 画像ファイルを読み込む。
            byte[] bytes = Files.readAllBytes(imageFile.toPath());
            ByteString imgBytes = ByteString.copyFrom(bytes);

            // リクエストを組み立てる。
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            // APIリクエストを送信し、解析後のレスポンスを受け取る。
            BatchAnnotateImagesResponse batchAnnotateImagesResponse = vision.batchAnnotateImages(requests);
            AnnotateImageResponse response = batchAnnotateImagesResponse.getResponses(0);

            // エラーの場合は例外をthrow
            // 正常の場合はラベルの検出結果のリストを返す。
            if (response.hasError()) {
                throw new GoogleVisionApiException("レスポンスがエラーです。" + response.getError().getMessage());
            } else {
                return response.getLabelAnnotationsList();
            }
        }
    }
}
