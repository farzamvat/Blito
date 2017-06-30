/**
 * Created by soroush on 4/18/17.
 */

angular.module('homePageModule', [])
    .controller('homeCtrl', function ($scope, miniSliderService, photoService) {
        $scope.concertRow = [];
        miniSliderService.getSlidingDataEvents("CONCERT", 6, false)
            .then(function (data, status) {
                $scope.concertRow = $scope.catchImagesEvents(data.data.content);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        ;
        miniSliderService.getSlidingDataEvents("TOURISM", 6, false)
            .then(function (data, status) {
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
                console.log(data);
                $scope.ended = $scope.catchImagesEvents(data.data.content);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        $scope.catchImagesEvents = function (events) {
            events.map(function (item) {
                var newSet =  item.images.filter(function (image) {
                    return image.type === "EVENT_PHOTO";
                });
                newSet.map(function (image) {
                    return photoService.download(image.imageUUID)
                        .then(function (data, status) {
                            image.imageUUID = data.data.encodedBase64;
                            return image;
                        })
                        .catch(function (data, status) {
                            console.log(data);
                        })
                });
                item.images = newSet;
                return item;

            });
            return events;
        }
        $scope.catchImagesExchange = function (events) {
            events.map(function (item) {
                     photoService.download(item.image.imageUUID)
                        .then(function (data, status) {
                           item.image.imageUUID = data.data.encodedBase64;
                            return item.image;
                        })
                        .catch(function (data, status) {
                            console.log(data);
                        })
                return item;

            });
            return events;
        }
    });