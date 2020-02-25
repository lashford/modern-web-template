
class ProductCtrl

    constructor: (@$log, @ProductService) ->
        @$log.debug "constructing ProductController"
        @products = []
        @getAllProducts()

    getAllProducts: () ->
        @$log.debug "getAllProducts()"

        @ProductService.listProducts()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Products"
                @products = data
            ,
            (error) =>
                @$log.error "Unable to get Products: #{error}"
            )

controllersModule.controller('ProductCtrl', ['$log', 'ProductService', ProductCtrl])