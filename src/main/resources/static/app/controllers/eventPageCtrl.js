/**
 * Created by soroush on 4/25/17.
 */
angular.module('eventsPageModule')
    .controller('eventPageCtrl', function ($scope, $routeParams, eventService, mapMarkerService, photoService, $q, dateSetterService, $timeout) {
        var promises = [];
        $scope.mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(35.7023, 51.3957),
            mapTypeId: google.maps.MapTypeId.TERRAIN
        };
        $scope.setClass = function (index) {
            return "classDate"+index;
        };
        $scope.eventData = {};
        eventService.getEvent($routeParams.eventLink)
            .then(function (data, status) {
                $scope.eventDataDetails = angular.copy(data.data);
                console.log($scope.eventDataDetails);
                mapMarkerService.initMapOnlyShowMarker(document.getElementById('map'));
                mapMarkerService.setMarker($scope.eventDataDetails.latitude, $scope.eventDataDetails.longitude);
                $scope.flatEventDates($scope.eventDataDetails.eventDates);
                $scope.getImages(data.data);

            })
            .catch(function (data) {
                console.log(data);
            });
        $scope.eventFlatDates = [];
        $scope.flatEventDates = function (dates) {
            var index = 0;
            for(var i = 0; i < dates.length; i++) {
                for(var j = 0; j < dates[i].blitTypes.length; j++) {
                    $scope.eventFlatDates[index] = {
                        name : dates[i].blitTypes[j].name,
                        capacity : dates[i].blitTypes[j].capacity,
                        blitTypeState : dates[i].blitTypes[j].blitTypeState,
                        price : dates[i].blitTypes[j].price,
                        date : dates[i].date,
                        soldCount : dates[i].blitTypes[j].soldCount,
                        isFree : dates[i].blitTypes[j].isFree,
                        blitTypeId : dates[i].blitTypes[j].blitTypeId

                    } ;
                    index++;
                }
            }
            $timeout(function () {
                for(var i = 0 ; i < $scope.eventFlatDates.length; i++) {
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
                    jqSelect.pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.eventFlatDates[i].date).pDate));
                }
            }, 300);
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
            });
        };

        $scope.buyTicket = function (info) {
            ticketsService.buyTicket()
                .then(function (data) {
                    console.log(data);
                })
                .catch(function (data) {
                    console.log(data);
                })
        };
        $scope.eventType = "theatre";
        $scope.nextStep1 = function (eventInfo) {
            $scope.totalPrice = eventInfo.ticketNumber * 10000;
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '50%');
            angular.element(document.getElementById('ticketPay1')).removeClass('active');
            angular.element(document.getElementById('selectTicket')).removeClass('active');
            angular.element(document.getElementById('ticketPay2')).addClass('active').addClass('in');
            angular.element(document.getElementById('payment')).addClass('active');
        };
        $scope.prevStep1 = function () {
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '0');
            angular.element(document.getElementById('ticketPay1')).addClass('active').addClass('in');
            angular.element(document.getElementById('selectTicket')).addClass('active');
            angular.element(document.getElementById('ticketPay2')).removeClass('active');

            angular.element(document.getElementById('payment')).removeClass('active');
        };
        $scope.nextStep2 = function () {
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '100%');
            angular.element(document.getElementById('ticketPay2')).removeClass('active');
            angular.element(document.getElementById('payment')).removeClass('active');
            angular.element(document.getElementById('ticketPay3')).addClass('active').addClass('in');
            angular.element(document.getElementById('paymentComplete')).addClass('active');

        };
        $scope.showBuyTicketModal = function () {

            $("#buyTicket").modal("show");
        }
        $scope.hideTicketPaymentModal = function () {
            $("#buyTicket").modal("hide");
        }


    });
