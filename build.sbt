organization := "org.make.backoffice"
name := "make-backoffice"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.1"

val CreateReactClassVersion = "15.5.3"
val HistoryVersion = "4.6.1"
val ReactVersion = "15.5.4"
val ReactReduxVersion = "5.0.3"
val ReactRouterVersion = "4.0.0"
val ReactRouterReduxVersion = "next"
val ReduxVersion = "3.6.0"
val ReduxDevToolsVersion = "2.13.0"
val WebpackVersion = "2.3.2"
val log4jsVersion = "1.4.9"
val circeVersion = "0.8.0"

val scalaJsReactVersion = "0.14.0"
val npmBulmaVersion = "0.4.4"
val npmReactAutosuggestVersion = "9.3.1"
val npmSassLoaderVersion = "6.0.6"
val npmNodeSassVersion = "4.5.3"
val npmExtractTextWebpackPluginVersion = "2.1.2"
val npmCssLoaderVersion = "0.28.4"
val npmStyleLoaderVersion = "0.18.2"
val npmReactModalVersion = "2.2.2"
val npmReactI18nifyVersion = "1.8.7"
val npmCleanWebpackPluginVersion = "0.1.16"
val npmHtmlWebpackPluginVersion = "2.29.0"
val npmWebpackMd5HashVersion = "0.0.5"
val npmFontAwesomeVersion = "4.7.0"
val npmFileLoaderVersion = "0.11.2"
val npmNormalizeVersion = "7.0.0"
val npmBootstrapVersion = "3.3.7"
val npmAdminOnRestVersion = "1.2.3"

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "org.scala-js"        %%% "scalajs-dom"                    % "0.9.1",
  "io.github.shogowada" %%% "scalajs-reactjs"                % scalaJsReactVersion, // For react facade
  "io.github.shogowada" %%% "scalajs-reactjs-router-redux"   % scalaJsReactVersion, // Optional. For react-router-dom facade
  "io.github.shogowada" %%% "scalajs-reactjs-router-dom"     % scalaJsReactVersion, // Optional. For react-router-dom facade
  "io.github.shogowada" %%% "scalajs-reactjs-redux"          % scalaJsReactVersion, // Optional. For react-redux facade
  "io.github.shogowada" %%% "scalajs-reactjs-redux-devtools" % scalaJsReactVersion, // Optional. For redux-devtools facade
  "io.circe"            %%% "circe-core"                     % circeVersion,
  "io.circe"            %%% "circe-java8"                    % circeVersion,
  "io.circe"            %%% "circe-generic"                  % circeVersion,
  "io.circe"            %%% "circe-parser"                   % circeVersion,
  "io.circe"            %%% "circe-scalajs"                  % circeVersion
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
  "react-google-login" -> "2.9.2",
  "admin-on-rest" -> npmAdminOnRestVersion,
  "material-ui" -> "0.18.7",
  "bulma" -> npmBulmaVersion,
  "sass-loader" -> npmSassLoaderVersion,
  "node-sass" -> npmNodeSassVersion,
  "extract-text-webpack-plugin" -> npmExtractTextWebpackPluginVersion,
  "css-loader" -> npmCssLoaderVersion,
  "style-loader" -> npmStyleLoaderVersion,
  "clean-webpack-plugin" -> npmCleanWebpackPluginVersion,
  "html-webpack-plugin" -> npmHtmlWebpackPluginVersion,
  "webpack-md5-hash" -> npmWebpackMd5HashVersion,
  "file-loader" -> npmFileLoaderVersion,
  "font-awesome" -> npmFontAwesomeVersion,
  "normalize-scss" -> npmNormalizeVersion,
  "bootstrap" -> npmBootstrapVersion
)

npmResolutions in Compile := {
  (npmDependencies in Compile).value.toMap
}

version in webpack := WebpackVersion
webpackResources := {
  baseDirectory.value / "src" / "main" / "static" / "sass" ** "*.sass" +++
    baseDirectory.value / "src" / "main" / "static" / "sass" ** "*.css"
}

webpackDevServerPort := 4242

gitCommitMessageHook := Some(baseDirectory.value / "bin" / "commit-msg.hook")

enablePlugins(GitHooks)

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "make-webpack-dev.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "make-webpack-prod.config.js")

// Custome task to manage assets
val prepareAssets = taskKey[Unit]("prepareAssets")
prepareAssets in ThisBuild := {
  val npmDirectory = (npmUpdate in Compile).value
  IO.copyDirectory(baseDirectory.value / "src" / "main" / "static", npmDirectory, overwrite = true)
  streams.value.log.info("Copy assets to working directory")
}
fastOptJS in Compile := {
  prepareAssets.value
  (fastOptJS in Compile).value
}
fullOptJS in Compile := {
  prepareAssets.value
  (fullOptJS in Compile).value
}
