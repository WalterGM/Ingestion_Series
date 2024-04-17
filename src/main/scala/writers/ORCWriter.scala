package writers

import org.apache.spark.sql.{DataFrame, SaveMode}

object ORCWriter {

  implicit class ORCWriter(df: DataFrame) {
    def writeORC(path: String, partition: String): Unit = {
      df.write.partitionBy(partition).mode(SaveMode.Overwrite).orc(path)
    }
  }

  implicit class ORCWriterNoPartition(df: DataFrame) {
    def writeORCNoPartition(path: String): Unit = {
      df.write.mode(SaveMode.Append).orc(path)

    }

  }
}


