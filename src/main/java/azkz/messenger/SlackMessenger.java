package azkz.messenger;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Slackにメッセージを送るクラス
 */
public class SlackMessenger {

    private String token;

    /**
     * コンストラクタ
     * @param token botのトークン([xoxb-]から始まるもの)
     */
    public SlackMessenger(String token) {
        this.token = token;
    }

    /**
     * @param channels チャンネルID or チャンネル名
     * @param comment  アップロード時のコメント
     * @param file     アップロードするファイル
     */
    public void uploadFile(List<String> channels, String comment, File file) throws IOException, SlackApiException {
        var client = Slack.getInstance().methods();
        var result = client.filesUpload(r -> r
                .token(token)
                .channels(channels)
                .file(file)
                .filename(file.getName())
                .initialComment(comment)
        );
        System.out.println(result);
    }
}
