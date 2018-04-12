layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer
        $ = layui.jquery;

    $(".loginBody .seraph").click(function(){
        layer.msg("这只是做个样式，至于功能，你见过哪个后台能这样登录的？还是老老实实的找管理员去注册吧",{
            time:5000
        });
    })

    //解决iframe下系统超时无法跳出iframe框架的问题
    if (window != top){
        top.location.href = location.href;
    }

    //登录按钮
    form.on("submit(login)",function(data){
        $(this).text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
        $.post("/api/site/login", {
            username:data.field.username,
            password:data.field.password,
            captcha:data.field.captcha,
            rememberMe:data.field.rememberMe,
        }, function (res) {
            console.log(res);
            if(res.code != 0){
                layui.layer.msg(res.msg, {icon: 5});
            }else{
                window.location.href = "/";
            }
        })
        $(this).text("登录").removeAttr("disabled").removeClass("layui-disabled");
    })

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
})
