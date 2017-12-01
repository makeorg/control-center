addSbtPlugin("org.scala-js"     % "sbt-scalajs"         % "0.6.20")
addSbtPlugin("ch.epfl.scala"    % "sbt-scalajs-bundler" % "0.9.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.2")
addSbtPlugin("com.geirsson"     % "sbt-scalafmt"        % "1.2.0")
addSbtPlugin("org.make"         % "git-hooks-plugin"    % "1.0.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-git"             % "0.9.3")

resolvers += Resolver.url("bintray-flaroche-sbt-plugins", url("http://dl.bintray.com/flaroche/make-sbt-plugins/"))(
  Resolver.ivyStylePatterns
)

classpathTypes += "maven-plugin"
