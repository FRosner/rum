package de.frosner.rum

import java.util

class ReadOptimized(val max: Int) extends DataStructure {

  private var values: Array[Boolean] = Array.fill(max + 1)(false)

  override def pointQuery(elem: Int): Option[Int] =
    if (elem < values.length && values(elem))
      Some(elem)
    else
      None

  override def insert(newElem: Int): DataStructure = {
    if (newElem <= max)
      values(newElem) = true // swallow illegal inserts
    this
  }

  override def delete(oldElem: Int): DataStructure = {
    if (oldElem < values.length) {
      values(oldElem) = false
    }
    this
  }

}

object ReadOptimized {

  object ReadOptimizedFactory extends DataStructureFactory[ReadOptimized] {

    def bulkCreate(baseData: Set[Int]): ReadOptimized = {
      val ro = new ReadOptimized(Int.MaxValue / 2)
      baseData.foreach(i => ro.insert(i))
      ro
    }

  }

}

/*
class ReadOptimized private (var values: Array[Boolean]) extends DataStructure {

  override def pointQuery(elem: Int): Option[Int] =
    if (elem < values.length && values(elem))
      Some(elem)
    else
      None

  override def insert(newElem: Int): DataStructure = {
    if (newElem >= values.length) {
      var newValues = util.Arrays.copyOf(values, newElem + 1)
      newValues(newElem) = true
      values = newValues
    } else {
      values(newElem) = true
    }
    this
  }

  override def delete(oldElem: Int): DataStructure = {
    if (oldElem < values.length) {
      values(oldElem) = false
    }
    this
  }

}

object ReadOptimized {

  object ReadOptimizedFactory extends DataStructureFactory[ReadOptimized] {

    def bulkCreate(baseData: Set[Int]): ReadOptimized = {
      val max = baseData.max
      val array = Array.fill[Boolean](max + 1)(false)
      baseData.foreach(i => array(i) = true)
      new ReadOptimized(array)
    }

  }

}
 */
