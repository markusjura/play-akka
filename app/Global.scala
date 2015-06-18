import com.typesafe.conductr.bundlelib.play.StatusService
import com.typesafe.conductr.bundlelib.play.ConnectionContext.Implicits.defaultContext
import play.api.{Application, GlobalSettings}

object Global extends GlobalSettings {

  override def onStart(app: Application): Unit = {
    super.onStart(app)

    // Signal to ConductR that app is ready
    StatusService.signalStartedOrExit()
  }
}
