/**
 * Created by soroush on 4/18/17.
 */

angular.module('homePageModule', [])
    .controller('homeCtrl', function ($scope, miniSliderService, photoService, indexBannerService, ourOffersService, eventDetailService, $q,config) {
        $scope.concertRow = [];
        $scope.showSections = [false,false,false,false,false,false,false];
        $scope.showSectionsExcahnge = [false,false];
        $scope.bannerData = [];
        var promises = [[],[],[],[],[],[],[]];
        var promisesExchange = [[], []];
        // $scope.url = "http://localhost:3000"+"/event-page/";
        //
        // $scope.urlExchange = "http://localhost:3000"+"/exchange-page/";
        $scope.url = config.baseUrl+"/event-page/";
        $scope.urlExchange = config.baseUrl+"/exchange-page/";
        indexBannerService.getIndexBanner()
            .then(function (data) {
                $scope.bannerData = $scope.catchImagesExchange(data.data.content, 0);
            })
            .catch(function (data) {
            });
        miniSliderService.getSlidingDataEvents("CONCERT", 6, false)
            .then(function (data) {
                $scope.concertRow = $scope.catchImagesEvents(data.data.content, 0);
                $scope.concertRow = $scope.concertRow.map(eventDetailService.calculateFreeBlits);
                $scope.calculateCapacitySoldOut($scope.concertRow);
            })
            .catch(function (data) {
            });
        ourOffersService.getOurOffer("CONCERT", false)
            .then(function (data) {
                $scope.ourOfferConcert = $scope.catchImagesEvents(data.data.content, 1);
                $scope.calculateCapacitySoldOut($scope.ourOfferConcert);

            })
            .catch(function (data) {
            });
        miniSliderService.getSlidingDataEvents("WORKSHOP", 6, false)
            .then(function (data) {
                $scope.secondSection = $scope.catchImagesEvents(data.data.content, 2);
                $scope.secondSection = $scope.secondSection.map(eventDetailService.calculateFreeBlits);
                $scope.calculateCapacitySoldOut($scope.secondSection);
            })
            .catch(function (data) {
            });
        ourOffersService.getOurOffer("WORKSHOP", false)
            .then(function (data) {
                $scope.ourOfferTour = $scope.catchImagesEvents(data.data.content, 3);
                $scope.calculateCapacitySoldOut($scope.ourOfferTour);
            })
            .catch(function (data) {
            });
        miniSliderService.getSlidingDataEvents("evento", 6, true)
            .then(function (data) {
                $scope.evento = $scope.catchImagesEvents(data.data.content, 4);
                $scope.evento = $scope.evento.map(eventDetailService.calculateFreeBlits);
                $scope.calculateCapacitySoldOut($scope.evento);
            })
            .catch(function (data) {
            });

        ourOffersService.getOurOffer("evento", true)
            .then(function (data) {
                $scope.ourOfferEvento = $scope.catchImagesEvents(data.data.content, 5);
                $scope.calculateCapacitySoldOut($scope.ourOfferEvento);
            })
            .catch(function (data) {
            });
        miniSliderService.getSlidingDataExchange(6)
            .then(function (data) {
                $scope.exchange = $scope.catchImagesExchange(data.data.content, 1);
            })
            .catch(function (data) {
            });
        miniSliderService.getEndedEvents(6)
            .then(function (data) {
                $scope.ended = $scope.catchImagesEvents(data.data.content, 6);
            })
            .catch(function (data) {
            });


        $scope.catchImagesEvents = function (events, i) {
            events =  events.map(function (item) {
                var newSet =  item.images.filter(function (image) {
                    return image.type === "EVENT_PHOTO";
                });
                promises[i].push(photoService.download(newSet[0].imageUUID)
                    .then(function (data) {
                        item.image = data.data.encodedBase64;
                    })
                    .catch(function (data) {
                    })
                );
                return item;

            });
            $q.all(promises[i])
                .then(function () {
                    $scope.showSections[i] = true;
                })
            return events;
        };
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
                    }))
                return item;

            });
            $q.all(promisesExchange[i])
                .then(function () {
                    $scope.showSectionsExcahnge[i] = true;
                })
            return events;
        }

    });