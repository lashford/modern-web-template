
class DiscountService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing DiscountService"

    listDiscounts: () ->
        @$log.debug "listDiscounts()"
        deferred = @$q.defer()

        @$http.get("/discounts")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Discounts - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Discounts - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    createDiscount: (tax) ->
        @$log.debug "createDiscount #{angular.toJson(tax, true)}"
        deferred = @$q.defer()

        @$http.post('/discount', tax)
        .success((data, status, headers) =>
                @$log.info("Successfully created Discount - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create Discount - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

servicesModule.service('DiscountService', ['$log', '$http', '$q', DiscountService])