/**
 * Created by soroush on 10/25/17.
 */
angular.module('blitoDirectives')
    .directive('seatMap',  function () {
        return {
            link : seatMapDraw,
            controller : function ($scope,seatmapService) {
                var seatMapController = this;
                seatMapController.validationCheckBlitType = function (blitIds, svgIndex) {
                        $scope.$emit("blitIdsChanged", [blitIds, svgIndex]);
                };
                $scope.$on('blitTypeSubmit', function (event, data) {
                    $scope.resetPickedSeats(data);
                });
                $scope.$on('newSVG', function (event, data) {
                    console.log(data);
                    $scope.drawSVG(data[0], data[1], data[2]);
                });
                seatMapController.getSVGImageDirective = function (svgName) {
                    return seatmapService.getSvgImage(svgName);
                }
            },
            restrict : 'E',
            scope : {
                salonSchema : '=',
                salonImage : '='
            }
        };
        function seatMapDraw(scope, element, attr, ctrl) {
            scope.drawSVG = function (svgData, svgImage, svgIndex) {
                var directiveInputs = [];
                directiveInputs[0] = svgData;
                directiveInputs[1] = svgImage;
                console.log(directiveInputs);
                if (document.getElementById("seatMaperChart" + svgIndex).childNodes[0]) {
                    var svgElement = document.getElementById("seatMaperChart" + svgIndex);
                    svgElement.removeChild(svgElement.childNodes[0]);
                }
                var chart,sectionsChart=[];
                var seatMapSeries;
                var svg = directiveInputs[1];
                chart=anychart.seatMap();
                chart.geoData(svg);
                var seatMapData = new Array();
                var wholeSalonData=[];
                for (var sectionIndex = 0; sectionIndex < directiveInputs[0].sections.length; sectionIndex++) {
                    var rowName = 0;
                    var salonDat={};
                    salonDat.id=directiveInputs[0].sections[sectionIndex].uid;
                    salonDat.info=directiveInputs[0].sections[sectionIndex].name;
                    wholeSalonData.push(salonDat);
                    sectionsChart[sectionIndex] = anychart.seatMap();
                    var sectionSvg;
                    console.log(sectionsChart[sectionIndex]);
                    console.log("siaaaaa");
                    ctrl.getSVGImageDirective('test'+(sectionIndex+1))
                        .then(function (data) {
                            sectionSvg = data.data;
                            console.log(sectionIndex);
                            console.log(sectionsChart);
                            sectionsChart[sectionIndex].geoData(sectionSvg);
                        })

                    seatMapData[sectionIndex] = new Array();
                    for (var rowIndex = 0; rowIndex < directiveInputs[0].sections[sectionIndex].rows.length; rowIndex++) {
                        seatMapData[sectionIndex][rowIndex] = new Array();
                        rowName = parseInt(directiveInputs[0].sections[sectionIndex].rows[rowIndex].name);
                        directiveInputs[0].sections[sectionIndex].rows[rowIndex].seats.forEach(function (seat) {
                            seatMapData[sectionIndex][rowIndex].push({id: seat.uid, info: seat.name, value: rowName});
                        });
                    }
                }
                // directiveInputs[0].sections.forEach(function (section) {
                //     var rowName = 0;
                //     seatMapData[directiveInputs[0].sections.getIndex] = new Array();
                //     console.log(directiveInputs[0].sections.itemIndex);
                //     section.rows.forEach(function (row) {
                //         rowName = parseInt(row.name);
                //         row.seats.forEach(function (seat) {
                //             seatMapData[directiveInputs[0].sections.getIndex][row.getIndex].push({ id : seat.uid, info : seat.name, value : rowName});
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
                var palette = anychart.palettes.rangeColors();
                palette.items(["#64b5f6"]);
                var rowCheck = function (sectionIndex,rowIndex) {
                    for (var i = 0; i < directiveInputs[0].sections[sectionIndex].rows[rowIndex].seats.length; i++) {
                        if (pickedSeats.indexOf(directiveInputs[0].sections[sectionIndex].rows[rowIndex].seats[i].uid) === -1) {
                            return false;
                        }
                    }
                    return true;
                };
                for (var sectionIndex = 0; sectionIndex < directiveInputs[0].sections.length; sectionIndex++) {
                    palette.count(directiveInputs[0].sections[sectionIndex].numberOfRows);
                    sectionsChart[sectionIndex].palette(palette);
                    chart.palette(palette);
                    for (var rowIndex = 0; rowIndex < directiveInputs[0].sections[sectionIndex].rows.length; rowIndex++) {
                        var rowSeats = [];
                        seatMapData[sectionIndex][rowIndex].forEach(function (seat) {
                            if (seat.value === directiveInputs[0].sections[sectionIndex].rows.name) {
                                rowSeats.push(seat);
                            }
                        });
                        sectionsChart[sectionIndex].choropleth(rowSeats)
                            .name("name")
                            .listen('click', seatClickFunction)
                    }

                    if (sectionIndex === 0) {
                        seatMapSeries=chart.choropleth();
                        seatMapSeries.data(wholeSalonData);
                        chart.title(directiveInputs[0].name.toString());
                        chart.contextMenu(false);
                    }
                    else {
                    var legend = sectionsChart[sectionIndex].legend();
                    legend.enabled(true)
                        .position('right')
                        .itemsLayout('vertical')
                        .removeAllListeners()
                    ;


                legend.listen("click", function (e) {
                    if (rowCheck(sectionIndex,e.itemIndex)) {

                        directiveInputs[0].sections[sectionIndex].rows[e.itemIndex].seats.forEach(function (seat) {
                            $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', '#64b5f6');
                            pickedSeats.splice(pickedSeats.indexOf(seat.uid), 1);
                        })
                    } else {
                        directiveInputs[0].sections[sectionIndex].rows[e.itemIndex].seats.forEach(function (seat) {
                            if (pickedSeats.indexOf(seat.uid) === -1) {
                                $('#' + "seatMaperChart" + svgIndex + ' ' + '#' + seat.uid).css('fill', 'green');
                                pickedSeats.push(seat.uid);
                            }
                        })
                    }
                    ctrl.validationCheckBlitType(pickedSeats);
                });
                    }
                    sectionsChart[sectionIndex].labels(true);
                    var labels = sectionsChart[sectionIndex].labels();

                    sectionsChart[sectionIndex].labels({fontSize: 10});
                    labels.format("{%info}");
            }
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();
                seatMapSeries.listen('pointClick',function(e){
                    // console.log(e.point.get('id'));
                        if(e.point.get('id')== "1")
                            chart.drillTo(e.point.get('id'),sectionsChart[0]);
                        if(e.point.get('id')== "2")
                            chart.drillTo(e.point.get('id'),sectionsChart[1]);
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