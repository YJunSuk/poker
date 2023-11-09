package kr.ac.kumoh.ce.s20200694.poker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class PokerViewModel : ViewModel() {
    private var _cards = MutableLiveData<IntArray>(IntArray(5) { -1 })
    val cards: LiveData<IntArray>
        get() = _cards
    fun shuffle() {
        var num = 0
        val newCards = IntArray(5) { -1 }

        for (i in newCards.indices) {
            var isDuplicate: Boolean
            do {
                num = Random.nextInt(52)
                isDuplicate = false
                for (j in 0 until i) {
                    if (newCards[j] == num) {
                        isDuplicate = true
                        break
                    }
                }
            } while (isDuplicate)
            newCards[i] = num
        }

        // 정렬
        newCards.sort()

        _cards.value = newCards
    }
    fun HandRankings(): String{
        val cards = _cards.value ?: return "카드가 없음"

        if (cards.size != 5){
            return "카드가 5장이 아님"
        }

        cards.sort() //카드 정렬

        //포커족보 평가 로직
        if(isRoyalFlush(cards)) {
            return "로열 플러시"
        }else if(isBackStraightFlush(cards)) {
            return "백 스트레이트 플러시"
        }
        else if (isStraightFlush(cards)) {
            return "스트레이트 플러시"
        } else if (isFourOfAKind(cards)) {
            return "포카드"
        } else if (isFullHouse(cards)) {
            return "풀하우스"
        } else if (isFlush(cards)) {
            return "플러시"
        } else if (isMountain(cards)) {
            return "마운틴"
        }else if (isStraight(cards)) {
            return "스트레이트"
        }else if (isBackStraight(cards)) {
            return "백 스트레이트"
        } else if (isThreeOfAKind(cards)) {
            return "트리플"
        } else if (isTwoPair(cards)) {
            return "투페어"
        } else if (isOnePair(cards)) {
            return "원페어"
        } else {
            return "탑"
        }
    }


    }
}
