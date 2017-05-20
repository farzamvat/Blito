/**
 * Created by soroush on 4/29/17.
 */

angular.module('User', [])
    .controller('activateUserCtrl', function ($scope, userInfo) {
        console.log(userInfo.getData());
        $scope.userData = userInfo.getData();

    })
;