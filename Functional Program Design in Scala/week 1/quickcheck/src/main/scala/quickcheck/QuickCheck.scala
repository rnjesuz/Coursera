package quickcheck

import org.scalacheck.*
import Arbitrary.*
import Gen.*
import Prop.forAll

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap:
  lazy val genHeap: Gen[H] =
    for {
      value <- arbitrary[A]
      heap <- oneOf(const(empty), genHeap)
} yield insert(value, heap)

  given Arbitrary[H] = Arbitrary(genHeap)

  // Adding the minimal element, and then finding it,
  // should return the element in question
  property("gen1") = forAll { (h: H) =>
    val m = if isEmpty(h) then 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  // Adding a single element to an empty heap, and then removing this element,
  // should yield the element in question
  property("min1") = forAll { (a: A) =>
    val h = insert(a, empty)
    findMin(h) == a
  }




  // If you insert any two elements into an empty heap,
  // finding the minimum of the resulting heap should get
  // the smallest of the two elements back.
  property("lesser of two") =
    forAll { (value1: A, value2: A) =>
      val heap = insert(value2, insert(value1, empty))
      findMin(heap) == Math.min(value1, value2)
    }

  // If you insert an element into an empty heap, then delete the minimum,
  // the resulting heap should be empty.
  property("empty after insert and remove") =
    forAll { (value: A) =>
      isEmpty(deleteMin(insert(value, empty)))
    }

  // Given any heap, you should get a sorted sequence of elements
  // when continually finding and deleting minima.
  property("Heap sorted after continually finding and deleting minima") =
    def checkOrder(heap: H): Boolean =
      val min = findMin(heap)
      val smallerHeap = deleteMin(heap)
      isEmpty(smallerHeap) || min <= findMin(smallerHeap) && checkOrder(smallerHeap)
    end checkOrder
    forAll { (heap: H) =>
      isEmpty(heap) || checkOrder(heap)
    }

  // Finding a minimum of the melding of any two heaps
  // should return a minimum of one or the other.
  property("Min of meld is minimum of either parent") =
    forAll { (heap1: H, heap2: H) =>
      val min = Math.min(findMin(heap1), findMin(heap2))
      findMin(meld(heap1, heap2)) == min
    }

  // Extra property to validate deleteMin of heaps with randoms values
  property("deleteMin returns heap without min") =
    forAll { () =>
      val value = scala.util.Random.nextInt
      val heap1 = insert(value + 2, insert(value + 1, insert(value, empty)))
      deleteMin(heap1) == insert(value + 2, insert(value + 1, empty))
    }