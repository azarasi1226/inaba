package jp.inaba.identity.service.presentation.user.get

import jp.inaba.identity.api.domain.user.UserId

class UserNotFoundException(userId: UserId)
    : Exception("userId:[${userId}]を持つユーザーが存在しませんでした。")