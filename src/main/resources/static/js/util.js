var DASHED_DATE_FORMAT_YYYYDDMM = "YYYY-MM-DD";

(function addBlockUI() {
    $.blockUI.defaults.baseZ = 20000; // for modal
    $.blockUI.defaults.css.backgroundColor = "transparent";
    $.blockUI.defaults.css.border = "none";
    $.blockUI.defaults.message = "<div class=\"spinner-border\" style=\"width: 4rem; height: 4rem;\" role=\"status\">\n" +
        "  <span class=\"sr-only\"></span>\n" +
        "</div>";
    $.blockUI();
    stopBlock();
    $(document).ajaxStart($.blockUI).ajaxStop(stopBlock);


})();

function stopBlock() {
    setTimeout(
        function () {
            $.unblockUI();
        },
        500
    );
}

function getElementFromArray(array, index) {
    if (array && array != null && array[index]) {
        return array[index];
    }
    return null;
}

function findMaxLengthArray(arrays) {
    var maxLength = 0;

    for (var i = 0; i < arrays.length; i++) {
        if (arrays[i] && arrays[i] != null && arrays[i].length > maxLength) {
            maxLength = arrays[i].length;
        }
    }

    return maxLength;
}

function formToObject(formData) {
    var unindexed_array = formData.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });
    return indexed_array;
}
