#Point of Sale
[TOC]
##Overview
This document outlines design specifications for a simple POS system. This system will not provide inventory management or other complex features.
##Data Models
###Associates
1. Name
2. Active

###Tax List
1. Name
2. Percentage

###Price List
1. Service/Product Name
2. Price
3. Tax

###Discount List
1. Name
2. Amount
3. Is Percent or Flat
4. Is Before or After Tax

###Invoice
1. Date of invoice
2. Client
3. Associate
4. List of:
	1. Service/Product/Other
	2. Unit
	3. Qty
	4. Total
5. Subtotal

##Screens
###Administration
These screens will be used for administrative purposes
####Associate List
Add and remove associates as well as activate and deactivate them from using POS
####Tax List
Manage tax categories and percent values for them
####Price List
Manage item prices
####Discount List
Manage various discounts
###Operations
These screens with be used for daily operations
####POS
Point of Sale screen
####Reports
This screen with house various reports:

1. Invoices
2. Shift Report (X)
3. Daily Report (Z)
