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

    });
