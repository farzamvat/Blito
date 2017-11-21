/**
 * Created by soroush on 10/25/17.
 */
angular.module('blitoDirectives')
    .directive('seatMap',  function () {
        return {
            link : seatMapDraw,
            controller : function ($scope,$timeout) {
                var seatMapController = this;
                $scope.pickedSeats = [];
                $scope.svgIndex = 0;
                seatMapController.validationCheckBlitType = function (blitIds, svgIndex) {
                    $scope.$emit("blitIdsChanged", [blitIds, svgIndex]);
                };
                $scope.$on('blitTypeSubmit', function (event, data) {
                    $scope.resetPickedSeats(data[0], data[1], data[2]);
                });
                $scope.$on('newSVG', function (event, data) {
                    $scope.svgIndex = data[1];
                    $scope.drawSVG(data[0], data[1]);
                });
            },
            restrict : 'E',
            scope : {}
        };
        function seatMapDraw(scope, element, attr, ctrl) {
            scope.drawSVG = function (svgData, svgIndex) {
                scope.pickedSeats = [];
                if (document.getElementById("seatMaperChart" + svgIndex).childNodes[0]) {
                    var svgElement = document.getElementById("seatMaperChart" + svgIndex);
                    svgElement.removeChild(svgElement.childNodes[0]);
                }
                var chart,sectionsChart = [];
                var seatMapSeries;
                chart = anychart.seatMap();
                chart.geoData(svgData.salonSvg);
                var seatMapData = new Array();
                var wholeSalonData=[];

                for (var sectionIndex = 0; sectionIndex < svgData.schema.sections.length; sectionIndex++) {
                    var rowName = 0;
                    var salonDat={};
                    salonDat.id = svgData.schema.sections[sectionIndex].uid;
                    salonDat.info = svgData.schema.sections[sectionIndex].name;
                    wholeSalonData.push(salonDat);
                    sectionsChart.push({chart: anychart.seatMap(), uid: salonDat.id});

                    var svgSection = svgData.sections.filter(function (section) {
                        if(section.sectionUid === salonDat.id) {
                            return section;
                        }
                    });

                    sectionsChart[sectionIndex].chart.geoData(svgSection[0].sectionSvg);

                    seatMapData[sectionIndex] = new Array();
                    for (var rowIndex = 0; rowIndex < svgData.schema.sections[sectionIndex].rows.length; rowIndex++) {
                        seatMapData[sectionIndex][rowIndex] = new Array();
                        rowName = svgData.schema.sections[sectionIndex].rows[rowIndex].name;
                        svgData.schema.sections[sectionIndex].rows[rowIndex].seats.forEach(function (seat) {

                            seatMapData[sectionIndex][rowIndex].push({
                                id: seat.uid,
                                info: seat.name,
                                value: rowName
                            });
                        });
                    }
                }
                seatMapSeries=chart.choropleth();
                seatMapSeries.data(wholeSalonData);
                chart.title(svgData.schema.name.toString());
                chart.contextMenu(false);
                chart.labels(true);
                chart.tooltip(false);
                var chartLabels=chart.labels();
                chart.labels({fontSize: 18});
                chartLabels.format("{%info}");
                var touchSS = function(e){
                        e.preventDefault();
                };
                function createDrillUpLabel(text, offset, stage , action ){
                    var label = anychart.standalones.label();
                    label.background({fill: "#9E9E9E"});
                    label.zIndex(99999);
                    label.text(text);
                    label.fontColor("#fff");
                    label.padding(5);
                    label.offsetX(0);
                    label.offsetY(10);
                    label.listen("click", action);
                    label.container(stage);
                    label.draw();
                    return label;
                };


                document.getElementById('seatMaperChart'+svgIndex).addEventListener('touchend', touchSS);
                var seatClickFunction = function (e) {
                    if (scope.pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        scope.pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', 'green');
                    } else {
                        scope.pickedSeats.splice(scope.pickedSeats.indexOf(e.domTarget.dd), 1);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#64b5f6');
                    }
                    e.preventDefault();
                    ctrl.validationCheckBlitType(scope.pickedSeats, svgIndex);
                };
                var rowCheck = function (section,rowIndex) {
                    for (var i = 0; i < section.rows[rowIndex].seats.length; i++) {
                        if ((scope.pickedSeats.indexOf(section.rows[rowIndex].seats[i].uid) === -1)  && (!$('#'+"seatMaperChart"+svgIndex+' '+'#'+section.rows[rowIndex].seats[i].uid).hasClass('noPointerEvents'))) {
                            return false;
                        }
                    }
                    return true;
                };
                var legendListener = function (e) {
                    var currentSection = svgData.schema.sections.filter(function (section) {
                        if(section.uid === sectionPickedUid) {
                            return section;
                        }
                    });
                    if (rowCheck(currentSection[0],e.itemIndex)) {
                        currentSection[0].rows[e.itemIndex].seats.forEach(function (seat) {
                            if(!$('#'+"seatMaperChart"+svgIndex+' '+'#'+seat.uid).hasClass('noPointerEvents')) {
                                $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#64b5f6');
                                scope.pickedSeats.splice(scope.pickedSeats.indexOf(seat.uid), 1);
                            }
                        });
                    } else {
                        currentSection[0].rows[e.itemIndex].seats.forEach(function (seat) {
                            if ((scope.pickedSeats.indexOf(seat.uid) === -1) && (!$('#'+"seatMaperChart"+svgIndex+' '+'#'+seat.uid).hasClass('noPointerEvents'))) {
                                $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'green');
                                scope.pickedSeats.push(seat.uid);
                            }
                        });
                    }
                    e.preventDefault();
                    ctrl.validationCheckBlitType(scope.pickedSeats, svgIndex);
                };
                var palette = anychart.palettes.rangeColors();
                palette.items(["#64b5f6"]);

                for (var sectionIndex = 0; sectionIndex < svgData.schema.sections.length; sectionIndex++) {
                    palette.count(svgData.schema.sections[sectionIndex].numberOfRows);
                    sectionsChart[sectionIndex].chart.palette(palette);
                    chart.palette(palette);

                    for (var rowIndex = 0; rowIndex < svgData.schema.sections[sectionIndex].rows.length; rowIndex++) {
                        var rowSeats = [];
                        seatMapData[sectionIndex][rowIndex].forEach(function (seat) {
                            if (seat.value === svgData.schema.sections[sectionIndex].rows[rowIndex].name) {
                                rowSeats.push(seat);
                            }
                        });
                        var section=sectionsChart[sectionIndex].chart.choropleth(rowSeats)
                            .name(svgData.schema.sections[sectionIndex].rows[rowIndex].name);
                        section.listen('touchstart', seatClickFunction);
                        section.listen('click', seatClickFunction);


                    }

                    var legend = sectionsChart[sectionIndex].chart.legend();
                    legend.enabled(true)
                        .position('right')
                        .itemsLayout('vertical')
                        .title("انتخاب ردیف")
                        .removeAllListeners()
                    ;

                    legend.title({fontSize:20});
                    legend.listen("click", legendListener);
                    legend.listen("touchstart", legendListener);

                    sectionsChart[sectionIndex].chart.labels(true);
                    var labels = sectionsChart[sectionIndex].chart.labels();


                    sectionsChart[sectionIndex].chart.labels({fontSize: 10});
                    labels.format("{%info}");
                }
                chart.bounds(0, '10%', '100%', '90%');
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();

                var DrillupLabel= createDrillUpLabel( "بازگشت" , 0, "seatMaperChart"+svgIndex ,function(){
                    chart.drillUp();
                    this.enabled(false);
                });
                DrillupLabel.enabled(false);

                var sectionPickedUid;
                seatMapSeries.listen('pointClick',function(e){
                    sectionsChart.forEach(function (section) {
                        if(section.uid === e.point.get('id')) {
                            chart.drillTo(e.point.get('id'), section.chart);
                            sectionPickedUid = section.uid;
                            DrillupLabel.enabled(true);
                        }
                    })
                });

                var interactivity = chart.interactivity();
                interactivity.keyboardZoomAndMove(false);
                for(var i = 0; i < document.getElementsByClassName("anychart-credits").length ; i++) {
                    document.getElementsByClassName("anychart-credits")[i].style.display = "none";
                }
                scope.resetPickedSeats = function (seatUids, isReserved, sansSeats) {
                    scope.pickedSeats = [];
                    seatUids.forEach(function (uid) {
                        $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).addClass('noPointerEvents');
                        if(isReserved){
                            $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).css('fill', '#999999')
                        } else {
                            $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).css('fill', 'red')
                        }
                    })
                }


            };
        }
    })
    .directive('seatMapEdit',  function () {
        return {
            link : seatMapDrawEdit,
            controller : function ($scope) {
                var seatMapController = this;
                $scope.pickedSeats = [];
                $scope.svgIndex = 0;
                seatMapController.validationCheckBlitType = function (blitIds, svgIndex) {
                    $scope.$emit("blitIdsChangedEdit", [blitIds, svgIndex]);
                };
                $scope.$on('blitTypeSubmitEdit', function (event, data) {
                    $scope.resetPickedSeats(data[0], data[1], data[2]);
                });
                $scope.$on('blitTypeUidsReset', function (event, data) {
                    $scope.resetPickedSeatsArray();
                });
                $scope.$on('newSVGEdit', function (event, data) {
                    $scope.svgIndex = data[1];
                    $scope.drawSVG(data[0], data[1]);
                });
            },
            restrict : 'E',
            scope : {}
        };
        function seatMapDrawEdit(scope, element, attr, ctrl) {
            scope.drawSVG = function (svgData, svgIndex) {
                var populateSchemaOnce = [];
                if (document.getElementById("seatMaperChart" + svgIndex).childNodes[0]) {
                    var svgElement = document.getElementById("seatMaperChart" + svgIndex);
                    svgElement.removeChild(svgElement.childNodes[0]);
                }
                var chart,sectionsChart = [];
                var seatMapSeries;
                chart = anychart.seatMap();
                chart.geoData(svgData.salonSvg);
                var seatMapData = new Array();
                var wholeSalonData=[];

                for (var sectionIndex = 0; sectionIndex < svgData.schema.sections.length; sectionIndex++) {
                    var rowName = 0;
                    var salonDat={};
                    salonDat.id = svgData.schema.sections[sectionIndex].uid;
                    salonDat.info = svgData.schema.sections[sectionIndex].name;
                    wholeSalonData.push(salonDat);
                    sectionsChart.push({chart: anychart.seatMap(), uid: salonDat.id});

                    var svgSection = svgData.sections.filter(function (section) {
                        if(section.sectionUid === salonDat.id) {
                            return section;
                        }
                    });

                    sectionsChart[sectionIndex].chart.geoData(svgSection[0].sectionSvg);

                    seatMapData[sectionIndex] = new Array();
                    for (var rowIndex = 0; rowIndex < svgData.schema.sections[sectionIndex].rows.length; rowIndex++) {
                        seatMapData[sectionIndex][rowIndex] = new Array();
                        rowName = svgData.schema.sections[sectionIndex].rows[rowIndex].name;
                        svgData.schema.sections[sectionIndex].rows[rowIndex].seats.forEach(function (seat) {

                            seatMapData[sectionIndex][rowIndex].push({
                                id: seat.uid,
                                info: seat.name,
                                value: rowName,
                                price : seat.price
                            });

                        });
                    }
                }
                seatMapSeries=chart.choropleth();
                seatMapSeries.data(wholeSalonData);
                chart.title(svgData.schema.name.toString());
                chart.contextMenu(false);


                var seatClickFunction = function (e) {
                    if (scope.pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        scope.pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', 'green');
                    } else {
                        scope.pickedSeats.splice(scope.pickedSeats.indexOf(e.domTarget.dd), 1);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#64b5f6');
                    }
                    e.preventDefault();
                    ctrl.validationCheckBlitType(scope.pickedSeats, svgIndex);
                };
                var rowCheck = function (section,rowIndex) {
                    for (var i = 0; i < section.rows[rowIndex].seats.length; i++) {
                        if ((scope.pickedSeats.indexOf(section.rows[rowIndex].seats[i].uid) === -1)  && (!$('#'+"seatMaperChart"+svgIndex+' '+'#'+section.rows[rowIndex].seats[i].uid).hasClass('noPointerEvents'))) {
                            return false;
                        }
                    }
                    return true;
                };
                var legendListener = function (e) {
                    var currentSection = svgData.schema.sections.filter(function (section) {
                        if(section.uid === sectionPickedUid) {
                            return section;
                        }
                    });
                    if (rowCheck(currentSection[0],e.itemIndex)) {
                        currentSection[0].rows[e.itemIndex].seats.forEach(function (seat) {
                            if(!$('#'+"seatMaperChart"+svgIndex+' '+'#'+seat.uid).hasClass('noPointerEvents')) {
                                $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#64b5f6');
                                scope.pickedSeats.splice(scope.pickedSeats.indexOf(seat.uid), 1);
                            }
                        });
                    } else {
                        currentSection[0].rows[e.itemIndex].seats.forEach(function (seat) {
                            if ((scope.pickedSeats.indexOf(seat.uid) === -1) && (!$('#'+"seatMaperChart"+svgIndex+' '+'#'+seat.uid).hasClass('noPointerEvents'))) {
                                $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'green');
                                scope.pickedSeats.push(seat.uid);
                            }
                        });
                    }
                    e.preventDefault();
                    ctrl.validationCheckBlitType(scope.pickedSeats, svgIndex);
                };
                var palette = anychart.palettes.rangeColors();
                palette.items(["#64b5f6"]);

                for (var sectionIndex = 0; sectionIndex < svgData.schema.sections.length; sectionIndex++) {
                    palette.count(svgData.schema.sections[sectionIndex].numberOfRows);
                    sectionsChart[sectionIndex].chart.palette(palette);
                    chart.palette(palette);

                    for (var rowIndex = 0; rowIndex < svgData.schema.sections[sectionIndex].rows.length; rowIndex++) {
                        var rowSeats = [];
                        seatMapData[sectionIndex][rowIndex].forEach(function (seat) {
                            if (seat.value === svgData.schema.sections[sectionIndex].rows[rowIndex].name) {
                                rowSeats.push(seat);
                            }
                        });
                        sectionsChart[sectionIndex].chart.choropleth(rowSeats)
                            .name(svgData.schema.sections[sectionIndex].rows[rowIndex].name)
                            .listen('click', seatClickFunction)
                    }


                    var legend = sectionsChart[sectionIndex].chart.legend();
                    legend.enabled(true)
                        .position('right')
                        .itemsLayout('vertical')
                        .removeAllListeners()
                    ;


                    legend.listen("click", legendListener);

                    sectionsChart[sectionIndex].chart.labels(true);
                    var labels = sectionsChart[sectionIndex].chart.labels();

                    sectionsChart[sectionIndex].chart.labels({fontSize: 10});
                    labels.format("{%info}");
                    var toolTip = sectionsChart[sectionIndex].chart.tooltip();
                    toolTip.format("{%price}")
                }
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();
                var sectionPickedUid;
                seatMapSeries.listen('pointClick',function(e){
                    sectionsChart.forEach(function (section) {
                        if(section.uid === e.point.get('id')) {
                            chart.drillTo(e.point.get('id'), section.chart);
                            sectionPickedUid = section.uid;
                            if(populateSchemaOnce.indexOf(section.uid) === -1) {
                                populateSchemaOnce.push(section.uid);
                                populatedSalon(section);
                            }
                        }
                    })
                });
                for(var i = 0; i < document.getElementsByClassName("anychart-credits").length ; i++) {
                    document.getElementsByClassName("anychart-credits")[i].style.display = "none";
                }


                scope.resetPickedSeats = function (seatUids, isReserved, sansSeats) {
                    scope.pickedSeats = [];
                    seatUids.forEach(function (uid) {
                        $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).addClass('noPointerEvents');
                        $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).css('fill', 'yellow')

                    })
                };
                scope.resetPickedSeatsArray = function () {
                    scope.pickedSeats = [];
                };
                var populatedSalon = function (section) {
                    svgData.schema.sections.forEach(function (sect) {
                        if(sect.uid === section.uid) {
                            sect.rows.forEach(function (row) {
                                row.seats.forEach(function (seat) {
                                    switch (seat.state) {
                                        case "RESERVED" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'blue');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "SOLD" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'orange');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "GUEST_NOT_AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#333');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "NOT_AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#999999');
                                            break;
                                        case "AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'red');
                                            break;
                                        default :
                                            break;
                                    }
                                })
                            });
                        }
                    })
                }

            };
        }
    })
    .directive('seatMapBuyTicket',  function () {
        return {
            link : seatMapDraw,
            controller : function ($scope,$timeout) {
                var seatMapController = this;
                $scope.pickedSeats = [];
                seatMapController.validationCheckBlitType = function (blitIds, svgIndex) {
                    $scope.$emit("blitIdsChangedBuyTicket", [blitIds, svgIndex]);
                };
                $scope.$on('blitTypeUidsReset', function (event, data) {
                    $scope.resetPickedSeatsArray();
                });
                $scope.$on('newSVGBuyTicket', function (event, data) {
                    $scope.drawSVG(data[0], data[1]);
                });
            },
            restrict : 'E',
            scope : {}
        };
        function seatMapDraw(scope, element, attr, ctrl) {
            scope.drawSVG = function (svgData, svgIndex) {
                var populateSchemaOnce = [];
                if (document.getElementById("seatMaperChart" + svgIndex).childNodes[0]) {
                    var svgElement = document.getElementById("seatMaperChart" + svgIndex);
                    svgElement.removeChild(svgElement.childNodes[0]);
                }
                var chart,sectionsChart = [];
                var seatMapSeries;
                chart = anychart.seatMap();
                chart.geoData(svgData.salonSvg);
                var seatMapData = new Array();
                var wholeSalonData=[];

                for (var sectionIndex = 0; sectionIndex < svgData.schema.sections.length; sectionIndex++) {
                    var rowName = 0;
                    var salonDat={};
                    salonDat.id = svgData.schema.sections[sectionIndex].uid;
                    salonDat.info = svgData.schema.sections[sectionIndex].name;
                    wholeSalonData.push(salonDat);
                    sectionsChart.push({chart: anychart.seatMap(), uid: salonDat.id});

                    var svgSection = svgData.sections.filter(function (section) {
                        if(section.sectionUid === salonDat.id) {
                            return section;
                        }
                    });

                    sectionsChart[sectionIndex].chart.geoData(svgSection[0].sectionSvg);

                    seatMapData[sectionIndex] = new Array();
                    for (var rowIndex = 0; rowIndex < svgData.schema.sections[sectionIndex].rows.length; rowIndex++) {
                        seatMapData[sectionIndex][rowIndex] = new Array();
                        rowName = svgData.schema.sections[sectionIndex].rows[rowIndex].name;
                        svgData.schema.sections[sectionIndex].rows[rowIndex].seats.forEach(function (seat) {

                            seatMapData[sectionIndex][rowIndex].push({
                                id: seat.uid,
                                info: seat.name,
                                value: rowName,
                                price : seat.price
                            });

                        });
                    }
                }
                seatMapSeries=chart.choropleth();
                seatMapSeries.data(wholeSalonData);
                chart.title(svgData.schema.name.toString());
                chart.contextMenu(false);


                var seatClickFunction = function (e) {
                    if (scope.pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        scope.pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', 'green');
                    } else {
                        scope.pickedSeats.splice(scope.pickedSeats.indexOf(e.domTarget.dd), 1);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#64b5f6');
                    }
                    e.preventDefault();
                    ctrl.validationCheckBlitType(scope.pickedSeats, svgIndex);
                };


                var palette = anychart.palettes.rangeColors();
                palette.items(["#64b5f6"]);

                for (var sectionIndex = 0; sectionIndex < svgData.schema.sections.length; sectionIndex++) {
                    palette.count(svgData.schema.sections[sectionIndex].numberOfRows);
                    sectionsChart[sectionIndex].chart.palette(palette);
                    chart.palette(palette);

                    for (var rowIndex = 0; rowIndex < svgData.schema.sections[sectionIndex].rows.length; rowIndex++) {
                        var rowSeats = [];
                        seatMapData[sectionIndex][rowIndex].forEach(function (seat) {
                            if (seat.value === svgData.schema.sections[sectionIndex].rows[rowIndex].name) {
                                rowSeats.push(seat);
                            }
                        });
                        sectionsChart[sectionIndex].chart.choropleth(rowSeats)
                            .name(svgData.schema.sections[sectionIndex].rows[rowIndex].name)
                            .listen('click', seatClickFunction)
                    }


                    var legend = sectionsChart[sectionIndex].chart.legend();
                    legend.enabled(true)
                        .position('right')
                        .itemsLayout('vertical')
                        .removeAllListeners()
                    ;



                    sectionsChart[sectionIndex].chart.labels(true);
                    var labels = sectionsChart[sectionIndex].chart.labels();

                    sectionsChart[sectionIndex].chart.labels({fontSize: 10});
                    labels.format("{%info}");
                    var toolTip = sectionsChart[sectionIndex].chart.tooltip();
                    toolTip.format("{%price}")
                }
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();
                var sectionPickedUid;
                seatMapSeries.listen('pointClick',function(e){
                    sectionsChart.forEach(function (section) {
                        if(section.uid === e.point.get('id')) {
                            chart.drillTo(e.point.get('id'), section.chart);
                            sectionPickedUid = section.uid;
                            if(populateSchemaOnce.indexOf(section.uid) === -1) {
                                populateSchemaOnce.push(section.uid);
                                populatedSalon(section);
                            }
                        }
                    })
                });
                for(var i = 0; i < document.getElementsByClassName("anychart-credits").length ; i++) {
                    document.getElementsByClassName("anychart-credits")[i].style.display = "none";
                }
                scope.resetPickedSeatsArray = function () {
                    scope.pickedSeats = [];
                };

                var populatedSalon = function (section) {
                    svgData.schema.sections.forEach(function (sect) {
                        if(sect.uid === section.uid) {
                            sect.rows.forEach(function (row) {
                                row.seats.forEach(function (seat) {
                                    switch (seat.state) {
                                        case "RESERVED" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'blue');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "SOLD" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'orange');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "GUEST_NOT_AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#333');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "NOT_AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#999999');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#64b5f6');
                                            break;
                                        default :
                                            break;
                                    }
                                })
                            });
                        }
                    })
                }

            };
        }
    })
