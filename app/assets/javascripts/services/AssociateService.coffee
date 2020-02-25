
class AssociateService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing AssociateService"

    listAssociates: () ->
        @$log.debug "listAssociates()"
        deferred = @$q.defer()

        @$http.get("/associates")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Associates - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Associates - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    createAssociate: (associate) ->
        @$log.debug "createAssociate #{angular.toJson(associate, true)}"
        deferred = @$q.defer()

        @$http.post('/associate', associate)
        .success((data, status, headers) =>
                @$log.info("Successfully created Associate - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create Associate - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

servicesModule.service('AssociateService', ['$log', '$http', '$q', AssociateService])