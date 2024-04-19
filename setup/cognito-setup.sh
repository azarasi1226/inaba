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

user_pool_name="inaba-local-pool"
user_pool_client_name="inaba-local-client"

# ユーザープール作成
echo "CognitoUserPoolを作成します..."
create_user_pool_response=$(aws cognito-idp create-user-pool \
  --endpoint http://localhost:9229 \
  --pool-name ${user_pool_name} \
  --username-attribute email \
  --auto-verified-attributes email \
  --user-attribute-update-settings AttributesRequireVerificationBeforeUpdate=email \
  --schema \
    Name=email,Required=true \
    Name=user_id,AttributeDataType=String,Mutable=true)

user_pool_id=$(echo ${create_user_pool_response} | jq -r '.UserPool.Id')
echo "CognitoUserPoolId:[${user_pool_id}]を作成しました"

# ユーザークライアント作成
echo "CognitoUserPoolClientを作成します..."
create_user_pool_client_response=$(aws cognito-idp create-user-pool-client \
  --endpoint http://localhost:9229 \
  --user-pool-id ${user_pool_id} \
  --client-name ${user_pool_client_name})

user_pool_client_id=$(echo ${create_user_pool_client_response} | jq -r '.UserPoolClient.ClientId')
echo "CognitoUserPoolClientId:[${user_pool_client_id}]を作成しました"