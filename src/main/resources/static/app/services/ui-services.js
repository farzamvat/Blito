/**
 * Created by soroush on 5/17/17.
 */
angular.module('UiServices', [])
    .service('mapMarkerService', function ($timeout) {
        var mapMarkerService = this;
        var markers = [], map, latitudeLongtitude = {lat : 35.7023, lng : 51.3957};

        mapMarkerService.getMarker = function () {
            return latitudeLongtitude;
        };
        mapMarkerService.setMarker = function (lat, lng) {
            latitudeLongtitude = {
                lat : lat,
                lng : lng
            }
        };
        mapMarkerService.getMarkerArray = function () {
            return markers;
        };
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

        };
        mapMarkerService.initMapOnlyShowMarker = function (mapInput) {

            try {
                map = new google.maps.Map(mapInput, mapOptions);
            } catch(err) {
                console.log(err);
            }
            mapMarkerOnlyShow(map);

        };

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
                    map.setCenter(new google.maps.LatLng(latitudeLongtitude.lat, latitudeLongtitude.lng));
                }, 600);

                map.addListener('click', function (e) {
                    mapMarkerService.setMarker(e.latLng.lat(), e.latLng.lng());
                    mapMarkerService.placeMarker(e.latLng, map);
                })

                mapMarkerService.placeMarker = function (latLong, map) {

                    var marker = new google.maps.Marker({
                        position: latLong,
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
        };
        var mapMarkerOnlyShow = function (map) {

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
                    map.setCenter(new google.maps.LatLng(latitudeLongtitude.lat, latitudeLongtitude.lng));
                }, 600);


                mapMarkerService.placeMarker = function (latLong, map) {

                    var marker = new google.maps.Marker({
                        position: latLong,
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
    .service('eventDetailService', function () {
        var eventDetail = this;
        var soldCount = 0, capacity = 0;
        eventDetail.calculateFreeBlits = function (event) {
            event.eventDates.forEach(function (blit) {
                capacity +=blit.capacity;
                soldCount +=blit.soldCount;
            });
            event.capacity = capacity;
            event.soldCount = soldCount;
            return event
        };
    })
    .service('imageServices', function ($rootScope, photoService) {
        var image = this;
        image.readBase64Data = function (fileSelector, className) {
            var f = fileSelector.files[0], r = new FileReader();

            r.onloadend = function (e) {
                console.log(e);
                var base64Data = e.target.result;
                $rootScope.$apply();
                angular.element(document.getElementsByClassName(className))[0].src = base64Data;
            };
            r.readAsDataURL(f);

        };
        image.downloadPhotos = function (UUID, className) {
            photoService.download(UUID)
                .then(function (data, status) {
                    angular.element(document.getElementsByClassName(className))[0].src = data.data.encodedBase64;
                })
                .catch(function (data, status) {
                    console.log(status);
                });
        };
    })
    .service('dateSetterService', function () {
        var dateSetter = this;
        dateSetter.initDate = function (className) {
            $("."+className).pDatepicker({
                timePicker: {
                    enabled: true
                },
                formatter : function (unixDate) {
                    var self = this;
                    var pdate = new persianDate(unixDate);
                    pdate.formatPersian = true;
                    return pdate.format(self.format);
                },
                altField: '#persianDigitAlt',
                altFormat: "YYYY MMM DD HH"

            });
        };
        dateSetter.persianToArray = function (date) {
            var dateArray = [];
            dateArray.push(date.year);
            dateArray.push(date.month);
            dateArray.push(date.date);
            dateArray.push(date.hours);
            dateArray.push(date.minutes);
            dateArray.push(date.seconds);
            dateArray.push(date.milliseconds);
            dateArray = dateArray.map(function (item) {
                return parseInt(item);
            });
            return dateArray;
        };

        dateSetter.persianToMs = function (date) {
            var newData = date.replace(/:|-/gi , ' ').split(" ");
            newData.pop();
            newData.pop();
            newData = newData.map(function (persianNumb) {
                var persian = {'۰':0,'۱':1,'۲':2,'۳':3,'۴':4,'۵': 5,'۶': 6,'۷': 7,'۸' : 8,'۹': 9};
                return persianNumb.split('').map(function (persianDigit) {
                    return persian[persianDigit];
                }).join('');
            });
            newData = newData.map(function (item) {
                return parseInt(item);
            });
            date = persianDate(newData).gDate.getTime();
            return date;
        };
        dateSetter.persianToArray = function (date) {
            var dateArray = [];
            dateArray.push(date.year);
            dateArray.push(date.month);
            dateArray.push(date.date);
            dateArray.push(date.hours);
            dateArray.push(date.minutes);
            dateArray.push(date.seconds);
            dateArray.push(date.milliseconds);
            dateArray = dateArray.map(function (item) {
                return parseInt(item);
            });

            return dateArray;
        };
    })
    .service('dataService', function () {
        var data = this;

        data.eventTypePersian = function (type) {
            var persianType = '';
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
                case "OTHER" :
                    persianType = 'سایر';
                    break;
                default :
                    persianType = 'گونه';
                    break;
            }
            return persianType;
        };

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
                    persianState = 'گونه';
                    break;

            }
            return persianState;
        };

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
                    persianOperatorState = 'گونه';
                    break;
            }
            return persianOperatorState;
        };
        data.ticketStatusPersian = function (operatorState) {
            var persianOperatorState = '';
            switch (operatorState) {
                case "PAID" :
                    persianOperatorState = 'پرداخت شده';
                    break;
                case "ERROR" :
                    persianOperatorState = 'خطا';
                    break;
                case "PENDING" :
                    persianOperatorState = 'انتظار';
                    break;
                case "FREE" :
                    persianOperatorState = 'رایگان';
                    break;
                default :
                    persianOperatorState = 'گونه';
                    break;
            }
            return persianOperatorState;
        };
        data.mapToPersianEvent = function (item) {
            item.eventState = data.stateTypePersian(item.eventState);
            item.eventType = data.eventTypePersian(item.eventType);
            item.operatorState = data.operatorStatePersian(item.operatorState);
            return item;
        };

        data.mapToPersianExchange = function (item) {
            item.operatorState = data.operatorStatePersian(item.operatorState);
            item.state = data.stateTypePersian(item.state);
            item.type = data.eventTypePersian(item.type);
            return item;
        }

    });
