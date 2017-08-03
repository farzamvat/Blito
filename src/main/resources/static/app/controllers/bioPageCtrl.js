/**
 * Created by soroush on 4/26/17.
 */

angular.module('bioPageModule', [])
    .controller('bioPageCtrl', function ($scope) {


        $scope.imageData = "";
        angular.element(document.getElementsByClassName('bioPageCoverPhoto')).css('background', 'url("'+$scope.imageData+'") fixed');
    })