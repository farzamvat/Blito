/**
 * Created by soroush on 10/25/17.
 */
angular.module('blitoDirectives')
    .directive('seatMap',  function () {
        return {
            link : seatMapDraw,
            controller : function ($scope) {
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

                var removeAnychartLogo = function () {
                    for(var i = 0; i < document.getElementsByClassName("anychart-credits").length ; i++) {
                        document.getElementsByClassName("anychart-credits")[i].style.display = "none";
                    }
                    if(document.getElementById("labelDrillUpSection" + svgIndex).children[1]) {
                        document.getElementById("labelDrillUpSection" + svgIndex).children[0].style.display = 'none';
                    }
                };
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
                    removeAnychartLogo();
                    var label = anychart.standalones.label();
                    label.background({fill: "#9E9E9E"});
                    label.text(text);
                    label.fontColor("#fff");
                    label.padding(15);
                    label.background().cornerType("round");
                    label.background().corners(5);
                    label.listen("click", action);
                    label.container(stage);
                    label.draw();
                    return label;
                };
                document.getElementById('seatMaperChart'+svgIndex).addEventListener('touchend', touchSS);

                var seatClickFunction = function (f) {
                    var e=f.originalEvent;
                    if (scope.pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        scope.pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#39A939');
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
                                $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#39A939');
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
                        section.listen('pointClick', seatClickFunction);
                        sectionsChart[sectionIndex].chart.contextMenu(false);

                    }

                    var legend = sectionsChart[sectionIndex].chart.legend();
                    legend.enabled(true)
                        .position('right')
                        .title("انتخاب ردیف")
                        .itemsLayout('vertical')
                        .removeAllListeners()
                    ;

                    legend.title({fontSize:20});
                    legend.listen("click", legendListener);
                    legend.listen("touchstart", legendListener);
                    legend.itemsSpacing(0);
                    legend.iconSize(10);
                    legend.margin(0,0,0,50);
                    sectionsChart[sectionIndex].chart.labels(true);
                    var labels = sectionsChart[sectionIndex].chart.labels();


                    sectionsChart[sectionIndex].chart.labels({fontSize: 10});
                    labels.format("{%info}");
                    var toolTip = sectionsChart[sectionIndex].chart.tooltip().enabled(false);
                    toolTip.format("{%price}")
                }
                chart.bounds(0, '10%', '100%', '90%');
                chart.tooltip().enabled(false);
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();

                var DrillupLabel= createDrillUpLabel( "بازگشت" , 0, "labelDrillUpSection"+svgIndex ,function(){
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
                removeAnychartLogo();
                // var interactivity = chart.interactivity();
                // interactivity.keyboardZoomAndMove(false);
                scope.resetPickedSeats = function (seatUids, isReserved, sansSeats) {
                    scope.pickedSeats = [];
                    seatUids.forEach(function (uid) {
                        $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).addClass('noPointerEvents');
                        if(isReserved){
                            $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).css('fill', '#999999')
                        } else {
                            $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).css('fill', '#FCB731')
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
                var removeAnychartLogo = function () {
                    for(var i = 0; i < document.getElementsByClassName("anychart-credits").length ; i++) {
                        document.getElementsByClassName("anychart-credits")[i].style.display = "none";
                    }
                    if(document.getElementById("labelDrillUpSection" + svgIndex).children[1]) {
                        document.getElementById("labelDrillUpSection" + svgIndex).children[0].style.display = 'none';
                    }
                };
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
                var touchSS = function(e){
                    e.preventDefault();
                };
                function createDrillUpLabel(text, offset, stage , action ){
                    var label = anychart.standalones.label();
                    label.background({fill: "#9E9E9E"});
                    label.text(text);
                    label.fontColor("#fff");
                    label.padding(15);
                    label.background().cornerType("round")
                    label.background().corners(5)
                    label.listen("click", action);
                    label.container(stage);
                    label.draw();
                    return label;
                };
                document.getElementById('seatMaperChart'+svgIndex).addEventListener('touchend', touchSS);

                var seatClickFunction = function (f) {
                    var e=f.originalEvent;
                    if (scope.pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        scope.pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#39A939');
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
                                $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#39A939');
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
                        section.listen('pointClick', seatClickFunction);
                    }


                    var legend = sectionsChart[sectionIndex].chart.legend();
                    legend.enabled(true)
                        .position('right')
                        .title("انتخاب ردیف")
                        .itemsLayout('vertical')
                        .removeAllListeners()
                    ;


                    legend.listen("click", legendListener);
                    legend.listen('touchstart', seatClickFunction);
                    legend.itemsSpacing(0);
                    legend.iconSize(10);
                    legend.margin(0,0,0,50);

                    sectionsChart[sectionIndex].chart.labels(true);
                    var labels = sectionsChart[sectionIndex].chart.labels();

                    sectionsChart[sectionIndex].chart.labels({fontSize: 10});
                    labels.format("{%info}");
                    var toolTip = sectionsChart[sectionIndex].chart.tooltip().enabled(true);
                    toolTip.title("قیمت")
                    toolTip.format("{%price}")
                }
                chart.tooltip().enabled(false);
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();
                var DrillupLabel= createDrillUpLabel( "بازگشت" , 0, "labelDrillUpSection"+svgIndex ,function(){
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
                            if(populateSchemaOnce.indexOf(section.uid) === -1) {
                                populateSchemaOnce.push(section.uid);
                                populatedSalon(section);
                            }
                        }
                    })
                });

                removeAnychartLogo();
                // var interactivity = chart.interactivity();
                // interactivity.keyboardZoomAndMove(false);

                scope.resetPickedSeats = function (seatUids, isReserved, sansSeats) {
                    scope.pickedSeats = [];
                    seatUids.forEach(function (uid) {
                        $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).addClass('noPointerEvents');
                        $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).css('fill', '#FCB731')

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
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#234BA1');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "SOLD" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#801515');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "GUEST_NOT_AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#333333');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "NOT_AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#999999');
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
                var removeAnychartLogo = function () {
                    for(var i = 0; i < document.getElementsByClassName("anychart-credits").length ; i++) {
                        document.getElementsByClassName("anychart-credits")[i].style.display = "none";
                    }
                    if(document.getElementById("labelDrillUpSection" + svgIndex).children[1]) {
                        document.getElementById("labelDrillUpSection" + svgIndex).children[0].style.display = 'none';
                    }
                };
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
                var touchSS = function(e){
                    e.preventDefault();
                };
                function createDrillUpLabel(text, offset, stage , action ){
                    removeAnychartLogo();
                    var label = anychart.standalones.label();
                    label.background({fill: "#9E9E9E"});
                    label.text(text);
                    label.fontColor("#fff");
                    label.padding(15);
                    label.listen("click", action);
                    label.background().cornerType("round")
                    label.background().corners(5)
                    label.container(stage);
                    label.draw();
                    return label;
                };
                document.getElementById('seatMaperChart'+svgIndex).addEventListener('touchend', touchSS);
                var toolTip = $("<div class='custom-tooltip'></div>").css({
                    "position": "absolute",
                    "pointerEvents": "none",
                    "width": "100px",
                    "background-color": "rgba(50, 50, 50, 0.7)",
                    "padding": "4px",
                    "color": "white",
                    "border-radius": "3px",
                    "border": "solid black 2px",
                    "display": "none"
                });
                var clientX , clientY;
                var initToolTip = function (e) {
                    var $container = $(this.container().getStage().container());
                    if (!$container.find('.custom-tooltip').length) {
                        $container.append(toolTip);
                    }
                };
                chart.listen("mouseMove", initToolTip);
                chart.listen("touchmove", initToolTip);

                //neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeew
                var seatClickFunction = function (e) {
                    if (scope.pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        scope.pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#39A939');
                    } else {
                        scope.pickedSeats.splice(scope.pickedSeats.indexOf(e.domTarget.dd), 1);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#64b5f6');
                    }
                    e.preventDefault();
                    ctrl.validationCheckBlitType(scope.pickedSeats, svgIndex);

                };
                var setToolTip = function (e) {
                    clientX = e.iF.b.Xw.left;
                    clientY = e.iF.b.Xw.top;
                    toolTip.css({"display": "block"});
                    svgData.schema.sections.forEach(function (sect) {
                            sect.rows.forEach(function (row, rowIndex) {
                                row.seats.forEach(function (seat) {
                                    if(e.domTarget.dd === seat.uid) {
                                        toolTip.html("قیمت: "+seat.price+"\n"+"ردیف: "+(rowIndex+1));
                                    }
                                })
                            });
                    });
                    toolTip.css({"left": clientX - 50, "top": clientY + toolTypeYOffset, "z-index": 99999});
                };
                var seatTouch = function (e) {
                    setToolTip(e);
                    seatClickFunction(e);
                };
                var mouseOverSeat = function (e) {
                    setToolTip(e);
                };
                var mouseOutSeat = function () {
                    toolTip.css("display","none");
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
                        //neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeew
                        section.listen('click', seatClickFunction);
                        section.listen('touchstart', seatTouch);
                        section.listen('mouseOver', mouseOverSeat);
                        section.listen('mouseOut', mouseOutSeat);
                        // section.listen('click', seatClickFunction);
                    }


                    var legend = sectionsChart[sectionIndex].chart.legend();
                    legend.enabled(false);


                    sectionsChart[sectionIndex].chart.labels(true);
                    var labels = sectionsChart[sectionIndex].chart.labels();
                    var toolTypeYOffset;
                    if($(window).width() < 1000) {
                        toolTypeYOffset = 10;
                        sectionsChart[sectionIndex].chart.labels({fontSize: 6});
                    } else {
                        toolTypeYOffset = 25;
                        sectionsChart[sectionIndex].chart.labels({fontSize: 10});
                    }
                    labels.format("{%info}");
                    //neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeew
                    sectionsChart[sectionIndex].chart.tooltip().enabled(false);

                }
                //neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeew
                chart.tooltip().enabled(false);
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();
                var DrillupLabel= createDrillUpLabel( "بازگشت" , 0, "labelDrillUpSection"+svgIndex ,function(){
                    chart.drillUp();
                    toolTip.css("display","none");
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
                            if(populateSchemaOnce.indexOf(section.uid) === -1) {
                                populateSchemaOnce.push(section.uid);
                                populatedSalon(section);

                            }
                        }
                    })
                });

                removeAnychartLogo();
                //neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeew
                // var interactivity = chart.interactivity();
                // interactivity.keyboardZoomAndMove(false);

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
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#234BA1');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "SOLD" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#801515');
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                            break;
                                        case "GUEST_NOT_AVAILABLE" :
                                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#999999');
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
    .directive('seatMapGenerateTicket',  function () {
        return {
            link : seatMapDraw,
            controller : function ($scope) {
                var seatMapController = this;
                $scope.pickedSeats = [];
                seatMapController.validationCheckBlitType = function (blitIds, svgIndex) {
                    $scope.$emit("blitIdsChangedGenerateTicket", [blitIds, svgIndex]);
                };
                $scope.$on('resetGuestListPicked', function (event, data) {
                    $scope.resetGuestList();
                });
                $scope.$on('newSVGGenrateTicket', function (event, data) {
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
                var removeAnychartLogo = function () {
                    for(var i = 0; i < document.getElementsByClassName("anychart-credits").length ; i++) {
                        document.getElementsByClassName("anychart-credits")[i].style.display = "none";
                    }
                    if(document.getElementById("labelDrillUpSection" + svgIndex).children[1]) {
                        document.getElementById("labelDrillUpSection" + svgIndex).children[0].style.display = 'none';
                    }
                };
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
                var touchSS = function(e){
                    e.preventDefault();
                };
                function createDrillUpLabel(text, offset, stage , action ){
                    removeAnychartLogo();
                    var label = anychart.standalones.label();
                    label.background({fill: "#9E9E9E"});
                    label.text(text);
                    label.fontColor("#fff");
                    label.padding(15);
                    label.background().cornerType("round")
                    label.background().corners(5)
                    label.listen("click", action);
                    label.container(stage);
                    label.draw();
                    return label;
                };
                document.getElementById('seatMaperChart'+svgIndex).addEventListener('touchend', touchSS);

                var seatClickFunction = function (f) {
                    var e=f.originalEvent;
                    if (scope.pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        scope.pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#39A939');
                    } else {
                        scope.pickedSeats.splice(scope.pickedSeats.indexOf(e.domTarget.dd), 1);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#64b5f6');
                    }
                    e.preventDefault();
                    ctrl.validationCheckBlitType(scope.pickedSeats, svgIndex);
                };
                scope.resetGuestList = function () {
                    if(scope.pickedSeats.length > 0) {
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + scope.pickedSeats[0]).addClass('noPointerEvents');
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + scope.pickedSeats[0]).css('fill', '#333');
                        scope.pickedSeats.pop();
                    }
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
                        section.listen('pointClick', seatClickFunction);

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
                    var toolTip = sectionsChart[sectionIndex].chart.tooltip().enabled(false);
                    toolTip.format("{%price}")
                }
                chart.tooltip().enabled(false);
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();
                var DrillupLabel= createDrillUpLabel( "بازگشت" , 0, "labelDrillUpSection"+svgIndex ,function(){
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
                            if(populateSchemaOnce.indexOf(section.uid) === -1) {
                                populateSchemaOnce.push(section.uid);
                                populatedSalon(section);
                            }
                        }
                    })
                });

                removeAnychartLogo();
                // var interactivity = chart.interactivity();
                // interactivity.keyboardZoomAndMove(false);

                scope.resetPickedSeats = function (seatUids, isReserved, sansSeats) {
                    scope.pickedSeats = [];
                    seatUids.forEach(function (uid) {
                        $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).addClass('noPointerEvents');
                        $('#'+"seatMaperChart"+sansSeats+' '+'#'+uid).css('fill', 'yellow')

                    })
                };
                var populatedSalon = function (section) {
                    svgData.schema.sections.forEach(function (sect) {
                        if(sect.uid === section.uid) {
                            sect.rows.forEach(function (row) {
                                row.seats.forEach(function (seat) {
                                    if(seat.state === "NOT_AVAILABLE") {
                                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#64b5f6');
                                    } else if (seat.state === "SOLD") {
                                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#801515');
                                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                    } else {
                                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#999');
                                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).addClass('noPointerEvents');
                                    }
                                })
                            });
                        }
                    })
                }

            };
        }
    })
