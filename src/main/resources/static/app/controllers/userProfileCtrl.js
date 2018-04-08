/**
 * Created by soroush on 5/2/17.
 */
angular.module('User')
    .controller('userProfileCtrl', [
        '$scope',
        'userInfo',
        'Auth',
        '$timeout',
        'mapMarkerService',
        'updateInfo',
        '$location',
        'scrollAnimation',
        'photoService',
        'eventService',
        'plannerService',
        'exchangeService',
        'dataService',
        'ticketsService',
        '$q',
        'dateSetterService',
        'imageServices',
        '$window',
        'FileSaver',
        'seatmapService',

        function ($scope,
                  userInfo,
                  Auth,
                  $timeout,
                  mapMarkerService,
                  updateInfo,
                  $location,
                  scrollAnimation,
                  photoService,
                  eventService,
                  plannerService,
                  exchangeService,
                  dataService,
                  ticketsService,
                  $q,
                  dateSetterService,
                  imageServices,
                  $window,
                  FileSaver,
                  seatmapService) {
            var userProfile = this;
            var sansWithSeats = [];
            var mainSeatMapPrices = [];
            var mainSeatMapPricesEdit = [];
            var sansIndexPicked = 100;
            $scope.userData = userInfo.getData();
            $scope.updateInfoSpinner = false;
            $scope.newShowTime = {blitTypes : []};
            $scope.newShowTimeEdit = {blitTypes : []};
            $scope.eventFields = {};
            $scope.exchange = {};

            $scope.updateSuccessNotif = false;
            $scope.updateFailNotif = false;
            $scope.showTimeForms = [];
            $scope.showTimeEditForms = [{blitTypes : [{}]}];

            $scope.showThumbnailProfile = false;
            $scope.showThumbnailEventPlanner = false;
            $scope.showThumbnailEventExchange = false;
            $scope.showThumbnailCover = false;
            $scope.uploadExchangePhoto = false;
            $scope.exchangePhotoSuccess = false;
            $scope.uploadEventPhoto = false;
            $scope.eventPhotoSuccess = false;
            $scope.createEventSpinner = false;
            $scope.createEventNotif = false;
            userProfile.showTimeNumber = 0;
            var userInfoPromise = [];
            //==================================================== EDIT USER GET INFO =================================
            $scope.editInfo = angular.copy(userInfo.getData());
            $scope.editUserInfo = function () {
                userInfoPromise.push(Auth.getUser()
                    .then(function (data) {
                        $scope.logoutMenu = true;
                        userProfile.userData = data.data;
                        userInfo.setData(userProfile.userData);
                        $scope.editInfo = angular.copy(userInfo.getData());

                    }, function (data) {
                    }))
            };
            $scope.changePassword = function (password) {
                document.getElementsByClassName("deletePlannerSpinner")[0].style.display = "none";
                document.getElementsByClassName("changePasswordSpinner")[0].style.display = "inline";
                updateInfo.changePasswordSubmit(password)
                    .then(function () {
                        document.getElementById("changePasswordSuccess").style.display = "block";
                        document.getElementsByClassName("changePasswordSpinner")[0].style.display = "none";
                    })
                    .catch(function (data) {
                        document.getElementsByClassName("changePasswordSpinner")[0].style.display = "none";
                        document.getElementById("changePasswordError").style.display = "block";
                        document.getElementById("changePasswordError").innerHTML = data.data.message;
                    })
            };
            $scope.editUserInfo();
            $scope.updateInfo = function (editInfo) {
                $scope.updateInfoSpinner = true;
                delete editInfo["email"];
                updateInfo.updateData(editInfo)
                    .then(function () {
                        $scope.updateInfoSpinner = false;
                        $scope.updateSuccessNotif = true;
                        $timeout(function () {
                            $scope.updateSuccessNotif = false;
                        }, 3000);
                    })
                    .catch(function () {
                        $scope.updateInfoSpinner = false;
                        $scope.updateFailNotif = true;
                        $timeout(function () {
                            $scope.updateFailNotif = false;
                        }, 3000);
                    })
            };

            //==================================================== ********* =================================

            //==================================================== IMAGE UPLOADS =================================
            $scope.deleteGallerySpinner = function (i) {
                return "deleteSpinner"+i;
            };
            var fileSelectProfile = document.createElement('input');
            fileSelectProfile.type = 'file';
            var fileSelectCover = document.createElement('input');
            fileSelectCover.type = 'file';
            var fileSelectCoverEdit = document.createElement('input');
            fileSelectCoverEdit.type = 'file';
            var fileSelectEventPlanner = document.createElement('input');
            fileSelectEventPlanner.type = 'file';
            var fileSelectEventPlannerEdit = document.createElement('input');
            fileSelectEventPlannerEdit.type = 'file';
            var fileSelectEventExchange = document.createElement('input');
            fileSelectEventExchange.type = 'file';
            var fileSelectEditExchange = document.createElement('input');
            fileSelectEditExchange.type = 'file';
            var fileSelectEditEvent = document.createElement('input');
            fileSelectEditEvent.type = 'file';
            var fileSelectEventGalleryOne = document.createElement('input');
            fileSelectEventGalleryOne.type = 'file';
            var fileSelectEventGalleryTwo = document.createElement('input');
            fileSelectEventGalleryTwo.type = 'file';
            var fileSelectEventGalleryThree = document.createElement('input');
            fileSelectEventGalleryThree.type = 'file';
            var fileSelectEventGalleryFour = document.createElement('input');
            fileSelectEventGalleryFour.type = 'file';
            var fileSelectEventGalleryFive = document.createElement('input');
            fileSelectEventGalleryFive.type = 'file';
            var fileSelectEventGallerySix = document.createElement('input');
            fileSelectEventGallerySix.type = 'file';
            var fileSelectEventGalleryOneEdit = document.createElement('input');
            fileSelectEventGalleryOneEdit.type = 'file';
            var fileSelectEventGalleryTwoEdit = document.createElement('input');
            fileSelectEventGalleryTwoEdit.type = 'file';
            var fileSelectEventGalleryThreeEdit = document.createElement('input');
            fileSelectEventGalleryThreeEdit.type = 'file';
            var fileSelectEventGalleryFourEdit = document.createElement('input');
            fileSelectEventGalleryFourEdit.type = 'file';
            var fileSelectEventGalleryFiveEdit = document.createElement('input');
            fileSelectEventGalleryFiveEdit.type = 'file';
            var fileSelectEventGallerySixEdit = document.createElement('input');
            fileSelectEventGallerySixEdit.type = 'file';

            $scope.uploadGallerySixEdit = function() {
                fileSelectEventGallerySixEdit.click();
            };
            $scope.uploadGalleryFiveEdit = function() {
                fileSelectEventGalleryFiveEdit.click();
            };
            $scope.uploadGalleryFourEdit = function() {
                fileSelectEventGalleryFourEdit.click();
            };
            $scope.uploadGalleryThreeEdit = function() {
                fileSelectEventGalleryThreeEdit.click();
            };
            $scope.uploadGalleryTwoEdit = function() {
                fileSelectEventGalleryTwoEdit.click();
            };
            $scope.uploadGalleryOneEdit = function() {
                fileSelectEventGalleryOneEdit.click();
            };
            $scope.uploadGallerySix = function() {
                fileSelectEventGallerySix.click();
            };
            $scope.uploadGalleryFive = function() {
                fileSelectEventGalleryFive.click();
            };
            $scope.uploadGalleryFour = function() {
                fileSelectEventGalleryFour.click();
            };
            $scope.uploadGalleryThree = function() {
                fileSelectEventGalleryThree.click();
            };
            $scope.uploadGalleryOne = function() {
                fileSelectEventGalleryOne.click();
            };
            $scope.uploadGalleryTwo = function() {
                fileSelectEventGalleryTwo.click();
            };
            $scope.uploadPicProfile = function() {
                fileSelectProfile.click();
            };
            $scope.uploadPicCoverEdit = function() {
                fileSelectCoverEdit.click();
            };
            $scope.uploadPicCover = function() {
                fileSelectCover.click();
            };
            $scope.uploadPicEventPlanner = function() {
                fileSelectEventPlanner.click();
            };
            $scope.uploadPicEventPlannerEdit = function() {
                fileSelectEventPlannerEdit.click();
            };
            $scope.uploadPicEventExchange = function() {
                fileSelectEventExchange.click();
            };
            $scope.uploadPicEditExchange = function() {
                fileSelectEditExchange.click();
            };
            $scope.uploadPicEditEvent = function() {
                fileSelectEditEvent.click();
            };
            $scope.photoGallerySixEdit = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("gallerySixEdit"))[0].src
                };
                $scope.eventPhotoSixEditSuccess = false;
                $scope.eventPhotoSixEditError = false;
                $scope.uploadEventSixEditPhoto = true;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventSixEditPhoto = false;
                        $scope.eventPhotoSixEditSuccess = true;

                        $scope.gallerySixEditUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoSixEditError").innerHTML= data.data.message;
                        $scope.eventPhotoSixEditError = true;
                        $scope.uploadEventSixEditPhoto = false;
                    })
            };
            $scope.photoGallerySixEditDelete = function (eventId) {
                $scope.eventPhotoSixEditSuccess = false;
                $scope.eventPhotoSixEditError = false;
                document.getElementsByClassName("deleteSpinner5")[0].style.display = "inline";
                photoService.deleteGalleryPhoto(eventId, $scope.gallerySixEditUUID)
                    .then(function () {
                        document.getElementsByClassName("deleteSpinner5")[0].style.display = "none";
                        $scope.uploadEventSixEditPhoto = false;
                    })
                    .catch(function (data) {
                        document.getElementsByClassName("deleteSpinner5")[0].style.display = "none";
                        document.getElementById("eventPhotoSixEditError").innerHTML= data.data.message;
                        $scope.eventPhotoSixEditError = true;
                    })
            };
            $scope.photoGalleryFiveEdit = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryFiveEdit"))[0].src
                };
                $scope.eventPhotoFiveEditSuccess = false;
                $scope.eventPhotoFiveEditError = false;
                $scope.uploadEventFiveEditPhoto = true;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventFiveEditPhoto = false;
                        $scope.eventPhotoFiveEditSuccess = true;

                        $scope.galleryFiveEditUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoFiveEditError").innerHTML= data.data.message;
                        $scope.eventPhotoFiveEditError = true;
                        $scope.uploadEventFiveEditPhoto = false;
                    })
            };
            $scope.photoGalleryFiveEditDelete = function (eventId) {
                $scope.eventPhotoFiveEditSuccess = false;
                $scope.eventPhotoFiveEditError = false;
                document.getElementsByClassName("deleteSpinner4")[0].style.display = "inline";
                photoService.deleteGalleryPhoto(eventId, $scope.galleryFiveEditUUID)
                    .then(function () {
                        $scope.eventPhotoSixEditSuccess = true;
                        document.getElementsByClassName("deleteSpinner4")[0].style.display = "none";
                    })
                    .catch(function (data) {
                        document.getElementsByClassName("deleteSpinner4")[0].style.display = "none";
                        document.getElementById("eventPhotoFiveEditError").innerHTML= data.data.message;
                        $scope.eventPhotoSixEditError = true;
                    })
            };
            $scope.photoGalleryFourEdit = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryFourEdit"))[0].src
                };
                $scope.uploadEventFourEditPhoto = true;
                $scope.eventPhotoFourEditError = false;
                $scope.eventPhotoFourEditSuccess = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventFourEditPhoto = false;
                        $scope.eventPhotoFourEditSuccess = true;
                        $scope.galleryFourEditUUID = data.data.imageUUID;
                    }, function (data) {
                        $scope.uploadEventFourEditPhoto = false;
                        $scope.eventPhotoFourEditError = true;
                        document.getElementById("eventPhotoFourEditError").innerHTML= data.data.message;
                    })
            };
            $scope.photoGalleryFourEditDelete = function (eventId) {
                $scope.eventPhotoFourEditError = false;
                $scope.eventPhotoFourEditSuccess = false;
                document.getElementsByClassName("deleteSpinner3")[0].style.display = "inline";
                photoService.deleteGalleryPhoto(eventId, $scope.galleryFourEditUUID)
                    .then(function () {
                        document.getElementsByClassName("deleteSpinner3")[0].style.display = "none";
                        $scope.eventPhotoFourEditSuccess = true;
                    })
                    .catch(function (data) {
                        document.getElementsByClassName("deleteSpinner3")[0].style.display = "none";
                        document.getElementById("eventPhotoFourEditError").innerHTML= data.data.message;
                        $scope.eventPhotoFourEditError = true;
                    })
            };
            $scope.photoGalleryThreeEdit = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryThreeEdit"))[0].src
                };
                $scope.eventPhotoThreeEditSuccess = false;
                $scope.eventPhotoThreeEditError = false;
                $scope.uploadEventThreeEditPhoto = true;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventThreeEditPhoto = false;
                        $scope.eventPhotoThreeEditSuccess = true;

                        $scope.galleryThreeEditUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoThreeEditError").innerHTML= data.data.message;
                        $scope.eventPhotoThreeEditError = true;
                        $scope.uploadEventThreeEditPhoto = false;
                    })
            };
            $scope.photoGalleryThreeEditDelete = function (eventId) {
                $scope.eventPhotoThreeEditSuccess = false;
                $scope.eventPhotoThreeEditError = false;
                document.getElementsByClassName("deleteSpinner2")[0].style.display = "inline";
                photoService.deleteGalleryPhoto(eventId, $scope.galleryThreeEditUUID)
                    .then(function () {
                        document.getElementsByClassName("deleteSpinner2")[0].style.display = "none";
                        $scope.eventPhotoThreeEditSuccess = true;
                    })
                    .catch(function (data) {
                        document.getElementsByClassName("deleteSpinner2")[0].style.display = "none";
                        document.getElementById("eventPhotoThreeEditError").innerHTML= data.data.message;
                        $scope.eventPhotoThreeEditError = true;
                    })
            };
            $scope.photoGalleryTwoEdit = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryTwoEdit"))[0].src
                };
                $scope.uploadEventTwoEditPhoto = true;
                $scope.eventPhotoTwoEditError = false;
                $scope.eventPhotoTwoEditSuccess = false;

                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventTwoEditPhoto = false;
                        $scope.eventPhotoTwoEditSuccess = true;

                        $scope.galleryTwoEditUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoTwoEditError").innerHTML= data.data.message;
                        $scope.uploadEventTwoEditPhoto = false;
                        $scope.eventPhotoTwoEditError = true;
                    })
            };
            $scope.photoGalleryTwoEditDelete = function (eventId) {
                $scope.eventPhotoTwoEditError = false;
                $scope.eventPhotoTwoEditSuccess = false;
                document.getElementsByClassName("deleteSpinner1")[0].style.display = "inline";
                photoService.deleteGalleryPhoto(eventId, $scope.galleryTwoEditUUID)
                    .then(function () {
                        $scope.eventPhotoTwoEditSuccess = true;
                        document.getElementsByClassName("deleteSpinner1")[0].style.display = "none";
                    })
                    .catch(function (data) {
                        document.getElementsByClassName("deleteSpinner1")[0].style.display = "none";
                        document.getElementById("eventPhotoTwoEditError").innerHTML= data.data.message;
                        $scope.eventPhotoTwoEditError = true;
                    })
            };
            $scope.photoGalleryOneEdit = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryOneEdit"))[0].src
                };
                $scope.uploadEventOneEditPhoto = true;
                $scope.eventPhotoOneEditError = false;
                $scope.eventPhotoOneEditSuccess = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventOneEditPhoto = false;
                        $scope.eventPhotoOneEditSuccess = true;

                        $scope.galleryOneEditUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoOneEditError").innerHTML= data.data.message;
                        $scope.eventPhotoOneEditError = true;
                        $scope.uploadEventOneEditPhoto = false;
                    })
            };
            $scope.photoGalleryOneEditDelete = function (eventId) {
                $scope.eventPhotoOneEditError = false;
                $scope.eventPhotoOneEditSuccess = false;
                document.getElementsByClassName("deleteSpinner0")[0].style.display = "inline";
                photoService.deleteGalleryPhoto(eventId, $scope.galleryOneEditUUID)
                    .then(function () {
                        $scope.eventPhotoOneEditSuccess = true;
                        document.getElementsByClassName("deleteSpinner0")[0].style.display = "none";
                    })
                    .catch(function (data) {
                        document.getElementsByClassName("deleteSpinner0")[0].style.display = "none";
                        document.getElementById("eventPhotoOneEditError").innerHTML= data.data.message;
                        $scope.eventPhotoOneEditError = true;
                    })
            };
            $scope.photoGallerySix = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("gallerySix"))[0].src
                };
                $scope.uploadEventSixPhoto = true;
                $scope.eventPhotoSixSuccess = false;
                $scope.eventPhotoSixError = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventSixPhoto = false;
                        $scope.eventPhotoSixSuccess = true;
                        $scope.gallerySixUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoSixError").innerHTML= data.data.message;
                        $scope.uploadEventSixPhoto = false;
                        $scope.eventPhotoSixError = true;
                    })
            };
            $scope.photoGalleryFive = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryFive"))[0].src
                };
                $scope.uploadEventFivePhoto = true;
                $scope.eventPhotoFiveError = false;
                $scope.eventPhotoFiveSuccess = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventFivePhoto = false;
                        $scope.eventPhotoFiveSuccess = true;
                        $scope.galleryFiveUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoFiveError").innerHTML= data.data.message;
                        $scope.eventPhotoFiveError = true;
                        $scope.uploadEventFivePhoto = false;
                    })
            };
            $scope.photoGalleryFour = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryFour"))[0].src
                };
                $scope.uploadEventFourPhoto = true;
                $scope.eventPhotoFourError = false;
                $scope.eventPhotoFourSuccess = false;

                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventFourPhoto = false;
                        $scope.eventPhotoFourSuccess = true;
                        $scope.galleryFourUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoFourError").innerHTML= data.data.message;
                        $scope.uploadEventFourPhoto = false;
                        $scope.eventPhotoFourError = true;
                    })
            };
            $scope.photoGalleryThree= function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryThree"))[0].src
                };
                $scope.uploadEventThreePhoto = true;
                $scope.eventPhotoThreeError = false;
                $scope.eventPhotoThreeSuccess = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.eventPhotoThreeSuccess = true;
                        $scope.uploadEventThreePhoto = false;
                        $scope.galleryThreeUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoThreeError").innerHTML= data.data.message;
                        $scope.uploadEventThreePhoto = false;
                        $scope.eventPhotoThreeError = true;
                    })
            };
            $scope.photoGalleryTwo= function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryTwo"))[0].src
                };
                $scope.uploadEventTwoPhoto = true;
                $scope.eventPhotoTwoSuccess = false;
                $scope.eventPhotoTwoError = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.eventPhotoTwoSuccess = true;
                        $scope.uploadEventTwoPhoto = false;
                        $scope.galleryTwoUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoTwoError").innerHTML= data.data.message;
                        $scope.uploadEventTwoPhoto = false;
                        $scope.eventPhotoTwoError = true;
                    })
            };

            $scope.photoGalleryOne= function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("galleryOne"))[0].src
                };

                $scope.uploadEventOnePhoto = true;
                $scope.eventPhotoOneSuccess = false;
                $scope.eventPhotoOneError = false;

                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.eventPhotoOneSuccess = true;
                        $scope.uploadEventOnePhoto = false;
                        $scope.galleryOneUUID = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoOneError").innerHTML= data.data.message;
                        $scope.eventPhotoOneError = true;
                        $scope.uploadEventOnePhoto = false;
                    })
            };
            $scope.photoUploadEditEvent= function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("profilePhotoUploadEditEvent"))[0].src
                };
                $scope.uploadEditEventPhoto = true;
                $scope.eventEditPhotoError = false;
                $scope.eventEditPhotoSuccess = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.eventEditPhotoSuccess = true;
                        $scope.uploadEditEventPhoto = false;
                        $scope.eventEditImageId = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventEditPhotoError").innerHTML= data.data.message;
                        $scope.uploadEditEventPhoto = false;
                        $scope.eventEditPhotoError = true;
                    })
            };
            $scope.photoUploadEditExchange = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("exchangePhotoUploadEdit"))[0].src
                };
                $scope.uploadEditExchangePhoto = true;
                $scope.exchangeEditPhotoSuccess = false;
                $scope.exchangeEditPhotoError = false;

                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEditExchangePhoto = false;
                        $scope.exchangeEditPhotoSuccess = true;
                        $scope.exchangeEditImageId = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("exchangeEditPhotoError").innerHTML= data.data.message;
                        $scope.uploadEditExchangePhoto = false;
                        $scope.exchangeEditPhotoError = true;
                    })
            };

            $scope.photoUploadExchange = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("exchangePhotoUpload"))[0].src
                };
                $scope.uploadExchangePhoto = true;
                $scope.exchangePhotoError = false;
                $scope.exchangePhotoSuccess = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadExchangePhoto = false;
                        $scope.exchangePhotoSuccess = true;
                        $scope.exchangeImageId = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("exchangePhotoError").innerHTML= data.data.message;
                        $scope.uploadExchangePhoto = false;
                        $scope.exchangePhotoError = true;
                    })
            };
            $scope.photoUploadEvent = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("profilePhotoUpload"))[0].src
                };
                $scope.uploadEventPhoto = true;
                $scope.eventPhotoSuccess = false;
                $scope.eventPhotoError = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadEventPhoto = false;
                        $scope.eventPhotoSuccess = true;
                        $scope.eventImageId = data.data.imageUUID;
                    }, function (data) {
                        document.getElementById("eventPhotoError").innerHTML= data.data.message;
                        $scope.eventPhotoError = true;
                        $scope.uploadEventPhoto = false;
                    })
            };

            $scope.uploadPlannerPhoto = false;
            $scope.plannerPhotoSuccess = false;

            $scope.photoUploadPlanner = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("eventPlannerPhotoUpload"))[0].src
                };
                $scope.uploadPlannerPhoto = true;
                $scope.plannerPhotoSuccess = false;
                $scope.plannerPhotoError = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadPlannerPhoto = false;
                        $scope.plannerPhotoSuccess = true;
                        $scope.plannerImageId = data.data.imageUUID;

                    }, function (data) {
                        document.getElementById("plannerPhotoError").innerHTML= data.data.message;
                        $scope.uploadPlannerPhoto = false;
                        $scope.plannerPhotoError = true;
                    })
            };

            $scope.photoUploadPlannerEdit = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("eventPlannerPhotoUploadEdit"))[0].src
                };
                $scope.uploadPlannerEditPhoto = true;
                $scope.plannerEditPhotoError = false;
                $scope.plannerEditPhotoSuccess = false;
                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadPlannerEditPhoto = false;
                        $scope.plannerEditPhotoSuccess = true;
                        $scope.plannerImageIdEdit = data.data.imageUUID;

                    }, function (data) {
                        document.getElementById("plannerEditPhotoError").innerHTML= data.data.message;
                        $scope.uploadPlannerEditPhoto = false;
                        $scope.plannerEditPhotoError = true;
                    })
            };
            $scope.uploadCoverPhoto = false;
            $scope.coverPhotoSuccess = false;

            $scope.photoUploadPlannerCover = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("coverPhotoUpload"))[0].src
                };
                $scope.uploadCoverPhoto = true;
                $scope.coverPhotoSuccess = false;
                $scope.coverPhotoError = false;

                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadCoverPhoto = false;
                        $scope.coverPhotoSuccess = true;
                        $scope.coverImageId = data.data.imageUUID;
                    }, function (data) {
                        $scope.uploadCoverPhoto = false;
                        document.getElementById("coverPhotoError").innerHTML= data.data.message;
                        $scope.coverPhotoError = true;
                    })
            };

            $scope.photoUploadPlannerCoverEdit = function () {
                var imageData = {
                    encodedBase64 : angular.element(document.getElementsByClassName("coverPhotoUploadEdit"))[0].src
                };
                $scope.uploadCoverEditPhoto = true;
                $scope.coverEditPhotoSuccess = false;
                $scope.coverEditPhotoError = false;

                photoService.upload(imageData)
                    .then(function (data) {
                        $scope.uploadCoverEditPhoto = false;
                        $scope.coverEditPhotoSuccess = true;
                        $scope.coverImageIdEdit = data.data.imageUUID;
                    }, function (data) {
                        $scope.uploadCoverEditPhoto = false;
                        document.getElementById("coverEditPhotoError").innerHTML= data.data.message;
                        $scope.coverEditPhotoError = true;
                    })
            };

            fileSelectEventGallerySixEdit.onchange = function() {
                imageServices.readBase64Data(fileSelectEventGallerySixEdit, "gallerySixEdit");
            };
            fileSelectEventGalleryFiveEdit.onchange = function() {
                imageServices.readBase64Data(fileSelectEventGalleryFiveEdit, "galleryFiveEdit");
            };
            fileSelectEventGalleryFourEdit.onchange = function() {
                imageServices.readBase64Data(fileSelectEventGalleryFourEdit, "galleryFourEdit");
            };
            fileSelectEventGalleryThreeEdit.onchange = function() {
                imageServices.readBase64Data(fileSelectEventGalleryThreeEdit, "galleryThreeEdit");
            };
            fileSelectEventGalleryTwoEdit.onchange = function() {
                imageServices.readBase64Data(fileSelectEventGalleryTwoEdit, "galleryTwoEdit");
            };

            fileSelectEventGalleryOneEdit.onchange = function() {
                imageServices.readBase64Data(fileSelectEventGalleryOneEdit, "galleryOneEdit");
            };
            fileSelectEventGallerySix.onchange = function() {
                $scope.showThumbnailSix = true;
                imageServices.readBase64Data(fileSelectEventGallerySix, "gallerySix");
            };
            fileSelectEventGalleryFive.onchange = function() {
                $scope.showThumbnailFive = true;
                imageServices.readBase64Data(fileSelectEventGalleryFive, "galleryFive");
            };
            fileSelectEventGalleryFour.onchange = function() {
                $scope.showThumbnailFour = true;
                imageServices.readBase64Data(fileSelectEventGalleryFour, "galleryFour");
            };
            fileSelectEventGalleryThree.onchange = function() {
                $scope.showThumbnailThree = true;
                imageServices.readBase64Data(fileSelectEventGalleryThree, "galleryThree");
            };

            fileSelectEventGalleryTwo.onchange = function() {
                $scope.showThumbnailTwo = true;
                imageServices.readBase64Data(fileSelectEventGalleryTwo, "galleryTwo");
            };
            fileSelectEventGalleryOne.onchange = function() {
                $scope.showThumbnailOne = true;
                imageServices.readBase64Data(fileSelectEventGalleryOne, "galleryOne");
            };
            fileSelectEditEvent.onchange = function() {
                imageServices.readBase64Data(fileSelectEditEvent, "profilePhotoUploadEditEvent");
            };
            fileSelectEventPlannerEdit.onchange = function() {
                imageServices.readBase64Data(fileSelectEventPlannerEdit, "eventPlannerPhotoUploadEdit");
            };
            fileSelectCoverEdit.onchange = function() {
                imageServices.readBase64Data(fileSelectCoverEdit, "coverPhotoUploadEdit");
            };
            fileSelectEditExchange.onchange = function() {
                imageServices.readBase64Data(fileSelectEditExchange, "exchangePhotoUploadEdit");
            };
            fileSelectProfile.onchange = function () {
                $scope.showThumbnailProfile = true;
                imageServices.readBase64Data(fileSelectProfile, "profilePhotoUpload");
            };
            fileSelectCover.onchange = function () {
                $scope.showThumbnailCover = true;
                imageServices.readBase64Data(fileSelectCover, "coverPhotoUpload");
            };

            fileSelectEventPlanner.onchange = function () {
                $scope.showThumbnailEventPlanner = true;
                imageServices.readBase64Data(fileSelectEventPlanner, "eventPlannerPhotoUpload");
            };
            fileSelectEventExchange.onchange = function () {
                $scope.showThumbnailEventExchange = true;
                imageServices.readBase64Data(fileSelectEventExchange, "exchangePhotoUpload");
            };
            //==================================================== ********* =================================
            //==================================================== SCROLL TO=================================
            $scope.scrollTo = function(id, section) {
                var old = $location.hash();
                $location.hash(id);
                $location.hash(old);
                scrollAnimation.scrollTo(id);
                setInitMaps(section);
                $(angular.element(document.getElementById(section))).next().slideDown(300);
                $(angular.element(document.getElementById(section))).addClass('orangeBackground');
            };
            //==================================================== ********* =================================
            //==================================================== SHOW TIME FIELDS =================================

            $scope.addFieldTicketType=function(){
                $scope.newShowTime.blitTypes.push({})
            };
            $scope.addFieldTicketTypeEdit=function(){
                $scope.blitTypesWithOutSeatsEdit.push({})
            };
            $scope.addFieldTicketTypeEditSubmit = function(){
                $scope.newShowTimeEdit.blitTypes.push({})
            };

            $scope.deleteFieldTicketTypeEdit=function(blitTypeIndex){
                if( 0 < $scope.blitTypesWithOutSeatsEdit.length) {
                    $scope.blitTypesWithOutSeatsEdit.splice(blitTypeIndex,1);
                }
            };
            $scope.deleteFieldTicketType=function(i){
                if( 0 < $scope.newShowTime.blitTypes.length) {
                    $scope.newShowTime.blitTypes.splice(i,1);
                }
            };
            $scope.deleteFieldTicketTypeEditSubmit = function(i){
                if( 0 < $scope.newShowTimeEdit.blitTypes.length) {
                    $scope.newShowTimeEdit.blitTypes.splice(i,1);
                }
            };

        $scope.deleteFieldShowTimeEdit=function(i){
            if( 1 < $scope.showTimeEditForms.length) {
                $scope.showTimeEditForms.splice(i,1);
            }
        };
        $scope.deleteFieldShowTime=function(i){
            if( 0 < $scope.showTimeForms.length) {
                userProfile.showTimeNumber--;
                $scope.showTimeForms.splice(i,1);
            }
        };
        //==================================================== ********* =================================
        //==================================================== EVENT PLANNER SUBMIT =================================
        $scope.submitPlannerSpinner = false;
        $scope.submitPlannerNotif = false;
        $scope.plannerSubmitOnce = false;
        $scope.submitEventPlanner = function (plannerData) {
            $scope.plannerSubmitOnce = true;
            $scope.submitPlannerNotif = false;
            var eventPlannerData = {
                hostName: plannerData.name,
                hostType: plannerData.type,
                images: [
                    {
                        imageUUID: $scope.plannerImageId,
                        type: "HOST_PHOTO"
                    },
                    {
                        imageUUID: $scope.coverImageId,
                        type: "HOST_COVER_PHOTO"
                    }
                ],
                instagramLink: plannerData.instagram,
                linkedinLink: plannerData.linkedin,
                telegramLink: plannerData.telegram,
                telephone: dataService.persianToEnglishDigit(persianJs(plannerData.mobile).englishNumber().toString()),
                twitterLink: plannerData.twitter,
                websiteLink: plannerData.website,
                description : plannerData.description
            };
            eventPlannerData.images = eventPlannerData.images.filter(function (images) {
                return images.imageUUID !== undefined;
            });
            if(!$scope.plannerImageId && !$scope.coverImageId){
                delete eventPlannerData.images;
            }
            $scope.submitPlannerSpinner = true;
            plannerService.submitPlannerForm(eventPlannerData)
                .then(function () {
                    $scope.plannerSubmitOnce = false;
                    $scope.submitPlannerSpinner = false;
                    $scope.submitPlannerNotif = true;
                    $scope.submitPlannerErrorNotif = false;
                    $scope.getPlannersData(1);
                    $scope.getPlannersDataList();
                    $scope.eventPlanner = [];
                    $scope.plannerImageId = '';
                    $scope.coverImageId = '';
                    $scope.coverPhotoSuccess = false;
                    $scope.plannerPhotoSuccess = false;
                    angular.element(document.getElementsByClassName("coverPhotoUpload"))[0].src = '';
                    angular.element(document.getElementsByClassName("eventPlannerPhotoUpload"))[0].src = '';
                }, function (data) {
                    $scope.plannerSubmitOnce = false;
                    $scope.submitPlannerErrorNotif = true;
                    document.getElementById("submitPlannerErrorNotif").innerHTML= data.data.message;
                    $scope.submitPlannerSpinner = false;
                })
        };
        //==================================================== ********* =================================
        //==================================================== EVENT SUBMIT =================================
        $scope.galleryOneUUID = null;
        $scope.galleryTwoUUID = null;
        $scope.galleryThreeUUID = null;
        $scope.galleryFourUUID = null;
        $scope.galleryFiveUUID = null;
        $scope.gallerySixUUID = null;
        $scope.eventSubmitOnce = false;
        $scope.submitEvent = function (eventFields) {
            $scope.eventSubmitOnce = true;
            $scope.createEventNotif = false;
            $scope.eventPhotoSuccess = false;
            $scope.eventPhotoOneSuccess = false;
            $scope.eventPhotoTwoSuccess = false;
            $scope.eventPhotoThreeSuccess = false;
            $scope.eventPhotoFourSuccess = false;
            $scope.eventPhotoFiveSuccess = false;
            $scope.eventPhotoSixSuccess = false;
            var latLng = mapMarkerService.getMarker();
            var newShowTime = angular.copy($scope.showTimeForms);
            newShowTime = newShowTime.map(function (showTime) {
                delete showTime.newSeatsPrice;
                delete showTime.persianDate;
                showTime.dateTime =  persianDate(showTime.date).format("dddd,DD MMMM, ساعت HH:mm");
                return showTime;
            });
            eventFields.ticketStartTime = document.getElementById("eventTicketStartTimeMain").value;
            eventFields.ticketEndTime = document.getElementById("eventTicketEndTimeMain").value;            var eventSubmitData = {
                eventName : eventFields.name,
                eventType : eventFields.eventType,
                eventHostId : eventFields.eventPlanner.eventHostId,
                address : eventFields.address,
                aparatDisplayCode : eventFields.aparatLink,
                blitSaleEndDate : dateSetterService.persianToMs(eventFields.ticketEndTime),
                blitSaleStartDate : dateSetterService.persianToMs(eventFields.ticketStartTime),
                description : eventFields.description,
                members : eventFields.members,
                isPrivate : eventFields.isPrivate,
                eventDates : newShowTime,
                images : [
                    {imageUUID : $scope.eventImageId,   type : "EVENT_PHOTO"},
                    {imageUUID : $scope.galleryOneUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryTwoUUID,  type : "GALLERY"},
                    {imageUUID : $scope.galleryThreeUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryFourUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryFiveUUID, type : "GALLERY"},
                    {imageUUID : $scope.gallerySixUUID, type : "GALLERY"}
                ],
                latitude : latLng.lat,
                longitude : latLng.lng,
                additionalFields : $scope.additionalFields
            };
            if($scope.seatPicker.isChosen === 'true') {
                eventSubmitData.salonUid = $scope.seatMapListChosen.salonUid;
            }
            eventSubmitData.images = eventSubmitData.images.filter(function (item) {
                return !(item.imageUUID === null || item.imageUUID === undefined);
            });
            if(!$scope.eventImageId){
                delete eventSubmitData.images;
            }
            $scope.createEventSpinner = true;
            eventService.submitEventForm(eventSubmitData)
                .then(function () {
                    $scope.eventSubmitOnce = false;
                    $scope.additionalFields = [];
                    $(angular.element(document.getElementById('PlannerSection')).siblings()[0]).slideUp(300);
                    $(angular.element(document.getElementById('PlannerSection'))).removeClass('orangeBackground');
                    angular.element(document.getElementsByClassName("profilePhotoUpload"))[0].src = '';
                    angular.element(document.getElementsByClassName("galleryOne"))[0].src = '';
                    angular.element(document.getElementsByClassName("galleryTwo"))[0].src = '';
                    angular.element(document.getElementsByClassName("galleryThree"))[0].src = '';
                    angular.element(document.getElementsByClassName("galleryFour"))[0].src = '';
                    angular.element(document.getElementsByClassName("galleryFive"))[0].src = '';
                    angular.element(document.getElementsByClassName("gallerySix"))[0].src = '';
                    document.getElementsByClassName("seatMapSection0")[0].style.display = "none";
                    $scope.eventImageId = null;
                    $scope.galleryOneUUID = null;
                    $scope.galleryTwoUUID = null;
                    $scope.galleryThreeUUID = null;
                    $scope.galleryFourUUID = null;
                    $scope.galleryFiveUUID = null;
                    $scope.gallerySixUUID = null;
                        $scope.mapMarkerClickCheckEvent = true;
                        setInitMaps('makeEventSection');
                        $scope.showTimeForms = [];
                        $timeout(function () {
                            dateSetterService.initDate("eventDateClass0");
                        }, 1000);
                        sansWithSeats = [];
                        mainSeatMapPrices = [];
                        document.getElementsByClassName("generateSeatMap0")[0].style.display = "block";
                        $scope.seatMapListChosen = false;
                        $(".slideUpOnSubmit").slideUp(800);
                        $scope.createEventSpinner = false;
                        $scope.createEventNotif = true;
                        $scope.createEventErrorNotif = false;
                        $scope.allCheckedSeats = 0;
                        $scope.schemaNumberOfSeats = 0;
                        $scope.getUserEvents(1);
                        $scope.eventFields = [];

                    }, function (data) {
                        $scope.eventSubmitOnce = false;
                        $scope.createEventErrorNotif = true;
                        document.getElementById("createEventErrorNotif").innerHTML= data.data.message;
                        $scope.createEventSpinner = false;
                    })
            };
            //==================================================== ********* =================================
            //==================================================== EXCHANGE SUBMIT =================================
            $scope.submitExchangeSpinner = false;
            $scope.submitExchangeNotif = false;
            $scope.exchangeSubmitOnce = false;

            $scope.submitExchangeTicket = function (exchangeFields) {
                $scope.exchangeSubmitOnce = true;

                $scope.submitExchangeNotif = false;
                var latLng = mapMarkerService.getMarker();
                $scope.submitExchangeSpinner = true;
                exchangeFields.eventTime = document.getElementById("eventExchangeDateMain").value;
                var exchangeData = {
                    blitCost: exchangeFields.price,
                    description: exchangeFields.description,
                    email: exchangeFields.sellerEmail,
                    eventAddress: exchangeFields.address,
                    eventDate:  dateSetterService.persianToMs(exchangeFields.eventTime),
                    image: {
                        imageUUID: $scope.exchangeImageId,
                        type: "EXCHANGEBLIT_PHOTO"
                    },
                    isBlitoEvent: exchangeFields.isBlito,
                    phoneNumber: dataService.persianToEnglishDigit(persianJs(exchangeFields.sellerPhoneNumber).englishNumber().toString()),
                    latitude: latLng.lat,
                    longitude: latLng.lng,
                    title: exchangeFields.name,
                    type: exchangeFields.type
                };
                if(!$scope.exchangeImageId){
                    delete exchangeData.image;
                }
                exchangeService.submitExchangeForm(exchangeData)
                    .then(function () {
                        $scope.exchangeSubmitOnce = false;
                        $scope.submitExchangeSpinner = false;
                        $scope.submitExchangeNotif = true;
                        $scope.submitExchangeErrorNotif = false;
                        $scope.exchangePhotoError = false;
                        $scope.exchangePhotoSuccess = false;
                        $scope.getExchangeData(1);
                        $scope.exchange = {};
                        $scope.exchangeImageId = '';
                        setInitMaps('exchangeTicketSection');
                        $scope.mapMarkerClickCheckExchange = true;
                        angular.element(document.getElementsByClassName("exchangePhotoUpload"))[0].src = '';

                    }, function (data) {
                        $scope.exchangeSubmitOnce = false;
                        $scope.submitExchangeErrorNotif = true;
                        $scope.submitExchangeSpinner = false;
                        $scope.exchangePhotoError = false;
                        $scope.exchangePhotoSuccess = false;
                        document.getElementById("submitExchangeErrorNotif").innerHTML= data.data.message;
                    })
            };
            //==================================================== ********* =================================
            $scope.dropDownTabToggleEvent = function (event) {
                $scope.getPlannersData(1);
                $scope.getPlannersDataList();
                $scope.getUserEvents(1);
                $scope.getSalonList();
                $(angular.element(document.getElementById('toggleExchange'))).slideUp(300);
                $(angular.element(event.currentTarget)).next().slideDown(300);
            };
            $scope.dropDownTabToggleExchange = function (event) {
                $scope.getExchangeData(1);
                $(angular.element(document.getElementById('toggleEvent'))).slideUp(300);
                $(angular.element(event.currentTarget)).next().slideDown(300);
            };
            $scope.closeTabDropDowns = function () {
                $(angular.element(document.getElementsByClassName('tabDropDown'))).slideUp(300);
            };

            $scope.toggleBody = function (section) {
                setInitMaps(section);
                $(angular.element(document.getElementById(section))).next().slideToggle(300);
                $(angular.element(document.getElementById(section))).toggleClass('orangeBackground');
            };
            var setInitMaps = function (section) {
                if(section === 'makeEventSection') {
                    $scope.mapMarkerClickCheckEvent = true;
                    mapMarkerService.initMap(document.getElementById('map'));
                }
                if(section === 'exchangeTicketSection') {
                    $scope.mapMarkerClickCheckExchange = true;
                    mapMarkerService.initMap(document.getElementById('mapExchange'));
                }
            };

            $scope.exchangeTickets = [];
            //==================================================== EDIT EVENT =================================
            $scope.editEventFields = {};
            $scope.editEvent = function (index) {
                $scope.editEventErrorNotif = false;
                $scope.editEventNotif = false;
                $scope.eventEditPhotoSuccess = false;
                $scope.eventPhotoOneEditSuccess = false;
                $scope.eventPhotoTwoEditSuccess = false;
                $scope.eventPhotoThreeEditSuccess = false;
                $scope.eventPhotoFourEditSuccess = false;
                $scope.eventPhotoFiveEditSuccess = false;
                $scope.eventPhotoSixEditSuccess = false;
                $scope.galleryOneEditUUID = null;
                $scope.galleryTwoEditUUID = null;
                $scope.galleryThreeEditUUID = null;
                $scope.galleryFourEditUUID = null;
                $scope.galleryFiveEditUUID = null;
                $scope.gallerySixEditUUID = null;
                $scope.additionalFieldsSection = false;
                $scope.checkSoldCountForAdditionalField = false;
                document.getElementById("editEventSansSubmit").style.display = "none";
                angular.element(document.getElementsByClassName("profilePhotoUploadEditEvent"))[0].src = "";
                angular.element(document.getElementsByClassName("galleryOneEdit"))[0].src = "";
                angular.element(document.getElementsByClassName("galleryTwoEdit"))[0].src = "";
                angular.element(document.getElementsByClassName("galleryThreeEdit"))[0].src = "";
                angular.element(document.getElementsByClassName("galleryFourEdit"))[0].src = "";
                angular.element(document.getElementsByClassName("galleryFiveEdit"))[0].src = "";
                angular.element(document.getElementsByClassName("gallerySixEdit"))[0].src = "";
                document.getElementById("editEventNewSansSubmit").style.display = "none";
                document.getElementById("createNewSansEdit").style.display = "block";
                $scope.eventEditPhotoSuccess = false;

            $scope.showTimeEditForms = angular.copy($scope.userEventsEdit[index].eventDates);
            $scope.additionalFieldsEdit = angular.copy($scope.userEventsEdit[index].additionalFields);
            if(!$scope.additionalFieldsEdit) {
                $scope.additionalFieldsEdit = [];
            } else {
                $scope.additionalFieldsSection = true;
            }
            $scope.userEventsEdit[index].eventDates.forEach(function (eventDate) {
                eventDate.blitTypes.forEach(function (blitType) {
                    if(blitType.soldCount > 0) {
                        $scope.checkSoldCountForAdditionalField = true;
                    }
                })
            });
            $scope.editEventFields = {
                eventId : $scope.userEventsEdit[index].eventId,
                eventName : $scope.userEventsEdit[index].eventName,
                eventType : $scope.userEventsEdit[index].eventType,
                description : $scope.userEventsEdit[index].description,
                address : $scope.userEventsEdit[index].address,
                aparatDisplayCode : $scope.userEventsEdit[index].aparatDisplayCode,
                eventHostId : $scope.userEventsEdit[index].eventHostId,
                eventLink : $scope.userEventsEdit[index].eventLink,
                members : $scope.userEventsEdit[index].members,
                isPrivate : $scope.userEventsEdit[index].isPrivate,
                eventState : $scope.userEventsEdit[index].eventState
            };
            if($scope.userEventsEdit[index].salonUid) {
                $scope.editEventFields.salonUid = $scope.userEventsEdit[index].salonUid;
            }
            $scope.dateClass = function (classNumber) {
                return "classDate"+classNumber;
            };
            $("#editEvent").modal("show");
            var imageUUID, gallery = [];
            $scope.userEventsEdit[index].images.forEach(function (image) {
                if(image.type === "EVENT_PHOTO") {
                    imageUUID = image.imageUUID;
                } else {
                    gallery.push(image.imageUUID);
                }
            });
            $scope.eventEditImageId = imageUUID;
            imageServices.downloadPhotos($scope.eventEditImageId, "profilePhotoUploadEditEvent");

                if(gallery.length >= 1) {
                    $scope.galleryOneEditUUID = gallery[0];
                    imageServices.downloadPhotos($scope.galleryOneEditUUID, "galleryOneEdit");
                }
                if(gallery.length >= 2) {
                    $scope.galleryTwoEditUUID = gallery[1];
                    imageServices.downloadPhotos($scope.galleryTwoEditUUID, "galleryTwoEdit");
                }
                if(gallery.length >= 3) {
                    $scope.galleryThreeEditUUID = gallery[2];
                    imageServices.downloadPhotos($scope.galleryThreeEditUUID, "galleryThreeEdit");
                }
                if(gallery.length >= 4) {
                    $scope.galleryFourEditUUID = gallery[3];
                    imageServices.downloadPhotos($scope.galleryFourEditUUID, "galleryFourEdit");
                }
                if(gallery.length >= 5) {
                    $scope.galleryFiveEditUUID = gallery[4];
                    imageServices.downloadPhotos($scope.galleryFiveEditUUID, "galleryFiveEdit");
                }
                if(gallery.length === 6) {
                    $scope.gallerySixEditUUID = gallery[5];
                    imageServices.downloadPhotos($scope.gallerySixEditUUID, "gallerySixEdit");
                }
                $timeout(function () {
                    for(var i = 0 ; i < $scope.userEventsEdit[index].eventDates.length; i++) {
                        dateSetterService.initDate("classDate"+i);
                        $(".classDate"+i).pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.userEventsEdit[index].eventDates[i].date).pDate));
                    }
                    dateSetterService.initDate("blitSaleEndDate");
                    dateSetterService.initDate("blitSaleStartDate");
                    $(".blitSaleEndDate").pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.userEventsEdit[index].blitSaleEndDate).pDate));
                    $(".blitSaleStartDate").pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.userEventsEdit[index].blitSaleStartDate).pDate));
                    $scope.editEventFields.blitSaleEndDate = document.getElementById("eventTicketEndTimeEdit").value;
                    $scope.editEventFields.blitSaleStartDate = document.getElementById("eventTicketStartTimeEdit").value;

                }, 500);
                $timeout(function () {
                    mapMarkerService.setMarker($scope.userEventsEdit[index].latitude, $scope.userEventsEdit[index].longitude);
                    mapMarkerService.initMap(document.getElementById('editEventMap'));
                    mapMarkerService.placeMarker(mapMarkerService.getMarker());
                },500);
            };
            $scope.editEventSans = function (sansIndex) {
                $scope.generateBlitTypeSeats = false;
                $scope.newShowTimeEdit = {blitTypes : []};
                sansWithSeats = [];
                document.getElementById("editEventNewSansSubmit").style.display = "none";
                document.getElementById("createNewSansEdit").style.display = "none";


                sansIndexPicked = sansIndex;
                $scope.$broadcast('blitTypeUidsReset', []);
                dateSetterService.initDate("eventDateClassEdit0");
                $scope.blitTypesWithSeatsEdit = [];
                $scope.blitTypesWithOutSeatsEdit = [];
                $(".eventDateClassEdit0").pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.showTimeEditForms[sansIndex].date).pDate));
                $scope.newShowTimeEdit.persianDate = document.getElementById("editSansDate").value;
                $scope.eventHasSalon = $scope.showTimeEditForms[sansIndex].hasSalon;
                if($scope.eventHasSalon) {
                    document.getElementById("editSansSpinner"+sansIndex).style.display = "inline";
                    seatmapService.getPopulatedSchema($scope.showTimeEditForms[sansIndex].eventDateId)
                        .then(function (data) {
                            document.getElementById("editSansSpinner"+sansIndex).style.display = "none";
                            var populatedSchema = data.data;
                            $scope.blitTypesWithOutSeatsEdit = seatmapService.generateWithoutSeatBlitTypes($scope.showTimeEditForms[sansIndex].blitTypes);
                            $scope.blitTypesWithSeatsEdit = seatmapService.generateWithSeatBlitTypes($scope.showTimeEditForms[sansIndex].blitTypes);
                            if($scope.showTimeEditForms[sansIndex].edited) {
                                populatedSchema = seatmapService.editedPopulatedSchema($scope.blitTypesWithSeatsEdit, populatedSchema);
                            }
                            $scope.$broadcast('newSVGEdit', [populatedSchema, 2]);

                            document.getElementById("editEventSansSubmit").style.display = "block";
                        })
                        .catch(function (data) {
                        })
                } else {
                    $scope.blitTypesWithOutSeatsEdit = $scope.showTimeEditForms[sansIndex].blitTypes;
                    document.getElementById("editEventSansSubmit").style.display = "block";
                }
            };
            $scope.newSansEdit = function () {
                dateSetterService.initDate("newSansEditSubmitDate");
                if(document.getElementById("editEventNewSansSubmit")) {
                    document.getElementById("editEventNewSansSubmit").style.display = "block";
                }
                if(document.getElementById("createNewSansEdit")){
                    document.getElementById("createNewSansEdit").style.display = "none";
                }
                if(document.getElementsByClassName("seatMapSection3")[0]) {
                    document.getElementsByClassName("seatMapSection3")[0].style.display = "none";

                }
                if(document.getElementsByClassName("generateSeatMap3")[0]) {
                    document.getElementsByClassName("generateSeatMap3")[0].style.display = "block";
                }
            };

        $scope.editEventSubmit = function (editEventData) {
            var sendingData = angular.copy(editEventData);
            var latLong = mapMarkerService.getMarker();
            $scope.newShowTimeEditForms = angular.copy($scope.showTimeEditForms);
            $scope.newShowTimeEditForms.map(function (item) {
                delete item.edited;
                item.dateTime =  persianDate(item.date).format("dddd,DD MMMM, ساعت HH:mm");
                return item;
            });
            editEventData.blitSaleEndDate = document.getElementById("eventTicketEndTimeEdit").value;
            editEventData.blitSaleStartDate = document.getElementById("eventTicketStartTimeEdit").value;

            sendingData.blitSaleEndDate = dateSetterService.persianToMs(editEventData.blitSaleEndDate);
            sendingData.blitSaleStartDate = dateSetterService.persianToMs(editEventData.blitSaleStartDate);
            sendingData.eventDates = $scope.newShowTimeEditForms;
            sendingData.latitude = latLong.lat;
            sendingData.longitude = latLong.lng;
            sendingData.additionalFields = $scope.additionalFieldsEdit;

                sendingData.images = [
                    {imageUUID : $scope.eventEditImageId, type : "EVENT_PHOTO"},
                    {imageUUID : $scope.galleryOneEditUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryTwoEditUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryThreeEditUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryFourEditUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryFiveEditUUID, type : "GALLERY"},
                    {imageUUID : $scope.gallerySixEditUUID, type : "GALLERY"}
                ];
                if(!$scope.eventEditImageId){
                    delete sendingData.images;
                }
                sendingData.images = sendingData.images.filter(function (item) {
                    return !(item.imageUUID === null || item.imageUUID === undefined);
                });
                $scope.editEventSpinner = true;
                eventService.editEvent(sendingData)
                    .then(function () {
                        $scope.editEventSpinner = false;
                        $scope.editEventNotif = true;
                        $scope.editEventErrorNotif = false;
                    })
                    .catch(function (data) {
                        $scope.editEventErrorNotif = true;
                        $scope.editEventSpinner = false;
                        document.getElementById("editEventErrorNotif").innerHTML= data.data.message;
                    })
            };

            //==================================================== ********* =================================


            //==================================================== EDIT HOST =================================
            $scope.eventPlannerEdit = {};
            $scope.editHost = function (index) {
                $scope.submitPlannerEditNotif = false;
                $scope.plannerEditPhotoSuccess = false;
                $scope.coverEditPhotoSuccess = false;
                $scope.eventPlannerEdit = $scope.eventHosts[index];
                $("#editHost").modal("show");
                $scope.eventHosts[index].images.forEach(function (item) {
                    if(item.type === "HOST_PHOTO") {
                        $scope.plannerImageIdEdit = item.imageUUID;
                    } else if (item.type === "HOST_COVER_PHOTO") {
                        $scope.coverImageIdEdit = item.imageUUID;
                    }
                });
                switch ($scope.coverImageIdEdit) {
                    case 'HOST-COVER-PHOTO-1' :
                        $(document.getElementById("coverPhotoOneEdit")).addClass('photoBorder');
                        break;
                    case 'HOST-COVER-PHOTO-2' :
                        $(document.getElementById("coverPhotoTwoEdit")).addClass('photoBorder');
                        break;
                    case 'HOST-COVER-PHOTO-3' :
                        $(document.getElementById("coverPhotoThreeEdit")).addClass('photoBorder');
                        break;
                    case 'HOST-COVER-PHOTO-4' :
                        $(document.getElementById("coverPhotoFourEdit")).addClass('photoBorder');
                        break;
                    default :
                        break;
                }
                imageServices.downloadPhotos('HOST-COVER-PHOTO-1', "coverPhotoOneEdit");
                imageServices.downloadPhotos('HOST-COVER-PHOTO-2', "coverPhotoTwoEdit");
                imageServices.downloadPhotos('HOST-COVER-PHOTO-3', "coverPhotoThreeEdit");
                imageServices.downloadPhotos('HOST-COVER-PHOTO-4', "coverPhotoFourEdit");

                imageServices.downloadPhotos($scope.plannerImageIdEdit, "eventPlannerPhotoUploadEdit");
                imageServices.downloadPhotos($scope.coverImageIdEdit, "coverPhotoUploadEdit");
            };
            $scope.editHostSubmit = function (editHostData) {
                editHostData.telephone = dataService.persianToEnglishDigit(persianJs(editHostData.telephone).englishNumber().toString());
                editHostData.images.forEach(function (item) {
                    if(item.type === "HOST_PHOTO") {
                        item.imageUUID = $scope.plannerImageIdEdit;
                    } else if (item.type === "HOST_COVER_PHOTO") {
                        item.imageUUID = $scope.coverImageIdEdit;
                    }
                });
                if(!$scope.plannerImageIdEdit && !$scope.coverImageIdEdit){
                    delete editHostData.images;
                }
                $scope.editPlannerSpinner = true;
                plannerService.editPlannerForm(editHostData)
                    .then(function () {
                        $scope.submitPlannerEditNotif = true;
                        $scope.editPlannerSpinner = false;
                        $scope.submitPlannerEditErrorNotif = false;
                    })
                    .catch(function (data) {
                        $scope.submitPlannerEditErrorNotif = true;
                        $scope.editPlannerSpinner = false;
                        document.getElementById("submitPlannerEditErrorNotif").innerHTML= data.data.message;
                    })
            };
            //==================================================== ********* =================================
            //==================================================== EDIT EXCHANGE =================================
            $scope.editExchangeNotif = false;
            $scope.editExchangeSpinner = false;

            $scope.editExchange = function (index) {
                $scope.editExchangeNotif = false;
                $scope.exchangeEditPhotoSuccess = false;
                $scope.exchangeEdit = {
                    title : $scope.exchangeEditTickets[index].title,
                    blitCost : $scope.exchangeEditTickets[index].blitCost,
                    isBlitoEvent : $scope.exchangeEditTickets[index].blitoEvent,
                    eventDate : persianDate($scope.exchangeEditTickets[index].eventDate).pDate,
                    type : $scope.exchangeEditTickets[index].type,
                    exchangeBlitId : $scope.exchangeEditTickets[index].exchangeBlitId,
                    description :  $scope.exchangeEditTickets[index].description,
                    eventAddress : $scope.exchangeEditTickets[index].eventAddress,
                    email : $scope.exchangeEditTickets[index].email,
                    phoneNumber : $scope.exchangeEditTickets[index].phoneNumber

                };
                var latlng = {
                    lat : $scope.exchangeEditTickets[index].latitude,
                    lng : $scope.exchangeEditTickets[index].longitude
                };
                dateSetterService.initDate("persianEditExchangeTime");
                $(".persianEditExchangeTime").pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.exchangeEditTickets[index].eventDate).pDate));
                $scope.exchangeEdit.eventDate = document.getElementById("eventEditExchangeDate").value;
                $scope.exchangeEditImageId = $scope.exchangeEditTickets[index].image.imageUUID;
                imageServices.downloadPhotos($scope.exchangeEditImageId, "exchangePhotoUploadEdit");
                mapMarkerService.initMap(document.getElementById('mapExchangeEdit'));
                mapMarkerService.setMarker(latlng.lat, latlng.lng);
                mapMarkerService.placeMarker(mapMarkerService.getMarker());
                $("#editExchange").modal("show");
            };

            $scope.editExchangeTicket = function (editExchangeData) {
                var latLng = mapMarkerService.getMarker();
                $scope.editExchangeSpinner = true;
                editExchangeData.eventDate = document.getElementById("eventEditExchangeDate").value;
                editExchangeData.eventDate = dateSetterService.persianToMs(editExchangeData.eventDate);
                editExchangeData.exchangeBlitId = $scope.exchangeEdit.exchangeBlitId;
                editExchangeData.latitude = latLng.lat;
                editExchangeData.longitude = latLng.lng;
                editExchangeData.phoneNumber = dataService.persianToEnglishDigit(persianJs(editExchangeData.phoneNumber).englishNumber().toString());
                editExchangeData.image = {
                    imageUUID : $scope.exchangeEditImageId,
                    type : "EXCHANGEBLIT_PHOTO"
                };
                if(!$scope.exchangeEditImageId){
                    delete editExchangeData.image;
                }
                exchangeService.editExchangeForm(editExchangeData)
                    .then(function () {
                        $scope.editExchangeNotif = true;
                        $scope.editExchangeSpinner = false;
                        $scope.editExchangeErrorNotif = false;
                    }, function (data) {
                        $scope.editExchangeErrorNotif = true;
                        $scope.editExchangeSpinner = false;
                        document.getElementById("editExchangeErrorNotif").innerHTML= data.data.message;

                    })
            };
            //==================================================== ********* =================================
            //==================================================== SETTINGS =================================
            var settingIndex;
            $scope.showSetting = function (index) {
                document.getElementsByClassName("approveSuccessSetting")[0].style.display = "none";
                document.getElementsByClassName("approveErrorSetting")[0].style.display = "none";

                settingIndex = index;
                $scope.eventStateSetting = $scope.userEvents[index].eventState;

                $("#settingModal").modal("show");
                document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
            };

            $scope.deleteEvent = function () {
                document.getElementsByClassName("deleteSpinner")[0].style.display = "inline";
                eventService.deleteEvent($scope.userEvents[settingIndex].eventId)
                    .then(function () {
                        document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
                        document.getElementsByClassName("approveSuccessSetting")[0].style.display = "block";

                    })
                    .catch(function (data) {
                        document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
                        document.getElementById("approveErrorSetting").innerHTML = data.data.message;
                        document.getElementsByClassName("approveErrorSetting")[0].style.display = "block";

                    })
            };

            var settingExchangeIndex;
            $scope.showSettingExchange = function (index) {
                $("#settingExchangeModal").modal("show");
                document.getElementsByClassName("exchangeStatusSpinner")[0].style.display = "none";
                document.getElementById("approveSuccessSettingExchange").style.display = "none";
                document.getElementById("approveErrorSettingExchange").style.display = "none";
                document.getElementsByClassName("deleteExchangeSpinner")[0].style.display = "none";

                settingExchangeIndex = index;
                $scope.exchangeStateSetting = $scope.exchangeEditTickets[index].state;

                document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
            };
            $scope.deleteExchange= function () {
                document.getElementsByClassName("deleteExchangeSpinner")[0].style.display = "inline";
                exchangeService.deleteExchange($scope.exchangeEditTickets[settingExchangeIndex].exchangeBlitId)
                    .then(function () {
                        document.getElementById("approveSuccessSettingExchange").style.display = "inline";
                        document.getElementsByClassName("deleteExchangeSpinner")[0].style.display = "none";
                    })
                    .catch(function (data) {
                        document.getElementById("approveErrorSettingExchange").innerHTML = data.data.message;
                        document.getElementById("approveErrorSettingExchange").style.display = "inline";
                        document.getElementsByClassName("deleteExchangeSpinner")[0].style.display = "none";
                    })
            };
            $scope.changeExchangeState = function (stateChange) {
                var stateData = {
                    exchangeBlitId : $scope.exchangeEditTickets[settingExchangeIndex].exchangeBlitId,
                    state : stateChange
                };
                document.getElementsByClassName("exchangeStatusSpinner")[0].style.display = "inline";
                exchangeService.editExchangeState(stateData)
                    .then(function () {
                        document.getElementById("approveSuccessSettingExchange").style.display = "inline";
                        document.getElementsByClassName("exchangeStatusSpinner")[0].style.display = "none";
                    })
                    .catch(function (data) {
                        document.getElementById("approveErrorSettingExchange").innerHTML = data.data.message;
                        document.getElementById("approveErrorSettingExchange").style.display = "inline";
                        document.getElementsByClassName("exchangeStatusSpinner")[0].style.display = "none";
                    })
            };
            var plannerSettingIndex;
            $scope.showSettingPlanner = function (index) {
                plannerSettingIndex = index;
                document.getElementsByClassName("deletePlannerSpinner")[0].style.display = "none";
                document.getElementsByClassName("approveSuccessSettingPlanner")[0].style.display = "none";
                document.getElementsByClassName("approveErrorSettingPlanner")[0].style.display = "none";
                $("#settingPlannerModal").modal("show");
            };
            $scope.deletePlanner = function () {
                document.getElementsByClassName("deletePlannerSpinner")[0].style.display = "inline";
                plannerService.deletePlanner($scope.eventHosts[plannerSettingIndex].eventHostId)
                    .then(function () {
                        document.getElementsByClassName("approveSuccessSettingPlanner")[0].style.display = "block";
                        document.getElementsByClassName("deletePlannerSpinner")[0].style.display = "none";
                    })
                    .catch(function () {
                        document.getElementById("approveErrorSettingPlanner").innerHTML = data.data.message;
                        document.getElementsByClassName("deletePlannerSpinner")[0].style.display = "none";
                        document.getElementsByClassName("approveErrorSettingPlanner")[0].style.display = "inline";
                    })
            };
            //==================================================== ********* =================================
            //==================================================== GET DATA =================================
            $scope.currentPageEvent = 1;
            $scope.currentPage = 1;
            $scope.currentPageDiscount = 1;
            $scope.pageChangedEvents = function(newPage) {
                $scope.getUserEvents(newPage);
            };
            $scope.plannersPageChanged = function(newPage) {
                $scope.getPlannersData(newPage);
            };
            $scope.exchangePageChanged = function(newPage) {
                $scope.getExchangeData(newPage);
            };
            $scope.userTicketsPageChanged = function (newPage) {
                $scope.getUserTickets(newPage);
            };
            //==================================================== ********* =================================
            //==================================================== GET DATA =================================
            $scope.mapMarkerClickCheckEvent = true;
            $scope.mapMarkerClickCheckExchange = true;
            $scope.clickMapValidationChangeEvent = function () {
                $scope.mapMarkerClickCheckEvent = false;
            };
            $scope.clickMapValidationChangeExchange = function () {
                $scope.mapMarkerClickCheckExchange = false;
            };
            $scope.getTicket = function (trackCode) {
                $window.open('/payment/'+trackCode);
            };
            $scope.dateClassGetTicket = function (i) {
                return "classDateTicket"+i;
            };
            $scope.getTicketsSubmit = function (index) {
                ticketsService.getExcelTickets($scope.eventDatesTickets[index].eventDateId)
                    .then(function (data) {
                        var excelData = new Blob([data.data], { type: 'application/vnd.ms-excel;charset=UTF-8'});
                        FileSaver.saveAs(excelData, 'blits.xls');
                    })
                    .catch(function (data) {
                    })
            };
            $scope.getTicketsSubmitWithSeat = function (index) {
                ticketsService.getExcelTicketsWithSeat($scope.eventDatesTickets[index].eventDateId)
                    .then(function (data) {
                        var excelData = new Blob([data.data], { type: 'application/vnd.ms-excel;charset=UTF-8'});
                        FileSaver.saveAs(excelData, 'blits.xls');
                    })
                    .catch(function (data) {
                    })
            };
            $scope.showTickets = function (index) {
                $scope.eventTicketsPageChanged = function (newPage) {
                    $scope.getEventTickets(newPage);
                };
                $scope.eventTicketsPageWithSeatChanged = function (newPage) {
                    $scope.getEventTicketsWithSeat(newPage);
                };
                $scope.eventDatesTickets = $scope.userEvents[index].eventDates;
                $timeout(function () {
                    for(var i = 0 ; i < $scope.eventDatesTickets.length; i++) {
                        $(".classDateTicket"+i).val(persianDate($scope.eventDatesTickets[i].date).format("dddd,DD MMMM, ساعت HH:mm"));
                    }
                    for(var i = 0 ; i < $scope.eventDatesTickets.length; i++) {
                        $(".dateClassWithSeat"+i).val(persianDate($scope.eventDatesTickets[i].date).format("dddd,DD MMMM, ساعت HH:mm"));
                    }
                }, 500);

                $scope.getEventTickets = function (pageNumber) {
                    ticketsService.getEventTickets(pageNumber, $scope.userEvents[index].eventId)
                        .then(function (data) {
                            $scope.totalTicketNumber = data.data.totalElements;
                            $scope.eventsTickets = data.data.content;
                            $scope.eventsTickets = $scope.eventsTickets.map(function (ticket) {
                                ticket.paymentStatus = dataService.ticketStatusPersian(ticket.paymentStatus);
                                return ticket;
                            })
                        })
                        .catch(function (data) {
                        })
                };
                $scope.getEventTicketsWithSeat = function (pageNumber) {
                    ticketsService.getEventTicketsWithSeat(pageNumber, $scope.userEvents[index].eventId)
                        .then(function (data) {
                            $scope.totalTicketNumberWithSeat = data.data.totalElements;
                            $scope.eventsTicketsWithSeat = data.data.content;
                            $scope.eventsTicketsWithSeat = $scope.eventsTicketsWithSeat.map(function (ticket) {
                                ticket.paymentStatus = dataService.ticketStatusPersian(ticket.paymentStatus);
                                return ticket;
                            })
                        })
                        .catch(function (data) {
                        })
                };
                $scope.getEventTickets(1);
                $scope.getEventTicketsWithSeat(1);
                $('#eventTickets').modal('show');
            };

            $scope.getExchangeData = function (page) {
                exchangeService.getExchangeTickets(page)
                    .then(function (data) {
                        $scope.totalExchangeNumber = data.data.totalElements;
                        $scope.exchangeTickets = data.data.content;
                        $scope.exchangeEditTickets = angular.copy(data.data.content);
                        $scope.exchangeTickets = $scope.exchangeTickets.map(function (ticket) {
                            ticket.operatorState = dataService.operatorStatePersian(ticket.operatorState);
                            ticket.state = dataService.stateTypePersian(ticket.state);
                            ticket.type = dataService.eventTypePersian(ticket.type);
                            ticket.eventDate = persianDate(ticket.eventDate).pDate;
                            return ticket;
                        })

                    }, function (data, status) {
                    })
            };
            $scope.getDataUserEvents = [];
            $scope.getUserEvents = function (pageNumber) {
                eventService.getUserEvents(pageNumber)
                    .then(function (data) {
                        $scope.totalEventsNumber = data.data.totalElements;
                        $scope.userEvents = data.data.content;

                        $scope.userEventsEdit = angular.copy(data.data.content);
                        $scope.userEvents = $scope.userEvents.map(function (event) {
                            event.eventType = dataService.eventTypePersian(event.eventType);
                            event.blitSaleEndDate = persianDate(event.blitSaleEndDate).pDate;
                            event.blitSaleStartDate = persianDate(event.blitSaleStartDate).pDate;
                            event.operatorState = dataService.operatorStatePersian(event.operatorState);
                            return event;
                        });
                        $scope.getDataUserEvents = angular.copy($scope.userEvents);
                        $scope.getDataUserEvents = $scope.getDataUserEvents.map(function (event) {
                            event.eventState = dataService.stateTypePersian(event.eventState);
                            return event;
                        });

                    }, function (data, status) {
                    })
            };
            $scope.getPlannersData = function (page) {
                plannerService.getPlanners(page, 4)
                    .then(function (data) {
                        $scope.totalPlannersNumber = data.data.totalElements;
                        $scope.eventHosts = data.data.content;
                    }, function (data) {
                    })
            };
            $scope.getPlannersDataList = function () {
                plannerService.getPlanners(1, 1000)
                    .then(function (data) {
                        $scope.eventHostsList = data.data.content;
                    }, function (data) {
                    })
            };
            $scope.getUserTickets = function (pageNumber) {
                ticketsService.getUserTickets(pageNumber)
                    .then(function (data) {
                        $scope.totalUserTickets = data.data.totalElements;
                        $scope.userTickets = data.data.content;
                        $scope.userTickets = $scope.userTickets.map(function (ticket) {
                            ticket.paymentStatus = dataService.ticketStatusPersian(ticket.paymentStatus);
                            return ticket;
                        })
                    })
                    .catch(function (data) {
                    })
            };

            $scope.getUserTickets(1);

            //==================================================== ********* =================================
            //==================================================== DISCOUNT SECTION =======================
            $scope.discountCodeShow = function (index) {
                document.getElementById("successDiscount").style.display = "none";
                document.getElementById("errorDiscount").style.display = "none";
                document.getElementById("activationDiscountSuccess").style.display = "none";
                document.getElementById("activationDiscountError").style.display = "none";
                $scope.eventDatesDiscount = [];
                $scope.eventChosen = false;
                $scope.sansChosen = false;
                $scope.blitChosen = false;
                $scope.blitIsPicked = false;
                $scope.sansisPicked = false;
                $scope.eventDiscount = angular.copy($scope.userEvents[index]);
                $scope.flatEventDates($scope.eventDiscount.eventDates);
                $timeout(function () {
                    dateSetterService.initDate("discountStartTime");
                    dateSetterService.initDate("discountEndTime");
                }, 1000);
                $scope.currentPageDiscount = 1;
                $scope.discountList(1);
                $('#discount-codes').modal('show');
            };
            $scope.sansDates = function (index) {
                return "discountTime"+index;
            };
            $scope.eventChosen = false;
            $scope.discountEvent = function () {
                setBlitTypeIds($scope.eventDiscount.eventDates);
                $scope.eventChosen = true;
                $scope.blitChosen = false;
                $scope.sansChosen = false;
                $scope.sansisPicked = false;
                $scope.blitIsPicked = false;
            };
            $scope.sansChosen = false;
            $scope.discountSans = function () {
                $scope.sansChosen = true;
                $scope.blitChosen = false;
                $scope.eventChosen = false;
                $scope.blitIsPicked = false;
                $scope.eventDatesDiscount = $scope.eventDiscount.eventDates;
                $timeout(function () {
                    for(var i = 0 ; i < $scope.eventDatesDiscount.length; i++) {
                        $(".discountTime"+i).html(persianDate($scope.eventDatesDiscount[i].date).format("dddd,DD MMMM, ساعت HH:mm"));
                    }
                }, 500);
            };
            $scope.blitChosen = false;
            $scope.discountBlit = function () {
                $scope.blitChosen = true;
                $scope.sansChosen = false;
                $scope.eventChosen = false;
                $scope.sansisPicked = false;
            };
            $scope.sansPicked = function (index) {
                setBlitTypeIds([$scope.eventDatesDiscount[index]]);
                $scope.sansisPicked = true;
            };
            $scope.blitPicked = function (index) {
                $scope.blitTypeIds = [];
                $scope.blitTypeIds[0] = $scope.eventFlatDates[index].blitTypeId;
                $scope.blitIsPicked = true;
            };
            var setBlitTypeIds = function (eventDates) {
                var blitTypeIds = eventDates.map(function (sans) {
                    return sans.blitTypes.map(function (blitType) {
                        return blitType.blitTypeId;
                    });
                });
                $scope.blitTypeIds = [];
                for(var i = 0; i < blitTypeIds.length; i++)
                {
                    $scope.blitTypeIds = $scope.blitTypeIds.concat(blitTypeIds[i]);
                }
            };
            $scope.eventFlatDates = [];
            $scope.flatEventDates = function (dates) {
                var index = 0;
                for(var i = 0; i < dates.length; i++) {
                    for(var j = 0; j < dates[i].blitTypes.length; j++) {
                        $scope.eventFlatDates[index] = {
                            name : dates[i].blitTypes[j].name,
                            capacity : dates[i].blitTypes[j].capacity,
                            blitTypeState : dates[i].blitTypes[j].blitTypeState,
                            price : dates[i].blitTypes[j].price,
                            date : dates[i].date,
                            soldCount : dates[i].blitTypes[j].soldCount,
                            isFree : dates[i].blitTypes[j].isFree,
                            blitTypeId : dates[i].blitTypes[j].blitTypeId

                        } ;
                        index++;
                    }
                }
            };
            $scope.discountList = function (page) {
                eventService.searchDiscount(page, $scope.eventDiscount.eventId)
                    .then(function (data) {
                        $scope.totalDiscounts = data.data.totalElements;
                        $scope.discountsList = data.data.content;
                        $timeout(function () {
                            for(var i = 0 ; i < $scope.discountsList.length; i++) {
                                $(".discountStartClass"+i).html(persianDate($scope.discountsList[i].effectDate).format("YYYY/MM/DD , HH:mm"));
                                $(".discountEndClass"+i).html(persianDate($scope.discountsList[i].expirationDate).format("YYYY/MM/DD , HH:mm"));
                            }
                        }, 300);
                    })
                    .catch(function (data) {
                    })
            };
            $scope.discountStateChange = function (state, id, i) {
                document.getElementById("activationDiscountSuccess").style.display = "none";
                document.getElementById("activationDiscountError").style.display = "none";
                var discountChangeState = {
                    discountId : id,
                    enable : state
                };
                if(state) {
                    document.getElementsByClassName("activateDiscount" + i)[0].style.display = "inline";
                } else {
                    document.getElementsByClassName("deActivateDiscount" + i)[0].style.display = "inline";
                }
                eventService.discountState(discountChangeState)
                    .then(function () {
                        document.getElementById("activationDiscountSuccess").style.display = "block";
                        if(state) {
                            document.getElementsByClassName("activateDiscount" + i)[0].style.display = "none";
                        } else {
                            document.getElementsByClassName("deActivateDiscount" + i)[0].style.display = "none";
                        }
                    })
                    .catch(function (data) {
                        document.getElementById("activationDiscountError").innerHTML= data.data.message;
                        document.getElementById("activationDiscountError").style.display = "block";
                        if(state) {
                            document.getElementsByClassName("activateDiscount" + i)[0].style.display = "none";
                        } else {
                            document.getElementsByClassName("deActivateDiscount" + i)[0].style.display = "none";
                        }
                    })
            };
            $scope.activateDiscount = function (i) {
                return "activateDiscount"+i;
            };
            $scope.deActivateDiscount = function (i) {
                return "deActivateDiscount"+i;
            };
            $scope.discountStartDateClass = function (i) {
                return "discountStartClass"+i;
            };
            $scope.discountEndDateClass = function (i) {
                return "discountEndClass"+i;
            };
            $scope.eventDiscountPageChanged = function (newPage) {
                $scope.discountList(newPage);
            };

            $scope.discountSubmitOnce = true;
            $scope.submitDiscountCode = function (discountData) {
                document.getElementById("successDiscount").style.display = "none";
                document.getElementById("errorDiscount").style.display = "none";
                var discountSubmit = {};
                $scope.discountSubmitOnce = false;
                discountSubmit.code = discountData.code;
                discountSubmit.reusability = discountData.reusability;
                discountSubmit.effectDate = dateSetterService.persianToMs(discountData.effectDatePersian);
                discountSubmit.expirationDate = dateSetterService.persianToMs(discountData.expirationDatePersian);
                discountSubmit.isPercent = true;
                discountSubmit.amount = 0;
                discountSubmit.percentage = parseInt(discountData.percentage);
                discountSubmit.blitTypeIds = $scope.blitTypeIds;
                document.getElementsByClassName("discountSpinner")[0].style.display = "inline";
                eventService.submitDiscount(discountSubmit)
                    .then(function () {
                        document.getElementById("successDiscount").style.display = "block";
                        document.getElementsByClassName("discountSpinner")[0].style.display = "none";
                        $scope.discountSubmitOnce = true;
                    })
                    .catch(function (data) {
                        $scope.discountSubmitOnce = true;
                        document.getElementById("errorDiscount").innerHTML= data.data.message;
                        document.getElementById("errorDiscount").style.display = "block";
                        document.getElementsByClassName("discountSpinner")[0].style.display = "none";
                    })
            };
            var discountIndex = 0;
            $scope.discountEditProperties= { reusability : 0 };
            $scope.editDiscount = function (index) {
                document.getElementById("successEditDiscount").style.display = "none";
                document.getElementById("errorEditDiscount").style.display = "none";
                discountIndex = index;
                $scope.discountEditProperties.reusability = $scope.discountsList[index].reusability;
                dateSetterService.initDate("discountEditStartTime");
                $(".discountEditStartTime").pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.discountsList[index].effectDate).pDate));
                dateSetterService.initDate("discountEditEndTime");
                $(".discountEditEndTime").pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.discountsList[index].expirationDate).pDate));
                $('#discount-codes').modal('hide');
                $('#discount-code-edit').modal('show');
            };
            $scope.editDiscountSubmit = function (discountEditData) {
                var discountEditSubmit = {
                    amount : 0,
                    blitTypeIds : $scope.discountsList[discountIndex].blitTypeIds,
                    code : $scope.discountsList[discountIndex].code,
                    discountId : $scope.discountsList[discountIndex].discountId,
                    effectDate : dateSetterService.persianToMs(discountEditData.effectDatePersian),
                    expirationDate : dateSetterService.persianToMs(discountEditData.expirationDatePersian),
                    isEnabled : $scope.discountsList[discountIndex].isEnabled,
                    isPercent : true,
                    percentage : $scope.discountsList[discountIndex].percentage,
                    reusability : discountEditData.reusability,
                    used : $scope.discountsList[discountIndex].used
                };
                document.getElementsByClassName("discountEditSpinner")[0].style.display = "inline";
                eventService.discountEdit(discountEditSubmit)
                    .then(function () {
                        document.getElementsByClassName("discountEditSpinner")[0].style.display = "none";
                        document.getElementById("errorEditDiscount").style.display = "none";
                        document.getElementById("successEditDiscount").style.display = "block";

                    })
                    .catch(function (data) {
                        document.getElementById("successEditDiscount").style.display = "none";
                        document.getElementsByClassName("discountEditSpinner")[0].style.display = "none";
                        document.getElementById("errorEditDiscount").innerHTML= data.data.message;
                        document.getElementById("errorEditDiscount").style.display = "block";
                    })
            };
            //==================================================== ********* =================================

            //==================================================== ADDITIONAL FIELDS =======================
            $scope.additionalFields = [];
            $scope.additionalFieldsEdit = [];
            $scope.additionalFieldsSection = false;
            $scope.openAdditionalFields = function () {
                document.getElementById("additionalFieldsSection").style.display = "block";
            };
            $scope.openAdditionalFieldsEdit = function () {
                $scope.additionalFieldsSection = true;
            };
            $scope.addAdditionalFields = function () {
                $scope.additionalFields.push({ key : "", value : "string"});
            };
            $scope.deleteAdditionalFields = function () {
                if($scope.additionalFields.length > 0) {
                    $scope.additionalFields.splice(-1, 1);
                }
            };
            $scope.addAdditionalFieldsEdit = function () {
                $scope.additionalFieldsEdit.push({ key : "", value : "string"});
            };
            $scope.deleteAdditionalFieldsEdit = function (i) {
                $scope.additionalFieldsEdit.splice(i, 1);
            };
            //==================================================== ********* =================================
            //==================================================== PERSIAN DATE PICKER =======================
            $timeout(function () {
                dateSetterService.initDate("persianTimeEventStart");
                dateSetterService.initDate("persianTimeEventEnd");
                dateSetterService.initDate("persianExchangeTime");
                if(document.getElementById("eventExchangeDateMain")) {
                    $scope.exchange.eventTime = document.getElementById("eventExchangeDateMain").value;
                }
                if(document.getElementById("eventTicketStartTimeMain")) {
                    $scope.eventFields.ticketStartTime = document.getElementById("eventTicketStartTimeMain").value;
                }
                if(document.getElementById("eventTicketEndTimeMain")) {
                    $scope.eventFields.ticketEndTime = document.getElementById("eventTicketEndTimeMain").value;
                }
            }, 1000);
            //==================================================== ********* =================================
            $scope.showMenuOnSm = function () {
                $('.profileTabs').toggleClass("pullMenuLeft");
                $('.profileToggleMenu').toggleClass("rotateToggleMenu");
            };
            $scope.uploadUserPhoto = false;
            $scope.showUploadPhoto = function () {
                $scope.uploadUserPhoto = true;
            };
            $scope.uploadUserPhotoEdit = false;
            $scope.showUploadPhotoEdit = function () {
                $scope.uploadUserPhotoEdit = true;
            };
            imageServices.downloadPhotos('HOST-COVER-PHOTO-1', "coverPhotoOne");
            imageServices.downloadPhotos('HOST-COVER-PHOTO-2', "coverPhotoTwo");
            imageServices.downloadPhotos('HOST-COVER-PHOTO-3', "coverPhotoThree");
            imageServices.downloadPhotos('HOST-COVER-PHOTO-4', "coverPhotoFour");
            $scope.coverPickedDefault = function (e, photoNumber) {
                $('.plannerCoverDefault').removeClass('photoBorder');
                $(e.currentTarget).addClass('photoBorder');
                switch (photoNumber) {
                    case 1 :
                        $scope.coverImageId = 'HOST-COVER-PHOTO-1';
                        break;
                    case 2 :
                        $scope.coverImageId = 'HOST-COVER-PHOTO-2';
                        break;
                    case 3 :
                        $scope.coverImageId = 'HOST-COVER-PHOTO-3';
                        break;
                    case 4 :
                        $scope.coverImageId = 'HOST-COVER-PHOTO-4';
                        break;
                    default :
                        $scope.coverImageId = '';
                        break;
                }

            };
            $scope.coverPickedDefaultEdit = function (e, photoNumber) {
                $('.plannerCoverDefault').removeClass('photoBorder');
                $(e.currentTarget).addClass('photoBorder');
                switch (photoNumber) {
                    case 1 :
                        $scope.coverImageIdEdit = 'HOST-COVER-PHOTO-1';
                        break;
                    case 2 :
                        $scope.coverImageIdEdit = 'HOST-COVER-PHOTO-2';
                        break;
                    case 3 :
                        $scope.coverImageIdEdit = 'HOST-COVER-PHOTO-3';
                        break;
                    case 4 :
                        $scope.coverImageIdEdit = 'HOST-COVER-PHOTO-4';
                        break;
                    default :
                        $scope.coverImageIdEdit = '';
                        break;
                }
            };
            $timeout(function () {
                var elements = document.querySelectorAll('input,select,textarea,checkbox');
                var invalidListener = function(e){ e.preventDefault(); };

                for(var i = elements.length; i--;) {
                    elements[i].addEventListener('invalid', invalidListener);
                }
            }, 1000);
            //==================================================== seatmap =======================
            var sansPickedGenerateTicket = {};
            $scope.openGuestTicketModal = function (index) {
                $('#guest-modal').modal('show');
                $scope.eventDatesGuestTicket = $scope.userEvents[index].eventDates;
                $timeout(function () {
                    for(var i = 0 ; i < $scope.eventDatesGuestTicket.length; i++) {
                        $(".dateClassGuestTicket"+i).val(persianDate($scope.eventDatesGuestTicket[i].date).format("dddd,DD MMMM, ساعت HH:mm"));
                    }
                }, 500);
                $scope.generateSeatMapGuestTicket = function (sansIndex) {
                    document.getElementsByClassName("seatMapLoading")[0].style.display = "block";
                    sansPickedGenerateTicket = $scope.eventDatesGuestTicket[sansIndex];
                    seatmapService.getPopulatedSchema($scope.eventDatesGuestTicket[sansIndex].eventDateId)
                        .then(function (data) {
                            document.getElementsByClassName("seatMapHelpSection")[0].style.display = "block";
                            document.getElementsByClassName("seatMapLoading")[0].style.display = "none";
                            var populatedSchema = data.data;
                            $scope.$broadcast('newSVGGenrateTicket', [populatedSchema, 5]);
                        })
                        .catch(function (data) {
                            document.getElementsByClassName("seatMapLoading")[0].style.display = "none";
                        })
                };

            };
            $scope.generateTicketForGuest = function () {
                document.getElementById("generateBlitForGuestSpinner").style.display = "inline";
                var guestData = {
                    eventDateId : sansPickedGenerateTicket.eventDateId,
                    eventDateAndTime : persianDate(sansPickedGenerateTicket.date).format("dddd,DD MMMM, ساعت HH:mm"),
                    seatUid : $scope.seatBlitUidsGenerateTicket[0]
                };
                seatmapService.getGuestTicket(guestData)
                    .then(function (data) {
                        $scope.seatBlitUidsGenerateTicket = [];
                        var pdfData = new Blob([data.data], { type: 'application/pdf;charset=UTF-8'});
                        FileSaver.saveAs(pdfData, 'blit.pdf');
                        $scope.$broadcast('resetGuestListPicked', []);
                        document.getElementById("generateBlitForGuestSpinner").style.display = "none";
                    })
                    .catch(function (data) {
                        document.getElementById("generateBlitForGuestSpinner").style.display = "none";
                    })
            };
            $scope.sansSet = function () {
                $timeout(function () {
                    dateSetterService.initDate("eventDateClass0");
                    if(document.getElementById("createSansWithoutSeat")) {
                        $scope.newShowTime.persianDate = document.getElementById("createSansWithoutSeat").value;
                    }
                    if(document.getElementById("createSansWithSeat")) {
                        $scope.newShowTime.persianDate = document.getElementById("createSansWithSeat").value;
                    }
                },1000)
            };
            $scope.getSalonList = function () {
                seatmapService.getSeatmapList()
                    .then(function (data) {
                        $scope.seatMapList = data.data.content;
                    })
                    .catch(function (data) {
                    })
            };

            $scope.schemaNumberOfSeats = 0;
            $scope.allCheckedSeats = 0;
            $scope.allCheckedSeatsSecondSeatMap = 0;
            var countNumberOfSeats = function (sections) {
                sections.forEach(function (section) {
                    section.rows.forEach(function (row) {
                        $scope.schemaNumberOfSeats += row.numberOfSeats;
                    })
                })
            };
            $scope.salonSchema = {sections : []};
            $scope.salonSeatPicker = function (salonUID, seatMapIndex) {
                for(var i = 0; i < 4 ; i++) {
                    if(document.getElementsByClassName("seatMapSection"+i)[0]) {
                        if(i !== 2) {
                            document.getElementsByClassName("seatMapSection" + i)[0].style.display = "none";
                        }
                        if(document.getElementsByClassName("generateSeatMap"+i)[0]) {
                            document.getElementsByClassName("generateSeatMap"+i)[0].style.display = "block";
                        }
                    }


                }
                document.getElementsByClassName("seatMapSection"+seatMapIndex)[0].style.display = "block";
                document.getElementsByClassName("seatMapSpinner")[0].style.display = "inline";

                seatmapService.getSalonData(salonUID)
                    .then(function (data) {
                        $scope.schemaNumberOfSeats = 0;
                        if(seatMapIndex === 0) {
                            $scope.allCheckedSeats = 0;
                        } else {
                            $scope.allCheckedSeatsSecondSeatMap = 0;
                        }
                        if(document.getElementById("successSeatBlitTypeEditSubmit")){
                            document.getElementById("successSeatBlitTypeEditSubmit").style.display = "none";
                        }
                        sansWithSeats = [];
                        $scope.seatBlitUids = [];
                        document.getElementsByClassName("seatMapSpinner")[0].style.display = "none";
                        $scope.salonSchema = data.data;
                        countNumberOfSeats($scope.salonSchema.schema.sections);
                        $scope.$broadcast('newSVG', [$scope.salonSchema, seatMapIndex]);
                    })
                    .catch(function (data) {
                        document.getElementsByClassName("seatMapSpinner")[0].style.display = "none";
                    })
            };

            $scope.blitTypeSubmited = false;
            $scope.seatsPickedBlitType = function () {
                document.getElementById("successSeatBlitType").style.display = "none";
                $scope.blitTypeSubmited = false;
                $scope.seatsPickedForm = {};
                $('#seatsPickedModel').modal('show');
                $('.modal-open').css('position', 'relative');
            };
            $scope.seatsPickedBlitTypeEditNewSans = function () {
                document.getElementById("blitTypeSeatsEdit").style.display = "block";
                document.getElementById("successSeatBlitTypeEditSubmit").style.display = "none";
                if(document.getElementById("successSeatBlitTypeEditSubmit")) {
                    document.getElementById("successSeatBlitTypeEditSubmit").style.display = "none";
                }


            };
            $scope.generateBlitTypeSeats = false;
            $scope.seatsPickedBlitTypeEdit = function () {
                $scope.seatsPickedForm = {};
                $scope.generateBlitTypeSeats = true;
                document.getElementById("successSeatBlitTypeEdit").style.display = "none";
            };

            $scope.seatsPickedForm = {};
            $scope.seatsPickedForm.isReserved = true;
            $scope.seatsPickedBlitTypeSubmit = function (bt) {
                var blitType = angular.copy(bt);
                if(blitType.isReserved) {

                    blitType.name = "HOST_RESERVED_SEATS";
                    blitType.isFree = true;
                    blitType.price = 0;
                }
                blitType.hasSeat = true;
                blitType.capacity = $scope.seatBlitUids.length;
                blitType.seatUids = $scope.seatBlitUids;
                $scope.blitTypeSubmited = true;
                $scope.blitTypeCreateValidation = 0;
                if($scope.sansPickedSeatMap === 0) {
                    $scope.allCheckedSeats += blitType.capacity;
                } else {
                    $scope.allCheckedSeatsSecondSeatMap += blitType.capacity;
                }
                if($scope.sansPickedSeatMap === 3) {
                    document.getElementById("successSeatBlitTypeEditSubmit").style.display = "block";
                    document.getElementById("blitTypeSeatsEdit").style.display = "none";
                }
                document.getElementById("successSeatBlitType").style.display = "block";
                $scope.$broadcast('blitTypeSubmit', [$scope.seatBlitUids, blitType.isReserved, $scope.sansPickedSeatMap]);

                delete blitType.isReserved;
                if($scope.sansPickedSeatMap === 0) {
                    mainSeatMapPrices.push(blitType);

                } else {
                    sansWithSeats.push(blitType);
                }
            };
            $scope.seatsPickedBlitTypeSubmitEdit = function (bt) {
                var blitType = angular.copy(bt);
                if(blitType.isReserved) {
                    blitType.name = "HOST_RESERVED_SEATS";
                    blitType.isFree = true;
                    blitType.price = 0;
                }
                blitType.hasSeat = true;
                blitType.capacity = $scope.seatBlitUidsEdit.length;
                blitType.seatUids = $scope.seatBlitUidsEdit;
                document.getElementById("successSeatBlitTypeEdit").style.display = "block";
                $scope.$broadcast('blitTypeSubmitEdit', [$scope.seatBlitUidsEdit, blitType.isReserved, $scope.sansPickedSeatMapEdit]);
                delete blitType.isReserved;
                mainSeatMapPricesEdit.push(blitType);
                $scope.generateBlitTypeSeats = false;
                $scope.blitTypeCreateValidationEdit = 0;
                $scope.seatBlitUidsEdit = [];
            };
            $scope.blitTypeCreateValidation = false;
            $scope.blitTypeCreateValidationEdit = false;
            $scope.$on("blitIdsChangedEdit",function (event ,data) {
                $scope.blitTypeCreateValidationEdit = data[0].length;
                $scope.$apply();
                $scope.seatBlitUidsEdit = data[0];
                $scope.sansPickedSeatMapEdit = data[1];
            });
            $scope.$on("blitIdsChanged",function (event ,data) {
                $scope.blitTypeCreateValidation = data[0].length;
                $scope.$apply();
                $scope.seatBlitUids = data[0];
                $scope.sansPickedSeatMap = data[1];
            });
            $scope.$on("blitIdsChangedGenerateTicket",function (event ,data) {
                $scope.blitTypeCreateValidationGenerateTicket = data[0].length;
                $scope.$apply();
                $scope.seatBlitUidsGenerateTicket = data[0];
            });
            $scope.newShowTime.newSeatsPrice = 'false';
            $scope.submitSansWithSeatpicker = function (newSans) {
                var newShowTime = angular.copy(newSans);
                if(newShowTime.newSeatsPrice === 'true') {
                    newShowTime.blitTypes = newShowTime.blitTypes.concat(sansWithSeats);
                } else {
                    newShowTime.blitTypes = newShowTime.blitTypes.concat(mainSeatMapPrices);
                }
                newShowTime.blitTypes.forEach(function (blitType) {
                    if(blitType.isFree) {
                        blitType.price = 0;
                    }
                });
                newShowTime.persianDate = document.getElementById("createSansWithSeat").value;

                newShowTime.date = dateSetterService.persianToMs(newShowTime.persianDate);
                $scope.showTimeForms.push(newShowTime);
                $scope.sansSet();
                $scope.newShowTime = {blitTypes : [], newSeatsPrice : 'false'};
                $timeout(function () {
                    $(".addedSansForEvent").addClass('fadeInWhite');
                }, 300)
            };
            var newEditedBlitTypes = [];
            $scope.submitSansWithSeatpickerEdit = function (newSans) {
                var newShowTimeEdit = angular.copy(newSans);
                newEditedBlitTypes = seatmapService.generateNewBlitTypes($scope.blitTypesWithSeatsEdit, mainSeatMapPricesEdit).concat($scope.blitTypesWithOutSeatsEdit);
                newShowTimeEdit.eventDateId = $scope.showTimeEditForms[sansIndexPicked].eventDateId;
                $scope.showTimeEditForms.splice(sansIndexPicked,1);
                newShowTimeEdit.persianDate = document.getElementById("editSansDate").value;
                newShowTimeEdit.date = dateSetterService.persianToMs(newShowTimeEdit.persianDate);
                delete newShowTimeEdit.persianDate;
                $scope.showTimeEditForms.push({
                    eventDateId : newShowTimeEdit.eventDateId,
                    date : newShowTimeEdit.date,
                    blitTypes : newEditedBlitTypes,
                    hasSalon : $scope.blitTypesWithSeatsEdit.length !== 0,
                    edited : true
                });
                $timeout(function () {
                    dateSetterService.initDate("classDate"+($scope.showTimeEditForms.length-1));
                    $(".classDate"+($scope.showTimeEditForms.length-1)).pDatepicker("setDate",dateSetterService.persianToArray(persianDate(newShowTimeEdit.date).pDate));
                }, 500);
                newEditedBlitTypes = [];
                mainSeatMapPricesEdit = [];
                if(document.getElementById("successSeatBlitTypeEdit")) {
                    document.getElementById("successSeatBlitTypeEdit").style.display = "none";
                }
                document.getElementById("createNewSansEdit").style.display = "block";
                document.getElementById("editEventSansSubmit").style.display = "none";
            };
            $scope.submitSansWithSeatpickerEditSubmit = function (newSans) {
                var newShowTimeEdit = angular.copy(newSans);
                newShowTimeEdit.persianDate = document.getElementById("newSansTimeEditSubmit").value;
                newShowTimeEdit.date = dateSetterService.persianToMs(newShowTimeEdit.persianDate);
                newShowTimeEdit.blitTypes = newShowTimeEdit.blitTypes.concat(sansWithSeats);
                if(sansWithSeats.length !== 0) {
                    newShowTimeEdit.hasSalon = true;
                } else {
                    newShowTimeEdit.hasSalon = false;
                }
                if(document.getElementsByClassName("seatMapSection3")[0]) {
                    document.getElementsByClassName("seatMapSection3")[0].style.display = "none";
                }
                if(document.getElementsByClassName("generateSeatMap3")[0]) {
                    document.getElementsByClassName("generateSeatMap3")[0].style.display = "block";
                }
                delete newShowTimeEdit.persianDate;
                $scope.showTimeEditForms.push(newShowTimeEdit);
                $timeout(function () {
                    dateSetterService.initDate("classDate"+($scope.showTimeEditForms.length-1));
                    $(".classDate"+($scope.showTimeEditForms.length-1)).pDatepicker("setDate",dateSetterService.persianToArray(persianDate(newShowTimeEdit.date).pDate));
                }, 500);
                $scope.newShowTimeEdit = {blitTypes : []};
                document.getElementById("editEventNewSansSubmit").style.display = "none";
                document.getElementById("createNewSansEdit").style.display = "block";
                sansWithSeats = [];
            };
            $scope.submitSansWithoutSeatpicker = function (newSans) {
                var newShowTime = angular.copy(newSans);
                newShowTime.persianDate = document.getElementById("createSansWithoutSeat").value;
                newShowTime.date = dateSetterService.persianToMs(newShowTime.persianDate);
                $scope.showTimeForms.push(newShowTime);
                $scope.newShowTime = {blitTypes : [], newSeatsPrice : 'false'};
                $scope.sansSet();
                $timeout(function () {
                    $(".addedSansForEvent").addClass('fadeInWhite');
                }, 300)
            };
            $scope.$watch('seatPicker.isChosen', function() {
                $scope.sansSet();
                $scope.newShowTime = {blitTypes : [], newSeatsPrice : 'false'};
                $scope.showTimeForms = [];
            });
            $scope.$watch('allCheckedSeats', function() {
                if(($scope.allCheckedSeats === $scope.schemaNumberOfSeats) && ($scope.schemaNumberOfSeats !== 0)) {
                    $scope.sansSet();
                }

            });

        }]);
