package de.frosner.jm.basic

import de.frosner.rum.ReadOptimized.ReadOptimizedFactory
import de.frosner.rum.SpaceOptimized.SpaceOptimizedFactory
import de.frosner.rum.WriteOptimized.WriteOptimizedFactory
import de.frosner.rum.{DataStructure, DataStructureFactory, ReadOptimized}
import org.scalatest.{Entry, FlatSpec}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.Matchers._

class DataStructureTest extends FlatSpec {

  private val dataStructureFactories =
    Table[DataStructureFactory[_ <: DataStructure]](
      "data structure factory", // First tuple defines column names
      ReadOptimizedFactory,
      WriteOptimizedFactory,
      SpaceOptimizedFactory
    )

  "Point Queries" should "work for existing elements (min)" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.pointQuery(1) shouldBe Some(1)
    }
  }

  it should "work for existing elements (median)" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.pointQuery(5) shouldBe Some(5)
    }
  }

  it should "work for existing elements (max)" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.pointQuery(10) shouldBe Some(10)
    }
  }

  it should "work for non-existing elements < min" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.pointQuery(0) shouldBe None
    }
  }

  it should "work for non-existing elements > max" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.pointQuery(100) shouldBe None
    }
  }

  it should "work for non-existing elements inside" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.pointQuery(7) shouldBe None
    }
  }

  "Insert" should "work for newly created elements inside" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.insert(7)
      ds.pointQuery(7) shouldBe Some(7)
    }
  }

  it should "work for newly created elements < min" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.insert(0)
      ds.pointQuery(0) shouldBe Some(0)
    }
  }

  it should "work for newly created elements > max" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.insert(100)
      ds.pointQuery(100) shouldBe Some(100)
    }
  }

  it should "work for existing elements" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.insert(5)
      ds.pointQuery(5) shouldBe Some(5)
    }
  }

  "Delete" should "work for non-existing elements inside" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.delete(7)
      ds.pointQuery(7) shouldBe None
    }
  }

  it should "work for non-existing elements < min" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.delete(0)
      ds.pointQuery(0) shouldBe None
    }
  }

  it should "work for non-existing elements > max" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.delete(100)
      ds.pointQuery(100) shouldBe None
    }
  }

  it should "work for existing elements" in {
    forAll(dataStructureFactories) { factory =>
      val ds = factory.bulkCreate(Set(1, 5, 10))
      ds.delete(5)
      ds.pointQuery(5) shouldBe None
    }
  }

}
