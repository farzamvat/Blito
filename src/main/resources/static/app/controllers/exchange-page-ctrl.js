/**
 * Created by soroush on 6/22/17.
 */

angular.module('eventsPageModule')
    .controller('exchangePageCtrl', function ($scope, $routeParams, exchangeService, mapMarkerService, dateSetterService, photoService) {
        $scope.userEmail = 'email';
        $scope.mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(35.7023, 51.3957),
            mapTypeId: google.maps.MapTypeId.TERRAIN
        };
        exchangeService.getExchange($routeParams.exchangeLink)
            .then(function (data) {
                $scope.exchangeData = data.data;
                $scope.catchImagesExchange($scope.exchangeData.image.imageUUID)
                var jqSelect = $(".dateInit");
                mapMarkerService.initMapOnlyShowMarker(document.getElementById('map'));
                mapMarkerService.setMarker($scope.exchangeData.latitude, $scope.exchangeData.longitude);
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
                jqSelect.pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.exchangeData.eventDate).pDate));
            })
            .catch(function (data) {
                console.log(data);
            });



        $scope.catchImagesExchange = function (UUID) {
            photoService.download(UUID)
                .then(function (data) {
                    $scope.exchangeData.newImage = data.data.encodedBase64;
                })
                .catch(function (data) {
                    console.log(data)
                })
        }

        $scope.eventType = "cinema";

    });