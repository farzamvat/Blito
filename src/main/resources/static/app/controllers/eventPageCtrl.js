/**
 * Created by soroush on 4/25/17.
 */
angular.module('eventsPageModule')
    .controller('eventPageCtrl', function ($scope, $routeParams, eventPageService, userInfo, Auth) {
        var eventPage = this;
        $scope.userEmail = 'email';
        $scope.mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(35.7023, 51.3957),
            mapTypeId: google.maps.MapTypeId.TERRAIN
        };
        var data = $routeParams.eventLink;
        eventPageService.getEvent(data)
            .then(function (data, status) {
                console.log(data);
            })
            .catch(function (data, status) {
                console.log(data);
            })
        console.log(data);



        $scope.map = new google.maps.Map(document.getElementById('map'), $scope.mapOptions);

        $scope.showTime = [
            {weekDay: "یکشنبه", price: "1000", date:"22/2", time: "۱۰:۳۰", sansState: true, expired: true},
            {weekDay: "سه شنبه", price: "2000", date:"23/2", time: "۹:۰۰",  sansState: false, expired: false},
            {weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: true, expired: true},
            {weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: false, expired: true},
            {weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: true, expired: true},
            {weekDay: "جمعه", price: "1000", date:"24/2", time: "۸:۰۰",  sansState: false, expired: true},

            {weekDay: "شنبه", price: "4000", date:"25/2", time: "۸:۳۰",  sansState: true, expired: false}
        ];
        $scope.showTime.forEach(function (item) {
            if(item.sansState && item.expired) {
                item.showTimeStateTitle = 'فعال';
            } else if (!item.expired) {
                item.showTimeStateTitle = 'تاریخ گذشته';
            } else if (item.sansState === false && item.expired === true) {
                item.showTimeStateTitle = 'بلیت تمام شده';
            }
        })

        $scope.eventType = "cinema";

        $('.slick-slider').slick('setPosition');




    });
