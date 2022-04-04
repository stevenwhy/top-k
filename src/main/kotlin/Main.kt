import java.util.*

fun main() {

    println("k largest elements: ${findKLargestNumbers(listOf(3,1,5,12,2,11),3)}")
    println("k largest elements: ${findKLargestNumbers(listOf(5, 12, 11, -1, 12),3)}")
}

/*
    Given an unsorted array of numbers, find the ‘K’ largest numbers in it.
    We could store all in a max heap and return top K elements
       Or better we can just have a min heap of size K and remove/add if next num is > heap.peek()
       O(n*logK) time and O(k) space for heap
 */
fun findKLargestNumbers(list: List<Int>, k: Int): List<Int> {
    val result: Queue<Int> = PriorityQueue<Int>(k)

    if(k >= list.size) return list
    var index = 0
    repeat(k) {
        result.add(list[index])
        index++
    }
    while(index < list.size) {
        val currentNum = list[index]
        if(currentNum > result.peek()) {
            result.poll()
            result.add(currentNum)
        }
        index++
    }

    return result.toList()
}