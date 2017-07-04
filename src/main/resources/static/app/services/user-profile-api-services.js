/**
 * Created by soroush on 5/29/17.
 */

angular.module('userProfileApi', [])
    .service('photoService', function ($http, config) {
        var photo = this;


        photo.upload = function (imageData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/images/upload', imageData)
        };
        photo.download = function (imageData) {
            var queryParam = {
                params : { id : imageData}
            };
            return $http.get(config.baseUrl + '/api/blito/v1.0/images/download', queryParam);

        }
    })


    .service('eventService', function ($http, config) {
        var event = this;

        event.submitEventForm = function (eventData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/events', eventData)
        };

        event.getUserEvents = function (page) {
            var queryParam = {
                params : {page: page-1, size: 4}
            };
            return $http.get(config.baseUrl+'/api/blito/v1.0/events/all-user-events', queryParam)
        };
        event.editEvent = function (editData) {
            return $http.put(config.baseUrl+'/api/blito/v1.0/events', editData)
        };
        event.editEventState = function (stateChange) {
            return $http.put(config.baseUrl+'/api/blito/v1.0/events/change-event-state', stateChange)
        };
        event.getEvent = function (eventLink) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/public/events/flat/link/'+eventLink)
        };
        event.getEventsByType = function (type, page) {
            var queryParam = {
                params : {page: page-1, size: 12}
            };
            var bodyJson = {
                restrictions: [
                    {field: "isDeleted", type: "simple", operation: "eq", value: "false"},
                    {field: "eventState", type: "simple", operation: "neq", value: "ENDED"},
                    {field: "operatorState", type: "simple", operation: "eq", value: "APPROVED"},
                    {field: "eventType", type: "simple", operation: "eq", value: type}
                ]
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/public/events/search', bodyJson, queryParam)
        }

    })
    .service('exchangeService', function ($http, config) {
        var exchange = this;


        exchange.submitExchangeForm = function (exchangeData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/exchange-blits', exchangeData)
        };

        exchange.getExchangeTickets = function (page) {
            var queryParam = {
                params : {page: page-1, size: 4}
            };

            return $http.get(config.baseUrl+'/api/blito/v1.0/exchange-blits/all', queryParam)
        };
        exchange.getExchange = function (exchangeLink) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/public/exchange-blits/'+exchangeLink)
        };

        exchange.editExchangeForm = function (editData) {
            return $http.put(config.baseUrl+'/api/blito/v1.0/exchange-blits', editData)
        };
        exchange.editExchangeState = function (editData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/exchange-blits/change-state', editData)
        };
        exchange.getAllExchanges = function (pageNumber) {
            var queryParam = {
                params : {page: pageNumber-1, size: 12, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions : [
                    {field : "isDeleted", type : "simple", operation : "eq", value: "false"}
                ]
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/public/exchange-blits/search', bodyJson,queryParam);
        };
    })
    .service('plannerService', function ($http, config) {
        var planner = this;

        planner.getPlanners = function (page) {
            var queryParam = {
                params : {page: page-1, size: 4}
            };
            return $http.get(config.baseUrl+'/api/blito/v1.0/event-hosts/all', queryParam)
        };
        planner.submitPlannerForm = function (eventData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/event-hosts', eventData)
        };
        planner.editPlannerForm = function (editData) {
            return $http.put(config.baseUrl+'/api/blito/v1.0/event-hosts', editData)
        };
    });