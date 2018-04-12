layui.use(['form', 'layer', 'table'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#roleList',
        url: '/api/role/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 20,
        id: "roleListTable",
        cols: [[
            {field: 'name', title: '角色名', minWidth: 100, align: "center"},
            {field: 'description', title: '描述', minWidth: 100, align: "center"},
            {field: 'data', title: '数据', minWidth: 100, align: "center"},
            {title: '操作', minWidth: 175, templet: '#roleListBar', fixed: "right", align: "center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click", function () {
        if ($(".searchVal").val() != '') {
            table.reload("roleListTable", {
                url: '/api/role/list',
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    name: $(".searchVal").val(),  //搜索的关键字
                }
            })
        } else {
            layer.msg("请输入搜索的内容");
        }
    });

    //添加数据
    function addData(data) {
        var url = data ? "/api/role/edit?id=" + data.id : "/api/role/edit";
        var index = layui.layer.open({
            title: "添加角色",
            type: 2,
            content: "edit.html",
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                if (data) {
                    body.find("#uid").val(data.id);
                    body.find(".name").val(data.name);
                    body.find(".description").val(data.description);
                    body.find(".data").val(data.data);
                    form.render();
                }
                setTimeout(function () {
                    layui.layer.tips('点击此处返回用户列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        })
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize", function () {
            layui.layer.full(index);
        })
    }

    // 角色详情
    function showData(data) {
        layui.layer.open({
            title:'赋值权限',
            type: 2,
            area: ['300px', '450px'],
            content: "view.html",
            btn: ['确定'],
            success: function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                body.find("#roleId").val(data.id);
                iframeWin.showTree();
            },
            yes: function(index,layero){
                var body = layui.layer.getChildFrame('body', index);
                var ids = body.find("input[name='ids']").val();
                $.post("/api/role/view/" + data.id, {
                    permissionIds:ids,
                }, function (data) {
                    layui.layer.close(index);
                })
            }
        });
    }

    $(".addNews_btn").click(function () {
        addData();
    })

    //列表操作
    table.on('tool(roleList)', function (obj) {
        var layEvent = obj.event,
            data = obj.data;

        if (layEvent === 'edit') {     //编辑
            addData(data);
        } else if (layEvent === 'detail') {
            showData(data);
        } else if (layEvent === 'del') { //删除
            layer.confirm('确定删除此角色？', {icon: 3, title: '提示信息'}, function (index) {
                $.get("/api/role/delete/" + data.id, {}, function (data) {
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }
    });

})