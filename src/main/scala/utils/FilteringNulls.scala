package utils

import org.apache.spark.sql.DataFrame

object FilteringNulls {

  def apply()(df: DataFrame): DataFrame = {

    df.na.drop("all")
  }

}
