package de.frosner.rum

import de.frosner.rum.ReadOptimized.ReadOptimizedFactory
import de.frosner.rum.SpaceOptimized.SpaceOptimizedFactory
import de.frosner.rum.WriteOptimized.WriteOptimizedFactory
import org.scalameter.api._
import org.scalameter.picklers.Implicits._

import scala.util.Random

// testOnly de.frosner.rum.DataStructurePointQueryPerformanceTest
object DataStructurePerformanceTest extends Bench.OfflineReport {

  val max = Integer.MAX_VALUE / 8
  val sizes = Gen.enumeration("sizes")(List.iterate(100, 3)(_ * 10): _*)
  val dataAndQuery = for {
    size <- sizes
  } yield
    (Seq.fill(size)(Random.nextInt(max)).toSet,
     Seq.fill(size / 2)(Random.nextInt(max)))
  val readOptimized = for {
    date <- dataAndQuery
  } yield (ReadOptimizedFactory.bulkCreate(date._1), date._2)
  val writeOptimized = for {
    date <- dataAndQuery
  } yield (WriteOptimizedFactory.bulkCreate(date._1), date._2)
  val writeOptimizedUsed = for {
    wo <- writeOptimized
    woUsed = {
      var result =
        WriteOptimizedFactory.bulkCreate(wo._1.updates.map(_._2).toSet)
      val (insert, delete) = wo._2.map(_ + 1).splitAt(wo._2.size / 2)
      insert.foreach(result.insert)
      delete.foreach(result.delete)
      result
    }
  } yield (woUsed, wo._2)
  val spaceOptimized = for {
    date <- dataAndQuery
  } yield (SpaceOptimizedFactory.bulkCreate(date._1), date._2)

  performance of "pointQuery" in {
    measure method "readOptimized" in {
      using(readOptimized) in {
        case (data, query) =>
          query.foreach(q => data.pointQuery(q))
      }
    }
    measure method "writeOptimized (fresh)" in {
      using(writeOptimized) in {
        case (data, query) =>
          query.foreach(q => data.pointQuery(q))
      }
    }
    measure method "writeOptimized (used)" in {
      using(writeOptimizedUsed) in {
        case (data, query) =>
          query.foreach(q => data.pointQuery(q))
      }
    }
    measure method "spaceOptimized" in {
      using(spaceOptimized) in {
        case (data, query) =>
          query.foreach(q => data.pointQuery(q))
      }
    }
  }

  performance of "insert" in {
    measure method "readOptimized" in {
      using(readOptimized) in {
        case (data, query) =>
          query.foreach(q => data.insert(q))
      }
    }
    measure method "writeOptimized (fresh)" in {
      using(writeOptimized) in {
        case (data, query) =>
          query.foreach(q => data.insert(q))
      }
    }
    measure method "writeOptimized (used)" in {
      using(writeOptimizedUsed) in {
        case (data, query) =>
          query.foreach(q => data.insert(q))
      }
    }
    measure method "spaceOptimized" in {
      using(spaceOptimized) in {
        case (data, query) =>
          query.foreach(q => data.insert(q))
      }
    }
  }

  performance of "delete" in {
    measure method "readOptimized" in {
      using(readOptimized) in {
        case (data, query) =>
          query.foreach(q => data.delete(q))
      }
    }
    measure method "writeOptimized (fresh)" in {
      using(writeOptimizedUsed) in {
        case (data, query) =>
          query.foreach(q => data.delete(q))
      }
    }
    measure method "writeOptimized (used)" in {
      using(writeOptimized) in {
        case (data, query) =>
          query.foreach(q => data.delete(q))
      }
    }
    measure method "spaceOptimized" in {
      using(spaceOptimized) in {
        case (data, query) =>
          query.foreach(q => data.delete(q))
      }
    }
  }

}
