/**
 * Created by soroush on 5/17/17.
 */
angular.module('UiServices', [])
    .service('mapMarkerService', function ($timeout) {
        var mapMarkerService = this;
        var markers = [], markersExchange = [], map, latLng;

        mapMarkerService.getMarker = function () {
            return latLng;
        }
        mapMarkerService.setMarker = function (lat, lng) {
            latLng = {
                lat : lat,
                lng : lng
            }
        }
        try {
            var mapOptions = {
                zoom: 14,
                center: new google.maps.LatLng(35.7023, 51.3957),
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
            mapMarker(map, 1);

        }
        // mapMarkerService.deleteMarkers = function () {
        //     for (var i = 0; i < markers.length; i++) {
        //         markers[i].setMap(null);
        //     }
        //     markers = [];
        // }

        var mapMarker = function (map, type) {
            try {
                $timeout(function () {
                    try {
                        google.maps.event.trigger(map, 'resize');
                    } catch (err) {
                        console.log(err);
                    }
                }, 300);

                map.addListener('click', function (e) {
                    console.log(e.latLng.lng());
                    mapMarkerService.setMarker(e.latLng.lat(), e.latLng.lng());
                    placeMarker(e.latLng, map);
                })

                var placeMarker = function (latLng, map) {

                    var marker = new google.maps.Marker({
                        position: latLng,
                        map: map
                    });
                    if (type === 1) {
                        markersExchange.push(marker);
                        if (markersExchange.length > 1) {
                            markersExchange[0].setMap(null);
                            markersExchange.shift();
                        }
                    } else if (type === 2) {
                        markers.push(marker);
                        if (markers.length > 1) {
                            markers[0].setMap(null);
                            markers.shift();
                        }
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
