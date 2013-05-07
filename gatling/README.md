gatling-maven-plugin
=========================

For a single node test, deploy "flights demo" (helloorm2) app from GAE SDK on http://localhost:8080/ and execute:

    mvn clean gatling:execute -Dgatling.simulationClass=fligtsDemo.FlightsDemo

For multinode test define hosts in `src/test/scala/fligtsDemo/FlightsDemoMultiNode.scala` and run

    mvn clean gatling:execute -Dgatling.simulationClass=fligtsDemo.FlightsDemoMultiNode

For more informations see [Gatling demo](https://github.com/excilys/gatling-maven-plugin-demo) and [maven plugin home page](https://github.com/excilys/gatling/wiki/Maven-plugin).

