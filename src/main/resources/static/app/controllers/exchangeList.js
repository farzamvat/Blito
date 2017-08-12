/**
 * Created by soroush on 6/3/17.
 */

angular.module('exchangesPageModule', [])
    .controller('exchangeListCtrl', function ($scope, exchangeService, photoService, dataService, config) {

        $scope.pageTitle = "آگهی بلیت";
        // $scope.urlExchange = "http://localhost:3000"+"/exchange-page/";
        $scope.urlExchange = config.baseUrl+"/exchange-page/";

        $scope.getAllExchanges = function (page) {
            exchangeService.getAllExchanges(page)
                .then(function (data) {
                    $scope.totalElements = data.data.totalElements;
                    $scope.exchangeList = $scope.catchImagesExchange(data.data.content);
                    $scope.exchangeList = $scope.exchangeList.map(function (item) {
                        item.type = dataService.eventTypePersian(item.type);
                        return item;
                    })
                })
                .catch(function (data) {
                })
        };
        $scope.pageChanged = function (newpage) {
            $scope.getAllExchanges(newpage);
        };
        $scope.catchImagesExchange = function (events) {
            events.map(function (item) {
                photoService.download(item.image.imageUUID)
                    .then(function (data) {
                        item.newImage = data.data.encodedBase64;
                    })
                    .catch(function (data) {
                    });
                return item;

            });
            return events;
        };
        $scope.getAllExchanges(1);
        $scope.currentPage = 1;
    });
