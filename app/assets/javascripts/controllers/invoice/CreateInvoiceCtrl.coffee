
class CreateInvoiceCtrl

    constructor: (@$log, @InvoiceService) ->
        @$log.debug "constructing Create Invoice Controller"
        @invoice = {}
        @invoice._id = ""
        @invoice.date = Date()
        @invoice.lineItems = []
        @invoice.total = 0

    a