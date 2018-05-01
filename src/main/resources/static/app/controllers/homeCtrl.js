/**
 * Created by soroush on 4/18/17.
 */

angular.module('homePageModule', [])
    .controller('homeCtrl', [
        '$scope',
        'miniSliderService',
        'photoService',
        'indexBannerService',
        'eventDetailService',
        '$q',
        'config',
        '$interval',

        function ($scope, miniSliderService, photoService, indexBannerService, eventDetailService, $q,config) {
            $scope.concertRow = [];
            $scope.timePickedSearch = [{n : 'امروز', v : 'T'}, {n : 'تا یک هفته', v : 'W'},{n : 'تا یک ماه', v : 'M'},{n : 'همه روزها', v : 'AllTimes'}];
            $scope.pricePickedSearch = [{p : 'تا ۱۵ هزار تومان', v : '15L'},{p : 'تا ۴۰ هزار تومان', v : '40L'} , {p : 'بالای ۴۰ هزار تومان', v : '40U'}, {p : 'همه قیمت‌ها', v : 'AllPrices'}];
            $scope.typePickedSearch = [{t : 'کنسرت', v : 'CONCERT'}, {t : 'تئاتر', v: 'THEATER'}, {t :'سینما', v: 'CINEMA'}, {t :'تور', v: 'TOUR'}, {t :'کارگاه', v: 'WORKSHOP'}, {t :'سرگرمی', v: 'ENTERTAINMENT'}, {t :'نمایشگاه', v: 'EXHIBITION'}, {t :'سایر', v: 'OTHER'}, {t :'همه رویداد‌ها', v: 'AllTypes'} ];
            $scope.searchData = {timePicked : {n : 'همه روزها', v : 'AllTimes'}, typePicked : {t :'همه رویداد‌ها', v: 'AllTypes'}, pricePicked : {p : 'همه قیمت‌ها', v : 'AllPrices'}, name:''};
            $scope.showSectionsExcahnge = [false,false];
            $scope.bannerData = [];
            var promisesExchange = [[], []];
            $scope.url = config.baseUrl+"/event-page/";
            $scope.urlExchange = config.baseUrl+"/exchange-page/";
            miniSliderService.getAllEventsCount()
                .then(function (data) {
                    $scope.totalNumberOfEvents = data.data.count;
                })
                .catch(function (data) {
                });
            indexBannerService.getIndexBanner()
                .then(function (data) {
                    $scope.bannerData = $scope.catchImagesExchange(data.data.content, 0);
                })
                .catch(function (data) {
                });
            // use search with getSlidingDataEvents api

            miniSliderService.getSlidingDataEvents(8, [])
                .then(function (data) {
                    $scope.eventsWithImage = $scope.setEventData(data.data.content);
                })
                .catch(function () {

                })

            miniSliderService.getSlidingDataExchange(6)
                .then(function (data) {
                    $scope.exchange = $scope.catchImagesExchange(data.data.content, 1);
                })
                .catch(function (data) {
                });

            $scope.searchHomePage = function (searchData) {
                console.log(searchData);
                var restrictions = [], endTime = 0;
                var d = new Date();
                if(searchData.timePicked.v !== 'AllTimes') {
                    console.log("11");
                    switch (searchData.timePicked.v) {
                        case "T" :
                            endTime =  (d.setHours(0,0,0,0) + 86400000);
                            break;
                        case "W" :
                            endTime =  (d.setHours(0,0,0,0) + (86400000*7));
                            break;
                        case "M" :
                            endTime =  (d.setHours(0,0,0,0) + (86400000*30));
                            break;
                        default:
                            endTime = 0;
                            break;
                    }
                    restrictions.push(
                        {
                            type : "complex",
                            operator : "and",
                            restrictions: [
                                {
                                    type : "time",
                                    field : "eventDates-date",
                                    operation : "gt",
                                    value : d.getTime()
                                },
                                {
                                    type : "time",
                                    field : "eventDates-date",
                                    operation : "lt",
                                    value : endTime
                                }
                            ]
                        }
                    )
                }
                if( searchData.typePicked.v !== 'AllTypes') {
                    restrictions.push(
                        {
                            field: "eventType",
                            type: "simple",
                            operation: "eq",
                            value: searchData.typePicked.v
                        }
                    )
                }
                if( searchData.pricePicked.v !== 'AllPrices') {
                    var price = 0, comp = '';
                    switch (searchData.pricePicked.v) {
                        case "15L":
                            price = 15000;
                            comp = "lt";
                            break;
                        case "40L":
                            price = 40000;
                            comp = "lt";
                            break;
                        case "40U":
                            price = 40000;
                            comp = "gt";
                            break;
                        default:
                            price = '0';
                            comp = "gt";
                            break;
                    }
                    restrictions.push(
                        {
                            type: "simple",
                            field : "eventDates-blitTypes-price",
                            value : price,
                            operation : comp
                        }
                    )
                }
                if(searchData.name !== '') {
                    restrictions.concat(
                        {
                            type : "simple",
                            field : "eventName",
                            value : searchData.name,
                            operation : "like"
                        }
                    )
                }
                console.log(restrictions);
                miniSliderService.getSlidingDataEvents(8, restrictions)
                    .then(function (data) {
                        console.log(data);
                    })
                    .catch(function () {

                    })
                };
            $scope.setEventData = function (events) {
                return events.map(function (item) {
                    var firstEventDate = '';
                    var tempDate = 10000000000000;
                    item.minPrice = 1000000000;
                    item.maxPrice = 0;
                    var eventImage =  item.images.filter(function (image) {
                        return image.type === "EVENT_PHOTO";
                    });
                    item.eventDates.forEach(function (eventDate) {
                        if(eventDate.date < tempDate) {
                            if(eventDate.dateTime) {
                                firstEventDate = eventDate.dateTime;
                            } else {
                                firstEventDate = persianDate(eventDate.date).format("dddd,DD MMMM, ساعت HH:mm")
                            }
                        }
                        if(item.minPrice > eventDate.price) {
                            item.minPrice = eventDate.price;
                        }
                        if(item.maxPrice < eventDate.price) {
                            item.maxPrice = eventDate.price;
                        }
                        tempDate = eventDate.date;
                    });
                    item.firstEventDate = firstEventDate;
                    photoService.download(eventImage[0].imageUUID)
                        .then(function (data) {
                            item.image = data.data.encodedBase64;
                        })
                        .catch(function () {
                            item.image = '';
                        })
                    return item;
                });
            };

            $scope.calculateCapacitySoldOut = function (eventList) {
                eventList.forEach(function (item) {
                    item.capacity = 0;
                    item.soldCount = 0;
                    item.eventDates.forEach(function (eventDate) {
                        item.soldCount += eventDate.soldCount;
                        item.capacity += eventDate.capacity;
                    })
                });
            };
            $scope.catchImagesExchange = function (events, i) {
                events.map(function (item) {
                    promisesExchange[i].push(photoService.download(item.image.imageUUID)
                        .then(function (data) {
                            item.newImage = data.data.encodedBase64;
                        })
                        .catch(function (data) {
                        }));
                    return item;
                });
                $q.all(promisesExchange[i])
                    .then(function () {
                        $scope.showSectionsExcahnge[i] = true;
                    });
                return events;
            };

            $("#timeSearchPart").click(function () {
                $(".timeDropDown").slideToggle();
                $(".typeDropDown").slideUp();
                $(".priceDropDown").slideUp();
            });
            $("#priceSearchPart").click(function () {
                $(".priceDropDown").slideToggle();
                $(".timeDropDown").slideUp();
                $(".typeDropDown").slideUp();
            });
            $("#typeSearchPart").click(function () {
                $(".typeDropDown").slideToggle();
                $(".priceDropDown").slideUp();
                $(".timeDropDown").slideUp();
            });
            $scope.setSearchTime = function (time) {
                $("#timeSearchPart").val(time.n);
                $scope.searchData.timePicked = time;
            };
            $scope.setSearchPrice = function (price) {
                $("#priceSearchPart").val(price.p);
                $scope.searchData.pricePicked = price;
            };
            $scope.setSearchType = function (type) {
                $("#typeSearchPart").val(type.t);
                $scope.searchData.typePicked = type;
            };

            document.body.addEventListener('click', function (event) {
                if(event.srcElement.classList.value.indexOf('withDropDown') === -1) {
                    $(".typeDropDown").slideUp();
                    $(".priceDropDown").slideUp();
                    $(".timeDropDown").slideUp();
                }

            }, true);
        }]);