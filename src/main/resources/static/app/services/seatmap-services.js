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
        seatMap.getGuestTicket = function (guestDate) {
            return $http({
                method: 'POST',
                url: config.baseUrl+"/api/blito/v1.0/blits/generate-reserved-blit.pdf",
                responseType: "arraybuffer",
                data : guestDate
            });
        };
        seatMap.getPopulatedSchema = function (eventDateId) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/salons/populated-schema/' + eventDateId);
        };
        seatMap.getPublicPopulatedSchema = function (eventDateId) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/public/salons/populated-schema/' + eventDateId);
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
        seatMap.getBoughtBlitTypes = function (selectedSeats, populatedSchema) {
            var pickedBlitPrices = 0;
            selectedSeats.forEach(function (seatPickedUid) {
                populatedSchema.schema.sections.forEach(function (section) {
                    section.rows.forEach(function (row) {
                        row.seats.forEach(function (seat) {
                            if(seat.uid === seatPickedUid) {
                                pickedBlitPrices += seat.price;
                            }
                        })
                    })
                })
            });
            return pickedBlitPrices;
        };
        seatMap.oneSeatUnpickedPayment = function (seatUids, populatedSchema) {
            var pickedSeatsCheck = false;
            seatUids.forEach(function (seatUid) {
                populatedSchema.schema.sections.forEach(function (section) {
                    section.rows.forEach(function (row) {
                        for(var seatIndex = 0; seatIndex < row.seats.length ; seatIndex++) {
                            if( (row.seats[seatIndex-1]) && (!row.seats[seatIndex-1].prevUid) && (row.seats[seatIndex-1].nextUid === seatUid) && (seatUids.indexOf(row.seats[seatIndex-1].uid) === -1) && (row.seats[seatIndex-1].state === 'AVAILABLE')) {
                                pickedSeatsCheck = true;
                            }
                            if((row.seats[seatIndex+1]) && (!row.seats[seatIndex+1].nextUid) &&(row.seats[seatIndex+1].prevUid === seatUid) && (seatUids.indexOf(row.seats[seatIndex+1].uid) === -1) && (row.seats[seatIndex+1].state === 'AVAILABLE')) {
                                pickedSeatsCheck = true;
                            }
                            if((row.seats[seatIndex].nextUid === seatUid) && (seatUids.indexOf(row.seats[seatIndex].uid) === -1) && (row.seats[seatIndex].state === 'AVAILABLE')) {
                                if(row.seats[seatIndex-1] && ((seatUids.indexOf(row.seats[seatIndex-1].uid) !== -1) || (row.seats[seatIndex-1].state !== 'AVAILABLE'))) {
                                    pickedSeatsCheck = true;
                                }
                            }
                            if((row.seats[seatIndex].prevUid === seatUid) && (seatUids.indexOf(row.seats[seatIndex].uid) === -1) && (row.seats[seatIndex].state === 'AVAILABLE')) {
                                if(row.seats[seatIndex+1] && ((seatUids.indexOf(row.seats[seatIndex+1].uid) !== -1) || (row.seats[seatIndex+1].state !== 'AVAILABLE'))) {
                                    pickedSeatsCheck = true;
                                }
                            }
                        }
                    })
                });
            });
            return pickedSeatsCheck;
        };
    });
