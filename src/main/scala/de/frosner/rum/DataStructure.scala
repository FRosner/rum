package de.frosner.rum

trait DataStructure {

  def pointQuery(elem: Int): Option[Int]

  def insert(newElem: Int): DataStructure

  def delete(oldElem: Int): DataStructure

}
