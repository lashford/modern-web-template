
class DiscountCtrl

    constructor: (@$log, @DiscountService) ->
        @$log.debug "constructing DiscountController"
        @discounts = []
        @getAllDiscounts()

    getAllDiscounts: () ->
        @$log.debug "getAllDiscounts()"

        @DiscountService.listDiscounts()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Discounts"
                @discounts = data
            ,
            (error) =>
                @$log.error "Unable to get Discounts: #{error}"
            )

controllersModule.controller('DiscountCtrl', ['$log', 'DiscountService', DiscountCtrl])