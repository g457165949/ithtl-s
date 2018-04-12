layui.use(['form','layer','table'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#logList',
        url : '/api/log/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20,
        id : "logListTable",
        cols: [[
            {field: 'id', title: 'ID', minWidth: 20},
            // {field: 'adminId', title: '用户ID', minWidth: 50, align: "center"},
            {field: 'adminName', title: '用户名', minWidth: 100, align: "center"},
            {field: 'route', title: '路由', minWidth: 100, align: "center"},
            {field: 'url', title: '链接', minWidth: 100, align: "center"},
            {field: 'method', title: '类型', align: "center"},
            {field: 'parameters', title: '参数', minWidth: 100, align: "center"},
            {field: 'ip', title: 'IP地址', minWidth: 100, align: "center"},
            // {title: '操作', minWidth: 175, templet: '#logListBar', fixed: "right", align: "center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("logListTable",{
                url:'/api/log/list',
                page: {
                    curr: 1
                },
                where: {
                    key: $(".searchVal").val(),  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });
})