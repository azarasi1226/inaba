package jp.inaba.basket.service.application.command.basket.create

//TODO(userIDの値オブジェクト化)
class UserDuplicatedException(userId: String)
    : Exception("userId:[${userId}]はすでに存在します。")