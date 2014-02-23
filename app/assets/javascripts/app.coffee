
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
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/view.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/create.html'
            })
            .otherwise({redirectTo: '/'})

@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])