package de.frosner.rum

import de.frosner.rum.ReadOptimized.ReadOptimizedFactory
import de.frosner.rum.SpaceOptimized.SpaceOptimizedFactory
import de.frosner.rum.WriteOptimized.WriteOptimizedFactory
import org.scalameter.api._
import org.scalameter.picklers.Implicits._

import scala.util.Random

// testOnly de.frosner.rum.DataStructureMemoryTest
object DataStructureMemoryTest extends Bench.OfflineReport {

  override def measurer = new Executor.Measurer.MemoryFootprint

  val max = Integer.MAX_VALUE / 8
  val sizes = Gen.enumeration("sizes")(List.iterate(1000, 1)(_ * 10): _*)
  val dataAndQuery = for {
    size <- sizes
  } yield
    (Seq.fill(size)(Random.nextInt(max)).toSet,
     Seq.fill(size / 2)(Random.nextInt(max)))

  performance of "space" in {
    measure method "readOptimized" in {
      using(dataAndQuery) in {
        case (data, query) => ReadOptimizedFactory.bulkCreate(data)
      }
    }
    measure method "writeOptimized (fresh)" in {
      using(dataAndQuery) in {
        case (data, query) => WriteOptimizedFactory.bulkCreate(data)
      }
    }
    measure method "writeOptimized (used)" in {
      using(dataAndQuery) in {
        case (data, query) =>
          val result = WriteOptimizedFactory.bulkCreate(data)
          query.foreach(i => result.insert(i))
      }
    }
    measure method "spaceOptimized" in {
      using(dataAndQuery) in {
        case (data, query) => SpaceOptimizedFactory.bulkCreate(data)
      }
    }
  }

}
