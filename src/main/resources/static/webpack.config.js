var webpack = require('webpack');
var UglifyJSPlugin = require('uglifyjs-webpack-plugin');
module.exports = {
    context: __dirname + '/app',
    entry: './app.js',
    output: {
        path: __dirname + '/',
        filename: 'bundle.js'
    },
    optimization: {
        minimizer: [
            new UglifyJSPlugin({
                uglifyOptions: {
                    beautify: false,
                    compress: true,
                    comments: false,
                    mangle: false,
                    toplevel: true,
                    keep_classnames: true,
                    keep_fnames: true
                }
            })
        ]
    }
};

