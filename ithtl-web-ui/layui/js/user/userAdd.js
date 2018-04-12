layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addUser)",function(data){
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8}),
            roleIds = [];
        $("input:checkbox[name='userRole']:checked").each(function () {
            roleIds.push($(this).val());
        });

        var datas = {
            username : $(".userName").val(),  //登录名
            nickname : $(".nickName").val(),  //昵称
            email : $(".userEmail").val(),  //邮箱
            password : $(".userPassword").val(),  //密码
            userRole : roleIds,
            description : $(".description").val(),
            status : $(".userStatus").val(),   //用户状态
        };

        var url = "/api/user/edit";
        if($("#uid").val() != ""){
            url += "?id="+$("#uid").val();
            datas.id = $("#uid").val();
        }

        console.log(JSON.stringify(datas));

        $.ajax({
            type: 'POST',
            url: url,
            data: datas,
            async: false,
            dataType: "json",
            success: function (result) {
                if(result.code == 0){
                    console.log(result.msg);
                }
            }
        })

        setTimeout(function(){
            top.layer.close(index);
            top.layer.msg("用户添加成功！");
            layer.closeAll("iframe");
            //刷新父页面
            parent.location.reload();
        },2000);
        return false;
    })
})

function xuanran()
{
    layui.use(['form','layer'],function(){
        var form = layui.form;
        form.render();
    });
}