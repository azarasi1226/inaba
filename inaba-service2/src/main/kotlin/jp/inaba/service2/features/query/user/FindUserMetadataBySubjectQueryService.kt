package jp.inaba.service2.features.query.user

// @Component
// class FindUserMetadataBySubjectQueryService(
//  private val dsl: DSLContext,
// ) {
//  @QueryHandler
//  fun handle(query: FindUserMetadataBySubjectQuery): FindUserMetadataBySubjectResult =
//    dsl
//      .selectFrom(USER_METADATA)
//      .where(USER_METADATA.SUBJECT.eq(query.subject))
//      .fetchOne {
//        FindUserMetadataBySubjectResult(
//          userId = it.userId,
//          // basketIDはCreateUserSagaが完了していれば必ず作成されているはずなので!!で良い
//          basketId = it.basketId!!,
//        )
//      } ?: throw UseCaseException(FindUserMetadataBySubjectError.USER_METADATA_NOT_FOUND)
// }
