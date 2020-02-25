
class CreateDiscountCtrl

    constructor: (@$log, @$location, @DiscountService) ->
        @$log.debug "constructing CreateDiscountController"
        @discount = {}
        @discount.isPercent = false
        @discount.isAfterTax = false

    createDiscount: () ->
        @$log.debug "createDiscount()"
        @discount._id = ""
        @DiscountService.createDiscount(@discount)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Discount"
                @discount = data
                @$location.path("/discounts")
            ,
            (error) =>
                @$log.error "Unable to create Discounts: #{error}"
            )

controllersModule.controller('CreateDiscountCtrl', ['$log', '$location', 'DiscountService', CreateDiscountCtrl])