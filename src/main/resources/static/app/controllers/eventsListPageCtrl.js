/**
 * Created by soroush on 4/24/17.
 */

angular.module('eventsPageModule', [])
    .controller('eventsListPageCtrl', function ($scope, $location, eventService, photoService, eventDetailService, config) {

        // $scope.url = "http://localhost:3000"+"/event-page/";
        $scope.url = config.baseUrl+"/event-page/";

        $scope.getEventsByTypeData = function (type,page) {
            eventService.getEventsByType(type, page)
                .then(function (data) {
                    $scope.totalEventsNumber = data.data.totalElements;
                    $scope.eventList = $scope.catchImagesEvents(data.data.content);
                    $scope.eventList = $scope.eventList.map(eventDetailService.calculateFreeBlits);
                    console.log($scope.eventList);
                })
                .catch(function (data) {
                    console.log(data)
                });
        };
        switch($location.path()) {
            case '/sports':
                $scope.pageTitle = "ورزشی";
                $scope.type = "SPORT";
                $scope.getEventsByTypeData("SPORT", 1);
                break;
            case '/cinema':
                $scope.pageTitle = "سینما";
                $scope.type = "CINEMA";
                $scope.getEventsByTypeData("CINEMA", 1);
                break;
            case '/theater':
                $scope.pageTitle = "تئاتر";
                $scope.type = "THEATER";
                $scope.getEventsByTypeData("THEATER", 1);
                break;
            case '/workshop':
                $scope.pageTitle = "کارگاه";
                $scope.type = "WORKSHOP";
                $scope.getEventsByTypeData("WORKSHOP", 1);
                break;
            case '/tour':
                $scope.pageTitle = "تور";
                $scope.type = "TOURISM";
                $scope.getEventsByTypeData("TOURISM", 1);
                break;
            case '/concert':
                $scope.pageTitle = "کنسرت";
                $scope.type = "CONCERT";
                $scope.getEventsByTypeData("CONCERT", 1);
                break;
            case '/other':
                $scope.pageTitle = "سایر";
                $scope.type = "OTHER";
                $scope.getEventsByTypeData("OTHER", 1);
                break;
            default:
                break;
        }
        $scope.pageChanged = function (newPage) {
            $scope.getEventsByTypeData($scope.type, newPage);
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
                        console.log(data);
                    });
                return item;

            });
        };


        $scope.currentPage = 1;
    });
