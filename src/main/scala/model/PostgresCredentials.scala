package model

import spray.json.DefaultJsonProtocol

case class PostgresCredentials(
                              url: String,
                              user: String,
                              driver: String,
                              password: String
                              )
object postgresCredentialsProtocol extends DefaultJsonProtocol{
  implicit val credentialsProtocol = jsonFormat4(PostgresCredentials)
}
