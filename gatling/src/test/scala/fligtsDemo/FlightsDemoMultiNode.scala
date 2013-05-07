package fligtsDemo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

class FlightsDemoMultiNode extends Simulation {

	val httpConf = httpConfig
			.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
			.acceptEncodingHeader("gzip, deflate")
			.acceptLanguageHeader("en-us,en;q=0.5")
			.connection("keep-alive")
			.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:18.0) Gecko/20100101 Firefox/18.0")

	val headers_2 = Map(
			"Content-Type" -> """application/x-www-form-urlencoded"""
	)

	val request1 = http("request_1")
					.get("/")
			
	val request2 = http("request_2")
					.post("/addFlight")
					.headers(headers_2)
					.param("""orig""", """LJ""")
					.param("""dest""", """KP""")
					.param("""q""", """select f from com.google.appengine.demos.helloorm.Flight as f""")
					.check(status.is(200))
			
	val request3 = http("request_3")
						.post("/")
						.headers(headers_2)
						.param("""q""", """select f from com.google.appengine.demos.helloorm.Flight as f""")
						.check(status.is(200))

	def scn(name:String) = scenario(name)
		.exec(request1)
		.pause(100 milliseconds, 200 milliseconds)
		.exec(request2)
		.pause(100 milliseconds, 200 milliseconds)
		.repeat(3) {
			exec(request3)
			.pause(100 milliseconds, 200 milliseconds)
		}

	setUp(
		scn("node1").inject(ramp(10 users) over(1 seconds)).protocolConfig(httpConf.baseURL("http://192.168.0.203:8080")),
		scn("node2").inject(ramp(10 users) over(1 seconds)).protocolConfig(httpConf.baseURL("http://192.168.0.204:8080"))
	)
}
