var VPOS_RC_SUCCESS = "VPOS-0000";
var MASTERPASS_IYZICO_PAYMENT_PROVIDER = "iyzico";

function generateVposApiRequest(params) {
    var resultJson = {};
    console.info(params);


    if (params && params != null) {

        if (params.additionalParameters && params.additionalParameters != null) {
            resultJson.billingAddress = {};

            resultJson.billingAddress.lastName = params.additionalParameters.bill_last_name;
            resultJson.billingAddress.firstName = params.additionalParameters.bill_first_name;
            resultJson.billingAddress.email = params.additionalParameters.bill_email;
            resultJson.billingAddress.phone = params.additionalParameters.bill_phone;
            resultJson.billingAddress.country = params.additionalParameters.bill_country_code;
            resultJson.billingAddress.fax = params.additionalParameters.bill_fax;
            resultJson.billingAddress.address1 = params.additionalParameters.bill_address;
            resultJson.billingAddress.address2 = params.additionalParameters.bill_address2;
            resultJson.billingAddress.zipCode = params.additionalParameters.bill_zip_code;
            resultJson.billingAddress.city = params.additionalParameters.bill_city;
            resultJson.billingAddress.state = params.additionalParameters.bill_state;

            resultJson.deliveryAddress = {};

            resultJson.deliveryAddress.lastName = params.additionalParameters.delivery_last_name;
            resultJson.deliveryAddress.firstName = params.additionalParameters.delivery_first_name;
            resultJson.deliveryAddress.email = params.additionalParameters.delivery_email;
            resultJson.deliveryAddress.phone = params.additionalParameters.delivery_phone;
            resultJson.deliveryAddress.country = params.additionalParameters.delivery_country_code;
            resultJson.deliveryAddress.company = params.additionalParameters.delivery_company;
            resultJson.deliveryAddress.address1 = params.additionalParameters.delivery_address;
            resultJson.deliveryAddress.address2 = params.additionalParameters.delivery_address2;
            resultJson.deliveryAddress.zipCode = params.additionalParameters.delivery_zip_code;
            resultJson.deliveryAddress.city = params.additionalParameters.delivery_city;
            resultJson.deliveryAddress.state = params.additionalParameters.delivery_state;

            resultJson.buyerInfo = {};

            resultJson.buyerInfo.buyerId = params.additionalParameters.buyer_id;
            resultJson.buyerInfo.tcid = params.additionalParameters.identity_number;
            resultJson.buyerInfo.lastLoginDate = params.additionalParameters.last_login_date;
            resultJson.buyerInfo.registeredDate = params.additionalParameters.registration_date;

            resultJson.amount = {};

            resultJson.amount.currency = 949;
            if (params.amount && params.amount != null) {
                resultJson.amount.total = (Number(params.amount) / 100).toFixed(2);
            }


            resultJson.bankId = null;

            resultJson.card = {};
            resultJson.card.cvv = "000";
            resultJson.card.expire = "2024-03";
            resultJson.card.pan = params.cardNumber;

            resultJson.companyId = 542;
            resultJson.groupId = "string";
            resultJson.institutionId = 2866;
            resultJson.id = calculateOrderId(resultJson.companyId, resultJson.institutionId);
            resultJson.installment = 0;

            resultJson.masterpassInfo = {};

            resultJson.masterpassInfo.clientToken = params.token;
            resultJson.masterpassInfo.map = {prop1: "string"};
            resultJson.masterpassInfo.orderId = params.orderNo;

            resultJson.productId = 1000;
            resultJson.reconDate = new Date().toISOString().slice(0, 10);

            resultJson.secure3DInfo = {
                cavv: "string",
                eci: "string",
                md: "string",
                transactionId: "qwe",
                xid: "string"
            };

            resultJson.basketInfo = {};

            resultJson.basketInfo.description = params.additionalParameters.transaction_description;
            resultJson.basketInfo.timeout = params.additionalParameters.order_timeout;
            resultJson.basketInfo.cardProgram = params.additionalParameters.card_program_name;
            resultJson.basketInfo.campaignType = "EXTRA_INSTALLMENTS";
            resultJson.basketInfo.shippingPrice = params.additionalParameters.order_shipping;
            resultJson.basketInfo.discountPrice = params.additionalParameters.order_discount;
            resultJson.basketInfo.basketId = uuid.v4();
            resultJson.basketInfo.paymentContent = params.additionalParameters.payment_content;
            resultJson.basketInfo.vendorId = "v1";
            resultJson.basketInfo.nonCommissionedAmount = 345;
            resultJson.basketInfo.commissionRate = 0;

            resultJson.basketInfo.basketItems = [];

            var maxLength = findMaxLengthArray([params.additionalParameters.order_product_name_arr, params.additionalParameters.order_product_code_arr, params.additionalParameters.order_price_type_arr,
                params.additionalParameters.order_price_arr, params.additionalParameters.order_product_info_arr, params.additionalParameters.order_qty_arr, params.additionalParameters.order_sub_merchant_keys_arr,
                params.additionalParameters.order_sub_merchant_prices_arr]);

            for (var i = 0; i < maxLength; i++) {
                resultJson.basketInfo.basketItems.push({});

                resultJson.basketInfo.basketItems[i].productName = getElementFromArray(params.additionalParameters.order_product_name_arr, i);
                resultJson.basketInfo.basketItems[i].productCode = getElementFromArray(params.additionalParameters.order_product_code_arr, i);
                resultJson.basketInfo.basketItems[i].price = getElementFromArray(params.additionalParameters.order_price_arr, i);
                resultJson.basketInfo.basketItems[i].productType = getElementFromArray(params.additionalParameters.order_price_type_arr, i);
                resultJson.basketInfo.basketItems[i].quantity = getElementFromArray(params.additionalParameters.order_qty_arr, i);
                resultJson.basketInfo.basketItems[i].productInfo = getElementFromArray(params.additionalParameters.order_product_info_arr, i);
                resultJson.basketInfo.basketItems[i].version = "v1";
                resultJson.basketInfo.basketItems[i].mplaceMerchant = "mpm";
                resultJson.basketInfo.basketItems[i].sellerId = "s1";
                resultJson.basketInfo.basketItems[i].sellerPaymentAmount = 0;
                resultJson.basketInfo.basketItems[i].sellerCommissionAmount = 0;
                resultJson.basketInfo.basketItems[i].submerchantKey = getElementFromArray(params.additionalParameters.order_sub_merchant_keys_arr, i);
                resultJson.basketInfo.basketItems[i].submerchantPrice = getElementFromArray(params.additionalParameters.order_sub_merchant_prices_arr, i);

            }

        }

        localStorage.setItem("vposApiRequest", JSON.stringify(resultJson));

    }

}


function sendVposApiRequest(params, successHandler, errorHandler) {
    console.info(params);

    $.ajax({
        type: 'POST',
        url: params.url,
        data: JSON.stringify(params.data),
        success: function (data, textStatus) {
            successHandler(data, 200);
        },
        error: function (xhr, textStatus) {
            errorHandler(xhr, xhr.status);
        },
        contentType: "application/json",
        dataType: 'json',
        headers: {Authorization: "Bearer " + params.oauthToken}
    });

}

function calculateOrderId(companyId, institutionId) {
    return String(companyId) + String(institutionId) + Math.floor(Math.random() * 1000000000000);
}
