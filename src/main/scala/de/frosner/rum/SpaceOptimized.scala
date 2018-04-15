package de.frosner.rum

import scala.collection.mutable.ArrayBuffer

class SpaceOptimized private (var values: ArrayBuffer[Int])
    extends DataStructure {

  override def pointQuery(elem: Int): Option[Int] =
    values.find(_ == elem)

  override def insert(newElem: Int): DataStructure = {
    if (pointQuery(newElem).isEmpty) {
      values += newElem
    }
    this
  }

  override def delete(oldElem: Int): DataStructure = {
    values -= oldElem
    this
  }

}

object SpaceOptimized {

  object SpaceOptimizedFactory extends DataStructureFactory[SpaceOptimized] {

    override def bulkCreate(baseData: Set[Int]): SpaceOptimized =
      new SpaceOptimized(ArrayBuffer(baseData.toArray: _*))

  }

}
