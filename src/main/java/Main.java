import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageAnalysis;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class Main {

    static String imageFilePath = "image.jpg";

    public static void main(String[] args) {
        // プロパティファイル(src/main/resources/env.properties)を読み込み、キーやエンドポイントを設定する。
        ResourceBundle resourceBundle = ResourceBundle.getBundle("env");
        String subscriptionKey = resourceBundle.getString("subscriptionKey");
        String endpoint = resourceBundle.getString("endpoint");
        String uriBase = endpoint + "vision/v3.1/analyze";

        // HTTPクライアントを作成する。
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try{
            URIBuilder builder = new URIBuilder(uriBase);

            // オプションのリクエストパラメーターを設定する。
            //   visualFeaturesは画像の特徴を返すオプション
            //   他にもBrandsやFaces、Tagsなどの値をカンマ区切りで設定できる。(https://westcentralus.dev.cognitive.microsoft.com/docs/services/computer-vision-v3-1-ga/operations/56f91f2e778daf14a499f21b)
            builder.setParameter("visualFeatures", "Description");

            // POSTリクエストを準備する。
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            FileEntity fileEntity = new FileEntity(new File(imageFilePath));

            request.setEntity(fileEntity);

            // リクエストを送信し、レスポンスを受信する。
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if(entity != null){
                // レスポンスのJSONを整形して、出力する。
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                System.out.println("REST Response:\n");
                System.out.println(json.toString(2));
                System.out.println();

                // JSONの文字列の中に「santa」が含まれているか判定する。
                if(jsonString.toLowerCase().contains("santa")){
                    System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
                    System.out.println("サンタさんが来た！！！！");
                    System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
                }else{
                    System.out.println();
                    System.out.println("サンタさん来ないなぁ。。。");
                    System.out.println();
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

//        // Webカメラを指定
//        VideoCapture videoCapture = new VideoCapture(0);
//
//        // マトリックスのインスタンスを生成
//        Mat mat = new Mat();
//
//        // 撮影
//        videoCapture.read(mat);
//
//        // ファイルとして出力
//        opencv_imgcodecs.imwrite("image.jpg",mat);
    }
}