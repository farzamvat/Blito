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
        // seatMap.generateSeatBlitTypesWithSeatUids = function (schemaSections) {
        //     var blitTypeIds = [];
        //     var blitTypes = [];
        //     schemaSections.forEach(function (section) {
        //         section.rows.forEach(function (row) {
        //             row.seats.forEach(function (seat) {
        //                 if(blitTypeIds.indexOf(seat.blitTypeId) === -1) {
        //                     blitTypeIds.push(seat.blitTypeId);
        //                     blitTypes.push({ blitTypeId : seat.blitTypeId, seatUids : []});
        //                     blitTypes[blitTypes.length - 1].seatUids.push(seat.uid);
        //                 } else {
        //                     blitTypes.forEach(function (blitType) {
        //                         if(blitType.blitTypeId === seat.blitTypeId) {
        //                             blitType.seatUids.push(seat.uid);
        //                         }
        //                     })
        //                 }
        //             })
        //         })
        //     });
        //     return blitTypes;
        // };
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
        seatMap.editedPopulatedSchema = function (newBlitTypes, populatedSchema) {
            newBlitTypes.forEach(function (newBlitType) {
                newBlitType.seatUids.forEach(function (seatUid) {
                    populatedSchema.schema.sections.forEach(function (section) {
                        section.rows.forEach(function (row) {
                            row.seats.forEach(function (populatedSeat) {
                                if((populatedSeat.uid === seatUid) && ((populatedSeat.state === 'NOT_AVAILABLE') || (populatedSeat.state === 'AVAILABLE')) ) {
                                    populatedSeat.blitTypeId = newBlitType.blitTypeId;
                                    if(newBlitType.name === 'HOST_RESERVED_SEATS') {
                                        populatedSeat.state = 'NOT_AVAILABLE';
                                    } else {
                                        populatedSeat.state = 'AVAILABLE';
                                    }
                                }
                            })
                        })
                    })
                })
            });
            console.log(populatedSchema);
            return populatedSchema;
        };

        seatMap.generateWithoutSeatBlitTypes = function (allBlitTypes) {
            return allBlitTypes.filter(function (blitType) {
                return !blitType.hasSeat;
            })
        };
        seatMap.generateWithSeatBlitTypes = function (allBlitTypes) {
            return allBlitTypes.filter(function (blitType) {
                return blitType.hasSeat;
            })
        };
        // seatMap.generateWithSeatBlitTypes = function (seatBlitTypesWithSeatUids, seatBlitTypesWithoutSeatUids) {
        //     seatBlitTypesWithSeatUids.forEach(function (blitTypesWithUids) {
        //         seatBlitTypesWithoutSeatUids.forEach(function (blitTypesWithoutUids) {
        //             if(blitTypesWithoutUids.blitTypeId === blitTypesWithUids.blitTypeId) {
        //                 blitTypesWithoutUids.seatUids = blitTypesWithUids.seatUids;
        //             }
        //         })
        //     });
        //     return seatBlitTypesWithoutSeatUids;
        // }
    });
