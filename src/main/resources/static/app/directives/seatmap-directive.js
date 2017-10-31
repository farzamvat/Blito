/**
 * Created by soroush on 10/25/17.
 */
angular.module('blitoDirectives')
    .directive('seatMap',  function () {
        return {
            link : seatMapDraw,
            restrict : 'E',
            scope : {
                salonSchema : '=',
                salonImage : '='
            }
        };
        function seatMapDraw(scope, element, attr, ctrl) {
            scope.$watchGroup(['salonSchema', 'salonImage'], function (directiveInputs) {
                if(document.getElementById("seatMaperChart").childNodes[0]) {
                    var svgElement = document.getElementById("seatMaperChart");
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
                            seatMapData.push({ id : seat.uid, info :"kir", value : rowName});
                            console.log(seatMapData);
                        });
                    });
                });
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
                    console.log(rowSeats);
                    chart.choropleth(rowSeats).name(i);
                }

                var seatMapSeries = chart.choropleth();
                seatMapSeries.data(seatMapData);

                var pickedSeats = [];
                var clickFunction = function (e) {
                        if (pickedSeats.indexOf(e.domTarget.dd) === -1) {
                            pickedSeats.push(e.domTarget.dd);
                            document.getElementById(e.domTarget.dd).style.fill = "green";
                        } else {
                            pickedSeats.splice(pickedSeats.indexOf(e.domTarget.dd), 1);
                            document.getElementById(e.domTarget.dd).style.fill = "#64b5f6";
                        }
                };
                seatMapSeries.listen('click', clickFunction);

                seatMapSeries.labels(true);
                var labels = seatMapSeries.labels();
                // enable labels and adjust them
                // labels.enabled(true);
                // labels.useHtml(false);
                seatMapSeries.labels({fontSize: 10});
                // seatMapSeries.labels().fontColor("white");
                labels.format("1");
                // labels.offsetY(5);

                chart.title(directiveInputs[0].name.toString());
                chart.contextMenu(false);

                chart.legend()
                    .enabled(true)
                    // items source mode categories
                    .position('right')
                    .itemsLayout('vertical');


                chart.container("seatMaperChart");
                chart.draw();
                document.getElementsByClassName("anychart-credits")[0].style.display = "none";

            });
        }
    });