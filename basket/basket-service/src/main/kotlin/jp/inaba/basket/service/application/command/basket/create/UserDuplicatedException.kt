package jp.inaba.basket.service.application.command.basket.create

//TODO(userIDの値オブジェクト化)
class UserDuplicatedException(userId: String)
    : Exception("userId:[${userId}]を持つユーザーはすでに存在します。")