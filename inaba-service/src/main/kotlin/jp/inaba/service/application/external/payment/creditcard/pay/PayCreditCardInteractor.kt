package jp.inaba.service.application.external.payment.creditcard.pay

import org.axonframework.commandhandling.CommandHandler
import org.springframework.stereotype.Component

@Component
class PayCreditCardInteractor
{
    @CommandHandler
    fun handle() {
        println("支払いしたよ～")
    }
}