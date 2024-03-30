package jp.inaba.basket.service.application.command.basket.setbasketitem

import jp.inaba.identity.api.domain.user.UserId

class UserNotFoundException(userId: UserId)
    : Exception("userId:[${userId.value}]を持つUserが存在しませんでした。")