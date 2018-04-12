layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addData)",function(data){
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var url = "";
        if(data.field.id){
            url = "/api/permission/edit?id="+data.field.id;
        }else{
            url = "/api/permission/edit";
        }
        $.post(url,{
            id : data.field.id,
            name : $(".name").val(),
            url : $(".url").val(),
            sort : $(".sort").val(),
            type : $("input[name='type']:checked").val(),
            perms : $(".perms").val(),
            parentId: $(".parentId").val(),
            data : $(".data").val(),
            description : $(".description").val(),
        },function(res){
            console.log(res);
            if(res.code == 0){
                top.layer.close(index);
                top.layer.msg("权限添加成功！");
                layer.closeAll("iframe");
                //刷新父页面
                parent.location.reload();
            }
        })
        return false;
    })

    $(".parentName").on("click",function() {
        layui.layer.open({
            title:'上级菜单',
            type: 2,
            area: ['300px', '450px'],
            content: 'tree.html',
            btn: ['确定'],
            yes: function(index,layero){
                var body = layui.layer.getChildFrame('body', index);
                $(".parentId").val(body.find("input[name='parentId']").val());
                $(".parentName").val(body.find("input[name='parentName']").val());
                layui.layer.close(index);
            }
        });
    })
})