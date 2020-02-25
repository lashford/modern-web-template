
class InvoiceCtrl

    constructor: (@$log, @InvoiceService) ->
        @$log.debug "constructing InvoiceCtrl"
        @invoices = []
        @invoice = {}
        @getAllInvoices()

    getAllInvoices: () ->
        @$log.debug "getAllInvoices"

        @InvoiceService.listInvoices()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Invoices"
                @invoices = data
            ,
            (error) =>
                @$log.error "Unable to get Invoices: #{error}"
            )

    getInvoice: (id) ->
        @$log.debug "getInvoice"

        @InvoiceService.getInvoice(id)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Invoice"
                @invoice = data
            ,
            (error) =>
                @$log.error "Unable to get Invoice: #{error}"
            )

controllersModule.controller('InvoiceCtrl',['$log','InvoiceService',InvoiceCtrl])