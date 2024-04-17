package readers

import model.PostgresCredentials
import org.apache.spark.sql.{DataFrame, SparkSession}

object JBDCRead {

  def postgresRead(
      spark: SparkSession,
      creds: PostgresCredentials,
      database: String,
      tableOrQuery: String
  ): DataFrame = {

    spark.read
      .format("jdbc")
      .option("url", creds.url + database)
      .option("driver", creds.driver)
      .option("user", creds.user)
      .option("password", creds.password)
      .option("dbtable", tableOrQuery)
      .load()
  }

}
