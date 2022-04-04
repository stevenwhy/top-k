import java.util.*

fun main() {

    println("k largest elements: ${findKLargestNumbers(listOf(3,1,5,12,2,11),3)}")
    println("k largest elements: ${findKLargestNumbers(listOf(5, 12, 11, -1, 12),3)}")

    println("kth smallest element: ${findKthSmallestNumber(listOf(1, 5, 12, 2, 11, 5),3)}")
    println("kth smallest element: ${findKthSmallestNumber(listOf(1, 5, 12, 2, 11, 5),4)}")
    println("kth smallest element: ${findKthSmallestNumber(listOf(5, 12, 11, -1, 12),3)}")
}

/*
    Given an unsorted array of numbers, find Kth smallest number in it.
    [1,4,4,6] the 2nd and 3rd smallest numbers are 4

    We can use a max heap, put the first k elements in, then remove top insert next element if
       next element is < heap.peek(). return heap.peek()

 */
fun findKthSmallestNumber(list: List<Int>, k: Int): Int {
    if(k > list.size) return -1

    // space is O(k)
    val result = PriorityQueue<Int>(k, compareByDescending { it })

    var index = 0
    // O(k*logk)
    while(index < k) {
        result.add(list[index])
        index++
    }

    // O((n-k)logk)
    while(index < list.size) {
        val currentNum = list[index]
        if(result.peek() > currentNum) {
            result.poll()
            result.add(currentNum)
        }
        index++
    }
    return result.peek()
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