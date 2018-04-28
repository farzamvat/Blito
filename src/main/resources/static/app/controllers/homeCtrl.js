/**
 * Created by soroush on 4/18/17.
 */

angular.module('homePageModule', [])
    .controller('homeCtrl', [
        '$scope',
        'miniSliderService',
        'photoService',
        'indexBannerService',
        'eventDetailService',
        '$q',
        'config',
        '$interval',

        function ($scope, miniSliderService, photoService, indexBannerService, eventDetailService, $q,config) {
        $scope.concertRow = [];
        $scope.showSectionsExcahnge = [false,false];
        $scope.bannerData = [];
        var promisesExchange = [[], []];
        $scope.url = config.baseUrl+"/event-page/";
        $scope.urlExchange = config.baseUrl+"/exchange-page/";
        miniSliderService.getAllEventsCount()
            .then(function (data) {
                $scope.totalNumberOfEvents = data.data.count;
            })
            .catch(function (data) {
            });
        indexBannerService.getIndexBanner()
            .then(function (data) {
                $scope.bannerData = $scope.catchImagesExchange(data.data.content, 0);
            })
            .catch(function (data) {
            });
        // use search with getSlidingDataEvents api



        miniSliderService.getSlidingDataExchange(6)
            .then(function (data) {
                $scope.exchange = $scope.catchImagesExchange(data.data.content, 1);
            })
            .catch(function (data) {
            });




        $scope.calculateCapacitySoldOut = function (eventList) {
            eventList.forEach(function (item) {
                item.capacity = 0;
                item.soldCount = 0;
                item.eventDates.forEach(function (eventDate) {
                    item.soldCount += eventDate.soldCount;
                    item.capacity += eventDate.capacity;
                })
            });
        };
        $scope.catchImagesExchange = function (events, i) {
            events.map(function (item) {
                promisesExchange[i].push(photoService.download(item.image.imageUUID)
                    .then(function (data) {
                        item.newImage = data.data.encodedBase64;
                    })
                    .catch(function (data) {
                    }));
                return item;
            });
            $q.all(promisesExchange[i])
                .then(function () {
                    $scope.showSectionsExcahnge[i] = true;
                });
            return events;
        };


    }]);