organization := "org.make.front"
name := "make-backoffice"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.1"


val CreateReactClassVersion = "15.5.1"
val HistoryVersion = "4.6.1"
val ReactVersion = "15.5.4"
val ReactReduxVersion = "5.0.3"
val ReactRouterVersion = "4.0.0"
val ReactRouterReduxVersion = "next"
val ReduxVersion = "3.6.0"
val ReduxDevToolsVersion = "2.13.0"
val WebpackVersion = "2.3.2"
val log4jsVersion = "1.4.9"

val scalaJsReactVersion = "0.14.0"

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "io.github.shogowada" %%% "scalajs-reactjs" % scalaJsReactVersion, // For react facade
  "io.github.shogowada" %%% "scalajs-reactjs-router-redux" % scalaJsReactVersion, // Optional. For react-router-dom facade
  "io.github.shogowada" %%% "scalajs-reactjs-router-dom" % scalaJsReactVersion, // Optional. For react-router-dom facade
  "io.github.shogowada" %%% "scalajs-reactjs-redux" % scalaJsReactVersion, // Optional. For react-redux facade
  "io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % scalaJsReactVersion // Optional. For redux-devtools facade
)

npmDependencies in Compile ++= Seq(
  "create-react-class" -> CreateReactClassVersion,
  "react" -> ReactVersion,
  "react-dom" -> ReactVersion,
  "react-router" -> ReactRouterVersion,
  "react-router-dom" -> ReactRouterVersion,
  "react-router-redux" -> ReactRouterReduxVersion,
  "redux-devtools-extension" -> ReduxDevToolsVersion,
  "react-redux" -> ReactReduxVersion,
  "redux" -> ReduxVersion,
  "history" -> HistoryVersion,
  "gapi-google" -> "1.0.3"
)

npmResolutions in Compile := {
  (npmDependencies in Compile).value.toMap
}

version in webpack := WebpackVersion
webpackResources := baseDirectory.value / "src" / "main" / "webpack" ** "*.js"
