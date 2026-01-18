package jp.inaba.core.domain.common

interface SortCondition {
    // TODO:DBの詳細がこの中に入っちゃうのきもくね..?Domain層やぞここ！！
    val dbColumnName: String
    val sortDirection: SortDirection
}
