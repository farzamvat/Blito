/**
 * Created by soroush on 6/22/17.
 */

angular.module('eventsPageModule')
    .controller('exchangePageCtrl', function ($scope, userInfo, Auth) {
        var eventPage = this;
        $scope.userEmail = 'email';
        $scope.mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(35.7023, 51.3957),
            mapTypeId: google.maps.MapTypeId.TERRAIN
        };



        $scope.map = new google.maps.Map(document.getElementById('map'), $scope.mapOptions);

        $scope.showTime = [
            {weekDay: "یکشنبه", price: "1000", date:"22/2", time: "۱۰:۳۰", sansState: true, expired: true}
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