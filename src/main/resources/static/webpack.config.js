var webpack = require('webpack');
var UglifyJSPlugin = require('uglifyjs-webpack-plugin');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
module.exports = {
    context: __dirname + '/app',
    entry: './app.js',
    output: {
        path: __dirname + '/webpack',
        filename: '[name].js'
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
        new ExtractTextPlugin("[name].css")
    ],

    module: {
        rules: [
            { test: /\.css$/, use: ['style-loader', 'css-loader' ]},
            { test: /\.jpg/,  use: ["file-loader"] },
            { test: /\.png/,  use: ["file-loader"] },
            { test: /\.gif/,  use: ["file-loader"] },
            { test: /\.svg/,  use: ["file-loader"] },
            { test: /\.(woff|woff2|eot|ttf)$/, loader: 'url-loader?limit=100000' }

        ]
    }
};

