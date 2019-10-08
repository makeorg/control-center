/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

addSbtPlugin("org.scala-js"     % "sbt-scalajs"            % "0.6.29")
addSbtPlugin("ch.epfl.scala"    % "sbt-scalajs-bundler"    % "0.13.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager"    % "1.3.3")
addSbtPlugin("com.geirsson"     % "sbt-scalafmt"           % "1.4.0")
addSbtPlugin("org.make"         % "git-hooks-plugin"       % "1.0.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-git"                % "0.9.3")
addSbtPlugin("org.scalastyle"   %% "scalastyle-sbt-plugin" % "1.0.0")

classpathTypes += "maven-plugin"
