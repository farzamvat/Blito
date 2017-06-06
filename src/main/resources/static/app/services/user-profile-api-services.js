/**
 * Created by soroush on 5/29/17.
 */

angular.module('userProfileApi', [])
    .service('photoService', function ($http, config) {
        var photo = this;


        photo.upload = function (imageData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/images/upload', imageData)
        }
        photo.download = function (imageData) {
            var queryParam = {
                params : { id : imageData}
            }
            return $http.get(config.baseUrl+'/api/blito/v1.0/images/download', queryParam)
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


        exchange.submitExchangeForm = function (exchangeData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/exchange-blits', exchangeData)
        }

        exchange.getExchangeTickets = function () {
            var queryParam = {
                params : {page: 0, size: 100}
            }

            return $http.get(config.baseUrl+'/api/blito/v1.0/exchange-blits/all', queryParam)
        }

        exchange.editExchangeForm = function (editData) {
            return $http.put(config.baseUrl+'/api/blito/v1.0/exchange-blits', editData)
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