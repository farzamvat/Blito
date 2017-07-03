/**
 * Created by soroush on 4/25/17.
 */
angular.module('eventsPageModule')
    .controller('eventPageCtrl', function ($scope, $routeParams, eventService, mapMarkerService, photoService, $q, dateSetterService, $timeout) {
        $scope.userEmail = 'email';
        var promises = [];
        $scope.mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(35.7023, 51.3957),
            mapTypeId: google.maps.MapTypeId.TERRAIN
        };
        $scope.eventData = {};
        eventService.getEvent($routeParams.eventLink)
            .then(function (data, status) {
                $scope.eventDataDetails = data.data;
                mapMarkerService.initMapOnlyShowMarker(document.getElementById('map'));
                mapMarkerService.setMarker($scope.eventDataDetails.latitude, $scope.eventDataDetails.longitude);
                $timeout(function () {
                for(var i = 0 ; i < $scope.eventDataDetails.eventDates.length; i++) {
                    var jqSelect = $(".classDate"+i);
                    jqSelect.pDatepicker({
                        timePicker: {
                            enabled: true
                        },
                        altField: '#persianDigitAlt',
                        altFormat: "YYYY MM DD HH:mm:ss",
                        persianDigit : true,
                        altFieldFormatter: function (unixDate) {
                        }
                    });
                    jqSelect.pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.eventDataDetails.eventDates[i].date).pDate));
                }
                }, 300);
                $scope.getImages(data.data);

                console.log($scope.eventData);
            })
            .catch(function (data) {
                console.log(data);
            });

        $scope.setClass = function (index) {
            return "classDate"+index;
        };
        $scope.filterImages = function (images, type) {
            return images.filter(function (item) {
                return item.type === type ;
            })
        };

        $scope.getImages = function (event) {
            event.images.map(function (imageItem) {
               promises.push(photoService.download(imageItem.imageUUID)
                    .then(function (data) {
                        imageItem.imageUUID = data.data.encodedBase64;
                        return imageItem;
                    })
                    .catch(function (data) {
                        console.log(data);
                    })
               )
            });
            $q.all(promises).then(function () {
                event.eventPhoto = $scope.filterImages(event.images,"EVENT_PHOTO");
                event.gallery = $scope.filterImages(event.images,"GALLERY");
                console.log(event);
               $scope.eventDataPhoto = event;
               console.log($scope.eventDataPhoto.gallery.length)
            })
        };

        $scope.eventType = "theatre";

        $('.slick-slider').slick('setPosition');




    });
