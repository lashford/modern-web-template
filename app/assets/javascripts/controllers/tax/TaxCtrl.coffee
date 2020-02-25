
class TaxCtrl

    constructor: (@$log, @TaxService) ->
        @$log.debug "constructing TaxController"
        @taxes = []
        @getAllTaxes()

    getAllTaxes: () ->
        @$log.debug "getAllTaxes"

        @TaxService.listTaxes()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Taxes"
                @taxes = data
            ,
            (error) =>
                @$log.error "Unable to get Taxes: #{error}"
            )

controllersModule.controller('TaxCtrl', ['$log','TaxService', TaxCtrl])