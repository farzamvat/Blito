/**
 * Created by soroush on 4/26/17.
 */

angular.module('bioPageModule', [])
    .controller('bioPageCtrl', function ($scope, plannerService, $routeParams, photoService, config) {
        $scope.url = config.baseUrl+"/event-page/";
        plannerService.getPlannerByLink($routeParams.plannerLink)
            .then(function (data) {
                $scope.plannerData = data.data;
                $scope.getEvents(1);
                for(var i = 0; i < $scope.plannerData.images.length; i++) {
                    if ($scope.plannerData.images[i].type === 'HOST_PHOTO') {
                        photoService.download($scope.plannerData.images[i].imageUUID)
                            .then(function (data) {
                                $scope.plannerPhoto = data.data.encodedBase64;
                            })
                            .catch(function () {
                            })
                    }
                    if ($scope.plannerData.images[i].type === 'HOST_COVER_PHOTO') {
                        photoService.download($scope.plannerData.images[i].imageUUID)
                            .then(function (data) {
                                $scope.plannerPhotoCover = data.data.encodedBase64;
                                angular.element(document.getElementsByClassName('plannerCover')).css('background', 'url("'+$scope.plannerPhotoCover+'") fixed');
                                angular.element(document.getElementsByClassName('plannerCover')).css('background-size', '100% 380px');
                            })
                            .catch(function () {
                            })
                    }
                }
            })
            .catch(function () {
            });
        $scope.getEvents = function (pageNumber) {
          plannerService.getPlannerEvents($scope.plannerData.eventHostLink, pageNumber)
              .then(function (data) {
                  $scope.totalEventsNumber = data.data.totalElements;
                  $scope.eventList = data.data.content;
                  $scope.eventList = $scope.catchImagesEvents(data.data.content);
              })
              .catch(function () {
              })
        };
        $scope.catchImagesEvents = function (events) {
            return events.map(function (item) {
                var newSet =  item.images.filter(function (image) {
                    return image.type === "EVENT_PHOTO";
                });
                photoService.download(newSet[0].imageUUID)
                    .then(function (data, status) {
                        item.image = data.data.encodedBase64;
                    })
                    .catch(function (data, status) {
                    });
                return item;

            });
        };
        $scope.pageChanged = function (newPage) {
            $scope.getEvents(newPage);
        };

        $scope.currentPage = 1;

    });