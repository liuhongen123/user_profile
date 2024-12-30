layui.use(['form', 'layer'], function () {
    var form = layui.form;
    var layer = layui.layer;
    // 添加验证规则
    form.verify({
        username: function (value, item) {
            value = value.trim();
            if (value.length < 0) {
                return "请输入用户名称";
            }
        }
    });

    // 动态加载下拉框
    $.ajax({
        url: '/role_load',
        data: {},
        dataType: 'json',
        success: function (data) {
            var $html = "";
            if (data != null) {
                $.each(data, function (index, item) {
                    $html += "<option value='" + item.roleid + "'>" + item.rolename + "</option>";
                });
            }
            $('#role').append($html);
            // 设置下拉框回显（注意绑定的是下拉框的value）
            $('#role').val($('#roleid').val());
            // append后必须从新渲染
            form.render('select');
        }, error: function () {
        }
    });

    form.on('submit(*)', function (data) {
        var index = layer.msg('提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        var context = data.field;
        var url = "/user_update";

        $.ajax({
            url: url,
            data: context,
            dataType: "text",
            success: function (data) {
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                parent.layer.close(index);  // 关闭layer

                if (data == 0) {
                    parent.layer.msg("操作成功", {icon: 6});
                    parent.table.reload('user', {
                        page: {
                            curr: 1 // 重新从第1页开始
                        },
                        where: {
                            'keyword': $("#keyword").val()
                        }
                    });
                } else {
                    parent.layer.msg("操作失败", {icon: 5});
                }
            }, error: function () {
                parent.layer.msg("操作失败", {icon: 5});
            }
        });
        return false;
    });
});