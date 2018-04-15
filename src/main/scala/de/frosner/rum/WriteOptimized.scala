package de.frosner.rum

class WriteOptimized private (var updates: List[(Op, Int)])
    extends DataStructure {

  override def pointQuery(elem: Int): Option[Int] =
    updates
      .find {
        case (op, value) => value == elem
      }
      .flatMap {
        case (InsertOp, value) => Some(value)
        case (DeleteOp, _)     => None
      }

  override def insert(newElem: Int): DataStructure = {
    updates = (InsertOp, newElem) :: updates
    this
  }

  override def delete(oldElem: Int): DataStructure = {
    updates = (DeleteOp, oldElem) :: updates
    this
  }

}

object WriteOptimized {

  object WriteOptimizedFactory extends DataStructureFactory[WriteOptimized] {

    override def bulkCreate(baseData: Set[Int]): WriteOptimized =
      new WriteOptimized(baseData.map(i => (InsertOp: Op, i)).toList)

  }

}
