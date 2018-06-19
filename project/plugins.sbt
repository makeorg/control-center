addSbtPlugin("org.scala-js"     % "sbt-scalajs"         % "0.6.22")
addSbtPlugin("ch.epfl.scala"    % "sbt-scalajs-bundler" % "0.13.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.3")
addSbtPlugin("com.geirsson"     % "sbt-scalafmt"        % "1.4.0")
addSbtPlugin("org.make"         % "git-hooks-plugin"    % "1.0.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-git"             % "0.9.3")

classpathTypes += "maven-plugin"
