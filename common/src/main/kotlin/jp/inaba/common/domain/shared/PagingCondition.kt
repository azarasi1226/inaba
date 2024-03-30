package jp.inaba.common.domain.shared

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
){
    init {
        if(pageSize <= 0) {
            throw DomainException("pageSizeは[1 ~]の数値を入力してください。現在のpageSize[${pageSize}]")
        }
        if(pageNumber < 0) {
            throw DomainException("pageNumberは[0 ~]の数値を入力してください。現在のpageNumber[${pageNumber}]")
        }
    }
    val offset
        get() = pageNumber * pageSize
}