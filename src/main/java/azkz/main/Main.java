package azkz.main;

import azkz.camera.Camera;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.ResourceBundle;

public class Main {

    public static void main(String[] args) {
        // プロパティファイル(src/main/resources/env.properties)を読み込む。
        ResourceBundle resourceBundle = ResourceBundle.getBundle("env");

        try {

            // 写真を撮影し、保存された画像の絶対パスを取得する。
            String imageFilePath = Camera.takePicture(resourceBundle.getString("imageFileDir"));


            // HTTPクライアントを作成する。
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();


            URIBuilder builder = new URIBuilder(resourceBundle.getString("endpoint") + "vision/v3.1/analyze");

            // オプションのリクエストパラメーターを設定する。
            //   visualFeaturesは画像の特徴を返すオプション
            //   他にもBrandsやFaces、Tagsなどの値をカンマ区切りで設定できる。(https://westcentralus.dev.cognitive.microsoft.com/docs/services/computer-vision-v3-1-ga/operations/56f91f2e778daf14a499f21b)
            builder.setParameter("visualFeatures", "Description,Tags");

            // POSTリクエストを準備する。
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", resourceBundle.getString("subscriptionKey"));
            FileEntity fileEntity = new FileEntity(new File(imageFilePath));

            request.setEntity(fileEntity);

            // リクエストを送信し、レスポンスを受信する。
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // レスポンスのJSONを整形して、出力する。
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                System.out.println("REST Response:\n");
                System.out.println(json.toString(2));
                System.out.println();

                // JSONの文字列の中に「santa」が含まれているか判定する。
                if (jsonString.toLowerCase().contains("santa")) {
                    System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
                    System.out.println("サンタさんが来た！！！！");
                    System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
                } else {
                    System.out.println();
                    System.out.println("サンタさん来ないなぁ。。。");
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}