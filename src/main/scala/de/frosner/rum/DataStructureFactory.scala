package de.frosner.rum

trait DataStructureFactory[DS <: DataStructure] {

  def bulkCreate(baseData: Set[Int]): DS

  def empty: DS = bulkCreate(Set.empty)

}
