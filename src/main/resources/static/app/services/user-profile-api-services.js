/**
 * Created by soroush on 5/29/17.
 */

angular.module('userProfileApi', [])
    .service('photoService', function ($http, config) {
        var photo = this;


        photo.upload = function (imageData) {
            var blob = new Blob([imageData.encodedBase64], {type: 'image/png'});
            var file = new File([blob], 'imageFileName.png');
            var fd = new FormData();
            fd.append("file", file);
            return $http.post(config.baseUrl+'/api/blito/v1.0/images/multipart/upload', fd,
                {transformRequest : angular.identity, headers : {'Content-Type': undefined}})
        };
        photo.uploadOld = function (imageData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/images/upload', imageData)
        };
        photo.download = function (imageData) {
            var queryParam = {
                params : { id : imageData},
                headers: {'Cache-Control': 'private, max-age=31536000', 'Pragma' : ''}
            };
            return $http.get(config.baseUrl + '/api/blito/v1.0/download', queryParam);
        };
        photo.deleteGalleryPhoto = function (eventId, UUID) {
            return $http.delete(config.baseUrl+'/api/blito/v1.0/events/'+eventId+'/'+UUID)
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
        event.deleteEvent = function (eventId) {
            return $http.delete(config.baseUrl+'/api/blito/v1.0/events/'+eventId)
        };

        event.getEvent = function (eventLink) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/public/events/link/'+eventLink)
        };
        event.getEventsByType = function (type, page) {
            var queryParam = {
                params : {page: page-1, size: 12, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions: [
                    {field: "eventState", type: "simple", operation: "neq", value: "ENDED"},
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
        exchange.deleteExchange = function (exchangeId) {
            return $http.delete(config.baseUrl+'/api/blito/v1.0/exchange-blits/'+exchangeId)
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
                restrictions : []
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/public/exchange-blits/search', bodyJson,queryParam);
        };
    })
    .service('ticketsService', function ($http, config) {
        var ticket = this;
        ticket.buyTicket = function (ticketInfo) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/blits/buy-request', ticketInfo);
        };
        ticket.buyTicketNotUser = function (ticketInfo) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/public/blits/buy-request', ticketInfo);
        };
        ticket.getUserTickets = function (pageNumber, userEmail) {
            var queryParam = {
                params : {page: pageNumber-1, size: 5, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions : [
                    {field : "user-email", type : "simple", operation : "eq", value: userEmail},
                    {field : "paymentStatus", type : "simple", operation : "neq", value: 'PENDING'},
                    {field : "paymentStatus", type : "simple", operation : "neq", value : 'ERROR'}
                ]
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/blits/search', bodyJson, queryParam)
        };
        ticket.getEventTickets = function (pageNumber, eventId) {
            var queryParam = {
                params : {page: pageNumber-1, size: 4, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions : [
                    {field : "blitType-eventDate-event-eventId", type : "simple", operation : "eq", value: eventId}
                ]
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/blits/search', bodyJson, queryParam)
        };
        ticket.getBoughtTicket = function (trackCode) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/public/blits/'+trackCode)
        };
        ticket.getPdfTicket = function (trackCode) {
            return $http({
                method: 'GET',
                url: config.baseUrl+"/api/blito/v1.0/public/blits/"+trackCode+"/blit.pdf",
                responseType: "arraybuffer"
            });
        };
        ticket.getExcelTickets = function (sansId) {
            var bodyJson = {
                restrictions : [
                    {field : "blitType-eventDate-eventDateId", type : "simple", operation : "eq", value : sansId},
                    {field : "paymentStatus", type : "simple", operation : "neq", value : 'PENDING'},
                    {field : "paymentStatus", type : "simple", operation : "neq", value : 'ERROR'}
                ]
            };
            return $http({
                method: 'POST',
                url: config.baseUrl+'/api/blito/v1.0/blits/blits.xlsx',
                data: bodyJson,
                responseType: "arraybuffer"
            });
        }
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
        planner.deletePlanner = function (plannerId) {
            var queryParam = {
                params : {eventHostId : plannerId}
            };
            return $http.delete(config.baseUrl+'/api/blito/v1.0/event-hosts', queryParam)
        };
    });