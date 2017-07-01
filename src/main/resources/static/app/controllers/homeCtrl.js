/**
 * Created by soroush on 4/18/17.
 */

angular.module('homePageModule', [])
    .controller('homeCtrl', function ($scope, miniSliderService, photoService, indexBannerService) {
        $scope.concertRow = [];
        $scope.showImages = [];
        $scope.bannerData = [];
        $scope.showImagesExchange = [];
        var newTemp;
        indexBannerService.getIndexBanner()
            .then(function (data, status) {
                console.log(data);
                $scope.bannerData = $scope.catchImagesExchange(data.data.content);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        miniSliderService.getSlidingDataEvents("CONCERT", 6, false)
            .then(function (data, status) {
                $scope.concertRow = $scope.catchImagesEvents(data.data.content);
                console.log($scope.concertRow);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        ;
        $scope.$watch('newTemp', function () {
            console.log($scope.concertRow);
        })
        miniSliderService.getSlidingDataEvents("TOURISM", 6, false)
            .then(function (data, status) {
                console.log($scope.catchImagesEvents(data.data.content))
                $scope.tourRow = $scope.catchImagesEvents(data.data.content);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        miniSliderService.getSlidingDataEvents("evento", 6, true)
            .then(function (data, status) {
                $scope.evento = $scope.catchImagesEvents(data.data.content);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        miniSliderService.getSlidingDataExchange(6)
            .then(function (data, status) {
                $scope.exchange = $scope.catchImagesExchange(data.data.content);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        miniSliderService.getEndedEvents(6)
            .then(function (data, status) {
                $scope.ended = $scope.catchImagesEvents(data.data.content);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        $scope.catchImagesEvents = function (events) {
            return events.map(function (item) {
                var newSet =  item.images.filter(function (image) {
                    return image.type === "EVENT_PHOTO";
                });
                 photoService.download(newSet[0].imageUUID)
                    .then(function (data, status) {
                        item.image = data.data.encodedBase64;
                        $scope.showImages.push(0);
                    })
                    .catch(function (data, status) {
                        console.log(data);
                    })
                return item;

            });
        }
        $scope.catchImagesExchange = function (events) {
            events.map(function (item) {
                photoService.download(item.image.imageUUID)
                    .then(function (data, status) {
                        item.newImage = data.data.encodedBase64;
                        $scope.showImagesExchange.push(0);
                    })
                    .catch(function (data, status) {
                        console.log(data);
                    })
                return item;

            });
            return events;
        }
    });