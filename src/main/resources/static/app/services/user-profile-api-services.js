/**
 * Created by soroush on 5/29/17.
 */

angular.module('userProfileApi', [])
    .service('uploadPhotoService', function ($http, config) {
        var uploadPhoto = this;

        uploadPhoto.upload = function (imageData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/images/upload', imageData)
        }
    })

    .service('eventService', function ($http, config) {
        var event = this;
        var queryParam = {
            params : {page: 0, size: 100}
        }

        event.submitEventForm = function (eventData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/events', eventData)
        }

        event.getUserEvents = function () {
            return $http.get(config.baseUrl+'/api/blito/v1.0/events/all-user-events', queryParam)
        }
    })
    .service('exchangeService', function ($http, config) {
        var exchange = this;

        var queryParam = {
            params : {page: 0, size: 100}
        }

        exchange.submitExchangeForm = function (exchangeData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/exchange-blits', exchangeData)
        }
        exchange.getExchangeTickets = function () {
            return $http.get(config.baseUrl+'/api/blito/v1.0/exchange-blits/all', queryParam)
        }
    })
    .service('plannerService', function ($http, config) {
        var planner = this;
        var planners = [];
        planner.setPlanners = function (p) {
            planners = p;
        }
        planner.getPlanners = function () {
            return planners;
        }
        planner.getPlanners = function () {
            var queryParam = {
                params : {page: 0, size: 100}
            }
            return $http.get(config.baseUrl+'/api/blito/v1.0/event-hosts/all', queryParam)
        }
        planner.submitPlannerForm = function (eventData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/event-hosts', eventData)
        }
    })