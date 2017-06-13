/**
 * Created by soroush on 5/17/17.
 */
angular.module('UiServices', [])
    .service('mapMarkerService', function ($timeout) {
        var mapMarkerService = this;
        var markers = [], map, latitudeLongtitude = {lat : 35.7023, lng : 51.3957};

        mapMarkerService.getMarker = function () {
            return latitudeLongtitude;
        }
        mapMarkerService.setMarker = function (lat, lng) {
            latitudeLongtitude = {
                lat : lat,
                lng : lng
            }
        }
        mapMarkerService.getMarkerArray = function () {
            return markers;
        }
        try {
            var mapOptions = {
                zoom: 14,
                center: new google.maps.LatLng(latitudeLongtitude.lat, latitudeLongtitude.lng),
                mapTypeId: google.maps.MapTypeId.TERRAIN
            };
        } catch (err) {
            console.log(err);
        }
        mapMarkerService.initMap = function (mapInput) {

            try {
                map = new google.maps.Map(mapInput, mapOptions);
            } catch(err) {
                console.log(err);
            }
            mapMarker(map);

        }
        // mapMarkerService.deleteMarkers = function () {
        //     for (var i = 0; i < markers.length; i++) {
        //         markers[i].setMap(null);
        //     }
        //     markers = [];
        // }

        var mapMarker = function (map) {
            markers = [];
            try {
                $timeout(function () {
                    try {
                        google.maps.event.trigger(map, 'resize');
                    } catch (err) {
                        console.log(err);
                    }
                }, 300);

                $timeout(function () {
                    mapMarkerService.placeMarker(latitudeLongtitude, map);
                }, 600);

                map.addListener('click', function (e) {
                    mapMarkerService.setMarker(e.latLng.lat(), e.latLng.lng());
                    mapMarkerService.placeMarker(e.latLng, map);
                })

                mapMarkerService.placeMarker = function (latLong, map) {

                    var marker = new google.maps.Marker({
                        position: latLong,
                        center: new google.maps.LatLng(latLong.lat, latLong.lng),
                        map: map
                    });

                    markers.push(marker);
                    if (markers.length > 1) {
                        markers[0].setMap(null);
                        markers.shift();
                    }


                }
            } catch (err) {
                console.log(err);
            }
        }
    })
    .service('dataService', function () {
        var data = this;

        data.eventTypePersian = function (type) {
            var persianType = ''
            switch (type) {
                case "CINEMA" :
                    persianType = 'سینما';
                    break;
                case "THEATER" :
                    persianType = 'تئاتر';
                    break;
                case "TOURISM" :
                    persianType = 'تور';
                    break;
                case "CONCERT" :
                    persianType = 'کنسرت';
                    break;
                case "SPORT" :
                    persianType = 'ورزشی';
                    break;
                case "DISCOUNT_TICKET" :
                    persianType = 'بن تخفیف';
                    break;
                case "WORKSHOP" :
                    persianType = 'کارگاه';
                    break;
                case "INDIVIDUAL" :
                    persianType = 'فرد';
                    break;
                case "CULTURALCENTER" :
                    persianType = 'فرهنگسرا';
                    break;
                case "ORGANIZATION" :
                    persianType = 'نهاد';
                    break;
                case "INSTITUTION" :
                    persianType = 'مؤسسه';
                    break;
                case "COFFEESHOP" :
                    persianType = 'کافی شاپ';
                    break;
                default :
                    persianType = 'گونه'
                    break;
            }
            return persianType;
        }

        data.stateTypePersian = function (state) {
            var persianState = '';
            switch (state) {
                case "SOLD" :
                    persianState = 'فروخته شده';
                    break;
                case "OPEN" :
                    persianState = 'فعال';
                    break;
                case "CLOSED" :
                    persianState = 'بسته';
                    break;
                default :
                    persianState = 'گونه'
                    break;

            }
            return persianState;
        }

        data.operatorStatePersian = function (operatorState) {
            var persianOperatorState = '';
            switch (operatorState) {
                case "REJECTED" :
                    persianOperatorState = 'رد شده';
                    break;
                case "PENDING" :
                    persianOperatorState = 'انتظار';
                    break;
                case "APPROVED" :
                    persianOperatorState = 'تأیید شده';
                    break;
                default :
                    persianOperatorState = 'گونه'
                    break;

            }
            return persianOperatorState;
        }

    })
