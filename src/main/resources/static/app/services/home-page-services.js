/**
 * Created by soroush on 6/30/17.
 */

angular.module('homePageApi', [])
    .service('miniSliderService', [
        '$http',
        'config',
        function ($http, config) {
            var miniSlider = this;
            miniSlider.getAllEventsCount = function () {
                return $http.get(config.baseUrl + '/api/blito/v1.0/public/events/count');
            };
            miniSlider.getSlidingDataEvents = function (page, size, restriction) {
                var queryParam = {
                    cache : true,
                    params : {page: page, size: size, sort: "orderNumber,desc"}
                };
                var bodyJson = {
                    restrictions: [
                        {field: "eventState", type: "simple", operation: "neq", value: "ENDED"}
                    ]
                };
                bodyJson.restrictions = bodyJson.restrictions.concat(restriction);

                return $http.post(config.baseUrl + '/api/blito/v1.0/public/events/search',bodyJson, queryParam);

            };
            miniSlider.getHomePagePlaners = function () {
                var queryParam = {
                    cache : true,
                    params : {page: 0, size: 4}
                };

                return $http.get(config.baseUrl + '/api/blito/v1.0/public/event-hosts/home-page/search', queryParam);
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
        }])
    .service('indexBannerService', [
        '$http',
        'config',
        function ($http, config) {
            var indexBanner = this;
            indexBanner.getIndexBanner = function () {
                var queryParam = {
                    cache : true,
                    params : {page: 0, size: 5}
                };
                return $http.get(config.baseUrl + '/api/blito/v1.0/public/index-banners', queryParam);

            }

        }])
;