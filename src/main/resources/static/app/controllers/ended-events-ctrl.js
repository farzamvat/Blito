/**
 * Created by soroush on 12/23/17.
 */

angular.module('eventsPageModule')
    .controller('endedEventsPageCtrl', function ($scope, $location, eventService, photoService, eventDetailService, config, miniSliderService) {

        $scope.url = config.baseUrl+"/event-page/";

        $scope.getEventsByTypeData = function (page) {
            miniSliderService.getEndedEvents(page, 12)
                .then(function (data) {
                    $scope.totalEventsNumber = data.data.totalElements;
                    $scope.eventList = $scope.catchImagesEvents(data.data.content);
                    $scope.eventList = $scope.eventList.map(eventDetailService.calculateFreeBlits);
                    console.log($scope.eventList);
                    $scope.calculateCapacitySoldOut($scope.eventList);
                })
                .catch(function (data) {
                });
        };
        $scope.pageChanged = function (newPage) {
            $scope.getEventsByTypeData(newPage);
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
        $scope.currentPage = 1;
        $scope.getEventsByTypeData(1);
    });
