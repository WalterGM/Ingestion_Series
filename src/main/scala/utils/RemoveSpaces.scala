package utils

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

object RemoveSpaces {

  def apply()(df: DataFrame): DataFrame = {
    val columns = df.columns
    columns.foldLeft(df: DataFrame){(df, x) =>
      df.withColumn(x,trim(col(x)))
    }

  }

}
