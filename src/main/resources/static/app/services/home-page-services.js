/**
 * Created by soroush on 6/30/17.
 */

angular.module('homePageApi', [])
    .service('miniSliderService', function ($http, config) {
        var miniSlider = this;
        miniSlider.getAllEventsCount = function () {
            return $http.get(config.baseUrl + '/api/blito/v1.0/public/events/count');
        };
        miniSlider.getSlidingDataEvents = function (eventType, size, evento) {
            var queryParam = {
                cache : true,
                params : {page: 0, size: size, sort: "orderNumber,desc"}
            };
            if(!evento) {
                var bodyJson = {
                    restrictions: [
                        {field: "eventState", type: "simple", operation: "neq", value: "ENDED"},
                        {field: "eventType", type: "simple", operation: "eq", value: eventType}

                    ]
                };
            } else if(evento) {
                var bodyJson = {
                    restrictions: [
                        {field: "eventState", type: "simple", operation: "neq", value: "ENDED"},
                        {field: "isEvento", type: "simple", operation: "eq", value: "true"}

                    ]
                };
            }
            return $http.post(config.baseUrl + '/api/blito/v1.0/public/events/search',bodyJson, queryParam);

        };
        miniSlider.getSlidingDataExchange = function (size) {
            var queryParam = {
                cache : true,
                params : {page: 0, size: size, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions: []
            };

            return $http.post(config.baseUrl + '/api/blito/v1.0/public/exchange-blits/search',bodyJson, queryParam);
        };
        miniSlider.getEndedEvents = function (page, size) {
            var queryParam = {
                cache : true,
                params : {page: page-1, size: size, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions: [
                    {field: "eventState", type: "simple", operation: "eq", value: "ENDED"}
                ]
            };

            return $http.post(config.baseUrl + '/api/blito/v1.0/public/events/search',bodyJson, queryParam);
        }
    })
    .service('indexBannerService', function ($http, config) {
        var indexBanner = this;
        indexBanner.getIndexBanner = function () {
            var queryParam = {
                cache : true,
                params : {page: 0, size: 5}
            };
            return $http.get(config.baseUrl + '/api/blito/v1.0/public/index-banners', queryParam);

        }

    })
    .service('ourOffersService', function ($http, config) {
        var ourOffer = this;
        ourOffer.getOurOffer = function (type, evento) {
            var queryParam = {
                cache : true,
                params : {page: 0, size: 0, sort: "createdAt,desc"}
            };
            var bodyJson ;
            if(!evento) {
                bodyJson = {
                    restrictions: [
                        {field: "eventState", type: "simple", operation: "neq", value: "ENDED"},
                        {field: "eventType", type: "simple", operation: "eq", value: type},
                        {field: "isEvento", type: "simple", operation: "eq", value: "false"},
                        {field: "offers", type: "collection", values: ["OUR_OFFER"]}
                    ]
                }
            } else if(evento) {
                bodyJson = {
                    restrictions: [
                        {field: "eventState", type: "simple", operation: "neq", value: "ENDED"},
                        {field: "isEvento", type: "simple", operation: "eq", value: "true"},
                        {field: "offers", type: "collection", values: ["OUR_OFFER"]}
                    ]
                }
            }
            return $http.post(config.baseUrl + '/api/blito/v1.0/public/events/search', bodyJson, queryParam);

        }

    });