
class AssociateCtrl

    constructor: (@$log, @AssociateService) ->
        @$log.debug "constructing AssociateController"
        @associates = []
        @getAllAssociates()

    getAllAssociates: () ->
        @$log.debug "getAllAssociates()"

        @AssociateService.listAssociates()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Associates"
                @associates = data
            ,
            (error) =>
                @$log.error "Unable to get Associates: #{error}"
            )

controllersModule.controller('AssociateCtrl', ['$log', 'AssociateService', AssociateCtrl])