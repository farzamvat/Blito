/**
 * Created by soroush on 5/2/17.
 */

angular.module('User')
    .controller('userProfileCtrl', function ($scope, userInfo, Auth, $timeout, mapMarkerService, updateInfo, $location, scrollAnimation, uploadPhotoService, eventCreateService, plannerCreateService) {
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
        }, 1000)
            userProfile.setDate(userProfile.showTimeNumber);



        $scope.deleteMarkers = function () {

        }

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

        $scope.convertToBase64 = function (pic) {



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
                    $scope.eventImageId = data.data.imageId;
                    console.log(data);
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
                    $scope.plannerImageId = data.data.imageId;
                    console.log(data);
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
                    $scope.coverImageId = data.data.imageId;
                    console.log(data);
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
            plannerCreateService.submitPlanenerForm(eventPlannerData)
                .then(function (data, status) {
                    $scope.submitPlannerSpinner = false;
                    $scope.submitPlannerNotif = true;
                    console.log(data);
                }, function (data, status) {
                    console.log(data);
                })
            console.log(eventPlannerData);
        }
        $scope.submitEvent = function (eventFields) {
            var latLng = mapMarkerService.getMarker();
            var eventSubmitData = {
                eventName : eventFields.name,
                eventType : eventFields.eventType,
                eventHostId : 97,
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
            console.log(eventSubmitData);
            $scope.createEventSpinner = true;
            eventCreateService.submitEventForm(eventSubmitData)
                .then(function (data, status) {
                    $scope.createEventSpinner = false;
                    $scope.createEventNotif = true;
                    console.log(data);
                }, function (data, status) {
                    console.log(data);
                })
            // console.log($scope.eventStartTime);
            // console.log($scope.eventEndTime);
            // console.log(eventFields);
            // console.log(mapMarkerService.getMarker());
            // console.log($scope.showTimeForms);
            // console.log($scope.eventImageId);
        }
        $scope.showTime = [
            {title : "نام رویداد", weekDay: "یکشنبه", price: "1000", date:"22/2", time: "۱۰:۳۰", sansState: true, expired: true},
            {title : "نام رویداد", weekDay: "سه شنبه", price: "2000", date:"23/2", time: "۹:۰۰",  sansState: false, expired: false},
            {title : "نام رویداد", weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: true, expired: true},
            {title : "نام رویداد", weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: false, expired: true},
            {title : "نام رویداد", weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: true, expired: true},
            {title : "نام رویداد", weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: false, expired: true},
            {title : "نام رویداد", weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: true, expired: true},
            {title : "نام رویداد", weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: true, expired: true},
            {title : "نام رویداد", weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: true, expired: true},
            {title : "نام رویداد", weekDay: "شنبه", price: "4000", date:"25/2", time: "۸:۳۰",  sansState: true, expired: false}
        ];

        $scope.dropDownTabToggleEvent = function (event) {
            mapMarkerService.initMap(document.getElementById('map'));
            $(angular.element(document.getElementById('toggleExchange'))).slideUp(300);
            $(angular.element(event.currentTarget).siblings()[0]).slideDown(300);
        }
        $scope.dropDownTabToggleExchange = function (event) {
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

    })
