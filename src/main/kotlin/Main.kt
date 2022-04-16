import java.util.*
import kotlin.math.abs
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

    println("sort string by frequency of each char: ${sortByFrequency("Programming")}")
    println("sort string by frequency of each char: ${sortByFrequency("abcbab")}")

    val newStream = KthLargestNumberInStream(listOf(3,1,5,12,2,11),4)

    println("adding to stream: ${newStream.add(6)}")
    println("adding to stream: ${newStream.add(13)}")
    println("adding to stream: ${newStream.add(4)}")

    println("Finding k closest numbers to X: ${findKClosestNumsToX(listOf(5,6,7,8,9), 3 , 7)}")
    println("Finding k closest numbers to X: ${findKClosestNumsToX(listOf(2, 4, 5, 6, 9), 3 , 6)}")
    println("Finding k closest numbers to X: ${findKClosestNumsToX(listOf(2, 4, 5, 6, 9), 3 , 10)}")

    println("find max distinct elements: ${findMaxdDistinctAfterKRemoves(listOf(7, 3, 5, 8, 5, 3, 3), 2)}")
    println("find max distinct elements: ${findMaxdDistinctAfterKRemoves(listOf(3, 5, 12, 11, 12), 3)}")
    println("find max distinct elements: ${findMaxdDistinctAfterKRemoves(listOf(1, 2, 3, 3, 3, 3, 4, 4, 5, 5, 5), 2)}")

    println("Find sum between k1 and k2 smallest nums: ${findSumOfElementBetween(listOf(1, 3, 12, 5, 15, 11), 3 , 6)}")
    println("Find sum between k1 and k2 smallest nums: ${findSumOfElementBetween(listOf(3, 5, 8, 7), 1 , 4)}")

    println("find if its letters can be rearranged: ${findIfStringCanBeRearranged("aappp")}")
    println("find if its letters can be rearranged: ${findIfStringCanBeRearranged("Programming")}")
    println("find if its letters can be rearranged: ${findIfStringCanBeRearranged("aapa")}")
    println("find if its letters can be rearranged: ${findIfStringCanBeRearranged("aaaaaaaaaabbbbbbbbbccccdc")}")
}

/*
    Given a string, find if its letters can be rearranged in such a way that no two same characters
       come next to each other.
    Input: "aappp"
    Output: "papap"

    Thinking we can keep a maxHeap of frequency of each letter like a Pair(letter, freq)
    If the max freq letter is > rest of the letters+1 then impossible
     else we can build the sample string by going down the heap adding 1 letter at a time until no more
    aaaaaaaaaa
    bbbbbbbbb
    ccccc
    d
 */
fun findIfStringCanBeRearranged(str: String): String {
    // first lets make a freq map O(N)
    val freqMap : Map<Char,Int> = str.groupingBy {it}.eachCount()

    val maxHeap = PriorityQueue<Map.Entry<Char,Int>>(compareByDescending { it.value })
    // O(NlogN)
    for(char in freqMap) {
        maxHeap.add(char)
    }
    val mostFreq = maxHeap.peek().value
    if(mostFreq > (str.length - mostFreq + 1)) return ""

    // else we can build a sample string O(N)
    var count = 0
    val sortedFreq = maxHeap.toMutableList()
    var index = 0
    val s = StringBuilder()
    while(count < str.length) {
        if(index >= sortedFreq.size || sortedFreq[index].value <= 0) {
            index = 0
        }
        s.append(sortedFreq[index].key)
        sortedFreq[index] = mapOf( Pair(sortedFreq[index].key, sortedFreq[index].value - 1) ).entries.first()
        count++
        index++
    }
    return s.toString()
}

/*
    Given an array, find the sum of all numbers between the K1’th and K2’th smallest elements of that array.
    Input: [1, 3, 12, 5, 15, 11], and K1=3, K2=6
    Output: 23

    can we sort the list first then loop through k1->k2 indices summing?
    Here we can put all the numbers in a minHeap. O(nlogn) or O(n) on average.
    Then go through popping elements K1 times. Then start summing numbers K2-K1 times. O(nlogn)
 */
fun findSumOfElementBetween(list: List<Int>, k1: Int, k2: Int): Int {
    val minHeap = PriorityQueue<Int>()
    var result = 0
    for(num in list) minHeap.add(num)

    repeat(k1) { minHeap.poll() }
    repeat(k2-k1-1) { result += minHeap.poll() }
    return result
}

/*
    Given an array of numbers and a number ‘K’,
    we need to remove ‘K’ numbers from the array such that we are left with maximum distinct numbers.
    Input: [7, 3, 5, 8, 5, 3, 3], and K=2
    Output: 3

    Here we can make a frequency map. Then go through and add any freq = 1 to result++
    and freq > 1 to a min heap.
    Then go through min heap, if freq <= k+1, sub (freq-1) from k and result++
     else we can stop

     return result-k
 */
fun findMaxdDistinctAfterKRemoves(list: List<Int>, k: Int): Int {
    var result = 0
    val hashMap = HashMap<Int,Int>()

    for(num in list) {
        hashMap[num] = hashMap.getOrDefault(num,0)+1
    }
    // now we have frequency map
    val minHeap = PriorityQueue<Int>()
    for(entry in hashMap) {
        if(entry.value == 1) result++
        else minHeap.add(entry.value)
    }
    println("Found $result values already distinct. Min Heap=$minHeap")
    // now have minHeap
    var count = 0
    var removals = k
    while(count <= minHeap.size) {
        val frequency = minHeap.poll()
        if(frequency <= removals+1) {
            removals -= (frequency-1)
            result++
        } else removals -= frequency
        if(removals <= 0) {
            removals = 0
            break
        }
        count++
    }
    return result-removals
}

/*
    Given a sorted number array and two integers ‘K’ and ‘X’,
        find ‘K’ closest numbers to ‘X’ in the array.
    Return the numbers in the sorted order. ‘X’ is not necessarily present in the array.

    Since the array is sorted, we can binary search for where X
        or right next to it if it doesnt exist
        Then start checking elements to left and adding to max heap if element-x is < heap.peek()
        repeat for right side

        we could also do the same binary search but then use two-pointer strat.
           This means checking left vs right of X' and picking which is closer to X
          this will allow O(1) space and if we use a double sided queue, we can keep the
            result queue in order by adding left pointer to left side of queue
            and right pointer to right side of queue
 */
fun findKClosestNumsToX(list: List<Int>, k: Int, X: Int): List<Int> {
    //start with binary search

    var start = 0
    var end = list.size-1
    while(start < end) {
        val middle = start + (end-start)/2

        if(list[middle] < X) {
            start = middle + 1
        } else if(list[middle] > X) end = middle - 1
        else {
            //found it
            start = middle
            end = middle
        }
    }
    //after this, start is index of X or nearby element
    val maxHeap = PriorityQueue<Int>(k, compareByDescending { abs(it-X) })
    maxHeap.add(list[start])
    var count = 1
    while(count < k) {
        if(count <= start) {
            val currentNumLeft = list[start-count]
            if(maxHeap.size < k) maxHeap.add(currentNumLeft)
            else if(abs(currentNumLeft-X) < abs(maxHeap.peek()-X)) {
                maxHeap.poll()
                maxHeap.add(currentNumLeft)
            }
        }
        if(start+count < list.size) {
            val currentNumRight = list[start+count]
            if(maxHeap.size < k) maxHeap.add(currentNumRight)
            else if(abs(currentNumRight-X) < abs(maxHeap.peek()-X)) {
                maxHeap.poll()
                maxHeap.add(currentNumRight)
            }
        }
        count++
    }
    return maxHeap.toList().sorted()
}

/*
    Given a string, sort it based on the decreasing frequency of its characters.
    Input: "Programming"
    Output: "rrggmmPiano"

    We can go through string once, saving letter->freq in hashMap.
    Then either put them in maxHeap and pop out while building new String
       or sort hashMap as a map by freq descending and build string from there.
 */
fun sortByFrequency(str: String): String {
    val hashMap: HashMap<Char,Int> = HashMap()

    var index = 0
    while(index < str.length) {
        val currentChar = str[index]
        hashMap[currentChar] = hashMap.getOrDefault(currentChar,0)+1
        index++
    }
    val sortedFreqMap = hashMap.toList().sortedByDescending { it.second }.toMap()
    val sb = StringBuilder()
    for(char in sortedFreqMap) {
        var count = 0
        while(count < char.value) {
            sb.append(char.key)
            count++
        }
    }
    return sb.toString()
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
    val result: Queue<Int> = PriorityQueue(k)

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