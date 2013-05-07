package fligtsDemo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

class FlightsDemo extends Simulation {

	val httpConf = httpConfig
			//.baseURL("http://192.168.30.206:8080")
			.baseURL("http://localhost:8080")
			.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
			.acceptEncodingHeader("gzip, deflate")
			.acceptLanguageHeader("en-us,en;q=0.5")
			.connection("keep-alive")
			.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:18.0) Gecko/20100101 Firefox/18.0")


	val headers_2 = Map(
			"Content-Type" -> """application/x-www-form-urlencoded"""
	)


	val scn = scenario("Scenario Name")
		.exec(http("request_1")
					.get("/")
			)
		.pause(100 milliseconds, 200 milliseconds)
		.exec(http("request_2")
					.post("/addFlight")
					.headers(headers_2)
					.param("""orig""", """LJ""")
					.param("""dest""", """KP""")
					.param("""q""", """select f from com.google.appengine.demos.helloorm.Flight as f""")
					.check(status.is(200))
			)
		.pause(100 milliseconds, 200 milliseconds)
		.repeat(3) {
			exec(http("request_3")
					.post("/")
					.headers(headers_2)
					.param("""q""", """select f from com.google.appengine.demos.helloorm.Flight as f""")
					.check(status.is(200))
				)
			.pause(100 milliseconds, 200 milliseconds)
		}

	setUp(scn.inject(ramp(10 users) over(1 seconds)).protocolConfig(httpConf))
}
