
class InvoiceService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing InvoiceService"

    createInvoice: (invoice) ->
        @$log.debug("createInvoice #{angular.toJson(invoice, true)}")
        deferred = @$q.defer()

        @$http.post('/invoice', invoice)
        .success((data, status, headers) =>
            @$log.info("Successfully created Invoice - status #{status}")
            deferred.resolve(data)
            )
        .error((data, status, headers) =>
            @$log.error("Failed to create Invoice - status #{status}")
            deferred.reject(data)
            )
        deferred.promise

    listInvoices: () ->
        @$log.debug "listInvoices()"
        deferred = @$log.defer()

        @$http.get("/invoices")
        .success((data, status, headers) =>
            @$log.info("Successfully listed invoices - status #{status}")
            deferred.resolve(data)
            )
        .error((data, status, headers) =>
            @$log.error("Failed to list invoices - status #{status}")
            deferred.reject(data)
            )
        deferred.promise

    getInvoice: (id) ->
        @$log.debug "getInvoice(#{id})"
        deferred = @$log.defer()

        @$http.get("/invoice/#{id}")
        .success((data, status, headers) =>
            @$log.info("Successfully got invoice - status #{status}")
            deferred.resolve(data)
            )
        .error((data, status, headers) =>
            @$log.error("Failed to get invoice - status #{status}")
            deferred.reject(data)
            )
        deferred.promise

    addLineItem: (id, lineItem) ->
        @$log.debug "addLineItem #{id} #{angular.toJson(lineItem. true)}"
        deferred = @$q.defer()

        @$http.post('/invoice/#{id}/line', lineItem)
        .success((data, status, headers) =>
            @$log.info("Successfully created LineItem - status #{status}")
            deferred.resolved(data)
            )
        .error((data, status, headers) =>
            @$log.error("Failed to create LineItem - status #{status}")
            deferred.reject(data)
            )
        deferred.promise

serviceModule.service('InvoiceService', ['$log', '$http', '$q', InvoiceService])