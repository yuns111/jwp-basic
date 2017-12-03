String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

$(".answerWrite input[type=submit]").click(addAnswer);

$(".qna-comment").on("click", ".form-delete", deleteAnswer);

function addAnswer(e) {
  e.preventDefault();
  var queryString = $("form[name=answer]").serialize();

  $.ajax({
      type: 'post',
      url: '/api/qna/addAnswer',
      data: queryString,
      dataType:'json',
      error: onError,
      success: onSuccess
  });
}

function onSuccess(json, status) {
  var answerTemplate = $("#answerTemplate").html();
  var template = answerTemplate.format(json.writer, new Date(json.createdDate), json.contents, json.answerId);
  $(".qna-comment-slipp-articles").prepend(template);
}

function deleteAnswer(e) {
    e.preventDefault();

    var deleteBtn = $(this);
    var queryString = deleteBtn.closest("form").serialize();

    $.ajax({
        type: 'post',
        url: "/api/qna/deleteAnswer",
        data: queryString,
        dataType: 'json',
        error: function (xhr, status) {
            alert("에러발생");
        },
        success: function (json, status) {
            if (json.status) {
                deleteBtn.closest('article').remove();
            }
        }
    });
}
