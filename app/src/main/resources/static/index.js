var mainApp = angular.module('app', ['ngStorage']);

mainApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);
mainApp.controller('indexController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:8080/files';

    $scope.loadPage = function () {
        $http({
            url: contextPath,
            method: 'GET',
        }).then(function (response) {
            $scope.fileList = response.data;
        });
    };

    $scope.sendFile = function () {
        var formData = new FormData();
        var config = {
            transformRequest: angular.identity,
            transformResponse: angular.identity,
            headers: {'Content-Type': undefined}
        }
        formData.append('file', $scope.myFile);
        $http.post(contextPath, formData, config).then(function (response) {
            $scope.loadPage();
        });
    }

    $scope.deleteFileByName = function (filename) {
        $http.delete(contextPath + '/' + filename)
            .then(function (response) {
            });
        $scope.loadPage();
    }

    $scope.loadPage();
});