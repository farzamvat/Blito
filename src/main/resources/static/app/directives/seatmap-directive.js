/**
 * Created by soroush on 10/25/17.
 */
angular.module('blitoDirectives')
    .directive('seatMap',  function () {
        return {
            link : seatMapDraw,
            controller : function ($scope) {
                var seatMapController = this;
                seatMapController.validationCheckBlitType = function (blitIds, svgIndex) {
                    $scope.$emit("blitIdsChanged", [blitIds, svgIndex]);
                };
                $scope.$on('blitTypeSubmit', function (event, data) {
                    $scope.resetPickedSeats(data);
                });
                $scope.$on('newSVG', function (event, data) {
                    console.log(data);
                    $scope.drawSVG(data[0], data[1]);
                });

            },
            restrict : 'E',
            scope : {
                salonSchema : '='
            }
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
                    console.log(svgSection);

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
                // svgData.schema.sections.forEach(function (section) {
                //     var rowName = 0;
                //     seatMapData[svgData.schema.sections.getIndex] = new Array();
                //     console.log(svgData.schema.sections.itemIndex);
                //     section.rows.forEach(function (row) {
                //         rowName = parseInt(row.name);
                //         row.seats.forEach(function (seat) {
                //             seatMapData[svgData.schema.sections.getIndex][row.getIndex].push({ id : seat.uid, info : seat.name, value : rowName});
                //         });
                //     });
                // });
                var pickedSeats = [];
                var seatClickFunction = function (e) {
                    if (pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        pickedSeats.push(e.domTarget.dd);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', 'green');
                    } else {
                        pickedSeats.splice(pickedSeats.indexOf(e.domTarget.dd), 1);
                        $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + e.domTarget.dd).css('fill', '#64b5f6');
                    }
                    ctrl.validationCheckBlitType(pickedSeats, svgIndex);
                };
                var rowCheck = function (section,rowIndex) {
                    for (var i = 0; i < section.rows[rowIndex].seats.length; i++) {
                        if (pickedSeats.indexOf(section.rows[rowIndex].seats[i].uid) === -1) {
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
                    console.log(currentSection[0]);
                    if (rowCheck(currentSection[0],e.itemIndex)) {
                        currentSection[0].rows[e.itemIndex].seats.forEach(function (seat) {
                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#64b5f6');
                            pickedSeats.splice(pickedSeats.indexOf(seat.uid), 1);
                        });
                    } else {
                        currentSection[0].rows[e.itemIndex].seats.forEach(function (seat) {
                            if (pickedSeats.indexOf(seat.uid) === -1) {
                                $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'green');
                                pickedSeats.push(seat.uid);
                            }
                        });
                    }
                    ctrl.validationCheckBlitType(pickedSeats);
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
                        console.log(rowSeats);
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
                document.getElementsByClassName("anychart-credits")[svgIndex].style.display = "none";

                scope.resetPickedSeats = function (seatUids) {
                    pickedSeats = [];
                    seatUids.forEach(function (uid) {
                        document.getElementById(uid).style.fill = "red";
                    })
                }

            };
        }
    });