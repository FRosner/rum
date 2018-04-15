package de.frosner.rum

trait DataStructureFactory[DS <: DataStructure] {

  def bulkCreate(baseData: Seq[Int]): DS

  def empty: DS = bulkCreate(Seq.empty)

}
