/**
 * Created by soroush on 6/22/17.
 */

angular.module('eventsPageModule')
    .controller('exchangePageCtrl', function ($rootScope, $scope, $routeParams, exchangeService, mapMarkerService, dateSetterService, photoService, $timeout, $location) {
        $scope.userEmail = 'email';

        exchangeService.getExchange($routeParams.exchangeLink)
            .then(function (data) {
                $scope.exchangeData = data.data;
                $scope.catchImagesExchange($scope.exchangeData.image.imageUUID)
                mapMarkerService.initMapOnlyShowMarker(document.getElementById('map'));
                mapMarkerService.setMarker($scope.exchangeData.latitude, $scope.exchangeData.longitude);
                $rootScope.title = $location.path().replace('/exchange-page/','').replace( /\d+/,'').replace( /-/,'');
                $rootScope.pageDescription = $scope.exchangeData.description;
                $rootScope.keyWord =  $scope.exchangeData.title +'آگهی بلیط'+ '،'+ 'آگهی بلیط کنسرت' + '،'+ 'آگهی بلیط تئاتر' ;
                $rootScope.robotValue = 'index';
                $timeout(function () {
                    $(".dateInit").val(persianDate($scope.exchangeData.eventDate).format("dddd,DD MMMM, ساعت HH:mm"));
                },1000)
            })
            .catch(function (data) {
            });
        $scope.catchImagesExchange = function (UUID) {
            photoService.download(UUID)
                .then(function (data) {
                    $scope.exchangeData.newImage = data.data.encodedBase64;
                })
                .catch(function (data) {
                })
        };

        $scope.eventType = "cinema";

    });