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
                            seatMapData.push({ id : seat.uid, info : seat.name, value : rowName});
                        });
                    });
                });
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
                        .listen('click', clickFunction)
                }
                chart.labels(true);
                var labels = chart.labels();
                // enable labels and adjust them
                // labels.enabled(true);
                // labels.useHtml(false);
                chart.labels({fontSize: 10});
                // seatMapSeries.labels().fontColor("white");
                labels.format("{%info}");
                // labels.offsetY(5);

                chart.title(directiveInputs[0].name.toString());
                chart.contextMenu(false);

               var legend = chart.legend();
                legend.enabled(true)
                    .position('right')
                    .itemsLayout('vertical')
                    .removeAllListeners()
                ;
                legend.listen("click", function(e) {
                    directiveInputs[0].sections[0].rows[e.itemIndex].seats.forEach(function (seat) {
                        if(pickedSeats.indexOf(seat.uid) === -1){
                            document.getElementById(seat.uid).style.fill = "green";
                            pickedSeats.push(seat.uid);
                        } else {
                            document.getElementById(seat.uid).style.fill = "#64b5f6";
                            pickedSeats.splice(pickedSeats.indexOf(seat.uid), 1);
                        }
                    });
                    console.log(pickedSeats);
                });
                chart.container("seatMaperChart");
                chart.draw();
                document.getElementsByClassName("anychart-credits")[0].style.display = "none";

            });
        }
    });