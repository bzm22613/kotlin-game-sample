const HtmlWebpackPlugin = require('html-webpack-plugin');

config.plugins.push(
    new HtmlWebpackPlugin({
      inject: true,
      template: require("path").resolve("") + '/../public/index.html'
    })
);
