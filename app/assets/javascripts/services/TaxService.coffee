
class TaxService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing TaxService"

    listTaxes: () ->
        @$log.debug "listTaxes()"
        deferred = @$q.defer()

        @$http.get("/taxes")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Taxes - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Taxes - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    createTax: (tax) ->
        @$log.debug "createTax #{angular.toJson(tax, true)}"
        deferred = @$q.defer()

        @$http.post('/tax', tax)
        .success((data, status, headers) =>
                @$log.info("Successfully created Tax - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create Tax - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

servicesModule.service('TaxService', ['$log', '$http', '$q', TaxService])