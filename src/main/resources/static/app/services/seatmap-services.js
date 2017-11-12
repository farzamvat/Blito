/**
 * Created by soroush on 10/25/17.
 */
angular.module('UiServices')
    .service('seatmapService', function ($http, config) {
        var seatMap = this;
        seatMap.getSeatmapList = function () {
            var queryParam = {
                params : {page: 0, size: 100}
            };
            return $http.get(config.baseUrl+'/api/blito/v1.0/salons', queryParam);

        };
        seatMap.getSalonData = function (uid) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/salons/'+uid);

        };
        seatMap.getPopulatedSchema = function (eventDateId) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/salons/populated-schema/' + eventDateId);
        };
        seatMap.schemaFormat = function (schemaSections) {
            var blitTypeIds = [];
            var blitTypes = [];
            console.log(schemaSections);
            schemaSections.forEach(function (section) {
                section.rows.forEach(function (row) {
                    row.seats.forEach(function (seat) {
                        if(blitTypeIds.indexOf(seat.blitTypeId) === -1) {
                            blitTypeIds.push(seat.blitTypeId);
                            blitTypes.push({ blitTypeId : seat.blitTypeId, price : seat.price, seatUids : []});
                            blitTypes[blitTypes.length - 1].seatUids.push(seat.uid);
                        } else {
                            blitTypes.forEach(function (blitType) {
                                if(blitType.blitTypeId === seat.blitTypeId) {
                                    blitType.seatUids.push(seat.uid);
                                }
                            })
                        }
                    })
                })
            });
            return blitTypes;
        };
        seatMap.getBlitTypesWithoutSeat = function (seatsBlitType, allBlitTypes) {
            var blitTypesWithoutSeat =  allBlitTypes.filter(function (blitType) {
                seatsBlitType.forEach(function (seatBlitType) {
                    return blitType.blitTypeId === seatBlitType.blitTypeId;
                })
            });
            return blitTypesWithoutSeat;
        };
        seatMap.editEventDataAnyChartFormat = function (schemaSections) {
            
        }
    });
