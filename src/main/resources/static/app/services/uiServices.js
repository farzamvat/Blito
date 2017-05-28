/**
 * Created by soroush on 5/17/17.
 */
angular.module('UiServices', [])
    .service('mapMarkerService', function ($timeout) {
        var mapMarkerService = this;
        var markers = [], markersExchange = [], map;


        var mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(35.7023, 51.3957),
            mapTypeId: google.maps.MapTypeId.TERRAIN
        };
        mapMarkerService.initMap = function (mapInput) {

            map = new google.maps.Map(mapInput, mapOptions);
            mapMarker(map, 1);

        }
        // mapMarkerService.deleteMarkers = function () {
        //     for (var i = 0; i < markers.length; i++) {
        //         markers[i].setMap(null);
        //     }
        //     markers = [];
        // }

        var mapMarker = function (map, type) {

            $timeout(function(){
                google.maps.event.trigger(map, 'resize');
            },300);

            map.addListener('click', function (e) {
                placeMarker(e.latLng, map);
            })

            var placeMarker = function (latLng, map) {

                var marker = new google.maps.Marker({
                    position: latLng,
                    map: map
                });
                if(type === 1) {
                    markersExchange.push(marker);
                    if(markersExchange.length > 1) {
                        markersExchange[0].setMap(null);
                        markersExchange.shift();
                    }
                } else if(type === 2) {
                    markers.push(marker);
                    if(markers.length > 1) {
                        markers[0].setMap(null);
                        markers.shift();
                    }
                }

            }
        }
    })
    .service('photoUploadService', function () {
        var photoUploadService = this;

        photoUploadService.uploadPic = function () {

        }
    })