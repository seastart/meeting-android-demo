package com.freewind.seastarvideo.base

import androidx.lifecycle.ViewModel
import java.io.Serializable

/**
 * @author: wiatt
 * @date: 2023/12/15 11:36
 * @description:
 */
abstract class BaseViewModel<M: BaseModel<CONTRACT_VM, *>,
        CONTRACT_VM: BaseContract.IViewModel>: ViewModel(), Serializable {

    protected var mModel: M = getModel()

    abstract fun getModel(): M
}