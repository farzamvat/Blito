/**
 * Created by soroush on 5/2/17.
 */
angular.module('User')
    .controller('userProfileCtrl', function ($scope,
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
                                             imageServices) {
        var userProfile = this;

        $scope.userData = userInfo.getData();
        $scope.updateInfoSpinner = false;

        $scope.updateSuccessNotif = false;
        $scope.updateFailNotif = false;
        $scope.showTimeForms = [{blitTypes : [{}]}];
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
                .then(function (data, status) {
                    $scope.logoutMenu = true;
                    userProfile.userData = data.data;
                    userInfo.setData(userProfile.userData);
                    $scope.editInfo = angular.copy(userInfo.getData());

                }, function (data) {
                    console.log(data);
                }))
        };
        $scope.changePassword = function (password) {
            document.getElementsByClassName("deletePlannerSpinner")[0].style.display = "none";
            document.getElementsByClassName("changePasswordSpinner")[0].style.display = "inline";
            console.log(password);
            updateInfo.changePasswordSubmit(password)
                .then(function (data) {
                    console.log(data);
                    document.getElementById("changePasswordSuccess").style.display = "block";
                    document.getElementsByClassName("changePasswordSpinner")[0].style.display = "none";
                })
                .catch(function (data) {
                    console.log(data);
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
                .then(function (status, data) {
                    $scope.updateInfoSpinner = false;
                    $scope.updateSuccessNotif = true;
                    $timeout(function () {
                        $scope.updateSuccessNotif = false;
                    }, 3000);
                })
                .catch(function (status, data) {
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
                .then(function (data, status) {
                    $scope.uploadEventSixEditPhoto = false;
                    $scope.eventPhotoSixEditSuccess = true;

                    $scope.gallerySixEditUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventFiveEditPhoto = false;
                    $scope.eventPhotoFiveEditSuccess = true;

                    $scope.galleryFiveEditUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventFourEditPhoto = false;
                    $scope.eventPhotoFourEditSuccess = true;
                    $scope.galleryFourEditUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventThreeEditPhoto = false;
                    $scope.eventPhotoThreeEditSuccess = true;

                    $scope.galleryThreeEditUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventTwoEditPhoto = false;
                    $scope.eventPhotoTwoEditSuccess = true;

                    $scope.galleryTwoEditUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventOneEditPhoto = false;
                    $scope.eventPhotoOneEditSuccess = true;

                    $scope.galleryOneEditUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventSixPhoto = false;
                    $scope.eventPhotoSixSuccess = true;
                    $scope.gallerySixUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventFivePhoto = false;
                    $scope.eventPhotoFiveSuccess = true;
                    $scope.galleryFiveUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventFourPhoto = false;
                    $scope.eventPhotoFourSuccess = true;
                    $scope.galleryFourUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.eventPhotoThreeSuccess = true;
                    $scope.uploadEventThreePhoto = false;
                    $scope.galleryThreeUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.eventPhotoTwoSuccess = true;
                    $scope.uploadEventTwoPhoto = false;
                    $scope.galleryTwoUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.eventPhotoOneSuccess = true;
                    $scope.uploadEventOnePhoto = false;
                    $scope.galleryOneUUID = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.eventEditPhotoSuccess = true;
                    $scope.uploadEditEventPhoto = false;
                    $scope.eventEditImageId = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEditExchangePhoto = false;
                    $scope.exchangeEditPhotoSuccess = true;
                    $scope.exchangeEditImageId = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadExchangePhoto = false;
                    $scope.exchangePhotoSuccess = true;
                    $scope.exchangeImageId = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadEventPhoto = false;
                    $scope.eventPhotoSuccess = true;
                    $scope.eventImageId = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadPlannerPhoto = false;
                    $scope.plannerPhotoSuccess = true;
                    $scope.plannerImageId = data.data.imageUUID;

                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadPlannerEditPhoto = false;
                    $scope.plannerEditPhotoSuccess = true;
                    $scope.plannerImageIdEdit = data.data.imageUUID;

                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadCoverPhoto = false;
                    $scope.coverPhotoSuccess = true;
                    $scope.coverImageId = data.data.imageUUID;
                }, function (data, status) {
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
                .then(function (data, status) {
                    $scope.uploadCoverEditPhoto = false;
                    $scope.coverEditPhotoSuccess = true;
                    $scope.coverImageIdEdit = data.data.imageUUID;
                }, function (data, status) {
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
            mapMarkerService.initMap(document.getElementById('map'));
            mapMarkerService.initMap(document.getElementById('mapExchange'));
            $(angular.element(document.getElementById(section)).siblings()[0]).slideDown(300);
            $(angular.element(document.getElementById(section))).addClass('orangeBackground');
        };
        //==================================================== ********* =================================
        //==================================================== SHOW TIME FIELDS =================================

        $scope.addFieldTicketType=function(i){
            if($scope.showTimeForms[i].blitTypes.length < 5) {
                $scope.showTimeForms[i].blitTypes.push({})
            }
        };
        $scope.addFieldTicketTypeEdit=function(i){
            if($scope.showTimeEditForms[i].blitTypes.length < 5) {
                $scope.showTimeEditForms[i].blitTypes.push({})
            }
        };

        $scope.deleteFieldTicketTypeEdit=function(i){
            if( 1 < $scope.showTimeEditForms[i].blitTypes.length) {
                $scope.showTimeEditForms[i].blitTypes.splice(-1,1);
            }
        };
        $scope.deleteFieldTicketType=function(i){
            if( 1 < $scope.showTimeForms[i].blitTypes.length) {
                $scope.showTimeForms[i].blitTypes.splice(-1,1);
            }
        };
        $scope.addFieldShowTime=function(){
            if($scope.showTimeForms.length < 10) {
                var classNumber = $scope.showTimeForms.length;
                $timeout(function () {
                    dateSetterService.initDate("eventDateClass"+classNumber);
                }, 1000);
                $scope.showTimeForms.push({blitTypes : [{}]})
            }
        };
        $scope.addFieldShowTimeEdit=function(){
            if($scope.showTimeEditForms.length < 10) {
                var classNumber = $scope.showTimeEditForms.length;
                $timeout(function () {
                    dateSetterService.initDate("classDate"+classNumber);
                }, 1000);
                $scope.showTimeEditForms.push({blitTypes : [{}]})
            }
        };
        $scope.deleteFieldShowTimeEdit=function(){
            if( 1 < $scope.showTimeEditForms.length) {
                $scope.showTimeEditForms.splice(-1,1);
            }
        };
        $scope.deleteFieldShowTime=function(){
            if( 1 < $scope.showTimeForms.length) {
                userProfile.showTimeNumber--;
                $scope.showTimeForms.splice(-1,1);
            }
        };
        //==================================================== ********* =================================
        //==================================================== EVENT PLANNER SUBMIT =================================
        $scope.submitPlannerSpinner = false;
        $scope.submitPlannerNotif = false;
        $scope.submitEventPlanner = function (plannerData) {
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
                telephone: plannerData.mobile,
                twitterLink: plannerData.twitter,
                websiteLink: plannerData.website,
                description : plannerData.description
            };
            eventPlannerData.images = eventPlannerData.images.filter(function (images) {
                return images.imageUUID !== undefined;
            });
            console.log(eventPlannerData.images);
            if(!$scope.plannerImageId && !$scope.coverImageId){
                delete eventPlannerData.images;
            }
            $scope.submitPlannerSpinner = true;
            plannerService.submitPlannerForm(eventPlannerData)
                .then(function (data, status) {
                    $scope.submitPlannerSpinner = false;
                    $scope.submitPlannerNotif = true;
                    $scope.submitPlannerErrorNotif = false;
                    $scope.getPlannersData(1);
                    $scope.eventPlanner = [];
                    $scope.plannerImageId = '';
                    $scope.coverImageId = '';
                    angular.element(document.getElementsByClassName("coverPhotoUploadEdit"))[0].src = '';
                    angular.element(document.getElementsByClassName("eventPlannerPhotoUploadEdit"))[0].src = '';
                }, function (data, status) {
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
        $scope.eventDateClass = function (index) {
            return "eventDateClass"+index;
        };
        $scope.submitEvent = function (eventFields) {
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
            newShowTime = newShowTime.map(function (item) {
                item.date = dateSetterService.persianToMs(item.date);
                return item;
            });
            var eventSubmitData = {
                eventName : eventFields.name,
                eventType : eventFields.eventType,
                eventHostId : eventFields.eventPlanner.eventHostId,
                address : eventFields.address,
                aparatDisplayCode : eventFields.aparatLink,
                blitSaleEndDate : dateSetterService.persianToMs(eventFields.ticketEndTime),
                blitSaleStartDate : dateSetterService.persianToMs(eventFields.ticketStartTime),
                description : eventFields.description,
                members : eventFields.members,
                eventDates : newShowTime,
                images : [
                    {imageUUID : $scope.eventImageId, type : "EVENT_PHOTO"},
                    {imageUUID : $scope.galleryOneUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryTwoUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryThreeUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryFourUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryFiveUUID, type : "GALLERY"},
                    {imageUUID : $scope.gallerySixUUID, type : "GALLERY"}
                ],
                latitude : latLng.lat,
                longitude : latLng.lng
            };
            eventSubmitData.images = eventSubmitData.images.filter(function (item) {
                return !(item.imageUUID === null || item.imageUUID === undefined);
            });
            if(!$scope.eventImageId){
                delete eventSubmitData.images;
            }
            $scope.createEventSpinner = true;

            eventService.submitEventForm(eventSubmitData)
                .then(function (data, status) {
                    // $scope.submitEventForm.$setPristine();
                    $scope.createEventSpinner = false;
                    $scope.createEventNotif = true;
                    $scope.createEventErrorNotif = false;
                    $scope.getUserEvents(1);
                    $scope.eventFields = [];

                }, function (data, status) {
                    $scope.createEventErrorNotif = true;
                    document.getElementById("createEventErrorNotif").innerHTML= data.data.message;
                    $scope.createEventSpinner = false;
                })
        };
        //==================================================== ********* =================================
        //==================================================== EXCHANGE SUBMIT =================================
        $scope.submitExchangeSpinner = false;
        $scope.submitExchangeNotif = false;

        $scope.submitExchangeTicket = function (exchangeFields) {

            $scope.submitExchangeNotif = false;
            var latLng = mapMarkerService.getMarker();
            $scope.submitExchangeSpinner = true;
            var exchangeData = {
                blitCost: exchangeFields.price,
                description: exchangeFields.description,
                email: $scope.userData.email,
                eventAddress: exchangeFields.address,
                eventDate:  dateSetterService.persianToMs(exchangeFields.eventTime),
                image: {
                    imageUUID: $scope.exchangeImageId,
                    type: "EXCHANGEBLIT_PHOTO"
                },
                isBlitoEvent: exchangeFields.isBlito,
                phoneNumber: $scope.userData.mobile,
                latitude: latLng.lat,
                longitude: latLng.lng,
                title: exchangeFields.name,
                type: exchangeFields.type
            };
            if(!$scope.exchangeImageId){
                delete exchangeData.image;
            }
            exchangeService.submitExchangeForm(exchangeData)
                .then(function (data, status) {
                    $scope.submitExchangeSpinner = false;
                    $scope.submitExchangeNotif = true;
                    $scope.submitExchangeErrorNotif = false;
                    $scope.exchangePhotoError = false;
                    $scope.exchangePhotoSuccess = false;
                    $scope.getExchangeData(1);
                    $scope.exchange = [];
                    $scope.exchangeImageId = '';
                    angular.element(document.getElementsByClassName("exchangePhotoUpload"))[0].src = '';

                }, function (data, status) {
                    $scope.submitExchangeErrorNotif = true;
                    $scope.submitExchangeSpinner = false;
                    $scope.exchangePhotoError = false;
                    $scope.exchangePhotoSuccess = false;
                    document.getElementById("submitExchangeErrorNotif").innerHTML= data.data.message;
                    console.log(data);
                })
        };
        //==================================================== ********* =================================
        $scope.showTime = [];
        $scope.dropDownTabToggleEvent = function (event) {
            $scope.getPlannersData(1);
            $scope.getUserEvents(1);
            mapMarkerService.initMap(document.getElementById('map'));
            $(angular.element(document.getElementById('toggleExchange'))).slideUp(300);
            $(angular.element(event.currentTarget).siblings()[0]).slideDown(300);
        };
        $scope.dropDownTabToggleExchange = function (event) {
            $scope.getExchangeData(1);
            mapMarkerService.initMap(document.getElementById('mapExchange'));

            $(angular.element(document.getElementById('toggleEvent'))).slideUp(300);
            $(angular.element(event.currentTarget).siblings()[0]).slideDown(300);
        };
        $scope.closeTabDropDowns = function () {
            $(angular.element(document.getElementsByClassName('tabDropDown'))).slideUp(300);
        };

        $scope.toggleBody = function (section) {
            mapMarkerService.initMap(document.getElementById('map'));
            mapMarkerService.initMap(document.getElementById('mapExchange'));
            $(angular.element(document.getElementById(section)).siblings()[0]).slideToggle(300);
            $(angular.element(document.getElementById(section))).toggleClass('orangeBackground');
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

            angular.element(document.getElementsByClassName("profilePhotoUploadEditEvent"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryOneEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryTwoEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryThreeEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryFourEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryFiveEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("gallerySixEdit"))[0].src = "";
            $scope.eventEditPhotoSuccess = false;

            $scope.showTimeEditForms = angular.copy($scope.userEventsEdit[index].eventDates);

            $scope.editEventFields = {
                eventId : $scope.userEventsEdit[index].eventId,
                eventName : $scope.userEventsEdit[index].eventName,
                eventType : $scope.userEventsEdit[index].eventType,
                description : $scope.userEventsEdit[index].description,
                address : $scope.userEventsEdit[index].address,
                aparatDisplayCode : $scope.userEventsEdit[index].aparatDisplayCode,
                eventHostId : $scope.userEventsEdit[index].eventHostId,
                eventLink : $scope.userEventsEdit[index].eventLink,
                members : $scope.userEventsEdit[index].members
            };
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
            console.log($scope.userEventsEdit[index]);
            $scope.eventEditImageId = imageUUID;
            console.log($scope.eventEditImageId);
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
            }, 500);
            $timeout(function () {
                mapMarkerService.initMap(document.getElementById('editEventMap'));
                mapMarkerService.setMarker($scope.userEventsEdit[index].latitude, $scope.userEventsEdit[index].longitude);
            },500);
        };

        $scope.editEventSubmit = function (editEventData) {
            var sendingData = angular.copy(editEventData);
            var latLong = mapMarkerService.getMarker();
            $scope.newShowTimeEditForms = angular.copy($scope.showTimeEditForms);

            $scope.newShowTimeEditForms.map(function (item) {
                item.date = dateSetterService.persianToMs(item.date);
                return item;
            });
            sendingData.blitSaleEndDate = dateSetterService.persianToMs(editEventData.blitSaleEndDate);
            sendingData.blitSaleStartDate = dateSetterService.persianToMs(editEventData.blitSaleStartDate);
            sendingData.eventDates = $scope.newShowTimeEditForms;
            sendingData.latitude = latLong.lat;
            sendingData.longitude = latLong.lng;

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
                .then(function (data, status) {
                    $scope.editEventSpinner = false;
                    $scope.editEventNotif = true;
                    $scope.editEventErrorNotif = false;
                    $scope.getUserEvents(1);
                })
                .catch(function (data, status) {
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
            imageServices.downloadPhotos($scope.plannerImageIdEdit, "eventPlannerPhotoUploadEdit");
            imageServices.downloadPhotos($scope.coverImageIdEdit, "coverPhotoUploadEdit");
        };
        $scope.editHostSubmit = function (editHostData) {
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
                .then(function (data, status) {
                    $scope.submitPlannerEditNotif = true;
                    $scope.editPlannerSpinner = false;
                    $scope.submitPlannerEditErrorNotif = false;
                    $scope.getPlannersData(1);
                })
                .catch(function (data, status) {
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
                eventAddress : $scope.exchangeEditTickets[index].eventAddress
            };
            var latlng = {
                lat : $scope.exchangeEditTickets[index].latitude,
                lng : $scope.exchangeEditTickets[index].longitude
            };
            dateSetterService.initDate("persianEditExchangeTime");
            $(".persianEditExchangeTime").pDatepicker("setDate",dateSetterService.persianToArray(persianDate($scope.exchangeEditTickets[index].eventDate).pDate));
            $scope.exchangeEditImageId = $scope.exchangeEditTickets[index].image.imageUUID;
            imageServices.downloadPhotos($scope.exchangeEditImageId, "exchangePhotoUploadEdit");
            mapMarkerService.initMap(document.getElementById('mapExchangeEdit'));
            mapMarkerService.setMarker(latlng.lat, latlng.lng);
            $("#editExchange").modal("show");
        };

        $scope.editExchangeTicket = function (editExchangeData) {
            var latLng = mapMarkerService.getMarker();
            $scope.editExchangeSpinner = true;
            editExchangeData.eventDate = dateSetterService.persianToMs(editExchangeData.eventDate);
            editExchangeData.email = $scope.userData.email;
            editExchangeData.exchangeBlitId = $scope.exchangeEdit.exchangeBlitId;
            editExchangeData.latitude = latLng.lat;
            editExchangeData.longitude = latLng.lng;
            editExchangeData.phoneNumber = $scope.userData.mobile;
            editExchangeData.image = {
                imageUUID : $scope.exchangeEditImageId,
                type : "EXCHANGEBLIT_PHOTO"
            };
            if(!$scope.exchangeEditImageId){
                delete editExchangeData.image;
            }
            exchangeService.editExchangeForm(editExchangeData)
                .then(function (data, status) {
                    $scope.editExchangeNotif = true;
                    $scope.editExchangeSpinner = false;
                    $scope.editExchangeErrorNotif = false;
                    $scope.getExchangeData(1);
                }, function (data, status) {
                    $scope.editExchangeErrorNotif = true;
                    $scope.editExchangeSpinner = false;
                    document.getElementById("editExchangeErrorNotif").innerHTML= data.data.message;

                })
        };
        //==================================================== ********* =================================
        //==================================================== SETTINGS =================================
        var settingIndex;
        $scope.showSetting = function (index) {
            console.log($scope.userEvents[index]);
            document.getElementsByClassName("eventStatusSpinner")[0].style.display = "none";
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
                .then(function (data) {
                    document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
                    document.getElementsByClassName("approveSuccessSetting")[0].style.display = "block";

                })
                .catch(function (data) {
                    document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
                    document.getElementById("approveErrorSetting").innerHTML = data.data.message;
                    document.getElementsByClassName("approveErrorSetting")[0].style.display = "block";

                })
        };
        $scope.changeEventState = function (stateChange) {
            var stateData = {
                eventId : $scope.userEvents[settingIndex].eventId,
                state : stateChange
            };
            document.getElementsByClassName("eventStatusSpinner")[0].style.display = "inline";
            eventService.editEventState(stateData)
                .then(function () {
                    document.getElementsByClassName("approveSuccessSetting")[0].style.display = "inline";
                    document.getElementsByClassName("eventStatusSpinner")[0].style.display = "none";
                })
                .catch(function (data) {
                    console.log(data);
                    document.getElementById("approveErrorSetting").innerHTML = data.data.message;
                    document.getElementsByClassName("approveErrorSetting")[0].style.display = "inline";
                    document.getElementsByClassName("eventStatusSpinner")[0].style.display = "none";
                })
        };
        var settingExchangeIndex;
        $scope.showSettingExchange = function (index) {
            console.log($scope.exchangeEditTickets[index]);
            document.getElementsByClassName("exchangeStatusSpinner")[0].style.display = "none";
            document.getElementsByClassName("approveSuccessSettingExchange")[0].style.display = "none";
            document.getElementsByClassName("approveErrorSettingExchange")[0].style.display = "none";

            settingExchangeIndex = index;
            $scope.exchangeStateSetting = $scope.exchangeEditTickets[index].state;

            $("#settingExchangeModal").modal("show");
            document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
        };
        $scope.deleteExchange= function () {
            document.getElementsByClassName("deleteSpinner")[0].style.display = "inline";
            exchangeService.deleteExchange($scope.exchangeEditTickets[settingExchangeIndex].exchangeBlitId)
                .then(function () {
                    document.getElementsByClassName("approveSuccessSettingExchange")[0].style.display = "inline";
                    document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
                })
                .catch(function () {
                    document.getElementById("approveErrorSettingExchange").innerHTML = data.data.message;
                    document.getElementsByClassName("approveErrorSettingExchange")[0].style.display = "inline";
                    document.getElementsByClassName("deleteSpinner")[0].style.display = "none";
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
                    document.getElementsByClassName("approveSuccessSettingExchange")[0].style.display = "inline";
                    document.getElementsByClassName("exchangeStatusSpinner")[0].style.display = "none";
                })
                .catch(function (data) {
                    console.log(data);
                    document.getElementById("approveErrorSettingExchange").innerHTML = data.data.message;
                    document.getElementsByClassName("approveErrorSettingExchange")[0].style.display = "inline";
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
        $scope.showTickets = function (index) {
            $scope.eventTicketsPageChanged = function (newPage) {
                $scope.getEventTickets(newPage);
            };
            $scope.getEventTickets = function (pageNumber) {

                ticketsService.getEventTickets(pageNumber, $scope.userEvents[index].eventId)
                    .then(function (data) {
                        $scope.totalTicketNumber = data.data.totalElements;
                        $scope.eventsTickets = data.data.content;
                        console.log(data);
                    })
                    .catch(function (data) {
                        console.log(data);
                    })
            };
            $scope.getEventTickets(1);
            $('#eventTickets').modal('show');

        };
        $scope.getExchangeData = function (page) {
            exchangeService.getExchangeTickets(page)
                .then(function (data, status) {
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
                    console.log(data);
                })
        };
        $scope.getDataUserEvents = [];
        $scope.getUserEvents = function (pageNumber) {
            eventService.getUserEvents(pageNumber)
                .then(function (data, status) {
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
                    console.log(data);
                })
        };
        $scope.getPlannersData = function (page) {
            plannerService.getPlanners(page)
                .then(function (data) {
                    $scope.totalPlannersNumber = data.data.totalElements;
                    $scope.eventHosts = data.data.content;
                }, function (data) {
                    console.log(data);
                })
        };

        $scope.getUserTickets = function (pageNumber) {
            ticketsService.getUserTickets(pageNumber, $scope.userData.email)
                .then(function (data) {
                    console.log(data);
                    $scope.totalUserTickets = data.data.totalElements;
                    $scope.userTickets = data.data.content;
                })
                .catch(function (data) {
                    console.log(data);
                })
        };
        console.log(userInfoPromise);
        $q.all(userInfoPromise)
            .then(function () {
                console.log($scope.userData);
                $scope.getUserTickets(1);
            })
            .catch(function () {

            });
        //==================================================== ********* =================================
        //==================================================== PERSIAN DATE PICKER =======================

        $timeout(function () {
            dateSetterService.initDate("persianTimeEventStart");
            dateSetterService.initDate("persianTimeEventEnd");
            dateSetterService.initDate("persianExchangeTime");
            dateSetterService.initDate("eventDateClass0");
        }, 1000);
        //==================================================== ********* =================================
        $scope.showMenuOnSm = function () {
            $('.profileTabs').toggleClass("pullMenuLeft");
            $('.profileToggleMenu').toggleClass("rotateToggleMenu");
        }
    });
