/**
 * Created by soroush on 6/3/17.
 */

angular.module('exchangesPageModule', [])
    .controller('exchangeListCtrl', function ($scope, exchangeService, photoService) {

        $scope.pageTitle = "بلیت های تعویضی ";
        $scope.getAllExchanges = function (page) {
            exchangeService.getAllExchanges(page)
                .then(function (data) {
                    console.log(data);
                    $scope.exchangeList = $scope.catchImagesExchange(data.data.content);
                })
                .catch(function (data) {
                    console.log(data);
                })
        };

        $scope.catchImagesExchange = function (events) {
            events.map(function (item) {
                photoService.download(item.image.imageUUID)
                    .then(function (data) {
                        item.newImage = data.data.encodedBase64;
                    })
                    .catch(function (data) {
                        console.log(data);
                    });
                return item;

            });
            return events;
        }
        $scope.getAllExchanges(1);
        $scope.currentPage = 1;
    });
