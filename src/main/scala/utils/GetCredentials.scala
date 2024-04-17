package utils

import com.google.cloud.secretmanager.v1.{
  SecretManagerServiceClient,
  SecretVersionName
}
import model.PostgresCredentials
import model.postgresCredentialsProtocol._
import spray.json._

import scala.util.{Failure, Success, Try}

object GetCredentials {

  def getSecret(projectID: String, secretId: String): PostgresCredentials = {
    val client = Try {
      SecretManagerServiceClient.create()
    }
    client match {
      case Success(value) =>
        val secretVersion = SecretVersionName.of(projectID, secretId, "latest")
        val secret = value.accessSecretVersion(secretVersion)
        val payload = secret.getPayload.getData.toStringUtf8.parseJson
        payload.convertTo[PostgresCredentials]
      case Failure(exception) =>
        throw exception

    }

  }

}
