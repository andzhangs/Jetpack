package io.dushu.databinding.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.NullPointerException

/**
 * author: zhangshuai 6/27/21 6:08 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class TeamScoreViewModel : ViewModel() {
    private val aTeamScore = MutableLiveData<Int>().apply {
        value = 0
    }
    private val bTeamScore = MutableLiveData<Int>().apply {
        value = 0
    }


    fun getAScore(): MutableLiveData<Int> {
        return aTeamScore
    }

    fun getBScore(): MutableLiveData<Int> {
        return bTeamScore
    }

    fun setAScore(i: Int) {
        saveALastScore()
        aTeamScore.value = aTeamScore.value?.plus(i)
    }

    fun setBScore(i: Int) {
        saveBLastScore()
        bTeamScore.value = bTeamScore.value?.plus(i)
    }

    /**
     * 撤销
     */
    fun undo() {
        aTeamScore.value = aLast
        bTeamScore.value = bLast
    }

    /**
     * 重置
     */
    fun reset() {
        aTeamScore.value = 0
        bTeamScore.value = 0
        aLast = 0
        bLast = 0
    }

    private var aLast: Int = 0
    private var bLast: Int = 0
    private fun saveALastScore() {
        try {
            aLast = aTeamScore.value!!
        }catch (e:NullPointerException){
            e.printStackTrace()
        }

    }

    private fun saveBLastScore() {
        try {
            bLast = bTeamScore.value!!
        }catch (e:NullPointerException){
            e.printStackTrace()
        }
    }

}