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

var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var WebpackMd5Hash = require('webpack-md5-hash');
var pathBuild = path.join(__dirname, 'dist');
var scalajs = require('./scalajs.webpack.config');
var UglifyJsPlugin = require('uglifyjs-webpack-plugin');


var prod = {
    entry: {
        "make-backoffice-opt": scalajs.entry["make-backoffice-opt"],
        "main": path.join(__dirname, "main.sass")
    },
    output: {
        path: pathBuild,
        "filename": "[name].[chunkhash].js",
        publicPath: '/'
    },
    plugins: [
        new HtmlWebpackPlugin({
            "title": "Make.org - Backoffice",
            "template": path.join(__dirname, "index.template.ejs"),
            "apiUrl": "API_URL",
            "googleAppId": "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com"
        }),
        new WebpackMd5Hash(),
        new ExtractTextPlugin({ // define where to save the file
            filename: '[name].[chunkhash].bundle.css',
            allChunks: true
        })
    ],
    module: {
        rules: [
            {
                test: /\.(sass|css)$/,
                loader: ExtractTextPlugin.extract(['css-loader', 'sass-loader'])
            },
            {
                test: /\.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
                loader: 'file-loader?name=fonts/[name].[ext]'
            },
            {
                test: /\.(jpe?g|gif|png)$/,
                loader: 'file-loader?name=images/[name].[hash].[ext]',
                include: [path.join(__dirname, "images")]
            }
        ]
    },
    optimization: {
          minimizer: [
            new UglifyJsPlugin({
                cache: true,
                parallel: true,
                uglifyOptions: {
                  compress: false,
                  ecma: 6,
                  mangle: true
                },
                sourceMap: true
              })
          ]
        }
};


module.exports = prod;
