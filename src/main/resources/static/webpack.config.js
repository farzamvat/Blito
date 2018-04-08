var webpack = require('webpack');
var UglifyJSPlugin = require('uglifyjs-webpack-plugin');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var HtmlWebpackPlugin = require('html-webpack-plugin');
module.exports = {
    context: __dirname + '/app',
    entry: './app.js',
    output: {
        path: __dirname + '/webpack',
        filename: '[name].[hash].js'
    },
    optimization: {
        minimizer: [
            new UglifyJSPlugin({
                uglifyOptions: {
                    beautify: false,
                    compress: true,
                    comments: false,
                    mangle: false,
                    toplevel: false,
                    keep_classnames: true,
                    keep_fnames: true
                }
            })
        ],
        splitChunks: {
            chunks: "all"
        }
        },
    plugins: [
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery"
        }),
        new ExtractTextPlugin("[name].css"),
        new HtmlWebpackPlugin({
            template : 'index.html',
            filename : '../index.html'
        })
    ],

    module: {
        rules: [
            { test: /\.css$/, use: ['style-loader', 'css-loader' ]},
            { test: /\.jpg/,  loader: 'file-loader',   query: { publicPath: '/webpack'} },
            { test: /\.png/,  loader: 'file-loader',   query: { publicPath: '/webpack'}  },
            { test: /\.gif/,  loader: 'file-loader',   query: { publicPath: '/webpack'}  },
            { test: /\.svg/,  loader: 'file-loader',   query: { publicPath: '/webpack'}  },
            { test: /\.(woff|woff2|eot|ttf)$/, loader: 'url-loader?limit=100000',   query: { publicPath: '/webpack'} }

        ]
    }
};

