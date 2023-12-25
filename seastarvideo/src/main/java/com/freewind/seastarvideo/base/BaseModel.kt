package com.freewind.seastarvideo.base

/**
 * @author: wiatt
 * @date: 2023/12/15 11:40
 * @description:
 */
abstract class BaseModel<CONTRACT_VM: BaseContract.IViewModel,
        CONTRACT_M: BaseContract.IModel> constructor(var listener: CONTRACT_VM) {

    abstract fun getContract(): CONTRACT_M
}