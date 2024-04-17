package utils

import org.apache.spark.sql.functions.{current_date, input_file_name, udf}
import org.apache.spark.sql.DataFrame

import java.util.UUID

object AddMetaDataColumns {

  def apply()(df: DataFrame): DataFrame = {
    val UUIDGenerator = udf(() => UUID.randomUUID().toString)
    df.withColumn("id", UUIDGenerator())
      .withColumn("loadDate", current_date())
  }

}
