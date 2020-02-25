
dependencies = [
    'ngRoute',
    'ui.bootstrap',
    'myApp.filters',
    'myApp.services',
    'myApp.controllers',
    'myApp.directives',
    'myApp.common',
    'myApp.routeConfig'
]

app = angular.module('myApp', dependencies)

angular.module('myApp.routeConfig', ['ngRoute'])
    .config(['$routeProvider', ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/associates/view.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/create.html'
            })
            .when('/users/edit/:firstName/:lastName', {
                templateUrl: '/assets/partials/update.html'
            })
            .when('/associates', {
                templateUrl: '/assets/partials/associates/view.html'
            })
            .when('/associates/create', {
                templateUrl: '/assets/partials/associates/create.html'
            })
            .when('/taxes', {
                templateUrl: '/assets/partials/taxes/view.html'
            })
            .when('/taxes/create', {
                templateUrl: '/assets/partials/taxes/create.html'
            })
            .when('/products', {
                templateUrl: '/assets/partials/products/view.html'
            })
            .when('/products/create', {
                templateUrl: '/assets/partials/products/create.html'
            })
            .when('/discounts', {
                templateUrl: '/assets/partials/discounts/view.html'
            })
            .when('/discounts/create', {
                templateUrl: '/assets/partials/discounts/create.html'
            })
            .otherwise({redirectTo: '/'})])
    .config(['$locationProvider', ($locationProvider) ->
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        })])

@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])