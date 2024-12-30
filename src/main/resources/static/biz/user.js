var table;
var layer;

layui.use(['layer', 'table', 'element'], function () {
    table = layui.table;
    layer = layui.layer;
    // 执行一个 table 实例
    table.render({
        elem: '#user',
        height: 472,
        url: 'user_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [[ // 表头
            {
                fixed: 'left',
                type: 'checkbox'
            }, {
                field: 'userid',
                title: '用户编号',
                width: 160,
                align: 'center'
            }, {
                field: 'username',
                title: '用户名称',
                width: 160,
                align: 'center'
            }, {
                field: 'password',
                title: '用户密码',
                width: 160,
                align: 'center'
            }, {
                title: '角色名称',
                width: 160,
                align: 'center',
                templet: function (d) {
                    return d.role.rolename;
                }
            }, {
                title: '操作',
                width: 160,
                align: 'center',
                toolbar: '#tools'
            }
        ]]
    });

    // 监听工具条
    table.on('tool(tools)', function (obj) {
        var data = obj.data // 获得当前行数据
            , layEvent = obj.event; // 获得lay-event对应的值
        if ('edit' == layEvent) {
            openDialog(data);
        } else if ('del' === layEvent) {
            layer.confirm('确定删除当前行吗？', {icon: 3, title: '用户删除'}, function (index) {
                console.log(data);
                $.ajax({
                    url: "user_delete",
                    type: "POST",
                    data: {"userid": data.userid},
                    dataType: "json",
                    success: function (result) {
                        if (result == 0) {
                            obj.del();
                            layer.close(index);
                            layer.msg("删除成功", {icon: 6});
                            query();
                        } else {
                            layer.msg("删除失败", {icon: 5});
                        }
                    }
                });
            });
        }
    });
});

var query = function () {
    table.reload('user', {
        page: {
            curr: 1 // 重新从第1页开始
        },
        where: {
            'condition': $('#condition').val(),
            'keyword': $("#keyword").val()
        }
    });
};

var openDialog = function (data) {
    var url = '/user_view';
    if (data != undefined) {
        url += '?userid=' + data.userid;
    }

    layer.open({
        type: 2
        , title: "用户信息"
        , area: ['400px', '300px']
        , id: 'user_dlg' // 防止重复弹出
        , content: url
        , btnAlign: 'c' // 按钮居中
        , shade: 0.1 // 不显示遮罩
        , yes: function () {
            layer.closeAll();
        },
        success: function (obj, index) {
            console.log(obj, index);
        }
    });
};