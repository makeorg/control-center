organization := "org.make.backoffice"
name := "make-backoffice"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.1"

/* scala libraries version */
val scalaJsReactVersion = "0.14.0"
val circeVersion = "0.8.0"
val scalajsDomVersion = "0.9.1"
val scalaCssCoreVersion = "0.5.3"

/* Npm versions */
val npmReactVersion = "15.5.4"
val npmReactRouterVersion = "4.1.2"
val npmWebpackVersion = "2.6.1"
val npmBulmaVersion = "0.4.4"
val npmSassLoaderVersion = "6.0.6"
val npmNodeSassVersion = "4.5.3"
val npmExtractTextWebpackPluginVersion = "2.1.2"
val npmCssLoaderVersion = "0.28.4"
val npmStyleLoaderVersion = "0.18.2"
val npmCleanWebpackPluginVersion = "0.1.16"
val npmHtmlWebpackPluginVersion = "2.29.0"
val npmWebpackMd5HashVersion = "0.0.5"
val npmFontAwesomeVersion = "4.7.0"
val npmFileLoaderVersion = "0.11.2"
val npmNormalizeVersion = "7.0.0"
val npmBootstrapVersion = "3.3.7"
val npmAdminOnRestVersion = "1.2.3"
val npmMaterialUi = "0.18.7"
val npmReactGoogleLogin = "2.9.2"

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "org.scala-js"        %%% "scalajs-dom"                    % scalajsDomVersion,
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
  "react" -> npmReactVersion,
  "react-dom" -> npmReactVersion,
  "react-router" -> npmReactRouterVersion,
  "react-router-dom" -> npmReactRouterVersion,
  "react-google-login" -> npmReactGoogleLogin,
  "admin-on-rest" -> npmAdminOnRestVersion,
  "material-ui" -> npmMaterialUi,
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

version in webpack := npmWebpackVersion
webpackResources := {
  baseDirectory.value / "src" / "main" / "static" / "sass" ** "*.sass" +++
    baseDirectory.value / "src" / "main" / "static" / "sass" ** "*.css"
}

webpackDevServerPort := 4242

emitSourceMaps := false

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "make-webpack-dev.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "make-webpack-prod.config.js")

// Prod settings
scalacOptions ++= Seq("-Xelide-below", "OFF")

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

gitCommitMessageHook := Some(baseDirectory.value / "bin" / "commit-msg.hook")

enablePlugins(GitHooks)