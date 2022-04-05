import java.util.*

class KthLargestNumberInStream(
    numbers: List<Int>,
    k: Int
) {
    // O(k) space
    val minHeap = PriorityQueue<Int>(k)

    init {
        var index = 0
        while(index < k) {
            minHeap.add(numbers[index])
            index++
        }

        while(index < numbers.size) {
            val currentNum = numbers[index]
            if(currentNum > minHeap.peek()) {
                minHeap.poll()
                minHeap.add(currentNum)
            }
            index++
        }
    }

    // store the given number and return the Kth largest number.
    // O(logk) time
    fun add(num: Int): Int {
        if(num > minHeap.peek()) {
            minHeap.poll()
            minHeap.add(num)
        }
        return minHeap.peek()
    }
}