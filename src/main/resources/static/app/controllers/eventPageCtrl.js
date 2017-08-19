/**
 * Created by soroush on 4/25/17.
 */
angular.module('eventsPageModule')
    .controller('eventPageCtrl', function ($scope,
                                           $routeParams,
                                           eventService,
                                           mapMarkerService,
                                           photoService,
                                           $q,
                                           dateSetterService,
                                           $timeout,
                                           userInfo,
                                           ticketsService,
                                           $window,
                                           dataService,
                                           plannerService) {
        var promises = [];
        $scope.persianSans = [];
        $scope.eventInfo = {};
        $scope.capacityArray = [1,2,3,4,5];
        $scope.setClass = function (index) {
            return "classDate"+index;
        };
        $scope.hideTicketShowLogin = function () {
            $("#buyTicket").modal("hide");
            $("#registrationModal").modal("show");
        };
        $scope.eventData = {};

        eventService.getEvent($routeParams.eventLink)
            .then(function (data) {
                $scope.eventDataDetails = angular.copy(data.data);
                $scope.getPlannerData($scope.eventDataDetails.eventHostId);
                $scope.eventType = $scope.eventDataDetails.eventType;
                $scope.buyTicketFormatData(data.data.eventDates);
                mapMarkerService.initMapOnlyShowMarker(document.getElementById('map'));
                mapMarkerService.setMarker($scope.eventDataDetails.latitude, $scope.eventDataDetails.longitude);
                $scope.flatEventDates($scope.eventDataDetails.eventDates);
                $scope.getImages(data.data);
                console.log($scope.eventDataDetails);
                document.getElementById('members').insertAdjacentHTML('afterbegin',$scope.eventDataDetails.members);
                document.getElementById('showStartTime').innerHTML = persianDate($scope.eventDataDetails.blitSaleStartDate).format("dddd,DD MMMM, ساعت HH:mm");
                if($scope.eventDataDetails.aparatDisplayCode) {
                    document.getElementById('menu1').insertAdjacentHTML('afterbegin',$scope.eventDataDetails.aparatDisplayCode);
                }
            })
            .catch(function (data) {
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
                    dateSetterService.initDate("classDate"+i);
                    $scope.eventFlatDates[i].persianDate = persianDate($scope.eventFlatDates[i].date).format("dddd,DD MMMM, ساعت HH:mm");
                    $(".classDate"+i).text(persianDate($scope.eventFlatDates[i].date).format("dddd,DD MMMM, ساعت HH:mm"));
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
                promises.push(
                    photoService.download(imageItem.imageUUID)
                        .then(function (data) {
                            imageItem.imageUUID = data.data.encodedBase64;
                            return imageItem;
                        })
                        .catch(function (data) {
                        })
                )
            });
            $q.all(promises).then(function () {
                event.eventPhoto = $scope.filterImages(event.images,"EVENT_PHOTO");
                event.gallery = $scope.filterImages(event.images,"GALLERY");
                $scope.eventDataPhoto = event;
            });
        };
        $scope.setCapacityBlit = function (sansId) {
            $scope.eventDatePicked = $scope.eventDataDetails.eventDates.filter(function (item) {
                return item.eventDateId === sansId;
            });
        };
        $scope.blitTypePicked = function (blitId) {
            $scope.itemWithCapacity = $scope.eventFlatDates.filter(function (item) {
                return item.blitTypeId === blitId;
            });
        };
        $scope.nextStep1 = function (eventInfo) {
            document.getElementById("buyBlitError").style.display = "none";
            if($scope.itemWithCapacity[0].isFree) {
                $scope.totalNumber = eventInfo.ticketNumber;
            } else {
                $scope.totalPrice = eventInfo.ticketNumber * $scope.itemWithCapacity[0].price;
                $scope.totalNumber = eventInfo.ticketNumber;
            }
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '50%');
            angular.element(document.getElementById('ticketPay1')).removeClass('active');
            angular.element(document.getElementById('selectTicket')).removeClass('active');
            angular.element(document.getElementById('ticketPay2')).addClass('active').addClass('in');
            angular.element(document.getElementById('payment')).addClass('active');
        };
        $scope.prevStep1 = function () {
            $scope.disableFreeButton = false;
            $scope.paymentSelectedDone = '';
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '0');
            angular.element(document.getElementById('ticketPay1')).addClass('active').addClass('in');
            angular.element(document.getElementById('selectTicket')).addClass('active');
            angular.element(document.getElementById('ticketPay2')).removeClass('active');
            angular.element(document.getElementById('payment')).removeClass('active');
        };
        var buyPaymentTicket = {};
        $scope.paymentSelected = function (payment) {
            var buyerData = userInfo.getData();
            $scope.paymentSelectedDone = "selected";
            $scope.setPaymentData(payment, buyerData);
        };
        $scope.buyerInfo = {};
        $scope.paymentSelectedNotUser = function (payment) {
            $scope.paymentSelectedDone = "selected";
            var buyerData = $scope.buyerInfo;
            $scope.setPaymentData(payment, buyerData)
        };
        $scope.setPaymentData = function (payment, buyerData) {
            var eventPersianDate = $scope.eventFlatDates.filter(function (ticket) { return ticket.name === $scope.itemWithCapacity[0].name});
            buyPaymentTicket = {
                blitTypeId : $scope.itemWithCapacity[0].blitTypeId,
                blitTypeName : $scope.itemWithCapacity[0].name,
                count : $scope.totalNumber,
                customerEmail : buyerData.email,
                customerMobileNumber : dataService.persianToEnglishDigit(persianJs(buyerData.mobile).englishNumber().toString()),
                customerName : buyerData.firstname+ " " + buyerData.lastname,
                eventAddress : $scope.eventDataDetails.address,
                eventDate : $scope.itemWithCapacity[0].date,
                eventDateAndTime : eventPersianDate[0].persianDate,
                eventName : $scope.eventDataDetails.eventName,
                seatType : "COMMON",
                totalAmount : $scope.totalPrice ,
                bankGateway : payment
            };
        };
        $scope.nextStep2 = function () {
            document.getElementsByClassName("payedBlitSpinner")[0].style.display = "inline";
            document.getElementById("buyBlitError").style.display = "none";
            ticketsService.buyTicket(buyPaymentTicket)
                .then(function (data) {
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    $scope.gateWayDetails = data.data;
                    if($scope.gateWayDetails.gateway === 'ZARINPAL') {
                        $window.location.href = $scope.gateWayDetails.zarinpalWebGatewayURL;
                    }
                })
                .catch(function (data) {
                    $scope.paymentSelectedDone = '';
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    document.getElementById("buyBlitError").innerHTML= data.data.message;
                    document.getElementById("buyBlitError").style.display = "inline";
                })
        };
        $scope.nextStep2NotUser = function () {
            document.getElementsByClassName("payedBlitSpinner")[0].style.display = "inline";
            document.getElementById("buyBlitError").style.display = "none";
            ticketsService.buyTicketNotUser(buyPaymentTicket)
                .then(function (data) {
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    $scope.gateWayDetails = data.data;
                    if($scope.gateWayDetails.gateway === 'ZARINPAL') {
                        $window.location.href = $scope.gateWayDetails.zarinpalWebGatewayURL;
                    }
                })
                .catch(function (data) {
                    $scope.paymentSelectedDone = '';
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    document.getElementById("buyBlitError").innerHTML= data.data.message;
                    document.getElementById("buyBlitError").style.display = "inline";
                })
        };
        $scope.disableFreeButton = false;
        $scope.nextStep2Free = function () {
            var userData = userInfo.getData();
            $scope.disableFreeButton = true;
            var eventPersianDate = $scope.eventFlatDates.filter(function (ticket) { return ticket.name === $scope.itemWithCapacity[0].name});
            var buyFreeTicket = {
                blitTypeId : $scope.itemWithCapacity[0].blitTypeId,
                blitTypeName : $scope.itemWithCapacity[0].name,
                count : $scope.totalNumber,
                customerEmail : userData.email,
                customerMobileNumber : userData.mobile,
                customerName : userData.firstname+ " " + userData.lastname,
                eventAddress : $scope.eventDataDetails.address,
                eventDate : $scope.itemWithCapacity[0].date,
                eventDateAndTime : eventPersianDate[0].persianDate,
                eventName : $scope.eventDataDetails.eventName,
                seatType : "COMMON",
                bankGateway : "NONE"
            };
            document.getElementsByClassName("freeBlitSpinner")[0].style.display = "inline";
            document.getElementById("buyBlitError").style.display = "none";
            ticketsService.buyTicket(buyFreeTicket)
                .then(function (data) {
                    document.getElementsByClassName("freeBlitSpinner")[0].style.display = "none";
                    $scope.ticketTrackCode = '/payment/'+data.data.trackCode;
                    angular.element(document.getElementsByClassName('progress-bar')).css('width', '100%');
                    angular.element(document.getElementById('ticketPay2')).removeClass('active');
                    angular.element(document.getElementById('payment')).removeClass('active');
                    angular.element(document.getElementById('ticketPayFree')).addClass('active').addClass('in');
                    angular.element(document.getElementById('paymentComplete')).addClass('active');
                })
                .catch(function (data) {
                    document.getElementsByClassName("freeBlitSpinner")[0].style.display = "none";
                    document.getElementById("buyBlitError").innerHTML= data.data.message;
                    document.getElementById("buyBlitError").style.display = "inline";
                })

        };
        $scope.getPlannerData = function (plannerId) {
            plannerService.getPlannerById(plannerId)
                .then(function (data) {
                    var plannerPhotos = data.data.images;
                    $scope.plannerLink = '/bio/'+data.data.eventHostLink;
                    plannerPhotos.forEach(function (image) {
                        if(image.type === "HOST_PHOTO") {
                            photoService.download(image.imageUUID)
                                .then(function (data) {
                                    $scope.plannerPhoto = data.data.encodedBase64;
                                })
                                .catch(function () {

                                })
                        }
                    });
                    console.log(data);
                })
                .catch(function (data) {
                    console.log(data);
                })
        };
        $scope.buyTicketFormatData = function (eventNestedData) {
            $scope.buyTicketPickData = eventNestedData.map(function (eventDate) {
                eventDate.date = persianDate(eventDate.date).format("dddd,DD MMMM, ساعت HH:mm");
                return eventDate;
            })
        };
        $scope.getFreeTicket = function () {
            $window.open($scope.ticketTrackCode);
        };
        $scope.showBuyTicketModal = function () {
            $scope.disableFreeButton = false;
            $scope.paymentSelectedDone = '';
            $("#buyTicket").modal("show");
        };
        $scope.hideTicketPaymentModal = function () {
            $("#buyTicket").modal("hide");
        };

        $scope.getTicketNumbers = function(num) {
            var ratings = [];
            for (var i = 0; i < num; i++) {
                ratings.push(i)
            }
            return ratings;
        }
    });
