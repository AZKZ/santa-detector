@echo off
cd %~dp0
chcp 65001
set IMAGE_SAVE_DIR=/xxxx/xxxx/xxxx/xxx
set GOOGLE_APPLICATION_CREDENTIALS=/xxxx/xxxx/xxxx/xxx/xxxx.json
set SLACK_BOT_TOKEN=xoxb-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
set SLACK_CHANNEL=#general
set VIDEO_CAPTURE_INDEX=0

java -jar build¥libs¥santa-detector-1.0-SNAPSHOT-all.jar