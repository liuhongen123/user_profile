layui.config({
    version: 1,
    base: '/echarts/'
}).use(['element', 'layer', 'jquery', 'echarts'], function () {
    var element = layui.element;
    var $ = layui.$;
    var echarts = layui.echarts;

    var echartAllSales = echarts.init(document.getElementById('EchartAllSales'));

    var ids = [];
    var names = [];
    var nums = [];

    $.ajax({
        type: "post",
        async: true,           // 异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
        url: "scoreAllSales",
        data: {},
        dataType: "json",      // 返回数据形式为json
        success: function (result) {
            // 请求成功时执行该函数内容，result即为服务器返回的json对象
            if (result) {
                $.each(result, function (index, item) {
                    // 挨个取出类别并填入类别数组
                    ids.push(item['0']);
                    names.push(item['1']);
                    nums.push(item['2']);
                });

                echartAllSales.hideLoading();      // 隐藏加载动画
                echartAllSales.setOption({         // 加载数据图表
                    xAxis: {
                        type: 'category',
                        data: names
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: nums,
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside'
                        }
                    }]
                });
            }
        },
        error: function (errorMsg) {
            // 请求失败时执行该函数
            alert("图表请求数据失败!");
            echartAllSales.hideLoading();
        }
    });

    // 点击柱状图，绘制雷达图
    echartAllSales.on('click', function (params) {
        $('#currentSales').text(params.name);
        var echartSales = echarts.init(document.getElementById('EchartSales'));

        var scores = [];

        $.ajax({
            type: "post",
            async: true,           // 异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
            url: "scoreSales",
            data: {salesname: params.name},
            dataType: "json",      // 返回数据形式为json
            success: function (result) {
                // 请求成功时执行该函数内容，result即为服务器返回的json对象
                if (result) {
                    $.each(result, function (index, item) {
                        // 挨个取出类别并填入类别数组
                        scores.push(item['2']);
                        scores.push(item['3']);
                        scores.push(item['4']);
                        scores.push(item['5']);
                        scores.push(item['6']);
                    });

                    echartSales.hideLoading();      // 隐藏加载动画
                    echartSales.setOption({         // 加载数据图表
                        title: {
                            text: '个人分项评分'
                        },
                        tooltip: {},
                        legend: {
                            data: ['分项评分']
                        },
                        radar: {
                            name: {
                                textStyle: {
                                    color: 'black',
                                    backgroundColor: 'yellow',
                                    borderRadius: 3,
                                    padding: [3, 5]
                                }
                            },
                            indicator: [
                                {name: '仪容仪表', max: 5},
                                {name: '沟通表达', max: 5},
                                {name: '临场应变', max: 5},
                                {name: '技术知识', max: 5},
                                {name: '组织协调', max: 5}
                            ]
                        },
                        series: [{
                            name: '评分',
                            type: 'radar',
                            areaStyle: {normal: {}},
                            data: [
                                {name: "分项评分", value: scores}
                            ]
                        }]
                    });
                }
            },
            error: function (errorMsg) {
                // 请求失败时执行该函数
                alert("图表请求数据失败!");
                echartSales.hideLoading();
            }
        });
    });
});