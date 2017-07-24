/**
 * Created by soroush on 7/19/17.
 */

angular.module('eventsPageModule')
    .controller('userTicketCtrl', function($scope, ticketsService, $routeParams, mapMarkerService, photoService){
        $("#buyTicket").modal("hide");
        $scope.imageDownload = function (UUID) {
            photoService.download(UUID)
                .then(function (data) {
                    $scope.imageBase64 = data.data.encodedBase64;
                })
                .catch(function (data) {
                    console.log(data);
                })
        };
        ticketsService.getBoughtTicket($routeParams.trackCode)
            .then(function (data) {
                $scope.ticketData = data.data;
                mapMarkerService.initMapOnlyShowMarker(document.getElementById('ticketMap'));
                mapMarkerService.setMarker($scope.ticketData.location.latitude, $scope.ticketData.location.longitude);
                $scope.imageDownload($scope.ticketData.eventPhotoId);
                console.log(data);
            })
            .catch(function (data) {
                console.log(data);
            })

    })
    ;