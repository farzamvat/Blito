/**
 * Created by soroush on 6/3/17.
 */

angular.module('exchangesPageModule', [])
    .controller('exchangeListCtrl', function ($scope, $location) {

        $scope.pageTitle = "بلیت های تعویضی ";


        $scope.exchangeList = [
            { title: "نام رویداد", img: "assets/img/a.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/d.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/e.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/f.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/a.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/a.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/a.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/a.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg",data: "1396/6/23", numberRemaining: "2", link: "/event-page", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"}

        ];

                console.log($scope.exchangeList);

        $scope.showDescription = function (event) {
            console.log(angular.element(event.target).children());
            $(angular.element(event.target).children()[2]).slideDown(150);
        }
        $scope.hideDescription = function (event) {
            $(angular.element(event.target).children()[2]).slideUp(150);
        }
        $scope.currentPage = 1;

    });
