/**
 * Created by soroush on 5/2/17.
 */

angular.module('User')
    .controller('userProfileCtrl', function ($scope, userInfo, Auth, $timeout, mapMarkerService, updateInfo, userInfo) {
        var userProfile = this;
        var markers = [], markersExchange = [], map, mapExhange;
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
        $scope.ticketTypes = [{}];
        $scope.showTimeForms = [{}];

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
        $scope.addFieldTicketType=function(){
            if($scope.ticketTypes.length < 5) {
                $scope.ticketTypes.push({})
            }
        };
        $scope.deleteFieldTicketType=function(){
            if( 1 < $scope.ticketTypes.length) {
                $scope.ticketTypes.splice(-1,1);
            }
        };
        $scope.addFieldShowTime=function(){
            if($scope.showTimeForms.length < 10) {
                $scope.showTimeForms.push({})
            }
        };
        $scope.deleteFieldShowTime=function(){
            if( 1 < $scope.showTimeForms.length) {
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
