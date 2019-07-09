

$(document).ready(function () {

        $("#btn").click(function () {

                var client_id = $("input[name='clent_id']").val();
                var client_securi = $("input[name='clent_securi']").val();
                alert(client_id + "获得这个两个值" + client_securi);


//                $('form[method="post"]').submit(function () {
                        
                        alert("进来发送");
                        $.post('/fromPost',{"client_id":client_id,"client_securi":client_securi}, function (data) {

                                alert("返回的成功的数据"+data);
				alert(data.value);
                               

                        }, 'text');
              //  });
        });
});
