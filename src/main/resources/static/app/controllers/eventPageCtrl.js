/**
 * Created by soroush on 4/25/17.
 */
angular.module('eventsPageModule')
    .controller('eventPageCtrl', [
        '$rootScope',
        '$scope',
        '$routeParams',
        'eventService',
        'mapMarkerService',
        'photoService',
        '$q',
        'dateSetterService',
        '$timeout',
        'userInfo',
        'ticketsService',
        '$window',
        'dataService',
        'plannerService',
        '$filter',
        'seatmapService',
        '$location',

        function ($rootScope,
                                           $scope,
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
                                           plannerService,
                                           $filter,
                                           seatmapService,
                                           $location) {
        var promises = [];
        $scope.persianSans = [];
        $scope.eventInfo = {};
        $scope.capacityArray = [1,2,3,4,5];
        $scope.discountInput = { code : null };
        $scope.discountIsValid = false;
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
                $rootScope.pageDescription = $scope.eventDataDetails.description;
                $rootScope.keyWord =  $scope.eventDataDetails.eventName + ","
                    +dataService.eventTypePersian($scope.eventDataDetails.eventType) + ","
                    + " بلیت " + dataService.eventTypePersian($scope.eventDataDetails.eventType) + ","
                    +  dataService.eventTypePersian($scope.eventDataDetails.eventType) + " این هفته " +","
                    + "سرگرمی تهران" +"رویداد بیلیتو" + "رویداد بلیتو";
                $rootScope.robotValue = 'index';
                $rootScope.title = $location.path().replace('/event-page/','').replace( /\d+/,'').replace( /-/,'');

                $scope.additionalFields = $scope.eventDataDetails.additionalFields;
                if($scope.additionalFields){
                    $scope.additionalFields.forEach(function (field) {
                        field.value = "";
                    });
                }
                $scope.getPlannerData($scope.eventDataDetails.eventHostId);
                $scope.eventType = $scope.eventDataDetails.eventType;
                $scope.buyTicketFormatData(data.data.eventDates);
                mapMarkerService.initMapOnlyShowMarker(document.getElementById('map'));
                mapMarkerService.setMarker($scope.eventDataDetails.latitude, $scope.eventDataDetails.longitude);
                $scope.flatEventDates($scope.eventDataDetails.eventDates);
                $scope.sansListData($scope.eventDataDetails.eventDates);
                $scope.getImages(data.data);
                document.getElementById('members').insertAdjacentHTML('afterbegin',$scope.eventDataDetails.members);
                document.getElementById('showStartTime').innerHTML = persianDate($scope.eventDataDetails.blitSaleStartDate).format("dddd,DD MMMM, ساعت HH:mm");
                if($scope.eventDataDetails.aparatDisplayCode) {
                    document.getElementById('menu1').insertAdjacentHTML('afterbegin',$scope.eventDataDetails.aparatDisplayCode);
                }
            })

        $scope.eventFlatDates = [];
        $scope.sansListData = function (dates) {
            $scope.eventDates = dates.sort(function (a, b) {
                return a.date - b.date;
            });
            $scope.eventDates.forEach(function (eventDate) {
                eventDate.capacity = 0;
                eventDate.soldCount = 0;
                eventDate.maxPrice = 0;
                eventDate.minPrice = 100000000;
                eventDate.blitTypes.forEach(function (blitType) {
                    if(blitType.name !== 'HOST_RESERVED_SEATS') {
                        if (blitType.price < eventDate.minPrice) {
                            eventDate.minPrice = blitType.price;
                        }
                        if (blitType.price > eventDate.maxPrice) {
                            eventDate.maxPrice = blitType.price;
                        }
                        eventDate.capacity += blitType.capacity;
                        eventDate.soldCount += blitType.soldCount;
                    }
                })
            });
            $timeout(function () {
                for(var sansIndex in $scope.eventDates) {
                    dateSetterService.initDate("classDate"+sansIndex, 0);
                    $scope.eventDates[sansIndex].persianDate = persianDate($scope.eventDates[sansIndex].date).format("dddd,DD MMMM, ساعت HH:mm");
                    $(".classDate"+sansIndex).text(persianDate($scope.eventDates[sansIndex].date).format("dddd,DD MMMM, ساعت HH:mm"));
                }
            }, 300);
        };
        $scope.filterImages = function (images, type) {
            return images.filter(function (item) {
                return item.type === type ;
            })
        };
        $scope.flatEventDates = function (dates) {
            var blitTypesIndex = 0;
            for(var sansIndex in dates) {
                for(var blitIndex in dates[sansIndex].blitTypes) {
                    $scope.eventFlatDates[blitTypesIndex] = {
                        name : dates[sansIndex].blitTypes[blitIndex].name,
                        capacity : dates[sansIndex].blitTypes[blitIndex].capacity,
                        blitTypeState : dates[sansIndex].blitTypes[blitIndex].blitTypeState,
                        price : dates[sansIndex].blitTypes[blitIndex].price,
                        date : dates[sansIndex].date,
                        soldCount : dates[sansIndex].blitTypes[blitIndex].soldCount,
                        isFree : dates[sansIndex].blitTypes[blitIndex].isFree,
                        blitTypeId : dates[sansIndex].blitTypes[blitIndex].blitTypeId,
                        seatUids : dates[sansIndex].blitTypes[blitIndex].seatUids,
                        hasSeat : dates[sansIndex].blitTypes[blitIndex].hasSeat

                    } ;
                    blitTypesIndex++;
                }
            }
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
        $scope.bothTypesOfBlits = false;
        $scope.showSeatSection = false;
        $scope.showWithoutSeatSection = false;
        $scope.blitTypeCreateValidation = 0;
        $scope.seatType = { isChosen : null};
        $scope.setCapacityBlit = function (sansId) {
            $scope.seatsPickedLimit = false;
            $scope.seatType.isChosen = null;
            $scope.showSeatSection = false;
            $scope.showWithoutSeatSection = false;
            $scope.seatsPickedChecked = false;
            $scope.bothTypesOfBlits  = false;
            $scope.blitTypeCreateValidation = 0;
            $scope.itemWithCapacity = false;
            $scope.$broadcast('blitTypeUidsReset', []);
            $scope.eventDatePicked = $scope.eventDataDetails.eventDates.filter(function (item) {
                return item.eventDateId === sansId;
            });
            $scope.blitTypesWithOutSeatsEdit = seatmapService.generateWithoutSeatBlitTypes($scope.eventDatePicked[0].blitTypes);
            $scope.blitTypesWithOutSeatsEdit.map(function (blitTypeWithoutSeat) {
                if(blitTypeWithoutSeat.isFree){
                    blitTypeWithoutSeat.nameWithPrice = '(رایگان)' + blitTypeWithoutSeat.name ;
                } else {
                    var newPrice = $filter('number')(blitTypeWithoutSeat.price);
                    newPrice = $filter('pNumber')(newPrice);
                    blitTypeWithoutSeat.nameWithPrice =   '('+newPrice+' تومان'+')' + blitTypeWithoutSeat.name;
                }
                return blitTypeWithoutSeat;
            });
            if(((seatmapService.generateWithoutSeatBlitTypes($scope.eventDatePicked[0].blitTypes)).length !== 0) && !$scope.eventDataDetails.salonUid) {
                $scope.showSeatSection = false;
                $scope.showWithoutSeatSection = true;
                $scope.bothTypesOfBlits  = false;

            }
            if(((seatmapService.generateWithoutSeatBlitTypes($scope.eventDatePicked[0].blitTypes)).length !== 0) && $scope.eventDataDetails.salonUid) {
                $scope.showSeatSection = false;
                $scope.showWithoutSeatSection = false;
                $scope.bothTypesOfBlits  = true;
            }
            if(!$scope.bothTypesOfBlits && !$scope.showWithoutSeatSection) {
                $scope.showSeatSection = true;
                $("#buyTicketModal").addClass('modalExpandWidth');
                $(".progress").addClass('dispNone');
                generateSalonSeatMap();
            }
        };
        $scope.seatTypePicked = function (seatType) {
            $scope.seatsPickedLimit = false;
            if(seatType) {
                $scope.showSeatSection = true;
                $scope.showWithoutSeatSection = false;
                $("#buyTicketModal").addClass('modalExpandWidth');
                $(".progress").addClass('dispNone');
                generateSalonSeatMap();
            } else {
                $(".progress").removeClass('dispNone');
                $("#buyTicketModal").removeClass('modalExpandWidth');
                $scope.showWithoutSeatSection = true;
                $scope.seatsPickedChecked = false;
                $scope.showSeatSection = false;
            }
        };
        var populatedSchema = {};
        var generateSalonSeatMap = function () {
            document.getElementsByClassName("seatMapLoading")[0].style.display = "block";
            seatmapService.getPublicPopulatedSchema($scope.eventDatePicked[0].eventDateId)
                .then(function (data) {
                    document.getElementsByClassName("seatMapLoading")[0].style.display = "none";
                    populatedSchema = angular.copy(data.data);
                    $scope.$broadcast('newSVGBuyTicket', [populatedSchema, 4]);
                })
                .catch(function (data) {
                    document.getElementsByClassName("seatMapLoading")[0].style.display = "none";
                })
        };
        $scope.seatsPickedChecked = true;
        $scope.seatsPickedLimit = false;
        $scope.$on("blitIdsChangedBuyTicket",function (event ,data) {
            if(data[0].length <= 10) {
                $scope.seatsPickedLimit = false;
                $scope.blitTypeCreateValidation = data[0].length;
                $scope.seatBlitUids = data[0];
                $scope.$apply();
            } else {
                $scope.seatsPickedLimit = true;
                $scope.$apply();
            }
        });
        $scope.blitTypePicked = function (blitId) {
            $scope.itemWithCapacity = $scope.blitTypesWithOutSeatsEdit.filter(function (item) {
                return item.blitTypeId === blitId;
            });
            $scope.eventInfo.ticketNumber = null;
        };
        $scope.nextStep1 = function (eventInfo) {
            document.getElementById("buyBlitError").style.display = "none";
            if($scope.itemWithCapacity[0].isFree) {
                $scope.totalNumber = eventInfo.ticketNumber;
            } else {
                $scope.totalPrice = eventInfo.ticketNumber * $scope.itemWithCapacity[0].price;
                $scope.primaryTotalPrice = $scope.totalPrice;
                $scope.totalNumber = eventInfo.ticketNumber;
            }
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '50%');
            angular.element(document.getElementById('ticketPay1')).removeClass('active');
            angular.element(document.getElementById('selectTicket')).removeClass('active');
            angular.element(document.getElementById('ticketPay2')).addClass('active').addClass('in');
            angular.element(document.getElementById('payment')).addClass('active');
        };
        $scope.selectedBlitsTotalPrice = 0;
        $scope.nextStep1WithSeat = function () {
            $scope.itemWithCapacity = null;
            $scope.selectedBlitsTotalPrice = seatmapService.getBoughtBlitTypes($scope.seatBlitUids, populatedSchema);
            $scope.selectedBlitsTotalPricePrimary = seatmapService.getBoughtBlitTypes($scope.seatBlitUids, populatedSchema);
            angular.element(document.getElementById('ticketPay1')).removeClass('active');
            angular.element(document.getElementById('selectTicket')).removeClass('active');
            angular.element(document.getElementById('ticketPay2')).addClass('active').addClass('in');
            angular.element(document.getElementById('payment')).addClass('active');
            $(".progress").removeClass('dispNone');
            $("#buyTicketModal").removeClass('modalExpandWidth');
        };
        $scope.prevStep1 = function () {
            $scope.discountIsValid = false;
            $scope.discountTotalAmount = '';
            $scope.discountInput.code = null;
            buyPaymentTicket = {};
            document.getElementById("discountSuccess").style.display = "none";
            document.getElementById("discountError").style.display = "none";
            angular.element(document.getElementsByClassName("btnPaymentActive")).removeClass("btnPaymentActivated");
            $scope.disableFreeButton = false;
            $scope.paymentSelectedDone = '';
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '0');
            angular.element(document.getElementById('ticketPay1')).addClass('active').addClass('in');
            angular.element(document.getElementById('selectTicket')).addClass('active');
            angular.element(document.getElementById('ticketPay2')).removeClass('active');
            angular.element(document.getElementById('payment')).removeClass('active');
            if($scope.showSeatSection) {
                $("#buyTicketModal").addClass('modalExpandWidth');
                $(".progress").addClass('dispNone');
            }
        };
        var buyPaymentTicket = {};
        $scope.buyerInfo = {};
        $scope.paymentSelected = function (payment) {
            var buyerData = userInfo.getData();
            $scope.paymentSelectedDone = "selected";
            $scope.setPaymentData(payment, buyerData);
        };
        $scope.paymentSelectedWithSeat = function (payment) {
            var buyerData;
            if(userInfo.getData().lastname === '') {
                buyerData = $scope.buyerInfo;
            } else {
                buyerData = userInfo.getData();
            }
            $scope.paymentSelectedDone = "selected";
            $scope.setPaymentDataWithSeat(payment, buyerData);
        };
        $scope.paymentSelectedNotUser = function (payment) {
            $scope.paymentSelectedDone = "selected";
            var buyerData = $scope.buyerInfo;
            $scope.setPaymentData(payment, buyerData);
        };
        $scope.setPaymentData = function (payment, buyerData) {
            angular.element(document.getElementsByClassName("btnPaymentActivated")).removeClass("btnPaymentActivated");
            angular.element(document.getElementsByClassName(payment)).addClass("btnPaymentActivated");
            buyPaymentTicket = {
                blitTypeId: $scope.itemWithCapacity[0].blitTypeId,
                blitTypeName: $scope.itemWithCapacity[0].name,
                count: $scope.totalNumber,
                customerEmail: buyerData.email,
                customerMobileNumber: dataService.persianToEnglishDigit(persianJs(buyerData.mobile).englishNumber().toString()),
                customerName: buyerData.firstname + " " + buyerData.lastname,
                eventAddress: $scope.eventDataDetails.address,
                eventDate: $scope.eventDatePicked[0].date,
                eventDateAndTime: $scope.eventDatePicked[0].persianDate,
                eventName: $scope.eventDataDetails.eventName,
                seatType: "COMMON",
                totalAmount: $scope.totalPrice,
                additionalFields : $scope.additionalFields,
                primaryAmount: $scope.primaryTotalPrice,
                bankGateway: payment
            };
            if($scope.discountIsValid) {
                buyPaymentTicket.discountCode = $scope.discountCodeName;
            }
        };
        $scope.setPaymentDataWithSeat = function (payment, buyerData) {
            angular.element(document.getElementsByClassName("btnPaymentActivated")).removeClass("btnPaymentActivated");
            angular.element(document.getElementsByClassName(payment)).addClass("btnPaymentActivated");
            buyPaymentTicket = {
                count: $scope.seatBlitUids.length,
                customerEmail: buyerData.email,
                customerMobileNumber: dataService.persianToEnglishDigit(persianJs(buyerData.mobile).englishNumber().toString()),
                customerName: buyerData.firstname + " " + buyerData.lastname,
                eventAddress: $scope.eventDataDetails.address,
                eventDateId : $scope.eventDatePicked[0].eventDateId,
                eventDate: $scope.eventDatePicked[0].date,
                eventDateAndTime: $scope.eventDatePicked[0].persianDate,
                eventName: $scope.eventDataDetails.eventName,
                seatType: "COMMON",
                totalAmount: $scope.selectedBlitsTotalPrice,
                additionalFields : $scope.additionalFields,
                primaryAmount: $scope.selectedBlitsTotalPricePrimary,
                seatUids : $scope.seatBlitUids,
                bankGateway: payment
            };
            if($scope.discountIsValid) {
                buyPaymentTicket.discountCode = $scope.discountCodeName;
            }
        };
        $scope.buyTicketOnce = false;
        $scope.nextStep2 = function () {
            $scope.buyTicketOnce = true;
            document.getElementsByClassName("payedBlitSpinner")[0].style.display = "inline";
            document.getElementById("buyBlitError").style.display = "none";
            ticketsService.buyTicket(buyPaymentTicket)
                .then(function (data) {
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    $scope.gateWayDetails = data.data;
                    if($scope.gateWayDetails.gateway === 'ZARINPAL') {
                        $window.location.href = $scope.gateWayDetails.zarinpalWebGatewayURL;
                    }
                    if($scope.gateWayDetails.gateway === 'PAYDOTIR') {
                        $window.location.href = $scope.gateWayDetails.payDotIrWebGatewayURL;
                    }
                    if($scope.gateWayDetails.gateway === 'JIBIT') {
                        $window.location.href = $scope.gateWayDetails.jibitWebGatewayURL;
                    }
                })
                .catch(function (data) {
                    $scope.buyTicketOnce = false;
                    $scope.paymentSelectedDone = '';
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    document.getElementById("buyBlitError").innerHTML= data.data.message;
                    document.getElementById("buyBlitError").style.display = "inline";
                })
        };
        $scope.nextStep2WithSeat = function () {
            $scope.buyTicketOnce = true;
            document.getElementsByClassName("payedBlitSpinner")[0].style.display = "inline";
            document.getElementById("buyBlitError").style.display = "none";
            ticketsService.buyTicketWithSeat(buyPaymentTicket)
                .then(function (data) {
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    $scope.gateWayDetails = data.data;
                    if($scope.gateWayDetails.gateway === 'ZARINPAL') {
                        $window.location.href = $scope.gateWayDetails.zarinpalWebGatewayURL;
                    }
                    if($scope.gateWayDetails.gateway === 'PAYDOTIR') {
                        $window.location.href = $scope.gateWayDetails.payDotIrWebGatewayURL;
                    }
                    if($scope.gateWayDetails.gateway === 'JIBIT') {
                        $window.location.href = $scope.gateWayDetails.jibitWebGatewayURL;
                    }
                })
                .catch(function (data) {
                    $scope.buyTicketOnce = false;
                    $scope.paymentSelectedDone = '';
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    document.getElementById("buyBlitError").innerHTML= data.data.message;
                    document.getElementById("buyBlitError").style.display = "inline";
                })
        };
        $scope.nextStep2WithSeatNotUser = function () {
            $scope.buyTicketOnce = true;
            document.getElementsByClassName("payedBlitSpinner")[0].style.display = "inline";
            document.getElementById("buyBlitError").style.display = "none";
            ticketsService.buyTicketWithSeatNotUser(buyPaymentTicket)
                .then(function (data) {
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    $scope.gateWayDetails = data.data;
                    if($scope.gateWayDetails.gateway === 'ZARINPAL') {
                        $window.location.href = $scope.gateWayDetails.zarinpalWebGatewayURL;
                    }
                    if($scope.gateWayDetails.gateway === 'PAYDOTIR') {
                        $window.location.href = $scope.gateWayDetails.payDotIrWebGatewayURL;
                    }
                    if($scope.gateWayDetails.gateway === 'JIBIT') {
                        $window.location.href = $scope.gateWayDetails.jibitWebGatewayURL;
                    }
                })
                .catch(function (data) {
                    $scope.buyTicketOnce = false;
                    $scope.paymentSelectedDone = '';
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    document.getElementById("buyBlitError").innerHTML= data.data.message;
                    document.getElementById("buyBlitError").style.display = "inline";
                })
        };
        $scope.buyTicketNotUserOnce = false;
        $scope.nextStep2NotUser = function () {
            $scope.buyTicketNotUserOnce = true;
            document.getElementsByClassName("payedBlitSpinner")[0].style.display = "inline";
            document.getElementById("buyBlitError").style.display = "none";
            ticketsService.buyTicketNotUser(buyPaymentTicket)
                .then(function (data) {
                    document.getElementsByClassName("payedBlitSpinner")[0].style.display = "none";
                    $scope.gateWayDetails = data.data;
                    if($scope.gateWayDetails.gateway === 'ZARINPAL') {
                        $window.location.href = $scope.gateWayDetails.zarinpalWebGatewayURL;
                    }
                    if($scope.gateWayDetails.gateway === 'PAYDOTIR') {
                        $window.location.href = $scope.gateWayDetails.payDotIrWebGatewayURL;
                    }
                    if($scope.gateWayDetails.gateway === 'JIBIT') {
                        $window.location.href = $scope.gateWayDetails.jibitWebGatewayURL;
                    }
                })
                .catch(function (data) {
                    $scope.buyTicketNotUserOnce = false;
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
            var buyFreeTicket = {
                blitTypeId : $scope.itemWithCapacity[0].blitTypeId,
                blitTypeName : $scope.itemWithCapacity[0].name,
                count : $scope.totalNumber,
                customerEmail : userData.email,
                customerMobileNumber : userData.mobile,
                customerName : userData.firstname+ " " + userData.lastname,
                eventAddress : $scope.eventDataDetails.address,
                eventDate : $scope.eventDatePicked[0].date,
                eventDateAndTime : $scope.eventDatePicked[0].persianDate,
                eventName : $scope.eventDataDetails.eventName,
                additionalFields : $scope.additionalFields,
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

        $scope.validateDiscountInput = function (discountCode) {
            var discountData = {};
            $scope.discountCodeName = discountCode;
            document.getElementsByClassName("discountSpinner")[0].style.display = "inline";
            document.getElementById("discountSuccess").style.display = "none";
            document.getElementById("discountError").style.display = "none";
            discountData.blitTypeId = $scope.itemWithCapacity[0].blitTypeId;
            discountData.totalAmount = $scope.totalPrice;
            discountData.count = $scope.totalNumber;
            discountData.code = discountCode;
            eventService.validateDiscount(discountData)
                .then(function (data) {
                    document.getElementsByClassName("discountSpinner")[0].style.display = "none";
                    if(data.data.isValid) {
                        $scope.discountIsValid = true;
                        document.getElementById("discountSuccess").style.display = "block";
                        $scope.discountTotalAmount = data.data.totalAmount;
                        $scope.totalPrice = data.data.totalAmount;
                        buyPaymentTicket.totalAmount = data.data.totalAmount;
                        buyPaymentTicket.discountCode = $scope.discountCodeName;
                    } else {
                        document.getElementById("discountError").style.display = "block";

                    }
                })
                .catch(function (data) {
                    document.getElementsByClassName("discountSpinner")[0].style.display = "none";
                    document.getElementById("discountError").innerHTML= data.data.message;
                    document.getElementById("discountError").style.display = "block";
                })

        };
        $scope.validateDiscountInputWithSeat = function (discountCode) {
            var discountData = {};
            $scope.discountCodeName = discountCode;
            document.getElementsByClassName("discountSpinner")[0].style.display = "inline";
            document.getElementById("discountSuccess").style.display = "none";
            document.getElementById("discountError").style.display = "none";
            discountData.seatUids = $scope.seatBlitUids;
            discountData.totalAmount = $scope.selectedBlitsTotalPrice;
            discountData.eventDateId = $scope.eventDatePicked[0].eventDateId;
            discountData.code = discountCode;
            eventService.validateDiscountWithSeat(discountData)
                .then(function (data) {
                    document.getElementsByClassName("discountSpinner")[0].style.display = "none";
                    if(data.data.isValid) {
                        $scope.discountIsValid = true;
                        document.getElementById("discountSuccess").style.display = "block";
                        $scope.discountTotalAmount = data.data.totalAmount;
                        $scope.totalPrice = data.data.totalAmount;
                        $scope.selectedBlitsTotalPrice = data.data.totalAmount;
                        buyPaymentTicket.totalAmount = data.data.totalAmount;
                        buyPaymentTicket.discountCode = $scope.discountCodeName;
                    } else {
                        document.getElementById("discountError").style.display = "block";

                    }
                })
                .catch(function (data) {
                    document.getElementsByClassName("discountSpinner")[0].style.display = "none";
                    document.getElementById("discountError").innerHTML= data.data.message;
                    document.getElementById("discountError").style.display = "block";
                })
        };
        $scope.getPlannerData = function (plannerId) {
            plannerService.getPlannerById(plannerId)
                .then(function (data) {
                    var plannerPhotos = data.data.images;
                    $scope.plannerLink = '/event-host-page/'+data.data.eventHostLink;
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
                })
                .catch(function (data) {
                })
        };
        $scope.buyTicketFormatData = function (eventNestedData) {
            $scope.buyTicketPickData = eventNestedData.sort(function (eventDateOne, eventDateSecond) {
                    return eventDateOne.date - eventDateSecond.date;
                }).map(function (eventDate) {
                    eventDate.date = (eventDate.dateTime !== null) ? eventDate.dateTime : (persianDate(eventDate.date).format("dddd,DD MMMM, ساعت HH:mm"));
                    return eventDate;
                });
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
        };
        $scope.showEventPic = function () {
            $("#event-photo").modal("show");
        };
        function sticky_relocate() {
            var window_top = $(window).scrollTop();
            var div_top = $('#sticky-pick').offset().top;
            if($( window ).width() > 768) {
                if (window_top > div_top) {
                    $('#sticky-anchor').addClass('buyTicketAfterScroll');
                } else {
                    $('#sticky-anchor').removeClass('buyTicketAfterScroll');
                }
            } else {
                $('#sticky-anchor').removeClass('buyTicketAfterScroll');
                if (window_top > div_top) {
                    $('#buyTicketOnScroll').fadeIn();
                } else {
                    $('#buyTicketOnScroll').fadeOut();
                }
            }
        }

        $(function() {
            $(window).scroll(sticky_relocate);
            sticky_relocate();
        });
    }]);
