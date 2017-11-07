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
                            seatMapData[sectionIndex][rowIndex].push({id: seat.uid, info: seat.name, value: rowName});
                        });
                    }
                }
                seatMapSeries=chart.choropleth();
                seatMapSeries.data(wholeSalonData);
                chart.title(svgData.schema.name.toString());
                chart.contextMenu(false);

                // scope.pickedSeats = [];
                var seatClickFunction = function (e) {
                    console.log(svgIndex);
                    if (scope.pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        scope.pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', 'green');
                    } else {
                        scope.pickedSeats.splice(scope.pickedSeats.indexOf(e.domTarget.dd), 1);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#64b5f6');
                    }
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
                    console.log(scope.svgIndex);
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
                }
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();
                var sectionPickedUid;
                seatMapSeries.listen('pointClick',function(e){
                    sectionsChart.forEach(function (section) {
                        if(section.uid === e.point.get('id')) {
                            chart.drillTo(e.point.get('id'), section.chart);
                            sectionPickedUid = section.uid;
                        }
                    })
                });
                document.getElementsByClassName("anychart-credits")[0].style.display = "none";

                scope.resetPickedSeats = function (seatUids, isReserved, sansSeats) {
                    scope.pickedSeats = [];
                    console.log(scope.pickedSeats);
                    console.log(seatUids);
                    console.log(sansSeats);
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
    });