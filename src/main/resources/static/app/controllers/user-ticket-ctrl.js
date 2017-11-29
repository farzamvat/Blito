/**
 * Created by soroush on 7/19/17.
 */

angular.module('eventsPageModule')
    .controller('userTicketCtrl', function($scope, ticketsService, $routeParams, mapMarkerService, photoService, FileSaver){
        $("#buyTicket").modal("hide");
        function contains(value, searchFor)
        {
            var v = (value || '').toLowerCase();
            var v2 = searchFor;
            if (v2) {
                v2 = v2.toLowerCase();
            }
            return v.indexOf(v2) > -1;
        }
        $scope.imageDownload = function (UUID) {
            photoService.download(UUID)
                .then(function (data) {
                    $scope.imageBase64 = data.data.encodedBase64;
                })
                .catch(function (data) {
                })
        };
        ticketsService.getBoughtTicket($routeParams.trackCode)
            .then(function (data) {
                $scope.paymentStatus = data.data.result.status;
                if($scope.paymentStatus) {
                    $scope.ticketData = data.data;
                    mapMarkerService.initMapOnlyShowMarker(document.getElementById('ticketMap'));
                    mapMarkerService.setMarker($scope.ticketData.location.latitude, $scope.ticketData.location.longitude);
                    $scope.imageDownload($scope.ticketData.eventPhotoId);
                } else {
                    $scope.ticketMessage = data.data.result.message;
                }
            })
            .catch(function (data) {
                console.log(data);
            })
        $scope.getPdf = function () {
            ticketsService.getPdfTicket($scope.ticketData.trackCode)
                .then(function (data) {
                    var excelData = new Blob([data.data], { type: 'application/pdf;charset=UTF-8'});
                    FileSaver.saveAs(excelData, 'blit.pdf');
                })
                .catch(function (data) {
                    console.log(data)
                })
        }
    })
    ;