package job

import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import readers.JBDCRead
import utils.GetCredentials.getSecret
import utils.{AddMetaDataColumns, FilteringNulls, RemoveSpaces, baseJob}
import writers.ORCWriter._

object SeriesEL extends baseJob {
  override def executeJob(spark: SparkSession, config: Config): Unit = {
    val secret = getSecret(
      config.getString("projectID"),
      config.getString("secretIdPostgres")
    )

    val dfPeriod = JBDCRead.postgresRead(
      spark,
      secret,
      "challenge",
      "(Select * From \"ce.period\")"
    )
    val enrichPeriodDF =
      dfPeriod
        .transform(AddMetaDataColumns())
        .transform(RemoveSpaces())
        .transform(FilteringNulls())

    enrichPeriodDF.writeORC(config.getString("periodTablePath"), "loadDate")

    val dfDataType = JBDCRead.postgresRead(
      spark,
      secret,
      "challenge",
      s"(${config.getString("queryDataType")})"
    )
    val enrinchDataTypeDF =
      dfDataType
        .transform(AddMetaDataColumns())
        .transform(RemoveSpaces())
        .transform(FilteringNulls())

    enrinchDataTypeDF.writeORC(
      config.getString("datatypeTablePath"),
      "loadDate"
    )

    val dfSeriesInfo = JBDCRead.postgresRead(
      spark,
      secret,
      "challenge",
      s"(${config.getString("querySeriesInfo")})"
    )
    val enrichSeriesInfo =
      dfSeriesInfo
        .transform(AddMetaDataColumns())
        .transform(RemoveSpaces())
        .transform(FilteringNulls())

    enrichSeriesInfo.writeORC(
      config.getString("seriesinfoTablePath"),
      "loadDate"
    )

    val dfSuperSector = JBDCRead.postgresRead(
      spark,
      secret,
      "challenge",
      s"(${config.getString("querySuperSector")})"
    )
    val enrichSuperSector =
      dfSuperSector
        .transform(AddMetaDataColumns())
        .transform(RemoveSpaces())
        .transform(FilteringNulls())

    enrichSuperSector.writeORC(
      config.getString("supersectorTablePath"),
      "loadDate"
    )

    val dfSeries = JBDCRead.postgresRead(
          spark,
          secret,
          "challenge",
          s"(${config.getString("querySeries")})"
        )
    val enrichSeriesDF =
      dfSeries
        .transform(AddMetaDataColumns())
        .transform(RemoveSpaces())
        .transform(FilteringNulls())
        .filter(col("year").between(config.getInt("startYearSeries"),config.getInt("endYearSeries")))

    enrichSeriesDF.writeORC(config.getString("seriesTablePath"), "year")
  }
}
