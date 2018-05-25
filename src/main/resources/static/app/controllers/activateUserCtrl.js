/**
 * Created by soroush on 4/29/17.
 */

angular.module('User', [])
    .controller('activateUserCtrl',[
        '$scope',
        'userInfo',
        function ($scope, userInfo) {
        $scope.userData = userInfo.getData();

    }])
;