package azkz.notification;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SlackMessenger {

    private String token;

    public SlackMessenger(String token) {
        this.token = token;
    }

    public void postMessage(String channel, String text) {
        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");
        try {
            // Call the chat.postMessage method using the built-in WebClient
            var result = client.chatPostMessage(r -> r
                            // The token you used to initialize your app
                            .token(token)
                            .channel(channel)
                            .text(text)
                    // You could also use a blocks[] array to send richer content
            );
            // Print result, which includes information about the message (like TS)
            logger.info("result {}", result);
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
    }

    public void uploadFile(List<String> channels, String comment, File file){
        var client = Slack.getInstance().methods();
        try {
            var result = client.filesUpload(r -> r
                    .token(token)
                    .channels(channels)
                    .file(file)
                    .filename(file.getName())
                    .initialComment(comment)
                    );
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}