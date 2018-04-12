layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addData)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var url = "/api/role/edit";
        if($("#uid").val() != ""){
            url += "?id="+data.field.id;
        }

        // 实际使用时的提交信息
        $.post(url,{
            id : data.field.id,
            name : $(".name").val(),
            description : $(".description").val(),
            data : $(".data").val(),
        },function(res){
            console.log(res);
            if(res.code == 0){
                // console.log(res.msg);
                top.layer.close(index);
                top.layer.msg("角色添加成功！");
                layer.closeAll("iframe");
                //刷新父页面
                parent.location.reload();
            }
        })
        return false;
    })

    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }
    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());

})