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
                headers: {'Cache-Control': 'private, max-age=31536000', 'Pragma' : ''},
                cache : true
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
                cache : true,
                params : {page: page-1, size: 12, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions: [
                    {field: "eventState", type: "simple", operation: "neq", value: "ENDED"},
                    {field: "eventType", type: "simple", operation: "eq", value: type}
                ]
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/public/events/search', bodyJson, queryParam)
        };
        event.submitDiscount = function (discountData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/discount/set-discount-code', discountData);
        };
        event.validateDiscount = function (validateData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/validate-discount-code', validateData);
        };
        event.validateDiscountWithSeat = function (validateData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/validate-discount-code-seat-blit', validateData);
        };
        event.discountState = function (discountData) {
            return $http.put(config.baseUrl+'/api/blito/v1.0/discount/set-enable', discountData);
        };
        event.discountEdit = function (discountEditData) {
            return $http.put(config.baseUrl+'/api/blito/v1.0/discount/update-discount-code', discountEditData);
        };
        event.searchDiscount = function (page, eventId) {
            var queryParam = {
                params : {page: page-1, size: 3, sort: "effectDate,desc"}
            };
            var bodyJson = {
                restrictions: [
                    {field: "blitTypes-eventDate-event-eventId", type: "simple", operation: "eq", value: eventId}
                    ]
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/discount/search', bodyJson, queryParam)
        };
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
                cache : true,
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
        ticket.buyTicketWithSeat = function (ticketInfo) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/blits/buy-request-with-seat', ticketInfo);
        };
        ticket.buyTicketWithSeatNotUser = function (ticketInfo) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/public/blits/buy-request-with-seat', ticketInfo);
        };
        ticket.buyTicketNotUser = function (ticketInfo) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/public/blits/buy-request', ticketInfo);
        };

        ticket.getUserTickets = function (pageNumber) {
            console.log(pageNumber);
            var queryParam = {
                params : {page: pageNumber-1, size: 5, sort: "createdAt,desc"}
            };
            return $http.get(config.baseUrl+'/api/blito/v1.0/blits', queryParam)
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
        ticket.getEventTicketsWithSeat = function (pageNumber, eventId) {
            var queryParam = {
                params : {page: pageNumber-1, size: 4, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions : [
                    {field : "blitTypeSeats-blitType-eventDate-event-eventId", type : "simple", operation : "eq", value: eventId}
                ]
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/blits/seats/search', bodyJson, queryParam)
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
        };
        ticket.getExcelTicketsWithSeat = function (sansId) {
            var bodyJson = {
                restrictions : [
                    {field : "blitTypeSeats-blitType-eventDate-eventDateId", type : "simple", operation : "eq", value : sansId},
                    {field : "paymentStatus", type : "simple", operation : "neq", value : 'PENDING'},
                    {field : "paymentStatus", type : "simple", operation : "neq", value : 'ERROR'}
                ]
            };
            return $http({
                method: 'POST',
                url: config.baseUrl+'/api/blito/v1.0/blits/seats/blits.xlsx',
                data: bodyJson,
                responseType: "arraybuffer"
            });
        }
    })
    .service('plannerService', function ($http, config) {
        var planner = this;
        planner.getPlanners = function (page, size) {
            var queryParam = {
                params : {page: page-1, size: size}
            };
            return $http.get(config.baseUrl+'/api/blito/v1.0/event-hosts/all', queryParam)
        };
        planner.submitPlannerForm = function (eventData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/event-hosts', eventData)
        };
        planner.editPlannerForm = function (editData) {
            return $http.put(config.baseUrl+'/api/blito/v1.0/event-hosts', editData)
        };
        planner.getPlannerByLink = function (plannerLink) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/public/event-hosts/link/'+plannerLink)
        };
        planner.getPlannerById = function (plannerId) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/public/event-hosts/'+plannerId)
        };
        planner.getPlannerEvents = function (plannerLink, pageNumber) {
            var queryParam = {
                params : {page: pageNumber-1, size: 6, sort: "createdAt,desc"}
            };
            var bodyJson = {
                restrictions : [
                    {field : "eventHost-eventHostLink", type : "simple", operation : "eq", value: plannerLink}
                ]
            };
            return $http.post(config.baseUrl+'/api/blito/v1.0/public/events/search',bodyJson,queryParam)
        };
        planner.deletePlanner = function (plannerId) {
            var queryParam = {
                params : {eventHostId : plannerId}
            };
            return $http.delete(config.baseUrl+'/api/blito/v1.0/event-hosts', queryParam)
        };
    });