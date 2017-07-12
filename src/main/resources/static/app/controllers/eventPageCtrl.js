/**
 * Created by soroush on 4/25/17.
 */
angular.module('eventsPageModule')
    .controller('eventPageCtrl', function ($scope, $routeParams, eventService, mapMarkerService, photoService, $q, dateSetterService, $timeout, userInfo, ticketsService) {
        var promises = [];
        $scope.persianSans = [];
        $scope.eventInfo = {};
        $scope.capacityArray = [1,2,3,4,5,6,7,8,9,10];
        $scope.mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(35.7023, 51.3957),
            mapTypeId: google.maps.MapTypeId.TERRAIN
        };
        $scope.setClass = function (index) {
            return "classDate"+index;
        };
        $scope.hideTicketShowLogin = function () {
            $("#buyTicket").modal("hide");
            $("#registrationModal").modal("show");
        };
        $scope.eventData = {};
        eventService.getEvent($routeParams.eventLink)
            .then(function (data, status) {
                $scope.eventDataDetails = angular.copy(data.data);
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
               $scope.eventDataPhoto = event;
            });
        };
        $scope.setCapacityBlit = function (inputId) {
            $scope.itemWithCapacity = $scope.eventFlatDates.filter(function (item) {
                return item.blitTypeId === inputId;
            });
            console.log($scope.itemWithCapacity);
        }

        $scope.eventType = "theatre";
        $scope.nextStep1 = function (eventInfo) {
            if($scope.itemWithCapacity[0].isFree) {
                $scope.totalNumber = eventInfo.ticketNumber;
            } else {
                $scope.totalPrice = eventInfo.ticketNumber * $scope.itemWithCapacity.price;
                $scope.totalNumber = eventInfo.ticketNumber;
            }
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
        $scope.nextStep2Free = function () {
            var userData = userInfo.getData();
                var buyFreeTicket = {
                    blitTypeId : $scope.itemWithCapacity[0].blitTypeId,
                    blitTypeName : $scope.itemWithCapacity[0].name,
                    count : $scope.totalNumber,
                    customerEmail : userData.email,
                    // customerMobileNumber : userData.mobile,
                    // customerName : userData.firstname+ " " + userData.lastname,
                    customerMobileNumber : "09122011273",
                    customerName : "سروش",
                    eventAddress : $scope.eventDataDetails.address,
                    eventDate : $scope.itemWithCapacity[0].date,
                    eventDateAndTime : dateSetterService.persianToArray(persianDate($scope.itemWithCapacity[0].date).pDate).join(),
                    eventName : $scope.eventDataDetails.eventName,
                    seatType : "COMMON",
                    bankGateway : "NONE"
                };
            document.getElementsByClassName("freeBlitSpinner")[0].style.display = "inline";
            document.getElementById("buyBlitError").style.display = "none";

                ticketsService.buyTicket(buyFreeTicket)
                    .then(function (data) {
                        document.getElementsByClassName("freeBlitSpinner")[0].style.display = "none";
                        console.log(data);
                        angular.element(document.getElementsByClassName('progress-bar')).css('width', '100%');
                        angular.element(document.getElementById('ticketPay2')).removeClass('active');
                        angular.element(document.getElementById('payment')).removeClass('active');
                        angular.element(document.getElementById('ticketPayFree')).addClass('active').addClass('in');
                        angular.element(document.getElementById('paymentComplete')).addClass('active');
                    })
                    .catch(function (data) {
                        console.log(data);
                        document.getElementsByClassName("freeBlitSpinner")[0].style.display = "none";
                        document.getElementById("buyBlitError").innerHTML= data.data.message;
                        document.getElementById("buyBlitError").style.display = "inline";
                    })

        };
        $scope.showBuyTicketModal = function () {

            $("#buyTicket").modal("show");
        }
        $scope.hideTicketPaymentModal = function () {
            $("#buyTicket").modal("hide");
        }


    });
