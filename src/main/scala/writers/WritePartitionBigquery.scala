//package writers
//
//import com.google.cloud.bigquery.InsertAllRequest.RowToInsert
//import com.google.cloud.bigquery.{BigQuery, BigQueryOptions, InsertAllRequest, TableId}
//import com.typesafe.config.Config
//import org.apache.spark.sql.DataFrame
//import org.apache.spark.sql.functions._
//
//import scala.collection.JavaConverters.mapAsJavaMap
//
//object  WritePartitionBigquery  {
//
//  def writePartitionBigquery(
//      df: DataFrame,
//      partitionName: String,
//      config: Config
//  ): Unit = {
//   lazy val bigquery: BigQuery = BigQueryOptions.getDefaultInstance.getService
//    val table: TableId = TableId.of(
//      config.getString("projectID"),
//      config.getString("datasetId"),
//      config.getString("tableId")
//    )
//
//    df.repartition(col(partitionName)).rdd.foreachPartition { row =>
//      val insertRequest = InsertAllRequest.newBuilder(table)
//        row.foreach { r =>
//          val fields: Map[String, Any] =
//            r.schema.fieldNames.foldLeft(Map(): Map[String, Any]) {
//              (acc, field) =>
//                if (field == "series_id" || field == "period")
//                  acc + (field -> r.getAs[String](field))
//                else
//                  acc + (field -> r.getAs[Int](field))
//            }
//          val insertId = s"$partitionName=1939"
//          val rowToInsert: RowToInsert =
//            InsertAllRequest.RowToInsert.of(insertId, mapAsJavaMap(fields))
//          insertRequest.addRow(rowToInsert)
//        }
//
//      bigquery.insertAll(insertRequest.build())
//    }
//  }
//
//}
