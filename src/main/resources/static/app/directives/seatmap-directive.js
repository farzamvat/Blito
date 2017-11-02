/**
 * Created by soroush on 10/25/17.
 */
angular.module('blitoDirectives')
    .directive('seatMap',  function () {
        return {
            link : seatMapDraw,
            controller : function ($scope) {
                var seatMapController = this;
                seatMapController.validationCheckBlitType = function (blitIds) {
                    if(blitIds.length === 0) {
                        $scope.$emit("blitIdsChanged", [false, blitIds]);

                    } else {
                        $scope.$emit("blitIdsChanged", [true, blitIds]);
                    }
                };
                $scope.$on('blitTypeSubmit', function (event, data) {
                    $scope.resetPickedSeats(data);
                });
                $scope.$on('newSVG', function (event, data) {
                    console.log(data);
                    $scope.drawSVG(data[0], data[1], data[2]);
                });

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

                if(document.getElementById("seatMaperChart"+svgIndex).childNodes[0]) {
                    var svgElement = document.getElementById("seatMaperChart"+svgIndex);
                    svgElement.removeChild(svgElement.childNodes[0]);
                }
                var chart = anychart.seatMap();
                var svg = directiveInputs[1];
                chart.geoData(svg);
                var seatMapData = [];
                var rowName = 0;
                directiveInputs[0].sections.forEach(function (section) {
                    section.rows.forEach(function (row) {
                        rowName = parseInt(row.name);
                        row.seats.forEach(function (seat) {
                            seatMapData.push({ id : seat.uid, info : seat.name, value : rowName});
                        });
                    });
                });
                var pickedSeats = [];
                var seatClickFunction = function (e) {
                    if (pickedSeats.indexOf(e.domTarget.dd) === -1) {
                        pickedSeats.push(e.domTarget.dd);
                        $('#'+"seatMaperChart"+svgIndex+' '+'#'+e.domTarget.dd).css('fill', 'green');
                    } else {
                        pickedSeats.splice(pickedSeats.indexOf(e.domTarget.dd), 1);
                        $('#'+"seatMaperChart"+svgIndex+' '+'#'+e.domTarget.dd).css('fill', '#64b5f6');
                    }
                    ctrl.validationCheckBlitType(pickedSeats);
                };
                var palette = anychart.palettes.rangeColors();
                palette.items(["#64b5f6"]);
                palette.count(directiveInputs[0].sections[0].numberOfRows);
                chart.palette(palette);
                for(var i = 1; i <= rowName ; i++) {
                    var rowSeats = [];
                    seatMapData.forEach(function (seat) {
                        if(seat.value == i) {
                            rowSeats.push(seat);
                        }
                    });
                    chart.choropleth(rowSeats)
                        .name(i)
                        .listen('click', seatClickFunction)
                }
                chart.labels(true);
                var labels = chart.labels();

                chart.labels({fontSize: 10});
                labels.format("{%info}");

                chart.title(directiveInputs[0].name.toString());
                chart.contextMenu(false);

                var legend = chart.legend();
                legend.enabled(true)
                    .position('right')
                    .itemsLayout('vertical')
                    .removeAllListeners()
                ;
                var rowCheck = function (rowIndex) {
                    for(var i = 0; i < directiveInputs[0].sections[0].rows[rowIndex].seats.length; i++) {
                        if(pickedSeats.indexOf(directiveInputs[0].sections[0].rows[rowIndex].seats[i].uid) === -1) {
                            return false;
                        }
                    }
                    return true;
                };
                legend.listen("click", function(e) {
                    if(rowCheck(e.itemIndex)) {
                        directiveInputs[0].sections[0].rows[e.itemIndex].seats.forEach(function (seat) {
                            $('#'+"seatMaperChart"+svgIndex+' '+'#'+seat.uid).css('fill', '#64b5f6');
                            pickedSeats.splice(pickedSeats.indexOf(seat.uid), 1);
                        })
                    } else {
                        directiveInputs[0].sections[0].rows[e.itemIndex].seats.forEach(function (seat) {
                            if(pickedSeats.indexOf(seat.uid) === -1){
                                $('#'+"seatMaperChart"+svgIndex+' '+'#'+seat.uid).css('fill', 'green');
                                pickedSeats.push(seat.uid);
                            }
                        })
                    }
                    ctrl.validationCheckBlitType(pickedSeats);
                });
                chart.container("seatMaperChart"+svgIndex);
                chart.draw();
                document.getElementsByClassName("anychart-credits")[0].style.display = "none";

                scope.resetPickedSeats = function (seatUids) {
                    pickedSeats = [];
                    seatUids.forEach(function (uid) {
                        document.getElementById(uid).style.fill = "red";
                    })
                }

            };
        }
    });