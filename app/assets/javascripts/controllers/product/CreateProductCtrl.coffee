
class CreateProductCtrl

    constructor: (@$log, @$location, @ProductService, @TaxService) ->
        @$log.debug "constructing CreateProductController"
        @product = {}
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

    createProduct: () ->
        @$log.debug "createProduct()"
        @product._id = ""
        @ProductService.createProduct(@product)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Product"
                @product = data
                @$location.path("/products")
            ,
            (error) =>
                @$log.error "Unable to create Product: #{error}"
            )

controllersModule.controller('CreateProductCtrl', ['$log', '$location', 'ProductService', 'TaxService', CreateProductCtrl])