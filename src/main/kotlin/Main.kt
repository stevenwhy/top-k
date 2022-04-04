import java.util.*
import kotlin.math.sqrt

fun main() {

    println("k largest elements: ${findKLargestNumbers(listOf(3,1,5,12,2,11),3)}")
    println("k largest elements: ${findKLargestNumbers(listOf(5, 12, 11, -1, 12),3)}")

    println("kth smallest element: ${findKthSmallestNumber(listOf(1, 5, 12, 2, 11, 5),3)}")
    println("kth smallest element: ${findKthSmallestNumber(listOf(1, 5, 12, 2, 11, 5),4)}")
    println("kth smallest element: ${findKthSmallestNumber(listOf(5, 12, 11, -1, 12),3)}")

    println("find k closest coordinates to origin: ${findKClosestPointsToOrigin(listOf(Pair(1,2),Pair(1,3)),1)}")
    println("find k closest coordinates to origin: ${findKClosestPointsToOrigin(listOf(Pair(1,3),Pair(3,4),Pair(2,-1)),2)}")

    println("link ropes with min cost: ${linkRopes(listOf(1, 3, 11, 5))}")
    println("link ropes with min cost: ${linkRopes(listOf(3, 4, 5, 6))}")
    println("link ropes with min cost: ${linkRopes(listOf(1, 3, 11, 5, 2))}")

    println("find top k most frequent numbers: ${findTopKFrequentNumbers(listOf(1, 3, 5, 12, 11, 12, 11), 2)}")
    println("find top k most frequent numbers: ${findTopKFrequentNumbers(listOf(5, 12, 11, 3, 11), 2)}")
}

/*
    Given an unsorted array of numbers, find the top ‘K’ frequently occurring numbers in it.
    Input: [1, 3, 5, 12, 11, 12, 11], K = 2
    Output: [12, 11]

    Here I am thinking of 2 data structures. A hash map to keep track of frequencies, and
     a min heap of size K to keep track of k most freq numbers.
     Another option is to convert hashMap into Map sorted by frequency and return the first k elements
        it is a similar time complexity
 */
data class Num(var value: Int, var freq: Int)
fun findTopKFrequentNumbers(list: List<Int>, k: Int): List<Int> {
    val minHeap = PriorityQueue<Num>(k, compareBy { it.freq })
    val hashMap: HashMap<Int,Int> = HashMap()

    // O(n)
    for(num in list) {
        hashMap[num] = hashMap.getOrDefault(num,0)+1
    }
    // now hashmap is storing all numbers -> their frequencies
    // lets put first k elements in minHeap
    var index = 0
    val keyList = hashMap.keys.toList()
    // O(k*logk)
    while(index < k) {
        val currentNum = Num(keyList[index], hashMap.getOrDefault(keyList[index],-1))
        minHeap.add(currentNum)
        index++
    }
    // now start adding rest of the list
    // O((d-k)logk)
    while(index < keyList.size) {
        val currentNum = Num(keyList[index], hashMap.getOrDefault(keyList[index],-1))
        if(currentNum.freq > minHeap.peek().freq) {
            minHeap.poll()
            minHeap.add(currentNum)
        }
        index++
    }

    return minHeap.map { it.value }
}
/*
    Given ‘N’ ropes with different lengths, we need to connect these ropes into one big rope with minimum cost.
    The cost of connecting two ropes is equal to the sum of their lengths. return the cost

    So since cost goes up as we rope grows, we want to link smallest ropes first.
     as we link, we want to maintain order and only link smallest ropes together
 */
fun linkRopes(list: List<Int>): Int {

    val heap = PriorityQueue<Int>(list.size)
    // O(N)
    for(num in list) {
        heap.add(num)
    }

    var sum = 0
    // O(n*logn)
    while(heap.size > 1) {
        val newRope = heap.poll() + heap.poll()
        sum += newRope
        heap.add(newRope)
    }

    return sum
}

/*
    Given an array of points in the a 2D2D plane, find ‘K’ closest points to the origin.
    Input: points = [[1,2],[1,3]], K = 1
    Output: [[1,2]]
    use sqrt(x^2 + y^2) to get distance.
    After calculating distance we can keep a max heap and remove/store when next element has a smaller distance
 */
fun findKClosestPointsToOrigin(list: List<Pair<Int,Int>>, k: Int): List<Pair<Int,Int>> {
    if(k >= list.size) return list

    val result = PriorityQueue<Pair<Int,Int>>(k, compareByDescending { getDistanceToOrigin(it) })

    // put first k elements
    var index = 0
    while(index < k) {
        result.add(list[index])
        index++
    }

    while(index < list.size) {
        if(getDistanceToOrigin(result.peek()) > getDistanceToOrigin(list[index])) {
            result.poll()
            result.add(list[index])
        }
        index++
    }
    return result.toList()
}
private fun getDistanceToOrigin(coordinate: Pair<Int,Int>): Double {
    return sqrt((coordinate.first*coordinate.first + coordinate.second*coordinate.second).toDouble())
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