/**
 * Created by soroush on 5/2/17.
 */

angular.module('User')
    .controller('userProfileCtrl', function ($scope, userInfo, Auth, $timeout, mapMarkerService, updateInfo, $location, scrollAnimation, uploadPhotoService, eventService, plannerService, exchangeService, dataService) {
        var userProfile = this;

        $scope.userData = userInfo.getData();
        $scope.updateInfoSpinner = false;

        $scope.updateSuccessNotif = false;
        $scope.updateFailNotif = false;
        $scope.showTimeForms = [{blitTypes : [{}]}];

        $scope.showThumbnailProfile = false;
        $scope.showThumbnailEventPlanner = false;
        $scope.showThumbnailEventExchange = false;
        $scope.showThumbnailCover = false;
        $scope.uploadExchangePhoto = false;
        $scope.exchangePhotoSuccess = false;
        $scope.uploadEventPhoto = false;
        $scope.eventPhotoSuccess = false;
        $scope.createEventSpinner = false;
        $scope.createEventNotif = false;


        $scope.picData = null;

        userProfile.showTimeNumber = 0;

        userProfile.setDate = function (i) {
            $timeout(function () {
                $(".persianTime").pDatepicker({
                    timePicker: {
                        enabled: true
                    },
                    altField: '#persianDigitAlt',
                    altFormat: "YYYY MM DD HH:mm:ss",
                    altFieldFormatter: function (unixDate) {
                        $scope.showTimeForms[i].date = unixDate;
                    }
                });
            }, 1000);

        }
        $timeout(function () {
            $(".persianTimeEventStart").pDatepicker({
                timePicker: {
                    enabled: true
                },
                altField: '#persianDigitAlt',
                altFormat: "YYYY MM DD HH:mm:ss",
                altFieldFormatter: function (unixDate) {
                    $scope.eventStartTime = unixDate;
                }
            });
            $(".persianTimeEventEnd").pDatepicker({
                timePicker: {
                    enabled: true
                },
                altField: '#persianDigitAlt',
                altFormat: "YYYY MM DD HH:mm:ss",
                altFieldFormatter: function (unixDate) {
                    $scope.eventEndTime = unixDate;
                }
            });
            $(".persianExchangeTime").pDatepicker({
                timePicker: {
                    enabled: true
                },
                altField: '#persianDigitAlt',
                altFormat: "YYYY MM DD HH:mm:ss",
                altFieldFormatter: function (unixDate) {
                    $scope.exchangeTime = unixDate;
                }
            });
        }, 1000)

        userProfile.setDate(userProfile.showTimeNumber);




        $scope.updateInfo = function (editInfo) {
            $scope.updateInfoSpinner = true;
            delete editInfo["email"];
            updateInfo.updateData(editInfo)
                .then(function (status, data) {
                    $scope.updateInfoSpinner = false;
                    $scope.updateSuccessNotif = true;
                    $timeout(function () {
                        $scope.updateSuccessNotif = false;
                    }, 3000);
                })
                .catch(function (status, data) {
                    $scope.updateInfoSpinner = false;
                    $scope.updateFailNotif = true;
                    $timeout(function () {
                        $scope.updateFailNotif = false;
                    }, 3000);
                })
        }



        var fileSelectProfile = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectProfile.type = 'file';
        fileSelectProfile.setAttribute('id', 'profileUpload') ;

        var fileSelectCover = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectCover.type = 'file';

        var fileSelectEventPlanner = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventPlanner.type = 'file';

        var fileSelectEventExchange = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventExchange.type = 'file';

        $scope.uploadPicProfile = function() {
            fileSelectProfile.click();
        }
        $scope.deleteProfilePic = function () {
            fileSelectProfile.value = "" ;
            $scope.showThumbnailProfile = false;
        }
        $scope.deleteCoverPic = function () {
            fileSelectCover.value = "" ;
            $scope.showThumbnailCover = false;
        }
        $scope.uploadPicCover = function() {
            fileSelectCover.click();
        }
        $scope.uploadPicEventPlanner = function() {
            fileSelectEventPlanner.click();
        }
        $scope.deletePicEventPlanner = function () {
            fileSelectEventPlanner.value = "" ;
            $scope.showThumbnailEventPlanner = false;
        }
        $scope.uploadPicEventExchange = function() {
            console.log("A");
            fileSelectEventExchange.click();
        }
        $scope.photoUploadExchange = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("exchangePhotoUpload"))[0].src
            }
            $scope.uploadExchangePhoto = true;
            uploadPhotoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadExchangePhoto = false;
                    $scope.exchangePhotoSuccess = true;
                    $scope.exchangeImageId = data.data.imageUUID;
                    console.log(data);
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoUploadEvent = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("profilePhotoUpload"))[0].src
            }
            $scope.uploadEventPhoto = true;
            uploadPhotoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventPhoto = false;
                    $scope.eventPhotoSuccess = true;
                    $scope.eventImageId = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.uploadPlannerPhoto = false;
        $scope.plannerPhotoSuccess = false;

        $scope.photoUploadPlanner = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementById("eventPlannerPhotoUpload"))[0].src
            }
            $scope.uploadPlannerPhoto = true;
            uploadPhotoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadPlannerPhoto = false;
                    $scope.plannerPhotoSuccess = true;
                    $scope.plannerImageId = data.data.imageUUID;

                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.uploadCoverPhoto = false;
        $scope.coverPhotoSuccess = false;

        $scope.photoUploadPlannerCover = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("coverPhotoUpload"))[0].src
            }
            $scope.uploadCoverPhoto = true;

            uploadPhotoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadCoverPhoto = false;
                    $scope.coverPhotoSuccess = true;
                    $scope.coverImageId = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.deletePicEventExchange = function () {
            fileSelectEventExchange.value = "" ;
            $scope.showThumbnailEventExchange = false;
        }

        fileSelectProfile.onchange = function() { //set callback to action after choosing file
            var f = fileSelectProfile.files[0], r = new FileReader();
            $scope.showThumbnailProfile = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                // console.log(base64Data.replace(/^data:image\/(png|jpg);base64,/, "")); //replace regex if you want to rip off the base 64 "header"
                angular.element(document.getElementsByClassName("profilePhotoUpload"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };

        fileSelectCover.onchange = function() { //set callback to action after choosing file
            var f = fileSelectCover.files[0], r = new FileReader();
            $scope.showThumbnailCover = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                // console.log(base64Data.replace(/^data:image\/(png|jpg);base64,/, "")); //replace regex if you want to rip off the base 64 "header"
                angular.element(document.getElementsByClassName("coverPhotoUpload"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };

        fileSelectEventPlanner.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventPlanner.files[0], r = new FileReader();
            $scope.showThumbnailEventPlanner = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                // console.log(base64Data.replace(/^data:image\/(png|jpg);base64,/, "")); //replace regex if you want to rip off the base 64 "header"
                angular.element(document.getElementById("eventPlannerPhotoUpload"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventExchange.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventExchange.files[0], r = new FileReader();
            $scope.showThumbnailEventExchange = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                // console.log(base64Data.replace(/^data:image\/(png|jpg);base64,/, "")); //replace regex if you want to rip off the base 64 "header"
                angular.element(document.getElementsByClassName("exchangePhotoUpload"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };


        $scope.editUserInfo = function () {
            Auth.getUser()
                .then(function (data, status) {
                    $scope.logoutMenu = true;
                    userProfile.userData = data.data;
                    userInfo.setData(userProfile.userData);
                    $scope.editInfo = angular.copy(userInfo.getData());

                },function (data, status) {

                }, function (data) {
                    console.log("updated");
                })
        }


        $scope.editInfo = angular.copy(userInfo.getData());

        $scope.scrollTo = function(id, section) {
            var old = $location.hash();
            $location.hash(id);
            $location.hash(old);
            scrollAnimation.scrollTo(id);

            $(angular.element(document.getElementById(section)).siblings()[0]).slideDown(300);
            $(angular.element(document.getElementById(section))).addClass('orangeBackground');

        }

        $scope.addFieldTicketType=function(i){
            if($scope.showTimeForms[i].blitTypes.length < 5) {
                $scope.showTimeForms[i].blitTypes.push({})
            }
        };
        $scope.deleteFieldTicketType=function(i){
            if( 1 < $scope.showTimeForms[i].blitTypes.length) {
                $scope.showTimeForms[i].blitTypes.splice(-1,1);
            }
        };
        $scope.addFieldShowTime=function(){
            if($scope.showTimeForms.length < 10) {
                userProfile.showTimeNumber++;
                userProfile.setDate(userProfile.showTimeNumber);
                $scope.showTimeForms.push({blitTypes : [{}]})
            }
        };
        $scope.deleteFieldShowTime=function(){
            if( 1 < $scope.showTimeForms.length) {
                userProfile.showTimeNumber--;
                $scope.showTimeForms.splice(-1,1);
            }
        };

        $scope.submitPlannerSpinner = false;
        $scope.submitPlannerNotif = false;
        $scope.submitEventPlanner = function (plannerData) {
            var eventPlannerData = {
                hostName: plannerData.name,
                hostType: plannerData.type,
                images: [
                    {
                        imageUUID: $scope.plannerImageId,
                        type: "HOST_PHOTO"
                    },
                    {
                        imageUUID: $scope.coverImageId,
                        type: "HOST_COVER_PHOTO"
                    }
                ],
                instagramLink: plannerData.instagram,
                linkedinLink: plannerData.linkedin,
                telegramLink: plannerData.telegram,
                telephone: plannerData.mobile,
                twitterLink: plannerData.twitter,
                websiteLink: plannerData.website
            }
            $scope.submitPlannerSpinner = true;
            plannerService.submitPlannerForm(eventPlannerData)
                .then(function (data, status) {
                    $scope.submitPlannerSpinner = false;
                    $scope.submitPlannerNotif = true;
                    $scope.getPlannersData();
                    console.log(data);
                }, function (data, status) {
                    $scope.submitPlannerSpinner = false;
                    console.log(data);
                })
            console.log(eventPlannerData);
        }

        $scope.submitEvent = function (eventFields) {
            var latLng = mapMarkerService.getMarker();
            var eventSubmitData = {
                eventName : eventFields.name,
                eventType : eventFields.eventType,
                eventHostId : eventFields.eventPlanner.eventHostId,
                address : eventFields.address,
                aparatDisplayCode : eventFields.aparatLink,
                blitSaleEndDate : $scope.eventEndTime,
                blitSaleStartDate : $scope.eventStartTime,
                description : eventFields.description,
                eventDates : $scope.showTimeForms,
                images : [ {imageUUID : $scope.eventImageId, type : "EVENT_PHOTO"}],
                latitude : latLng.lat,
                longitude : latLng.lng
            }
            $scope.createEventSpinner = true;
            eventService.submitEventForm(eventSubmitData)
                .then(function (data, status) {
                    $scope.createEventSpinner = false;
                    $scope.createEventNotif = true;
                    console.log(data);
                }, function (data, status) {
                    console.log(data);
                    $scope.createEventSpinner = false;
                })

        }

        $scope.submitExchangeSpinner = false;
        $scope.submitExchangeNotif = false;

        $scope.submitExchangeTicket = function (exchangeFields) {
            var latLng = mapMarkerService.getMarker();
            $scope.submitExchangeSpinner = true;
            var exchangeData = {
                blitCost: exchangeFields.price,
                description: exchangeFields.description,
                email: $scope.userData.email,
                eventAddress: exchangeFields.address,
                eventDate:  $scope.exchangeTime,
                image: {
                    imageUUID: $scope.exchangeImageId,
                    type: "EXCHANGEBLIT_PHOTO"
                },
                isBlitoEvent: exchangeFields.isBlito,
                phoneNumber: $scope.userData.mobile,
                latitude: latLng.lat,
                longitude: latLng.lng,
                title: exchangeFields.name,
                type: exchangeFields.type
            };
            exchangeService.submitExchangeForm(exchangeData)
                .then(function (data, status) {
                    console.log(data);
                    $scope.submitExchangeSpinner = false;
                    $scope.submitExchangeNotif = true;

                }, function (data, status) {
                    console.log(data);
                })
            console.log(exchangeData);
        }
        $scope.showTime = [];

     
        $scope.dropDownTabToggleEvent = function (event) {
            $scope.getPlannersData();
            $scope.getUserEvents();
            mapMarkerService.initMap(document.getElementById('map'));
            $(angular.element(document.getElementById('toggleExchange'))).slideUp(300);
            $(angular.element(event.currentTarget).siblings()[0]).slideDown(300);
        }
        $scope.dropDownTabToggleExchange = function (event) {
            $scope.getExchangeData();
            mapMarkerService.initMap(document.getElementById('mapExchange'));
            $(angular.element(document.getElementById('toggleEvent'))).slideUp(300);
            $(angular.element(event.currentTarget).siblings()[0]).slideDown(300);
        }
        $scope.closeTabDropDowns = function () {
            $(angular.element(document.getElementsByClassName('tabDropDown'))).slideUp(300);
        }

        $scope.toggleBody = function (section) {
            $(angular.element(document.getElementById(section)).siblings()[0]).slideToggle(300);
            $(angular.element(document.getElementById(section))).toggleClass('orangeBackground');
        }

        $scope.exchangeTickets = [];
        $scope.getExchangeData = function () {
            exchangeService.getExchangeTickets()
                .then(function (data, status) {
                    $scope.exchangeTickets = data.data.content;
                    console.log(data.data);
                    $scope.exchangeTickets = $scope.exchangeTickets.map(function (ticket) {
                        ticket.operatorState = dataService.operatorStatePersian(ticket.operatorState);
                        ticket.state = dataService.stateTypePersian(ticket.state);
                        ticket.type = dataService.eventTypePersian(ticket.type);
                        ticket.eventDate = persianDate(ticket.eventDate).pDate;
                        return ticket;
                    })
                    $timeout(function () {
                        $(".persianTimeExchange").pDatepicker({
                            timePicker: {
                                enabled: true
                            },
                            altField: '#persianDigitAlt',
                            altFormat: "YYYY MM DD HH:mm:ss",
                            altFieldFormatter: function (unixDate) {
                            }
                        });
                    },1000)
                    console.log($scope.exchangeTickets);

                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.getUserEvents = function () {
            eventService.getUserEvents()
                .then(function (data, status) {
                    console.log(data);
                    $scope.userEvents = data.data.content;
                    $scope.userEvents = $scope.userEvents.map(function (event) {
                        event.eventType = dataService.eventTypePersian(event.eventType);
                        event.blitSaleEndDate = persianDate(event.blitSaleEndDate).pDate;
                        event.blitSaleStartDate = persianDate(event.blitSaleStartDate).pDate;
                        return event;
                    })
                    $timeout(function () {
                        $(".persianTimeExchange").pDatepicker({
                            timePicker: {
                                enabled: true
                            },
                            altField: '#persianDigitAlt',
                            altFormat: "YYYY MM DD HH:mm:ss",
                            altFieldFormatter: function (unixDate) {
                            }
                        });
                    },1000)
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.getPlannersData = function () {
            plannerService.getPlanners()
                .then(function (data, status) {
                    plannerService.setPlanners(data.data.content)
                    $scope.eventHosts = data.data.content;
                    console.log($scope.eventHosts)
                }, function (data, status) {
                    console.log(data);
                })
        }
    })
