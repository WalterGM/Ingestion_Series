package utils

import org.apache.spark.sql.functions.{concat, current_date, date_format, lit, udf}
import org.apache.spark.sql.DataFrame

import java.util.UUID

object AddMetaDataColumns {

  def apply()(df: DataFrame): DataFrame = {
    val UUIDGenerator = udf(() => UUID.randomUUID().toString)
    df.withColumn("id", UUIDGenerator())
      .withColumn("loadDate", current_date())
      .withColumn("batchID", concat(lit("SeriesIngestion|"),date_format(current_date(),"yyyyMMd")))
  }

}
