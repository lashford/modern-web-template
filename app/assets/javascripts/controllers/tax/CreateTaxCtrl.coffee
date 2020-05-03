
class CreateTaxCtrl

    constructor: (@$log, @$location, @TaxService) ->
        @$log.debug "constructing CreateTaxController"
        @tax = {}

    createTax: () ->
        @$log.debug "createTax()"
        @tax._id = ""
        @TaxService.createTax(@tax)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Tax"
                @tax = data
                @$location.path("/taxes")
            ,
            (error) =>
                @$log.error "Unable to create Tax #{error}"
            )

controllersModule.controller('CreateTaxCtrl', ['$log', '$location', 'TaxService', CreateTaxCtrl])