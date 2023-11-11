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

    private fun isRoyalFlush(cards: IntArray): Boolean {
        val values = setOf(10, 11, 12, 13, 1)
        val shape = cards[0] / 13

        return cards.all{it/13 == shape && it % 13 in values}
    }
    private fun isBackStraightFlush(cards: IntArray): Boolean {
        val values = setOf(1, 2, 3, 4, 5)
        val shape = cards[0] / 13

        return cards.all{it/13 == shape && it % 13 in values}
    }
    private fun isStraightFlush(cards: IntArray): Boolean {
        val sortCards = cards.sorted()

        return (0 until 4).all{
            (sortCards[it] + 1) % 13 == sortCards[it] % 13 && sortCards[it]/13 == sortCards[0]/13
        }
    }
    private fun isFourOfAKind(cards: IntArray): Boolean {
        return (0 until 2).any {
            cards[it] % 13 == cards[it + 1] % 13 &&
                    cards[it + 1] % 13 == cards[it + 2] % 13 &&
                    cards[it + 2] % 13 == cards[it + 3] % 13 &&
                    cards[it + 3] % 13 == cards[it + 4] % 13
        }
    }
    private fun isFullHouse(cards: IntArray): Boolean {
        val counts = mutableMapOf<Int, Int>()

        for (card in cards) {
            val rank = card % 13
            counts[rank] = counts.getOrDefault(rank, 0) + 1
        }

        val threeOfAKind = counts.any { it.value == 3 }
        val pair = counts.any { it.value == 2 }

        return threeOfAKind && pair
    }

    private fun isFlush(cards: IntArray): Boolean {
        val shape = cards[0] / 13
        return cards.all { it / 13 == shape }
    }

    private fun isMountain(cards: IntArray): Boolean {
        // 마운틴이 10, J, Q, K, A 순서라고 가정합니다.
        val values = setOf(10, 11, 12, 13, 1)
        return cards.sorted().map { it % 13 }.toSet() == values
    }

    private fun isStraight(cards: IntArray): Boolean {
        val sortedCards = cards.sorted()

        return (0 until 4).all {
            (sortedCards[it] + 1) % 13 == sortedCards[it + 1] % 13
        }
    }

    private fun isBackStraight(cards: IntArray): Boolean {
        val values = setOf(1, 2, 3, 4, 5)
        return cards.sorted().map { it % 13 }.toSet() == values
    }

    private fun isThreeOfAKind(cards: IntArray): Boolean {
        val counts = mutableMapOf<Int, Int>()

        for (card in cards) {
            val rank = card % 13
            counts[rank] = counts.getOrDefault(rank, 0) + 1
        }

        return counts.any { it.value == 3 }
    }

    private fun isTwoPair(cards: IntArray): Boolean {
        val counts = mutableMapOf<Int, Int>()

        for (card in cards) {
            val rank = card % 13
            counts[rank] = counts.getOrDefault(rank, 0) + 1
        }

        return counts.values.count { it == 2 } == 2
    }

    private fun isOnePair(cards: IntArray): Boolean {
        val counts = mutableMapOf<Int, Int>()

        for (card in cards) {
            val rank = card % 13
            counts[rank] = counts.getOrDefault(rank, 0) + 1
        }

        return counts.any { it.value == 2 }
    }
    
}
