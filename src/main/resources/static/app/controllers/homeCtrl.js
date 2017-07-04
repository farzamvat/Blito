/**
 * Created by soroush on 4/18/17.
 */

angular.module('homePageModule', [])
    .controller('homeCtrl', function ($scope, miniSliderService, photoService, indexBannerService, ourOffersService, $q,config) {
        $scope.concertRow = [];
        $scope.showImages = [[],[], [], []];
        $scope.bannerData = [];
        $scope.showImagesExchange = [];
        $scope.url = "http://localhost:3000"+"/event-page/";
        $scope.urlExchange = "http://localhost:3000"+"/exchange-page/";
        indexBannerService.getIndexBanner()
            .then(function (data) {
                console.log(data);
                $scope.bannerData = $scope.catchImagesExchange(data.data.content);
            })
            .catch(function (data) {
                console.log(data);
            });
        miniSliderService.getSlidingDataEvents("CONCERT", 6, false)
            .then(function (data) {
                $scope.concertRow = $scope.catchImagesEvents(data.data.content, 0);
                console.log($scope.concertRow);
            })
            .catch(function (data) {
                console.log(data);
            });
        miniSliderService.getSlidingDataEvents("TOURISM", 6, false)
            .then(function (data) {
                $scope.tourRow = $scope.catchImagesEvents(data.data.content, 1);
                console.log(data);
            })
            .catch(function (data) {
                console.log(data);
            });
        miniSliderService.getSlidingDataEvents("evento", 6, true)
            .then(function (data) {
                $scope.evento = $scope.catchImagesEvents(data.data.content, 2);
                console.log(data);
            })
            .catch(function (data) {
                console.log(data);
            });
        miniSliderService.getSlidingDataExchange(6)
            .then(function (data) {
                $scope.exchange = $scope.catchImagesExchange(data.data.content);
                console.log($scope.exchange);
            })
            .catch(function (data) {
                console.log(data);
            });
        miniSliderService.getEndedEvents(6)
            .then(function (data) {
                $scope.ended = $scope.catchImagesEvents(data.data.content);
            })
            .catch(function (data) {
                console.log(data);
            });
        ourOffersService.getOurOffer("CONCERT", false)
            .then(function (data) {
                $scope.ourOfferConcert = $scope.catchImagesEvents(data.data.content, 3);
            })
            .catch(function (data) {
                console.log(data);
            });
        ourOffersService.getOurOffer("TOURISM", false)
            .then(function (data) {
                $scope.ourOfferTour = $scope.catchImagesEvents(data.data.content, 3);
                console.log(data);
            })
            .catch(function (data) {
                console.log(data);
            });
        ourOffersService.getOurOffer("evento", true)
            .then(function (data) {
                $scope.ourOfferEvento = $scope.catchImagesEvents(data.data.content, 3);
                console.log(data);
            })
            .catch(function (data) {
                console.log(data);
            });
        $scope.catchImagesEvents = function (events, i) {
            return events.map(function (item) {
                var newSet =  item.images.filter(function (image) {
                    return image.type === "EVENT_PHOTO";
                });
                 photoService.download(newSet[0].imageUUID)
                    .then(function (data) {
                        item.image = data.data.encodedBase64;
                        $scope.showImages[i].push(0);
                    })
                    .catch(function (data) {
                        console.log(data);
                    });
                return item;

            });
        };
        $scope.catchImagesExchange = function (events) {
            events.map(function (item) {
                photoService.download(item.image.imageUUID)
                    .then(function (data) {
                        item.newImage = data.data.encodedBase64;
                        $scope.showImagesExchange.push(0);
                    })
                    .catch(function (data) {
                        console.log(data);
                    });
                return item;

            });
            return events;
        }
    });