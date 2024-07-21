#!/bin/bash

# AWSコマンドを打つときにクレデンシャルが無いと怒られるのでダミーをセット
export AWS_ACCESS_KEY_ID="Dummy"
export AWS_SECRET_ACCESS_KEY="Dummy"
export AWS_SESSION_TOKEN="Dummy"

# jqがインストールされているかどうかをチェック
if ! command -v jq &> /dev/null; then
    echo "Error: jqをインストールしてください"
    exit 1
fi

# マスターキー作成
aws kms create-key \
  --endpoint http://localhost:8080 \
  --description "DataKeyServiceMasterKey" \
  --key-usage ENCRYPT_DECRYPT
