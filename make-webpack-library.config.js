var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = require('./scalajs.webpack.config');

module.exports.entry.main = path.join(__dirname, "main.sass");

module.exports.module = module.exports.module || {};

module.exports.plugins = [
    new HtmlWebpackPlugin({
        "title": "Make.org - Backoffice",
        "template": path.join(__dirname, "index-library.template.ejs"),
        "apiUrl": "https://api.preprod.makeorg.tech",
        "googleAppId": "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com",
        "inject": false
    }),
    new ExtractTextPlugin({ // define where to save the file
        filename: '[name].bundle.css',
        allChunks: true
    })
];



module.exports.module.rules = [
    {
        test: /\.sass$/,
        loader: ExtractTextPlugin.extract(['css-loader', 'sass-loader'])
    },
    {
        "test": new RegExp("\\.js$"),
        "enforce": "pre",
        "loader": "source-map-loader",
        "exclude": [
            path.join(__dirname, "node_modules/react-google-login")
        ]
    },
    {
        test: /\.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
        loader: 'file-loader?name=fonts/[name].[ext]'
    },
    {
        test: /\.(jpe?g|gif|png)$/,
        loader: 'file-loader?name=images/[name].[ext]',
        include: [path.join(__dirname, "images")]
    }
];