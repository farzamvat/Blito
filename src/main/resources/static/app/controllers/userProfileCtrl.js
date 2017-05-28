/**
 * Created by soroush on 5/2/17.
 */

angular.module('User')
    .controller('userProfileCtrl', function ($scope, userInfo, Auth, $timeout, mapMarkerService, updateInfo, $location, scrollAnimation) {
        var userProfile = this;

        $scope.userData = userInfo.getData();
        $scope.showContentEvent = true;
        $scope.showAdvertiseEvent = true;
        $scope.myTicketList = true;
        $scope.showAdvertiseListEvent = false;
        $scope.showContentPlanner = false;
        $scope.showContentList = false;
        $scope.updateInfoSpinner = false;

        $scope.updateSuccessNotif = false;
        $scope.updateFailNotif = false;
        $scope.showTimeForms = [{ticketTypes : [{}]}];

        $scope.showThumbnailProfile = false;
        $scope.showThumbnailEventPlanner = false;
        $scope.showThumbnailEventExchange = false;
        $scope.showThumbnailCover = false;


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
                        $scope.showTimeForms[i].timeStamp = unixDate;
                    }
                });
                console.log($scope.showTimeForms);
            }, 1000);

        }
        userProfile.setDate(userProfile.showTimeNumber);
        // $(angular.element(document.getElementsByClassName("eventShowTime"))).pDatepicker();
        // console.log($(angular.element(document.getElementsByClassName("eventShowTime"))));
        $scope.initMapExchange = function () {
            mapMarkerService.initMap(document.getElementById('mapExchange'));
        }
        $scope.initMap = function () {
            mapMarkerService.initMap(document.getElementById('map'));
        }
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
                console.log(base64Data);
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
                console.log(base64Data);
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

        $scope.scrollTo = function(id) {
            var old = $location.hash();
            $location.hash(id);
            $location.hash(old);
            scrollAnimation.scrollTo(id);



            $(angular.element(document.getElementById('PlannerSection')).siblings()[0]).slideDown(300);
            $(angular.element(document.getElementById('PlannerSection'))).addClass('orangeBackground');
            $scope.showContentPlanner = true;
        }

        $scope.addFieldTicketType=function(i){
            if($scope.showTimeForms[i].ticketTypes.length < 5) {
                $scope.showTimeForms[i].ticketTypes.push({})
            }
        };
        $scope.deleteFieldTicketType=function(i){
            if( 1 < $scope.showTimeForms[i].ticketTypes.length) {
                $scope.showTimeForms[i].ticketTypes.splice(-1,1);
            }
        };
        $scope.addFieldShowTime=function(){
            if($scope.showTimeForms.length < 10) {
                userProfile.showTimeNumber++;
                userProfile.setDate(userProfile.showTimeNumber);
                $scope.showTimeForms.push({ticketTypes : [{}]})
            }
        };
        $scope.deleteFieldShowTime=function(){
            if( 1 < $scope.showTimeForms.length) {
                userProfile.showTimeNumber--;
                $scope.showTimeForms.splice(-1,1);
            }
        };

        $scope.submitEvent = function (eventFields) {
            console.log(eventFields);
            console.log($scope.ticketTypes);
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
        $scope.toggleAdvertiseEventBody = function (event) {
            $(angular.element(event.currentTarget).siblings()[0]).slideToggle(300);
            $(angular.element(event.currentTarget)).toggleClass('orangeBackground');
            $scope.showAdvertiseEvent = !$scope.showAdvertiseEvent;
        }
        $scope.toggleAdvertiseListEventBody = function (event) {
            $(angular.element(event.currentTarget).siblings()[0]).slideToggle(300);
            $(angular.element(event.currentTarget)).toggleClass('orangeBackground');
            $scope.showAdvertiseListEvent = !$scope.showAdvertiseListEvent;
        }
        $scope.toggleMYyTicketList = function (event) {
            $(angular.element(event.currentTarget).siblings()[0]).slideToggle(300);
            $(angular.element(event.currentTarget)).toggleClass('orangeBackground');
            $scope.myTicketList = !$scope.myTicketList;
        }

        $scope.togglePanelEventBody = function (event) {
            $(angular.element(event.currentTarget).siblings()[0]).slideToggle(300);
            $(angular.element(event.currentTarget)).toggleClass('orangeBackground');
            $scope.showContentEvent = !$scope.showContentEvent;
        }
        $scope.togglePanelPlannerBody = function (event) {
            $(angular.element(event.currentTarget).siblings()[0]).slideToggle(300);
            $(angular.element(event.currentTarget)).toggleClass('orangeBackground');
            $scope.showContentPlanner = !$scope.showContentPlanner;
        }
        $scope.togglePanelListBody = function (event) {
            $(angular.element(event.currentTarget).siblings()[0]).slideToggle(300);
            $(angular.element(event.currentTarget)).toggleClass('orangeBackground');
            $scope.showContentList = !$scope.showContentList;
        }
    })
