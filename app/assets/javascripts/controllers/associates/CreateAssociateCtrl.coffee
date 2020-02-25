
class CreateAssociateCtrl

    constructor: (@$log, @$location, @AssociateService) ->
        @$log.debug "constructing CreateAssociateController"
        @associate = {}
        @associate.active = false

    createAssociate: () ->
        @$log.debug "createAssociate()"
        @associate._id = ""
        @AssociateService.createAssociate(@associate)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Associate"
                @associate = data
                @$location.path("/associates")
            ,
            (error) =>
                @$log.error "Unable to create Associates: #{error}"
            )

controllersModule.controller('CreateAssociateCtrl', ['$log', '$location', 'AssociateService', CreateAssociateCtrl])