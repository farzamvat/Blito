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
                console.log(directiveInputs);
                var seatMapData = [];
                directiveInputs[0].sections.forEach(function (section) {
                    section.rows.forEach(function (row) {
                        row.seats.forEach(function (seat) {
                            seatMapData.push({ id : seat.uid, info : seat.name});
                        });
                    });
                });

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

                var labels = seatMapSeries.labels();
                // enable labels and adjust them
                labels.enabled(true);
                labels.useHtml(false);
                seatMapSeries.labels({fontSize: 10, color: {fill: 'white 1'}});
                labels.format("{%info} \n");
                labels.offsetY(5);

                chart.title(directiveInputs[0].name.toString());
                chart.contextMenu(false);

                chart.container("seatMaperChart");
                chart.draw();
                document.getElementsByClassName("anychart-credits")[0].style.display = "none";

            });
        }
    });