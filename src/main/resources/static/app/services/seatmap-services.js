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
            return allBlitTypes.filter(function (blitType) {
                var isWithSeats = true;
                for(var i = 0; i < seatsBlitType.length; i++) {
                    if(blitType.blitTypeId === seatsBlitType[i].blitTypeId) {
                        isWithSeats = false;
                        break;
                    }
                }
                return isWithSeats;
            });
        };
        seatMap.generateNewBlitTypes = function (oldBlitTypes, newBlitTypes) {
            newBlitTypes.forEach(function (newBlitType) {
                newBlitType.seatUids.forEach(function (seatUid) {
                    oldBlitTypes.forEach(function (oldBlitType) {
                        oldBlitType.seatUids = oldBlitType.seatUids.filter(function (oldSeatUid) {
                            return oldSeatUid !== seatUid;
                        })
                    })
                })
            });
            oldBlitTypes = oldBlitTypes.filter(function (oldBlitType) {
                oldBlitType.capacity = oldBlitType.seatUids.length;
                return oldBlitType.seatUids.length !== 0;
            });
            return oldBlitTypes.concat(newBlitTypes);
        };
        seatMap.generateMainBlitTypesFormat = function (generatedBlitTypes, allBlitTypes) {
            generatedBlitTypes.forEach(function (generatedBlitType) {
                allBlitTypes.forEach(function (blitType) {
                    if(blitType.blitTypeId === generatedBlitType.blitTypeId) {
                        generatedBlitType.name = blitType.name;
                        generatedBlitType.capacity = blitType.capacity;
                        generatedBlitType.isFree = blitType.isFree;
                    }
                })
            });
            return generatedBlitTypes;
        }
    });
