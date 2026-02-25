package jp.inaba.message.brand

import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.message.brand.command.CreateBrandResult
import jp.inaba.message.brand.command.DeleteBrandCommand
import jp.inaba.message.brand.command.DeleteBrandResult
import jp.inaba.message.brand.query.FindBrandByIdQuery
import jp.inaba.message.brand.query.FindBrandByIdResult
import jp.inaba.message.brand.query.SearchBrandsQuery
import jp.inaba.message.brand.query.SearchBrandsResult
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

fun CommandGateway.createBrand(command: CreateBrandCommand): CreateBrandResult =
    this.sendAndWait(command, CreateBrandResult::class.java)

fun CommandGateway.deleteBrand(command: DeleteBrandCommand): DeleteBrandResult =
    this.sendAndWait(command, DeleteBrandResult::class.java)

fun QueryGateway.findBrandById(query: FindBrandByIdQuery): FindBrandByIdResult =
    this.query(query, FindBrandByIdResult::class.java).join()

fun QueryGateway.searchBrands(query: SearchBrandsQuery): SearchBrandsResult =
    this.query(query, SearchBrandsResult::class.java).join()
