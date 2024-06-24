$(function () {
    $("form").submit(function () {
        $(this).find("button").prop("disabled", true);
    });
});